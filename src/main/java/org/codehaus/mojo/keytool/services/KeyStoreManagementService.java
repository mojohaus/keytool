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
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for managing keystores (list, change alias, change passwords, import keystore).
 * Follows Single Responsibility Principle (SOLID-S).
 *
 * @since 2.0
 */
@Named
@Singleton
public class KeyStoreManagementService {

    private static final Logger log = LoggerFactory.getLogger(KeyStoreManagementService.class);

    /**
     * List all aliases in a keystore.
     */
    public void listAliases(File keystoreFile, String keystoreType, char[] password, String specificAlias)
            throws Exception {

        KeyStore keystore = loadKeyStore(keystoreFile, keystoreType, password);

        if (specificAlias != null && !specificAlias.isEmpty()) {
            listSingleAlias(keystore, specificAlias);
        } else {
            listAllAliases(keystore);
        }
    }

    /**
     * Change alias name.
     */
    public void changeAlias(
            File keystoreFile,
            String keystoreType,
            char[] storePassword,
            String alias,
            String destAlias,
            char[] keyPassword)
            throws Exception {

        KeyStore keystore = loadKeyStore(keystoreFile, keystoreType, storePassword);

        if (!keystore.containsAlias(alias)) {
            throw new KeyStoreException("Source alias '" + alias + "' does not exist");
        }

        if (keystore.containsAlias(destAlias)) {
            throw new KeyStoreException("Destination alias '" + destAlias + "' already exists");
        }

        Key key = keystore.getKey(alias, keyPassword);
        Certificate[] chain = keystore.getCertificateChain(alias);

        if (key != null) {
            keystore.setKeyEntry(destAlias, key, keyPassword, chain);
        } else {
            Certificate cert = keystore.getCertificate(alias);
            keystore.setCertificateEntry(destAlias, cert);
        }

        keystore.deleteEntry(alias);
        saveKeyStore(keystore, keystoreFile, storePassword);

        log.info("Alias changed from '{}' to '{}'", alias, destAlias);
    }

    /**
     * Change key password.
     */
    public void changeKeyPassword(
            File keystoreFile,
            String keystoreType,
            char[] storePassword,
            String alias,
            char[] oldKeyPassword,
            char[] newKeyPassword)
            throws Exception {

        KeyStore keystore = loadKeyStore(keystoreFile, keystoreType, storePassword);

        if (!keystore.containsAlias(alias)) {
            throw new KeyStoreException("Alias '" + alias + "' does not exist");
        }

        Key key = keystore.getKey(alias, oldKeyPassword);
        Certificate[] chain = keystore.getCertificateChain(alias);

        keystore.setKeyEntry(alias, key, newKeyPassword, chain);
        saveKeyStore(keystore, keystoreFile, storePassword);

        log.info("Key password changed for alias: {}", alias);
    }

    /**
     * Change store password.
     */
    public void changeStorePassword(File keystoreFile, String keystoreType, char[] oldPassword, char[] newPassword)
            throws Exception {

        KeyStore keystore = loadKeyStore(keystoreFile, keystoreType, oldPassword);
        saveKeyStore(keystore, keystoreFile, newPassword);

        log.info("Keystore password changed");
    }

    /**
     * Import keystore.
     */
    public void importKeystore(
            File sourceKeystoreFile,
            String sourceType,
            char[] sourcePassword,
            File destKeystoreFile,
            String destType,
            char[] destPassword,
            String sourceAlias,
            String destAlias,
            char[] sourceKeyPassword,
            char[] destKeyPassword)
            throws Exception {

        KeyStore sourceKs = loadKeyStore(sourceKeystoreFile, sourceType, sourcePassword);
        KeyStore destKs = loadOrCreateKeyStore(destKeystoreFile, destType, destPassword);

        if (sourceAlias != null && !sourceAlias.isEmpty()) {
            importSingleEntry(sourceKs, destKs, sourceAlias, destAlias, sourceKeyPassword, destKeyPassword);
        } else {
            importAllEntries(sourceKs, destKs, sourceKeyPassword, destKeyPassword);
        }

        saveKeyStore(destKs, destKeystoreFile, destPassword);
        log.info("Keystore import completed");
    }

