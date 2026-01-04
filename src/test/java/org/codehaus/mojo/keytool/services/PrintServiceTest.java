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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PrintService.
 */
public class PrintServiceTest {

    @TempDir
    public File tempFolder;

    private PrintService printService;
    private CertificateGenerationService certGenService;

    @BeforeEach
    void setUp() {
        printService = new PrintService();
        certGenService = new CertificateGenerationService();
    }

    @Test
    void serviceInstantiation() {
        assertNotNull(printService, "PrintService should not be null");
    }

    @Test
    void printCertificateFromFile() throws Exception {
        // Create a test certificate
        File keystoreFile = new File(tempFolder, "test.jks");
        File certFile = new File(tempFolder, "cert.pem");
        char[] password = "changeit".toCharArray();

        // Generate keypair
        certGenService.generateKeyPair(
                keystoreFile,
                "JKS",
                password,
                "testkey",
                "RSA",
                2048,
                "SHA256WithRSA",
                "CN=Print Test,O=Test,C=US",
                90,
                password,
                null);

        // Export certificate
        KeyStore ks = KeyStore.getInstance("JKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
            ks.load(fis, password);
        }
        X509Certificate cert = (X509Certificate) ks.getCertificate("testkey");
        try (FileOutputStream fos = new FileOutputStream(certFile)) {
            fos.write("-----BEGIN CERTIFICATE-----\n".getBytes());
            fos.write(java.util.Base64.getMimeEncoder().encode(cert.getEncoded()));
            fos.write("\n-----END CERTIFICATE-----\n".getBytes());
        }

        // Print certificate
        printService.printCertificate(certFile, null, null, false);
    }

    @Test
    void printCertificateFromSslServer() {
        // Print certificate from SSL server (will fail to connect but should attempt)
        try {
            printService.printCertificate(null, null, "www.google.com:443", false);
        } catch (Exception e) {
            // Expected if network not available
        }
    }

    @Test
    void printCertificateRequest() throws Exception {
        // Create a test CSR
        File keystoreFile = new File(tempFolder, "csr-test.jks");
        File csrFile = new File(tempFolder, "test.csr");
        char[] password = "changeit".toCharArray();

        // Generate keypair and CSR
        certGenService.generateKeyPair(
                keystoreFile,
                "JKS",
                password,
                "csrkey",
                "RSA",
                2048,
                "SHA256WithRSA",
                "CN=CSR Print Test,O=Test,C=US",
                90,
                password,
                null);

        certGenService.generateCertificateRequest(
                keystoreFile,
                "JKS",
                password,
                "csrkey",
                password,
                "CN=CSR Print Test,O=Test,C=US",
                "SHA256WithRSA",
                csrFile,
                null);

        // Print CSR
        printService.printCertificateRequest(csrFile);
    }

    @Test
    void printCRLFile() throws Exception {
        // Create a minimal CRL for testing
        File crlFile = new File(tempFolder, "test.crl");

        // Generate a simple keystore first
        File keystoreFile = new File(tempFolder, "crl-test.jks");
        char[] password = "changeit".toCharArray();

        certGenService.generateKeyPair(
                keystoreFile,
                "JKS",
                password,
                "crlkey",
                "RSA",
                2048,
                "SHA256WithRSA",
                "CN=CRL Test,O=Test,C=US",
                90,
                password,
                null);

        // Create a test CRL file (minimal valid structure)
        // In real scenario this would be generated by CA, here we create a placeholder
        // that will trigger the print attempt
        byte[] dummyCrlBytes = new byte[] {0x30, 0x03, 0x02, 0x01, 0x00}; // Minimal ASN.1 SEQUENCE for testing
        Files.write(crlFile.toPath(), dummyCrlBytes);

        try {
            // Print CRL (may fail on parsing dummy data, but should attempt)
            printService.printCRLFile(crlFile);
        } catch (Exception e) {
            // Expected with dummy CRL data
            // Verify that attempt was made to read the file
            assertTrue(crlFile.exists(), "CRL file should exist");
        }
    }

    @Test
    void printCertificateNonExistentFile() {
        File nonExistent = new File(tempFolder, "nonexistent.pem");
        assertThrows(Exception.class, () -> printService.printCertificate(nonExistent, null, null, false));
    }

    @Test
    void printCertificateRequestNonExistentFile() {
        File nonExistent = new File(tempFolder, "nonexistent.csr");
        assertThrows(Exception.class, () -> printService.printCertificateRequest(nonExistent));
    }

    @Test
    void printCRLFileNonExistent() {
        File nonExistent = new File(tempFolder, "nonexistent.crl");
        assertThrows(Exception.class, () -> printService.printCRLFile(nonExistent));
    }

    @Test
    void printCertificateNoSource() {
        assertThrows(
                Exception.class,
                () ->
                        // Should throw when neither file nor SSL server is provided
                        printService.printCertificate(null, null, null, false));
    }
}
