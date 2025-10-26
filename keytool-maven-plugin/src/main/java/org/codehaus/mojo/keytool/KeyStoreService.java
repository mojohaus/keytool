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
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

import org.apache.maven.plugin.logging.Log;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

/**
 * Service for performing KeyStore operations using Java's KeyStore API.
 * This replaces the external keytool command-line tool invocations.
 *
 * @since 1.8
 */
public class KeyStoreService {

    private final Log log;

    /**
     * Constructor.
     *
     * @param log Maven logger
     */
    public KeyStoreService(Log log) {
        this.log = log;
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
     * @throws KeyStoreException if keystore operation fails
     * @throws IOException if file operations fail
     * @throws NoSuchAlgorithmException if algorithm is not available
     * @throws CertificateException if certificate is invalid
     */
    public void importCertificate(
            File keystoreFile,
            String keystoreType,
            char[] keystorePassword,
            String alias,
            File certificateFile,
            boolean skipIfAliasExists)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {

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
    }

    /**
     * Import a certificate into a keystore.
     * This is a convenience method that calls {@link #importCertificate(File, String, char[], String, File, boolean)}
     * with skipIfAliasExists set to false.
     *
     * @param keystoreFile keystore file
     * @param keystoreType keystore type (e.g., "JKS", "PKCS12")
     * @param keystorePassword keystore password
     * @param alias certificate alias
     * @param certificateFile certificate file to import
     * @throws KeyStoreException if keystore operation fails
     * @throws IOException if file operations fail
     * @throws NoSuchAlgorithmException if algorithm is not available
     * @throws CertificateException if certificate is invalid
     */
    public void importCertificate(
            File keystoreFile, String keystoreType, char[] keystorePassword, String alias, File certificateFile)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        importCertificate(keystoreFile, keystoreType, keystorePassword, alias, certificateFile, false);
    }

    /**
     * Generate a key pair and self-signed certificate in a keystore.
     *
     * @param keystoreFile keystore file
     * @param keystoreType keystore type (e.g., "JKS", "PKCS12")
     * @param keystorePassword keystore password
     * @param alias key alias
     * @param keyPassword key password (if null, uses keystore password)
     * @param dname distinguished name (e.g., "CN=Test, OU=Dev, O=Org, L=City, ST=State, C=US")
     * @param keyAlg key algorithm (e.g., "RSA", "DSA", "EC")
     * @param keySize key size in bits (e.g., 2048, 4096)
     * @param sigAlg signature algorithm (e.g., "SHA256withRSA")
     * @param validity validity period in days
     * @throws KeyStoreException if keystore operation fails
     * @throws IOException if file operations fail
     * @throws NoSuchAlgorithmException if algorithm is not available
     * @throws CertificateException if certificate generation fails
     * @throws OperatorCreationException if certificate signer creation fails
     */
    public void generateKeyPair(
            File keystoreFile,
            String keystoreType,
            char[] keystorePassword,
            String alias,
            char[] keyPassword,
            String dname,
            String keyAlg,
            int keySize,
            String sigAlg,
            int validity)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException,
                    OperatorCreationException {

        // Use default values if not specified
        String actualKeyAlg = (keyAlg != null && !keyAlg.isEmpty()) ? keyAlg : "RSA";
        int actualKeySize = (keySize > 0) ? keySize : 2048;
        String actualSigAlg = (sigAlg != null && !sigAlg.isEmpty()) ? sigAlg : "SHA256withRSA";
        int actualValidity = (validity > 0) ? validity : 365;
        char[] actualKeyPassword = (keyPassword != null) ? keyPassword : keystorePassword;

        // Load the keystore
        KeyStore keystore = loadKeyStore(keystoreFile, keystoreType, keystorePassword);

        // Generate key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(actualKeyAlg);
        keyPairGenerator.initialize(actualKeySize, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Create self-signed certificate
        X509Certificate certificate = generateSelfSignedCertificate(keyPair, dname, actualSigAlg, actualValidity);

        // Store key entry in keystore
        keystore.setKeyEntry(alias, keyPair.getPrivate(), actualKeyPassword, new Certificate[] {certificate});

        // Save the keystore
        saveKeyStore(keystore, keystoreFile, keystorePassword);

        log.info("Generated key pair with alias '" + alias + "' in keystore");
    }

    /**
     * Generate a self-signed X.509 certificate.
     *
     * @param keyPair key pair to use
     * @param dname distinguished name
     * @param sigAlg signature algorithm
     * @param validity validity period in days
     * @return generated certificate
     * @throws OperatorCreationException if certificate signer creation fails
     * @throws CertificateException if certificate generation fails
     */
    private X509Certificate generateSelfSignedCertificate(KeyPair keyPair, String dname, String sigAlg, int validity)
            throws OperatorCreationException, CertificateException {

        // Set validity period
        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, validity);
        Date endDate = calendar.getTime();

        // Generate serial number
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

        // Parse distinguished name
        X500Name issuer = new X500Name(dname);
        X500Name subject = issuer; // Self-signed

        // Build certificate
        X509v3CertificateBuilder certBuilder =
                new JcaX509v3CertificateBuilder(issuer, serialNumber, startDate, endDate, subject, keyPair.getPublic());

        // Create content signer
        ContentSigner contentSigner = new JcaContentSignerBuilder(sigAlg).build(keyPair.getPrivate());

        // Generate certificate
        X509CertificateHolder certHolder = certBuilder.build(contentSigner);

        // Convert to X509Certificate
        return new JcaX509CertificateConverter().getCertificate(certHolder);
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
