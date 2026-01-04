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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for managing certificates in keystores using Java KeyStore API.
 * Follows Single Responsibility Principle (SOLID-S).
 *
 * @since 2.0
 */
@Named
@Singleton
public class CertificateManagementService {

    private static final Logger log = LoggerFactory.getLogger(CertificateManagementService.class);

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

        KeyStore keystore = loadOrCreateKeyStore(keystoreFile, keystoreType, keystorePassword);

        if (skipIfAliasExists && keystore.containsAlias(alias)) {
            log.info("Certificate alias '{}' already exists in keystore. Skipping import.", alias);
            return;
        }

        Certificate certificate = loadCertificate(certificateFile);
        keystore.setCertificateEntry(alias, certificate);
        saveKeyStore(keystore, keystoreFile, keystorePassword);

        log.info("Certificate was added to keystore");
    }

    /**
     * Export a certificate from keystore to file.
     *
     * @param keystoreFile keystore file
     * @param keystoreType keystore type
     * @param keystorePassword keystore password
     * @param alias certificate alias
     * @param outputFile output file for certificate
     * @throws Exception if operation fails
     */
    public void exportCertificate(
            File keystoreFile, String keystoreType, char[] keystorePassword, String alias, File outputFile)
            throws Exception {

        KeyStore keystore = loadOrCreateKeyStore(keystoreFile, keystoreType, keystorePassword);

        if (!keystore.containsAlias(alias)) {
            throw new KeyStoreException("Alias '" + alias + "' does not exist in keystore");
        }

        Certificate cert = keystore.getCertificate(alias);
        if (cert == null) {
            throw new KeyStoreException("No certificate found for alias: " + alias);
        }

        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(cert.getEncoded());
        }

        log.info("Certificate exported to: {}", outputFile.getAbsolutePath());
    }

    /**
     * Delete an alias from keystore.
     *
     * @param keystoreFile keystore file
     * @param keystoreType keystore type
     * @param keystorePassword keystore password
     * @param alias alias to delete
     * @throws Exception if operation fails
     */
    public void deleteAlias(File keystoreFile, String keystoreType, char[] keystorePassword, String alias)
            throws Exception {

        KeyStore keystore = loadOrCreateKeyStore(keystoreFile, keystoreType, keystorePassword);

        if (!keystore.containsAlias(alias)) {
            log.warn("Alias '{}' does not exist in keystore", alias);
            return;
        }

        keystore.deleteEntry(alias);
        saveKeyStore(keystore, keystoreFile, keystorePassword);

        log.info("Deleted alias: {}", alias);
    }

    /**
     * Load or create keystore.
     */
    private KeyStore loadOrCreateKeyStore(File keystoreFile, String keystoreType, char[] password)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {

        KeyStore keystore = KeyStore.getInstance(keystoreType != null ? keystoreType : KeyStore.getDefaultType());

        if (keystoreFile.exists()) {
            try (FileInputStream fis = new FileInputStream(keystoreFile)) {
                keystore.load(fis, password);
            }
        } else {
            keystore.load(null, password);
        }

        return keystore;
    }

    /**
     * Load certificate from file.
     */
    private Certificate loadCertificate(File certificateFile) throws IOException, CertificateException {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

        try (InputStream is = Files.newInputStream(certificateFile.toPath())) {
            return certFactory.generateCertificate(is);
        }
    }

    /**
     * Save keystore to file.
     */
    private void saveKeyStore(KeyStore keystore, File keystoreFile, char[] password)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {

        File parentDir = keystoreFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            keystore.store(fos, password);
        }
    }
}
