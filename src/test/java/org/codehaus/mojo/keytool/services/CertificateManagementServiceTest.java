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
import java.io.FileOutputStream;
import java.security.KeyStore;

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

        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, false);

        // Verify keystore was created and contains the certificate
        assertTrue(keystoreFile.exists(), "Keystore should exist");

        KeyStore ks = KeyStore.getInstance("JKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
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
        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, false);

        // Second import with skipIfExists=true
        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, true);
    }

    @Test
    void exportCertificate() throws Exception {
        File keystoreFile = new File(tempFolder, "export-test.jks");
        File certFile = createTestCertificateFile();
        File exportFile = new File(tempFolder, "exported.cer");

        char[] password = "changeit".toCharArray();
        String alias = "testcert";

        // First import a certificate
        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, false);

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
        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, false);

        // Verify it exists
        KeyStore ks = KeyStore.getInstance("JKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
            ks.load(fis, password);
        }
        assertTrue(ks.containsAlias(alias), "Certificate should exist before deletion");

        // Delete the alias
        service.deleteAlias(keystoreFile, "JKS", password, alias);

        // Verify it's gone
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
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
                        keystoreFile, "JKS", "changeit".toCharArray(), "test", certFile, false));
    }

    /**
     * Creates a simple self-signed test certificate file using Bouncy Castle.
     */
    private File createTestCertificateFile() throws Exception {
        File certFile = new File(tempFolder, "test-cert.cer");

        // Generate a keypair
        java.security.KeyPairGenerator keyGen = java.security.KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        java.security.KeyPair keyPair = keyGen.generateKeyPair();

        // Create a self-signed certificate using Bouncy Castle
        org.bouncycastle.asn1.x500.X500Name issuer = new org.bouncycastle.asn1.x500.X500Name("CN=Test,O=Test,C=US");
        java.math.BigInteger serial = java.math.BigInteger.valueOf(System.currentTimeMillis());
        java.util.Date notBefore = new java.util.Date(System.currentTimeMillis() - 86400000L);
        java.util.Date notAfter = new java.util.Date(System.currentTimeMillis() + 86400000L * 365);

        org.bouncycastle.cert.X509v3CertificateBuilder certBuilder =
                new org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder(
                        issuer, serial, notBefore, notAfter, issuer, keyPair.getPublic());

        org.bouncycastle.operator.ContentSigner signer = new org.bouncycastle.operator.jcajce.JcaContentSignerBuilder(
                        "SHA256WithRSA")
                .build(keyPair.getPrivate());
        org.bouncycastle.cert.X509CertificateHolder certHolder = certBuilder.build(signer);
        java.security.cert.X509Certificate cert =
                new org.bouncycastle.cert.jcajce.JcaX509CertificateConverter().getCertificate(certHolder);

        // Write certificate to PEM file
        try (FileOutputStream fos = new FileOutputStream(certFile)) {
            fos.write("-----BEGIN CERTIFICATE-----\n".getBytes());
            fos.write(java.util.Base64.getMimeEncoder()
                    .encodeToString(cert.getEncoded())
                    .getBytes());
            fos.write("\n-----END CERTIFICATE-----\n".getBytes());
        }

        return certFile;
    }
}
