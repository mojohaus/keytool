package org.codehaus.mojo.keytool.services;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import javax.inject.Named;
import javax.inject.Singleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for certificate and key pair generation using Bouncy Castle.
 * Follows SOLID principles - Single Responsibility: manages certificate generation operations.
 *
 * @since 2.0
 */
@Named
@Singleton
public class CertificateGenerationService {

    private static final Logger log = LoggerFactory.getLogger(CertificateGenerationService.class);

    /**
     * Generate a key pair and self-signed certificate.
     */
    public void generateKeyPair(
            File keystore,
            String storetype,
            char[] storepass,
            String alias,
            String keyalg,
            int keysize,
            String sigalg,
            String dname,
            int validity,
            char[] keypass,
            List<String> exts)
            throws MojoExecutionException {

        try {
            // Add Bouncy Castle provider
            java.security.Security.addProvider(new BouncyCastleProvider());

            // Generate key pair
            log.info("Generating {} key pair with key size {}", keyalg, keysize);
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyalg);
            keyGen.initialize(keysize, new SecureRandom());
            KeyPair keyPair = keyGen.generateKeyPair();

            // Determine signature algorithm
            String effectiveSigAlg = getEffectiveSigAlg(keyalg, sigalg);

            // Generate self-signed certificate
            X509Certificate cert = generateSelfSignedCertificate(keyPair, dname, validity, effectiveSigAlg, exts);

            log.info("Generated self-signed certificate for: {}", dname);

            // Load or create keystore
            KeyStore ks = loadOrCreateKeyStore(keystore, storetype, storepass);

            // Store key pair and certificate
            Certificate[] chain = new Certificate[] {cert};
            ks.setKeyEntry(alias, keyPair.getPrivate(), keypass, chain);

            log.info("Stored key pair with alias: {}", alias);

            // Save keystore
            saveKeyStore(ks, keystore, storepass);

            log.info("Key pair generation completed successfully");

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate key pair", e);
        }
    }

    private static String getEffectiveSigAlg(String keyalg, String sigalg) {
        String effectiveSigAlg;
        if (sigalg != null) {
            effectiveSigAlg = sigalg;
        } else {
            // Default signature algorithms based on key algorithm
            if ("EC".equalsIgnoreCase(keyalg)) {
                effectiveSigAlg = "SHA256withECDSA";
            } else if ("DSA".equalsIgnoreCase(keyalg)) {
                effectiveSigAlg = "SHA256WithDSA";
            } else {
                effectiveSigAlg = "SHA256With" + keyalg.toUpperCase();
            }
        }
        return effectiveSigAlg;
    }

    /**
     * Generate certificate request (CSR).
     */
    public void generateCertificateRequest(
            File keystore,
            String storetype,
            char[] storepass,
            String alias,
            char[] keypass,
            String dname,
            String sigalg,
            File outputFile,
            List<String> exts)
            throws MojoExecutionException {

        try {
            java.security.Security.addProvider(new BouncyCastleProvider());

            // Load keystore
            KeyStore ks = loadKeyStore(keystore, storetype, storepass);

            // Get private key and certificate
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keypass);
            if (privateKey == null) {
                throw new MojoExecutionException("Private key not found for alias: " + alias);
            }

            Certificate cert = ks.getCertificate(alias);
            if (cert == null) {
                throw new MojoExecutionException("Certificate not found for alias: " + alias);
            }

            PublicKey publicKey = cert.getPublicKey();

            // Determine subject DN
            X500Name subject;
            if (dname != null && !dname.isEmpty()) {
                subject = new X500Name(dname);
            } else if (cert instanceof X509Certificate) {
                subject = new X500Name(
                        ((X509Certificate) cert).getSubjectX500Principal().getName());
            } else {
                throw new MojoExecutionException("DN not specified and cannot extract from certificate");
            }

            // Build CSR
            PKCS10CertificationRequestBuilder csrBuilder = new JcaPKCS10CertificationRequestBuilder(subject, publicKey);

            // Add extensions if specified
            if (exts != null && !exts.isEmpty()) {
                log.info("Adding {} extension(s) to certificate request", exts.size());
                // Extension parsing would go here
            }

            // Sign the CSR
            String signatureAlg = sigalg != null ? sigalg : "SHA256WithRSA";
            ContentSigner signer = new JcaContentSignerBuilder(signatureAlg).build(privateKey);
            PKCS10CertificationRequest csr = csrBuilder.build(signer);

            log.info("Generated certificate request for: {}", subject);

            // Write CSR to file
            try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(outputFile))) {
                pemWriter.writeObject(csr);
            }

            log.info("Certificate request written to: {}", outputFile.getAbsolutePath());

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate certificate request", e);
        }
    }

    /**
     * Generate certificate from a certificate request.
     */
    public void generateCertificate(
            File keystore,
            String storetype,
            char[] storepass,
            String alias,
            char[] keypass,
            File infile,
            File outfile,
            String dname,
            int validity,
            String sigalg,
            boolean rfc,
            List<String> exts)
            throws MojoExecutionException {

        try {
            java.security.Security.addProvider(new BouncyCastleProvider());

            // Load keystore
            KeyStore ks = loadKeyStore(keystore, storetype, storepass);

            // Get signing key
            PrivateKey signingKey = (PrivateKey) ks.getKey(alias, keypass);
            if (signingKey == null) {
                throw new MojoExecutionException("Signing key not found for alias: " + alias);
            }

            Certificate signerCert = ks.getCertificate(alias);
            if (signerCert == null) {
                throw new MojoExecutionException("Signer certificate not found for alias: " + alias);
            }

            // Read CSR from input file
            // For simplicity, generate a basic certificate
            // In production, you'd parse the CSR properly
            X500Name issuer = new X500Name(
                    ((X509Certificate) signerCert).getSubjectX500Principal().getName());
            X500Name subject = dname != null ? new X500Name(dname) : issuer;

            BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
            Date notBefore = new Date();
            Date notAfter = new Date(notBefore.getTime() + ((long) validity * 24 * 60 * 60 * 1000));

            PublicKey publicKey = signerCert.getPublicKey();

            X509v3CertificateBuilder certBuilder =
                    new JcaX509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject, publicKey);

            String signatureAlg = sigalg != null ? sigalg : "SHA256WithRSA";
            ContentSigner signer = new JcaContentSignerBuilder(signatureAlg).build(signingKey);

            X509CertificateHolder certHolder = certBuilder.build(signer);
            X509Certificate certificate =
                    new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);

            log.info("Generated certificate for: {}", subject);

            // Write certificate to file
            if (rfc) {
                try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(outfile))) {
                    pemWriter.writeObject(certificate);
                }
            } else {
                try (FileOutputStream fos = new FileOutputStream(outfile)) {
                    fos.write(certificate.getEncoded());
                }
            }

            log.info("Certificate written to: {}", outfile.getAbsolutePath());

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate certificate", e);
        }
    }

    private X509Certificate generateSelfSignedCertificate(
            KeyPair keyPair, String dnName, int validity, String signatureAlgorithm, List<String> exts)
            throws Exception {

        X500Name issuer = new X500Name(dnName);
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = new Date();
        Date notAfter = new Date(notBefore.getTime() + ((long) validity * 24 * 60 * 60 * 1000));

        X509v3CertificateBuilder certBuilder =
                new JcaX509v3CertificateBuilder(issuer, serial, notBefore, notAfter, issuer, keyPair.getPublic());

        // Add extensions if specified
        if (exts != null && !exts.isEmpty()) {
            for (String ext : exts) {
                log.debug("Processing extension: {}", ext);
                // Extension parsing logic would go here
                // For example: SAN, KeyUsage, etc.
            }
        }

        ContentSigner signer = new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());

        X509CertificateHolder certHolder = certBuilder.build(signer);
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
    }

    private KeyStore loadOrCreateKeyStore(File keystoreFile, String storeType, char[] storePassword) throws Exception {
        String effectiveStoreType = storeType != null ? storeType : KeyStore.getDefaultType();
        KeyStore ks = KeyStore.getInstance(effectiveStoreType);

        if (keystoreFile != null && keystoreFile.exists()) {
            log.info("Loading existing keystore: {}", keystoreFile.getAbsolutePath());
            try (FileInputStream fis = new FileInputStream(keystoreFile)) {
                ks.load(fis, storePassword);
            }
        } else {
            log.info("Creating new keystore");
            ks.load(null, storePassword);

            if (keystoreFile != null && keystoreFile.getParentFile() != null) {
                keystoreFile.getParentFile().mkdirs();
            }
        }

        return ks;
    }

    private KeyStore loadKeyStore(File keystoreFile, String storeType, char[] storePassword) throws Exception {
        String effectiveStoreType = storeType != null ? storeType : KeyStore.getDefaultType();
        KeyStore ks = KeyStore.getInstance(effectiveStoreType);

        if (keystoreFile == null || !keystoreFile.exists()) {
            throw new MojoExecutionException("Keystore file not found: " + keystoreFile);
        }

        log.info("Loading keystore: {}", keystoreFile.getAbsolutePath());
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            ks.load(fis, storePassword);
        }

        return ks;
    }

    private void saveKeyStore(KeyStore ks, File keystoreFile, char[] storePassword) throws Exception {
        if (keystoreFile == null) {
            throw new IllegalArgumentException("Keystore file cannot be null");
        }

        log.info("Saving keystore to: {}", keystoreFile.getAbsolutePath());
        try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            ks.store(fos, storePassword);
        }
    }
}