    private void listSingleAlias(KeyStore keystore, String alias) throws KeyStoreException {
        if (!keystore.containsAlias(alias)) {
            log.warn("Alias '{}' does not exist in keystore", alias);
            return;
        }

        log.info("Alias: {}", alias);
        log.info("  Type: {}", keystore.getType());

        if (keystore.isKeyEntry(alias)) {
            log.info("  Entry type: PrivateKeyEntry");
            Certificate[] chain = keystore.getCertificateChain(alias);
            if (chain != null) {
                log.info("  Certificate chain length: {}", chain.length);
            }
        } else if (keystore.isCertificateEntry(alias)) {
            log.info("  Entry type: TrustedCertificateEntry");
            Certificate cert = keystore.getCertificate(alias);
            if (cert != null) {
                log.info("  Certificate type: {}", cert.getType());
            }
        }
    }

    private void listAllAliases(KeyStore keystore) throws KeyStoreException {
        Enumeration<String> aliases = keystore.aliases();
        int count = 0;

        log.info("Keystore type: {}", keystore.getType());
        log.info("Keystore provider: {}", keystore.getProvider().getName());
        log.info("");

        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            count++;

            log.info("Alias {}: {}", count, alias);
            if (keystore.isKeyEntry(alias)) {
                log.info("  Entry type: PrivateKeyEntry");
            } else if (keystore.isCertificateEntry(alias)) {
                log.info("  Entry type: TrustedCertificateEntry");
            }
        }

        log.info("");
        log.info("Total entries: {}", count);
    }

    private void importSingleEntry(
            KeyStore sourceKs,
            KeyStore destKs,
            String sourceAlias,
            String destAlias,
            char[] sourceKeyPassword,
            char[] destKeyPassword)
            throws Exception {

        String targetAlias = (destAlias != null && !destAlias.isEmpty()) ? destAlias : sourceAlias;

        if (sourceKs.isKeyEntry(sourceAlias)) {
            Key key = sourceKs.getKey(sourceAlias, sourceKeyPassword);
            Certificate[] chain = sourceKs.getCertificateChain(sourceAlias);
            destKs.setKeyEntry(targetAlias, key, destKeyPassword != null ? destKeyPassword : sourceKeyPassword, chain);
        } else {
            Certificate cert = sourceKs.getCertificate(sourceAlias);
            destKs.setCertificateEntry(targetAlias, cert);
        }

        log.info("Imported entry: {} -> {}", sourceAlias, targetAlias);
    }

    private void importAllEntries(KeyStore sourceKs, KeyStore destKs, char[] sourceKeyPassword, char[] destKeyPassword)
            throws Exception {

        Enumeration<String> aliases = sourceKs.aliases();
        int count = 0;

        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();

            if (sourceKs.isKeyEntry(alias)) {
                Key key = sourceKs.getKey(alias, sourceKeyPassword);
                Certificate[] chain = sourceKs.getCertificateChain(alias);
                destKs.setKeyEntry(alias, key, destKeyPassword != null ? destKeyPassword : sourceKeyPassword, chain);
            } else {
                Certificate cert = sourceKs.getCertificate(alias);
                destKs.setCertificateEntry(alias, cert);
            }

            count++;
        }

        log.info("Imported {} entries", count);
    }

    private KeyStore loadKeyStore(File keystoreFile, String keystoreType, char[] password) throws Exception {

        if (!keystoreFile.exists()) {
            throw new IOException("Keystore file does not exist: " + keystoreFile);
        }

        KeyStore keystore = KeyStore.getInstance(keystoreType != null ? keystoreType : KeyStore.getDefaultType());

        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            keystore.load(fis, password);
        }

        return keystore;
    }

    private KeyStore loadOrCreateKeyStore(File keystoreFile, String keystoreType, char[] password) throws Exception {

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

    private void saveKeyStore(KeyStore keystore, File keystoreFile, char[] password) throws Exception {

        File parentDir = keystoreFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            keystore.store(fos, password);
        }
    }
}
