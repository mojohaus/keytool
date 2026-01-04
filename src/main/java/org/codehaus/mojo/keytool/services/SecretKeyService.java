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

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.inject.Named;
import javax.inject.Singleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;

import org.apache.maven.plugin.MojoExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for secret key operations using Java KeyStore API.
 */
@Named
@Singleton
public class SecretKeyService {

    private static final Logger log = LoggerFactory.getLogger(SecretKeyService.class);

    /**
     * Generates a secret key and stores it in the keystore.
     *
     * @param keystore keystore location
     * @param storetype keystore type (defaults to KeyStore.getDefaultType() if null)
     * @param storepass keystore password
     * @param alias key alias
     * @param keyalg key algorithm (e.g., "AES", "DES", "DESede")
     * @param keysize key size in bits (e.g., "128", "256")
     * @param keypass key password (defaults to storepass if null)
     * @throws MojoExecutionException if operation fails
     */
    public void generateSecretKey(
            File keystore,
            String storetype,
            String storepass,
            String alias,
            String keyalg,
            String keysize,
            String keypass)
            throws MojoExecutionException {

        try {
            // Default values
            String effectiveStoreType = storetype != null ? storetype : KeyStore.getDefaultType();
            char[] storePassword = storepass != null ? storepass.toCharArray() : new char[0];
            char[] keyPassword = keypass != null ? keypass.toCharArray() : storePassword;

            // Load or create keystore
            KeyStore ks = loadOrCreateKeyStore(keystore, effectiveStoreType, storePassword);

            // Validate algorithm
            if (keyalg == null || keyalg.trim().isEmpty()) {
                throw new MojoExecutionException("Key algorithm (keyalg) is required");
            }

            // Validate alias
            if (alias == null || alias.trim().isEmpty()) {
                throw new MojoExecutionException("Alias is required");
            }

            // Generate secret key
            log.info("Generating secret key with algorithm: {}", keyalg);
            KeyGenerator keyGen = KeyGenerator.getInstance(keyalg);

            if (keysize != null && !keysize.trim().isEmpty()) {
                try {
                    int size = Integer.parseInt(keysize);
                    log.info("Initializing key generator with key size: {}", size);
                    keyGen.init(size);
                } catch (NumberFormatException e) {
                    throw new MojoExecutionException("Invalid key size: " + keysize, e);
                }
            }

            SecretKey secretKey = keyGen.generateKey();

            // Store secret key in keystore
            KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
            KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(keyPassword);

            ks.setEntry(alias, secretKeyEntry, protectionParam);

            log.info("Secret key stored with alias: {}", alias);

            // Save keystore
            saveKeyStore(ks, keystore, storePassword);

            log.info("Secret key generation completed successfully");

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate secret key", e);
        }
    }

    /**
     * Loads an existing keystore or creates a new one.
     */
    private KeyStore loadOrCreateKeyStore(File keystoreFile, String storeType, char[] storePassword) throws Exception {
        KeyStore ks = KeyStore.getInstance(storeType);

        if (keystoreFile != null && keystoreFile.exists()) {
            log.info("Loading existing keystore: {}", keystoreFile.getAbsolutePath());
            try (FileInputStream fis = new FileInputStream(keystoreFile)) {
                ks.load(fis, storePassword);
            }
        } else {
            log.info("Creating new keystore");
            ks.load(null, storePassword);

            // Create parent directories if necessary
            if (keystoreFile != null && keystoreFile.getParentFile() != null) {
                keystoreFile.getParentFile().mkdirs();
            }
        }

        return ks;
    }

    /**
     * Saves the keystore to file.
     */
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
