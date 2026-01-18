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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for CertificateManagementService.
 */
public class CertificateManagementServiceTest {

    @TempDir
    public File tempFolder;

    private CertificateManagementService service;

    @BeforeEach
    void setUp() {
        service = new CertificateManagementService();
    }

    @Test
    void serviceInstantiation() {
        assertNotNull(service, "Service should not be null");
    }

    @Test
    void importCertificate() throws Exception {
        File keystoreFile = new File(tempFolder, "test.jks");
        File certFile = createTestCertificateFile();

        char[] password = "changeit".toCharArray();
        String alias = "testcert";

        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, false, null);

        // Verify keystore was created and contains the certificate
        assertTrue(keystoreFile.exists(), "Keystore should exist");

        KeyStore ks = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            ks.load(fis, password);
        }

        assertTrue(ks.containsAlias(alias), "Certificate should exist in keystore");
    }

    @Test
    void importCertificateSkipIfExists() throws Exception {
        File keystoreFile = new File(tempFolder, "test-skip.jks");
        File certFile = createTestCertificateFile();

        char[] password = "changeit".toCharArray();
        String alias = "testcert";

        // First import
        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, false, null);

        // Second import with skipIfExists=true
        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, true, null);
    }

    @Test
    void exportCertificate() throws Exception {
        File keystoreFile = new File(tempFolder, "export-test.jks");
        File certFile = createTestCertificateFile();
        File exportFile = new File(tempFolder, "exported.cer");

        char[] password = "changeit".toCharArray();
        String alias = "testcert";

        // First import a certificate
        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, false, null);

        // Then export it
        service.exportCertificate(keystoreFile, "JKS", password, alias, exportFile);

        assertTrue(exportFile.exists(), "Exported certificate should exist");
        assertTrue(exportFile.length() > 0, "Exported certificate should not be empty");
    }

    @Test
    void deleteAlias() throws Exception {
        File keystoreFile = new File(tempFolder, "delete-test.jks");
        File certFile = createTestCertificateFile();

        char[] password = "changeit".toCharArray();
        String alias = "testcert";

        // First import a certificate
        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, false, null);

        // Verify it exists
        KeyStore ks = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            ks.load(fis, password);
        }
        assertTrue(ks.containsAlias(alias), "Certificate should exist before deletion");

        // Delete the alias
        service.deleteAlias(keystoreFile, "JKS", password, alias);

        // Verify it's gone
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            ks.load(fis, password);
        }
        assertFalse(ks.containsAlias(alias), "Certificate should not exist after deletion");
    }

    @Test
    void importNonExistentCertificate() {
        File keystoreFile = new File(tempFolder, "test.jks");
        File certFile = new File(tempFolder, "nonexistent.cer");
        assertThrows(
                Exception.class,
                () -> service.importCertificate(
                        keystoreFile, "JKS", "changeit".toCharArray(), "test", certFile, false, null));
    }

    @Test
    void importCertificateReplyForKeyPair() throws Exception {
        // This test simulates the scenario from issue #135:
        // 1. Generate a key pair
        // 2. Import a signed certificate to replace the self-signed certificate

        File keystoreFile = new File(tempFolder, "keypair-test.jks");
        char[] password = "changeit".toCharArray();
        String alias = "localhost";

        // Step 1: Create a keystore with a key pair (private key + self-signed certificate)
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, password);

        // Generate a key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        // Create a self-signed certificate
        X500Name subject = new X500Name("CN=localhost,OU=eng,O=apache.org");
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = new Date(System.currentTimeMillis() - 86400000L);
        Date notAfter = new Date(System.currentTimeMillis() + 86400000L * 365);

        X509v3CertificateBuilder certBuilder =
                new JcaX509v3CertificateBuilder(subject, serial, notBefore, notAfter, subject, keyPair.getPublic());

        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());
        X509CertificateHolder certHolder = certBuilder.build(signer);
        X509Certificate selfSignedCert = new JcaX509CertificateConverter().getCertificate(certHolder);

        // Store the key pair in the keystore
        Certificate[] chain = new Certificate[] {selfSignedCert};
        ks.setKeyEntry(alias, keyPair.getPrivate(), password, chain);

        // Save the keystore
        try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            ks.store(fos, password);
        }

        // Verify the keystore contains a key entry
        KeyStore verifyKs = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            verifyKs.load(fis, password);
        }
        assertTrue(verifyKs.isKeyEntry(alias), "Alias should be a key entry");
        assertNotNull(verifyKs.getKey(alias, password), "Private key should exist");

        // Step 2: Create a "signed" certificate (in reality another self-signed cert with different subject)
        // This simulates a CA-signed certificate
        X500Name signedSubject = new X500Name("CN=localhost,OU=signed,O=apache.org");
        BigInteger signedSerial = BigInteger.valueOf(System.currentTimeMillis() + 1);

        X509v3CertificateBuilder signedCertBuilder = new JcaX509v3CertificateBuilder(
                signedSubject, signedSerial, notBefore, notAfter, signedSubject, keyPair.getPublic());

        X509CertificateHolder signedCertHolder = signedCertBuilder.build(signer);
        X509Certificate signedCert = new JcaX509CertificateConverter().getCertificate(signedCertHolder);

        // Write signed certificate to file
        File signedCertFile = new File(tempFolder, "signed-cert.pem");
        try (FileOutputStream fos = new FileOutputStream(signedCertFile)) {
            fos.write("-----BEGIN CERTIFICATE-----\n".getBytes());
            fos.write(Base64.getMimeEncoder()
                    .encodeToString(signedCert.getEncoded())
                    .getBytes());
            fos.write("\n-----END CERTIFICATE-----\n".getBytes());
        }

        // Step 3: Import the signed certificate as a certificate reply
        // This should replace the certificate in the key pair without affecting the private key
        service.importCertificate(keystoreFile, "JKS", password, alias, signedCertFile, false, null);

        // Step 4: Verify the certificate was replaced but the private key is still there
        KeyStore finalKs = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            finalKs.load(fis, password);
        }

        assertTrue(finalKs.isKeyEntry(alias), "Alias should still be a key entry");
        assertNotNull(finalKs.getKey(alias, password), "Private key should still exist");

        Certificate[] finalChain = finalKs.getCertificateChain(alias);
        assertNotNull(finalChain, "Certificate chain should exist");
        assertTrue(finalChain.length > 0, "Certificate chain should not be empty");

        // Verify the certificate was actually replaced (check the subject DN)
        X509Certificate finalCert = (X509Certificate) finalChain[0];
        assertTrue(
                finalCert.getSubjectDN().toString().contains("OU=signed"),
                "Certificate should be the signed one, not the self-signed one");
    }

    /**
     * Creates a simple self-signed test certificate file using Bouncy Castle.
     */
    private File createTestCertificateFile() throws Exception {
        File certFile = new File(tempFolder, "test-cert.cer");

        // Generate a keypair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        // Create a self-signed certificate using Bouncy Castle
        X500Name issuer = new X500Name("CN=Test,O=Test,C=US");
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = new Date(System.currentTimeMillis() - 86400000L);
        Date notAfter = new Date(System.currentTimeMillis() + 86400000L * 365);

        X509v3CertificateBuilder certBuilder =
                new JcaX509v3CertificateBuilder(issuer, serial, notBefore, notAfter, issuer, keyPair.getPublic());

        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());
        X509CertificateHolder certHolder = certBuilder.build(signer);
        X509Certificate cert = new JcaX509CertificateConverter().getCertificate(certHolder);

        // Write certificate to PEM file
        try (FileOutputStream fos = new FileOutputStream(certFile)) {
            fos.write("-----BEGIN CERTIFICATE-----\n".getBytes());
            fos.write(Base64.getMimeEncoder().encodeToString(cert.getEncoded()).getBytes());
            fos.write("\n-----END CERTIFICATE-----\n".getBytes());
        }

        return certFile;
    }
}
