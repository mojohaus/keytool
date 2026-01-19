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
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for CertificateGenerationService.
 */
public class CertificateGenerationServiceTest {

    @TempDir
    public File tempFolder;

    private CertificateGenerationService service;

    @BeforeEach
    void setUp() {
        service = new CertificateGenerationService();
    }

    @Test
    void serviceInstantiation() {
        assertNotNull(service, "Service should not be null");
    }

    @Test
    void generateKeyPairRSA() throws Exception {
        File keystoreFile = new File(tempFolder, "keypair-rsa.jks");
        char[] password = "changeit".toCharArray();

        service.generateKeyPair(
                keystoreFile,
                "JKS",
                password,
                "testkey",
                "RSA",
                2048,
                "SHA256WithRSA",
                "CN=Test,O=Test,C=US",
                90,
                password,
                null);

        // Verify keystore was created
        assertTrue(keystoreFile.exists(), "Keystore should exist");

        KeyStore ks = KeyStore.getInstance("JKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
            ks.load(fis, password);
        }

        assertTrue(ks.containsAlias("testkey"), "Key should exist in keystore");
        assertNotNull(ks.getCertificate("testkey"), "Should have certificate");
        assertNotNull(ks.getKey("testkey", password), "Should have private key");
    }

    @Test
    void generateKeyPairDSA() throws Exception {
        File keystoreFile = new File(tempFolder, "keypair-dsa.jks");
        char[] password = "changeit".toCharArray();

        service.generateKeyPair(
                keystoreFile,
                "JKS",
                password,
                "dsakey",
                "DSA",
                1024,
                "SHA256WithDSA",
                "CN=DSA Test,O=Test,C=US",
                30,
                password,
                null);

        // Verify keystore was created
        assertTrue(keystoreFile.exists(), "Keystore should exist");

        KeyStore ks = KeyStore.getInstance("JKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
            ks.load(fis, password);
        }

        assertTrue(ks.containsAlias("dsakey"), "Key should exist");
        assertNotNull(ks.getCertificate("dsakey"), "Should have certificate");
    }

    @Test
    void generateKeyPairWithExtensions() throws Exception {
        File keystoreFile = new File(tempFolder, "keypair-ext.jks");
        char[] password = "changeit".toCharArray();

        service.generateKeyPair(
                keystoreFile,
                "JKS",
                password,
                "extkey",
                "RSA",
                2048,
                "SHA256WithRSA",
                "CN=Test With Extensions,O=Test,C=US",
                365,
                password,
                Arrays.asList("bc:c", "ku:c=digitalSignature,keyEncipherment"));

        // Verify keystore was created
        assertTrue(keystoreFile.exists(), "Keystore should exist");

        KeyStore ks = KeyStore.getInstance("JKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
            ks.load(fis, password);
        }

        assertTrue(ks.containsAlias("extkey"), "Key should exist");
        assertNotNull(ks.getCertificate("extkey"), "Should have certificate");

        // Verify extensions are present
        X509Certificate cert = (X509Certificate) ks.getCertificate("extkey");

        // Check Basic Constraints extension (bc:c)
        assertNotNull(cert.getBasicConstraints(), "Basic Constraints extension should be present");
        assertTrue(cert.getCriticalExtensionOIDs().contains("2.5.29.19"), "Basic Constraints should be critical");

        // Check Key Usage extension (ku:c=digitalSignature,keyEncipherment)
        boolean[] keyUsage = cert.getKeyUsage();
        assertNotNull(keyUsage, "Key Usage extension should be present");
        assertTrue(cert.getCriticalExtensionOIDs().contains("2.5.29.15"), "Key Usage should be critical");
        assertTrue(keyUsage[0], "digitalSignature should be set");
        assertTrue(keyUsage[2], "keyEncipherment should be set");
    }

    @Test
    void generateCertificateRequest() throws Exception {
        // First create a keypair
        File keystoreFile = new File(tempFolder, "csr.jks");
        File csrFile = new File(tempFolder, "request.csr");
        char[] password = "changeit".toCharArray();

        service.generateKeyPair(
                keystoreFile,
                "JKS",
                password,
                "csrkey",
                "RSA",
                2048,
                "SHA256WithRSA",
                "CN=CSR Test,O=Test,C=US",
                90,
                password,
                null);

        // Generate CSR
        service.generateCertificateRequest(
                keystoreFile,
                "JKS",
                password,
                "csrkey",
                password,
                "CN=CSR Test,O=Test,C=US",
                "SHA256WithRSA",
                csrFile,
                null);

        // Verify CSR file was created
        assertTrue(csrFile.exists(), "CSR file should exist");
        assertTrue(csrFile.length() > 0, "CSR file should not be empty");

        // Verify it contains PEM header
        String content = new String(Files.readAllBytes(csrFile.toPath()));
        assertTrue(content.contains("BEGIN") && content.contains("REQUEST"), "CSR should contain BEGIN marker");
    }

    @Test
    void generateCertificate() throws Exception {
        // First create a keypair and CSR
        File keystoreFile = new File(tempFolder, "cert.jks");
        File csrFile = new File(tempFolder, "request.csr");
        File certFile = new File(tempFolder, "certificate.cer");
        char[] password = "changeit".toCharArray();

        service.generateKeyPair(
                keystoreFile,
                "JKS",
                password,
                "certkey",
                "RSA",
                2048,
                "SHA256WithRSA",
                "CN=Cert Test,O=Test,C=US",
                90,
                password,
                null);

        service.generateCertificateRequest(
                keystoreFile,
                "JKS",
                password,
                "certkey",
                password,
                "CN=Cert Test,O=Test,C=US",
                "SHA256WithRSA",
                csrFile,
                null);

        // Generate certificate from CSR
        service.generateCertificate(
                keystoreFile,
                "JKS",
                password,
                "certkey",
                password,
                csrFile,
                certFile,
                "CN=Cert Test,O=Test,C=US",
                30,
                "SHA256WithRSA",
                false,
                null);

        // Verify certificate file was created
        assertTrue(certFile.exists(), "Certificate file should exist");
        assertTrue(certFile.length() > 0, "Certificate file should not be empty");
    }

    @Test
    void generateKeyPairInvalidKeySize() {
        File keystoreFile = new File(tempFolder, "invalid.jks");
        char[] password = "changeit".toCharArray();

        try {
            // RSA key size too small (may or may not throw depending on provider)
            service.generateKeyPair(
                    keystoreFile, "JKS", password, "key", "RSA", 512, "SHA256WithRSA", "CN=Test", 90, password, null);
            // If it doesn't throw, that's also acceptable (some providers allow it)
        } catch (Exception e) {
            assertTrue(
                    e.getMessage().contains("key size") || e.getMessage().contains("KeyPairGenerator"),
                    "Exception should indicate invalid key size");
        }
    }

    @Test
    void generateCertificateRequestNonExistentAlias() throws Exception {
        File keystoreFile = new File(tempFolder, "empty.jks");
        File csrFile = new File(tempFolder, "request.csr");
        char[] password = "changeit".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, password);
        try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            ks.store(fos, password);
        }
        assertThrows(
                Exception.class,
                () ->
                        // Try to generate CSR for non-existent alias
                        service.generateCertificateRequest(
                                keystoreFile,
                                "JKS",
                                password,
                                "nonexistent",
                                password,
                                "CN=Test",
                                "SHA256WithRSA",
                                csrFile,
                                null));
    }
}
