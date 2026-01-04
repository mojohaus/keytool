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
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for KeyStoreManagementService.
 */
public class KeyStoreManagementServiceTest {

    @TempDir
    public File tempFolder;

    private KeyStoreManagementService service;

    @BeforeEach
    void setUp() {
        service = new KeyStoreManagementService();
    }

    @Test
    void serviceInstantiation() {
        assertNotNull(service, "Service should not be null");
    }

    @Test
    void listAliases() throws Exception {
        File keystoreFile = createTestKeyStore();
        char[] password = "changeit".toCharArray();

        service.listAliases(keystoreFile, "JKS", password, null);
    }

    @Test
    void changeAlias() throws Exception {
        File keystoreFile = createTestKeyStore();
        char[] password = "changeit".toCharArray();

        String oldAlias = "testkey";
        String newAlias = "newkey";

        service.changeAlias(keystoreFile, "JKS", password, oldAlias, newAlias, password);

        // Verify alias was changed
        KeyStore ks = KeyStore.getInstance("JKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
            ks.load(fis, password);
        }

        assertFalse(ks.containsAlias(oldAlias), "Old alias should not exist");
        assertTrue(ks.containsAlias(newAlias), "New alias should exist");
    }

    @Test
    void changeKeyPassword() throws Exception {
        File keystoreFile = createTestKeyStore();
        char[] storePassword = "changeit".toCharArray();
        char[] oldKeyPassword = "changeit".toCharArray();
        char[] newKeyPassword = "newpass".toCharArray();

        String alias = "testkey";

        service.changeKeyPassword(keystoreFile, "JKS", storePassword, alias, oldKeyPassword, newKeyPassword);

        // Verify we can access the key with new password
        KeyStore ks = KeyStore.getInstance("JKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
            ks.load(fis, storePassword);
        }

        assertNotNull(ks.getKey(alias, newKeyPassword), "Should be able to get key with new password");
    }

    @Test
    void changeStorePassword() throws Exception {
        File keystoreFile = createTestKeyStore();
        char[] oldPassword = "changeit".toCharArray();
        char[] newPassword = "newstorepass".toCharArray();

        service.changeStorePassword(keystoreFile, "JKS", oldPassword, newPassword);

        // Verify we can load keystore with new password
        KeyStore ks = KeyStore.getInstance("JKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
            ks.load(fis, newPassword);
        }

        assertTrue(ks.size() > 0, "Should be able to load with new password");
    }

    @Test
    void importKeystore() throws Exception {
        File sourceKeystoreFile = createTestKeyStore();
        File destKeystoreFile = new File(tempFolder, "dest.jks");

        char[] password = "changeit".toCharArray();

        service.importKeystore(
                sourceKeystoreFile, "JKS", password, destKeystoreFile, "JKS", password, null, null, password, password);

        // Verify destination keystore exists and contains the entry
        assertTrue(destKeystoreFile.exists(), "Destination keystore should exist");

        KeyStore ks = KeyStore.getInstance("JKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(destKeystoreFile)) {
            ks.load(fis, password);
        }

        assertTrue(ks.containsAlias("testkey"), "Should contain imported alias");
    }

    @Test
    void listAliasesWithWrongPassword() throws Exception {
        File keystoreFile = createTestKeyStore();
        char[] wrongPassword = "wrongpass".toCharArray();
        assertThrows(Exception.class, () -> service.listAliases(keystoreFile, "JKS", wrongPassword, null));
    }

    /**
     * Creates a test keystore with one key entry.
     */
    private File createTestKeyStore() throws Exception {
        File keystoreFile = new File(tempFolder, "test.jks");
        char[] password = "changeit".toCharArray();

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, password);

        // Generate a simple key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        // Create a simple self-signed certificate
        X509Certificate cert = createSelfSignedCertificate(keyPair);

        // Store the key entry
        ks.setKeyEntry("testkey", keyPair.getPrivate(), password, new Certificate[] {cert});

        // Save keystore
        try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            ks.store(fos, password);
        }

        return keystoreFile;
    }

    private X509Certificate createSelfSignedCertificate(KeyPair keyPair) throws Exception {
        // Use Bouncy Castle to create a simple self-signed cert
        BouncyCastleProvider bcProvider = new BouncyCastleProvider();
        Security.addProvider(bcProvider);

        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Date endDate = new Date(now + 365L * 24 * 60 * 60 * 1000);

        org.bouncycastle.asn1.x500.X500Name dnName = new org.bouncycastle.asn1.x500.X500Name("CN=Test,O=Test,C=US");
        BigInteger certSerialNumber = BigInteger.valueOf(now);

        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                dnName, certSerialNumber, startDate, endDate, dnName, keyPair.getPublic());

        ContentSigner contentSigner =
                new JcaContentSignerBuilder("SHA256WithRSA").setProvider("BC").build(keyPair.getPrivate());

        X509CertificateHolder certHolder = certBuilder.build(contentSigner);

        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
    }
}
