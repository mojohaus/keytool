package org.codehaus.mojo.keytool;

/*
 * Copyright 2005-2013 The Codehaus
 *
 * Licensed under the Apache License, Version 2.0 (the "License" );
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.mojo.keytool.api.*;

/**
 * Service for performing KeyStore operations using Java's KeyStore API.
 * This replaces the external keytool command-line tool invocations.
 *
 * @since 1.8
 */
public class KeyStoreService {

    private final Log log;

    /**
     * Constructor.
     *
     * @param log Maven logger
     */
    public KeyStoreService(Log log) {
        this.log = log;
    }

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

        // Load the keystore
        KeyStore keystore = loadKeyStore(keystoreFile, keystoreType, keystorePassword);

        // Check if alias already exists
        if (skipIfAliasExists && keystore.containsAlias(alias)) {
            log.info("Certificate alias '" + alias + "' already exists in keystore. Skipping import.");
            return;
        }

        // Load the certificate
        Certificate certificate = loadCertificate(certificateFile);

        // Import the certificate
        keystore.setCertificateEntry(alias, certificate);

        // Save the keystore
        saveKeyStore(keystore, keystoreFile, keystorePassword);

        log.info("Certificate was added to keystore");
    }

    /**
     * Import a certificate into a keystore.
     * This is a convenience method that calls {@link #importCertificate(File, String, char[], String, File, boolean)}
     * with skipIfAliasExists set to false.
     *
     * @param keystoreFile keystore file
     * @param keystoreType keystore type (e.g., "JKS", "PKCS12")
     * @param keystorePassword keystore password
     * @param alias certificate alias
     * @param certificateFile certificate file to import
     * @throws KeyStoreException if keystore operation fails
     * @throws IOException if file operations fail
     * @throws NoSuchAlgorithmException if algorithm is not available
     * @throws CertificateException if certificate is invalid
     */
    public void importCertificate(
            File keystoreFile, String keystoreType, char[] keystorePassword, String alias, File certificateFile)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        importCertificate(keystoreFile, keystoreType, keystorePassword, alias, certificateFile, false);
    }

    /**
     * Load a keystore from file.
     *
     * @param keystoreFile keystore file
     * @param keystoreType keystore type
     * @param password keystore password
     * @return loaded keystore
     * @throws KeyStoreException if keystore operation fails
     * @throws IOException if file operations fail
     * @throws NoSuchAlgorithmException if algorithm is not available
     * @throws CertificateException if certificate is invalid
     */
    private KeyStore loadKeyStore(File keystoreFile, String keystoreType, char[] password)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {

        KeyStore keystore = KeyStore.getInstance(keystoreType != null ? keystoreType : KeyStore.getDefaultType());

        if (keystoreFile.exists()) {
            try (FileInputStream fis = new FileInputStream(keystoreFile)) {
                keystore.load(fis, password);
            }
        } else {
            // Create a new keystore
            keystore.load(null, password);
        }

        return keystore;
    }

    /**
     * Load a certificate from file.
     *
     * @param certificateFile certificate file
     * @return loaded certificate
     * @throws IOException if file operations fail
     * @throws CertificateException if certificate is invalid
     */
    private Certificate loadCertificate(File certificateFile) throws IOException, CertificateException {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

        try (InputStream is = new FileInputStream(certificateFile)) {
            return certFactory.generateCertificate(is);
        }
    }

    /**
     * Save a keystore to file.
     *
     * @param keystore keystore to save
     * @param keystoreFile target file
     * @param password keystore password
     * @throws KeyStoreException if keystore operation fails
     * @throws IOException if file operations fail
     * @throws NoSuchAlgorithmException if algorithm is not available
     * @throws CertificateException if certificate is invalid
     */
    private void saveKeyStore(KeyStore keystore, File keystoreFile, char[] password)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {

        // Ensure parent directory exists
        File parentDir = keystoreFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            keystore.store(fos, password);
        }
    }
}
