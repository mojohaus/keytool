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

import javax.crypto.SecretKey;

import java.io.File;
import java.security.KeyStore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SecretKeyService.
 */
public class SecretKeyServiceTest {

    @TempDir
    public File tempFolder;

    private SecretKeyService service;

    @BeforeEach
    void setUp() {
        service = new SecretKeyService();
    }

    @Test
    void serviceInstantiation() {
        assertNotNull(service, "Service should not be null");
    }

    @Test
    void generateSecretKeyAES() throws Exception {
        File keystoreFile = new File(tempFolder, "secret-aes.jks");
        String password = "changeit";

        service.generateSecretKey(
                keystoreFile,
                "JCEKS", // JCEKS supports secret keys
                password,
                "secretkey",
                "AES",
                "256",
                "keypass");

        // Verify keystore was created and contains the secret key
        assertTrue(keystoreFile.exists(), "Keystore should exist");

        KeyStore ks = KeyStore.getInstance("JCEKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
            ks.load(fis, password.toCharArray());
        }

        assertTrue(ks.containsAlias("secretkey"), "Secret key should exist in keystore");

        SecretKey key = (SecretKey) ks.getKey("secretkey", "keypass".toCharArray());
        assertNotNull(key, "Secret key should not be null");
        assertEquals("AES", key.getAlgorithm(), "Algorithm should be AES");
    }

    @Test
    void generateSecretKeyDES() throws Exception {
        File keystoreFile = new File(tempFolder, "secret-des.jks");
        String password = "changeit";

        service.generateSecretKey(keystoreFile, "JCEKS", password, "deskey", "DES", "56", "keypass");

        // Verify keystore was created
        assertTrue(keystoreFile.exists(), "Keystore should exist");

        KeyStore ks = KeyStore.getInstance("JCEKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
            ks.load(fis, password.toCharArray());
        }

        assertTrue(ks.containsAlias("deskey"), "Secret key should exist");

        SecretKey key = (SecretKey) ks.getKey("deskey", "keypass".toCharArray());
        assertNotNull(key, "Secret key should not be null");
        assertEquals("DES", key.getAlgorithm(), "Algorithm should be DES");
    }

    @Test
    void generateSecretKeyDESede() throws Exception {
        File keystoreFile = new File(tempFolder, "secret-3des.jks");
        String password = "changeit";

        service.generateSecretKey(keystoreFile, "JCEKS", password, "3deskey", "DESede", "168", "keypass");

        // Verify keystore was created
        assertTrue(keystoreFile.exists(), "Keystore should exist");

        KeyStore ks = KeyStore.getInstance("JCEKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
            ks.load(fis, password.toCharArray());
        }

        assertTrue(ks.containsAlias("3deskey"), "Secret key should exist");

        SecretKey key = (SecretKey) ks.getKey("3deskey", "keypass".toCharArray());
        assertNotNull(key, "Secret key should not be null");
        assertEquals("DESede", key.getAlgorithm(), "Algorithm should be DESede");
    }

    @Test
    void generateSecretKeyInvalidAlgorithm() {
        File keystoreFile = new File(tempFolder, "invalid.jks");
        String password = "changeit";
        assertThrows(
                Exception.class,
                () -> service.generateSecretKey(
                        keystoreFile, "JCEKS", password, "key", "INVALID_ALGORITHM", "128", "keypass"));
    }

    @Test
    void generateSecretKeyInExistingKeystore() throws Exception {
        File keystoreFile = new File(tempFolder, "multi-keys.jks");
        String password = "changeit";

        // Generate first key
        service.generateSecretKey(keystoreFile, "JCEKS", password, "key1", "AES", "128", "keypass");

        // Generate second key in same keystore
        service.generateSecretKey(keystoreFile, "JCEKS", password, "key2", "AES", "256", "keypass");

        // Verify both keys exist
        KeyStore ks = KeyStore.getInstance("JCEKS");
        try (java.io.FileInputStream fis = new java.io.FileInputStream(keystoreFile)) {
            ks.load(fis, password.toCharArray());
        }

        assertTrue(ks.containsAlias("key1"), "First key should exist");
        assertTrue(ks.containsAlias("key2"), "Second key should exist");
        assertEquals(2, ks.size(), "Should have 2 entries");
    }
}
