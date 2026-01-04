package org.codehaus.mojo.keytool;

/*
 * Copyright 2005-2013 The Codehaus
 *
 * Licensed under the Apache License, Version 2.0 (the "License" );
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

/**
 * Service for performing KeyStore operations using Java's KeyStore API.
 * This replaces the external keytool command-line tool invocations.
 *
 * @since 2.0
 */
public class KeyStoreService {

    private final Log log;

    static {
        // Register BouncyCastle provider for advanced operations
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * Constructor.
     *
     * @param log Maven logger
     */
    public KeyStoreService(Log log) {
        this.log = log;
    }

    /**
     * Generate a key pair and store it in the keystore.
     *
     * @param keystoreFile keystore file
     * @param keystoreType keystore type (e.g., "JKS", "PKCS12")
     * @param keystorePassword keystore password
     * @param alias key pair alias
     * @param keyPassword private key password
     * @param algorithm key algorithm (e.g., "RSA", "DSA", "EC")
     * @param keySize key size in bits
     * @param signatureAlgorithm signature algorithm (e.g., "SHA256WithRSA")
     * @param dname distinguished name
     * @param validityDays certificate validity in days
     * @throws MojoExecutionException if operation fails
     */
    public void generateKeyPair(
            File keystoreFile,
            String keystoreType,
            char[] keystorePassword,
            String alias,
            char[] keyPassword,
            String algorithm,
            int keySize,
            String signatureAlgorithm,
            String dname,
            int validityDays)
            throws MojoExecutionException {

        try {
            // Load or create keystore
            KeyStore keystore = loadKeyStore(keystoreFile, keystoreType, keystorePassword);

            // Generate key pair
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
            keyPairGen.initialize(keySize, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();

            // Create self-signed certificate
            X509Certificate certificate =
                    generateSelfSignedCertificate(keyPair, signatureAlgorithm, dname, validityDays);

            // Store the key and certificate
            char[] keyPwd = keyPassword != null ? keyPassword : keystorePassword;
            keystore.setKeyEntry(alias, keyPair.getPrivate(), keyPwd, new Certificate[] {certificate});

            // Save keystore
            saveKeyStore(keystore, keystoreFile, keystorePassword);

            log.info("Generated key pair for alias: " + alias);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate key pair: " + e.getMessage(), e);
        }
    }

    /**
     * Generate a self-signed X.509 certificate.
     *
     * @param keyPair key pair
     * @param signatureAlgorithm signature algorithm
     * @param dname distinguished name
     * @param validityDays validity in days
     * @return X.509 certificate
     * @throws OperatorCreationException if signer creation fails
     * @throws CertificateException if certificate generation fails
     */
    private X509Certificate generateSelfSignedCertificate(
            KeyPair keyPair, String signatureAlgorithm, String dname, int validityDays)
            throws OperatorCreationException, CertificateException {

        X500Name issuer = new X500Name(dname);
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = new Date();
        Date notAfter = new Date(notBefore.getTime() + ((long) validityDays * 24 * 60 * 60 * 1000));

        X509v3CertificateBuilder certBuilder =
                new JcaX509v3CertificateBuilder(issuer, serial, notBefore, notAfter, issuer, keyPair.getPublic());

        ContentSigner signer = new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());
        X509CertificateHolder certHolder = certBuilder.build(signer);

        return new JcaX509CertificateConverter()
                .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .getCertificate(certHolder);
    }

    /**
     * Import a certificate into a keystore.
     *
     * @param keystoreFile keystore file
     * @param keystoreType keystore type (e.g., "JKS", "PKCS12")
     * @param keystorePassword keystore password
     * @param alias certificate alias
     * @param certificateFile certificate file to import
     * @param skipIfAliasExists if true, skip import if alias already exists
     * @throws MojoExecutionException if operation fails
     */
    public void importCertificate(
            File keystoreFile,
            String keystoreType,
            char[] keystorePassword,
            String alias,
            File certificateFile,
            boolean skipIfAliasExists)
            throws MojoExecutionException {

        try {
            // Load the keystore
            KeyStore keystore = loadKeyStore(keystoreFile, keystoreType, keystorePassword);

            // Check if alias already exists
            if (skipIfAliasExists && keystore.containsAlias(alias)) {
                log.info("Certificate alias '" + alias + "' already exists in keystore. Skipping import.");
                return;
            }

            // Load the certificate
            Certificate certificate = loadCertificate(certificateFile);

            // Import the certificate
            keystore.setCertificateEntry(alias, certificate);

            // Save the keystore
            saveKeyStore(keystore, keystoreFile, keystorePassword);

            log.info("Certificate was added to keystore");
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to import certificate: " + e.getMessage(), e);
        }
    }

    /**
     * Export a certificate from a keystore.
     *
     * @param keystoreFile keystore file
     * @param keystoreType keystore type
     * @param keystorePassword keystore password
     * @param alias certificate alias
     * @param outputFile output file
     * @param rfc if true, output in RFC/PEM format
     * @throws MojoExecutionException if operation fails
     */
    public void exportCertificate(
            File keystoreFile, String keystoreType, char[] keystorePassword, String alias, File outputFile, boolean rfc)
            throws MojoExecutionException {

        try {
            // Load keystore
            KeyStore keystore = loadKeyStore(keystoreFile, keystoreType, keystorePassword);

            // Get certificate
            Certificate certificate = keystore.getCertificate(alias);
            if (certificate == null) {
                throw new MojoExecutionException("Alias '" + alias + "' does not exist in keystore");
            }

            // Ensure parent directory exists
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Export certificate
            if (rfc) {
                exportCertificatePEM(certificate, outputFile);
            } else {
                exportCertificateDER(certificate, outputFile);
            }

            log.info("Certificate exported to: " + outputFile);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to export certificate: " + e.getMessage(), e);
        }
    }

    /**
     * Export certificate in PEM format.
     *
     * @param certificate certificate
     * @param outputFile output file
     * @throws IOException if write fails
     * @throws CertificateEncodingException if encoding fails
     */
    private void exportCertificatePEM(Certificate certificate, File outputFile)
            throws IOException, CertificateEncodingException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write("-----BEGIN CERTIFICATE-----\n");
            String encoded = Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(certificate.getEncoded());
            writer.write(encoded);
            writer.write("\n-----END CERTIFICATE-----\n");
        }
    }

    /**
     * Export certificate in DER format.
     *
     * @param certificate certificate
     * @param outputFile output file
     * @throws IOException if write fails
     * @throws CertificateEncodingException if encoding fails
     */
    private void exportCertificateDER(Certificate certificate, File outputFile)
            throws IOException, CertificateEncodingException {
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(certificate.getEncoded());
        }
    }

    /**
     * Delete an alias from a keystore.
     *
     * @param keystoreFile keystore file
     * @param keystoreType keystore type
     * @param keystorePassword keystore password
     * @param alias alias to delete
     * @throws MojoExecutionException if operation fails
     */
    public void deleteAlias(File keystoreFile, String keystoreType, char[] keystorePassword, String alias)
            throws MojoExecutionException {

        try {
            // Load keystore
            KeyStore keystore = loadKeyStore(keystoreFile, keystoreType, keystorePassword);

            // Check if alias exists
            if (!keystore.containsAlias(alias)) {
                log.warn("Alias '" + alias + "' does not exist in keystore");
                return;
            }

            // Delete the alias
            keystore.deleteEntry(alias);

            // Save keystore
            saveKeyStore(keystore, keystoreFile, keystorePassword);

            log.info("Deleted alias: " + alias);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to delete alias: " + e.getMessage(), e);
        }
    }

    /**
     * Load a keystore from file.
     *
     * @param keystoreFile keystore file
     * @param keystoreType keystore type
     * @param password keystore password
     * @return loaded keystore
     * @throws KeyStoreException if keystore operation fails
     * @throws IOException if file operations fail
     * @throws NoSuchAlgorithmException if algorithm is not available
     * @throws CertificateException if certificate is invalid
     */
    private KeyStore loadKeyStore(File keystoreFile, String keystoreType, char[] password)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {

        KeyStore keystore = KeyStore.getInstance(keystoreType != null ? keystoreType : KeyStore.getDefaultType());

        if (keystoreFile.exists()) {
            try (FileInputStream fis = new FileInputStream(keystoreFile)) {
                keystore.load(fis, password);
            }
        } else {
            // Create a new keystore
            keystore.load(null, password);
        }

        return keystore;
    }

    /**
     * Load a certificate from file.
     *
     * @param certificateFile certificate file
     * @return loaded certificate
     * @throws IOException if file operations fail
     * @throws CertificateException if certificate is invalid
     */
    private Certificate loadCertificate(File certificateFile) throws IOException, CertificateException {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

        try (InputStream is = new FileInputStream(certificateFile)) {
            return certFactory.generateCertificate(is);
        }
    }

    /**
     * Save a keystore to file.
     *
     * @param keystore keystore to save
     * @param keystoreFile target file
     * @param password keystore password
     * @throws KeyStoreException if keystore operation fails
     * @throws IOException if file operations fail
     * @throws NoSuchAlgorithmException if algorithm is not available
     * @throws CertificateException if certificate is invalid
     */
    private void saveKeyStore(KeyStore keystore, File keystoreFile, char[] password)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {

        // Ensure parent directory exists
        File parentDir = keystoreFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            keystore.store(fos, password);
        }
    }
}
