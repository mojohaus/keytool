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
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.maven.plugin.MojoExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for printing certificate, certificate request, and CRL information.
 * Follows SOLID principles - Single Responsibility: manages print operations.
 *
 * @since 1.7
 */
@Named
@Singleton
public class PrintService {

    private static final Logger log = LoggerFactory.getLogger(PrintService.class);

    /**
     * Prints certificate information from a file, JAR, or SSL server.
     *
     * @param file certificate file
     * @param jarfile JAR file containing certificates
     * @param sslserver SSL server (host:port format)
     * @param rfc output in RFC format
     * @throws MojoExecutionException if operation fails
     */
    public void printCertificate(File file, File jarfile, String sslserver, boolean rfc) throws MojoExecutionException {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            if (file != null) {
                printCertificateFromFile(file, rfc, cf);
            } else if (jarfile != null) {
                printCertificateFromJar(jarfile, rfc, cf);
            } else if (sslserver != null) {
                printCertificateFromSSLServer(sslserver, rfc);
            } else {
                throw new MojoExecutionException("One of file, jarfile, or sslserver must be specified");
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to print certificate", e);
        }
    }

    /**
     * Prints certificate request information from a file.
     *
     * @param file certificate request file
     * @throws MojoExecutionException if operation fails
     */
    public void printCertificateRequest(File file) throws MojoExecutionException {
        if (file == null || !file.exists()) {
            throw new MojoExecutionException("Certificate request file not found: " + file);
        }

        log.info("Reading certificate request from: {}", file.getAbsolutePath());

        try (FileInputStream fis = new FileInputStream(file)) {
            // Read all bytes (Java 8 compatible)
            byte[] data = new byte[(int) file.length()];
            int bytesRead = fis.read(data);

            if (bytesRead != data.length) {
                throw new MojoExecutionException("Could not read entire file");
            }

            // Display basic information about the certificate request
            // Full PKCS#10 parsing would require Bouncy Castle library
            log.info("Certificate request file size: {} bytes", data.length);

            // Try to extract some basic info by looking for DER structure
            if (data.length > 10) {
                log.info("First 20 bytes (hex): {}", bytesToHex(data, 20));
            }

            // Check if it's PEM encoded (starts with "-----BEGIN")
            String dataStr = new String(data, 0, Math.min(100, data.length));
            if (dataStr.startsWith("-----BEGIN")) {
                log.info("Format: PEM encoded certificate request");
            } else {
                log.info("Format: DER encoded certificate request");
            }

            log.info("Note: For detailed PKCS#10 parsing, Bouncy Castle library is required");

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to print certificate request", e);
        }
    }

    /**
     * Prints CRL (Certificate Revocation List) information from a file.
     *
     * @param file CRL file
     * @throws MojoExecutionException if operation fails
     */
    public void printCRLFile(File file) throws MojoExecutionException {
        if (file == null || !file.exists()) {
            throw new MojoExecutionException("CRL file not found: " + file);
        }

        try {
            log.info("Reading CRL from: {}", file.getAbsolutePath());

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            try (FileInputStream fis = new FileInputStream(file)) {
                X509CRL crl = (X509CRL) cf.generateCRL(fis);

                log.info("CRL Information:");
                log.info("Issuer: {}", crl.getIssuerX500Principal());
                log.info("This Update: {}", crl.getThisUpdate());
                log.info("Next Update: {}", crl.getNextUpdate());
                log.info("Signature Algorithm: {}", crl.getSigAlgName());
                log.info("Version: {}", crl.getVersion());

                if (crl.getRevokedCertificates() != null) {
                    log.info(
                            "Number of revoked certificates: {}",
                            crl.getRevokedCertificates().size());
                } else {
                    log.info("No revoked certificates");
                }
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to print CRL file", e);
        }
    }

    private void printCertificateFromFile(File file, boolean rfc, CertificateFactory cf) throws Exception {
        if (!file.exists()) {
            throw new MojoExecutionException("Certificate file not found: " + file);
        }

        log.info("Reading certificate from: {}", file.getAbsolutePath());

        try (FileInputStream fis = new FileInputStream(file)) {
            Collection<? extends Certificate> certificates = cf.generateCertificates(fis);

            if (certificates.isEmpty()) {
                log.warn("No certificates found in file");
                return;
            }

            int count = 1;
            for (Certificate cert : certificates) {
                if (certificates.size() > 1) {
                    log.info("\n=== Certificate #{} ===", count);
                }
                printCertificateInfo((X509Certificate) cert, rfc);
                count++;
            }
        }
    }

    private void printCertificateFromJar(File jarfile, boolean rfc, CertificateFactory cf) throws Exception {
        if (!jarfile.exists()) {
            throw new MojoExecutionException("JAR file not found: " + jarfile);
        }

        log.info("Reading certificates from JAR: {}", jarfile.getAbsolutePath());

        try (JarFile jar = new JarFile(jarfile, true)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    try {
                        InputStream is = jar.getInputStream(entry);
                        // Read all bytes (Java 8 compatible)
                        byte[] buffer = new byte[8192];
                        while (is.read(buffer) != -1) {
                            // Read to trigger signature verification
                        }
                        is.close();

                        Certificate[] certs = entry.getCertificates();
                        if (certs != null && certs.length > 0) {
                            log.info("\nEntry: {}", entry.getName());
                            for (Certificate cert : certs) {
                                printCertificateInfo((X509Certificate) cert, rfc);
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Error reading entry: {}", entry.getName(), e);
                    }
                }
            }
        }
    }

    private void printCertificateFromSSLServer(String sslserver, boolean rfc) throws Exception {
        String[] parts = sslserver.split(":");
        if (parts.length != 2) {
            throw new MojoExecutionException("SSL server must be in format 'host:port', got: " + sslserver);
        }

        String host = parts[0];
        int port;
        try {
            port = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new MojoExecutionException("Invalid port number: " + parts[1], e);
        }

        log.info("Connecting to SSL server: {}:{}", host, port);

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try (SSLSocket socket = (SSLSocket) factory.createSocket(host, port)) {
            socket.startHandshake();

            Certificate[] certs = socket.getSession().getPeerCertificates();
            log.info("Retrieved {} certificate(s) from server", certs.length);

            for (int i = 0; i < certs.length; i++) {
                if (certs.length > 1) {
                    log.info("\n=== Certificate #{} ===", i + 1);
                }
                printCertificateInfo((X509Certificate) certs[i], rfc);
            }
        }
    }

    private void printCertificateInfo(X509Certificate cert, boolean rfc) {
        if (rfc) {
            log.info("\n{}", certificateToRFC(cert));
        } else {
            log.info("Subject: {}", cert.getSubjectX500Principal());
            log.info("Issuer: {}", cert.getIssuerX500Principal());
            log.info("Serial Number: {}", cert.getSerialNumber().toString(16));
            log.info("Valid From: {}", cert.getNotBefore());
            log.info("Valid To: {}", cert.getNotAfter());
            log.info("Signature Algorithm: {}", cert.getSigAlgName());
            log.info("Version: {}", cert.getVersion());

            try {
                if (cert.getSubjectAlternativeNames() != null) {
                    log.info("Subject Alternative Names: {}", cert.getSubjectAlternativeNames());
                }
            } catch (Exception e) {
                log.debug("Could not get subject alternative names", e);
            }
        }
    }

    private String certificateToRFC(X509Certificate cert) {
        try {
            byte[] encoded = cert.getEncoded();
            String base64 = Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(encoded);
            return "-----BEGIN CERTIFICATE-----\n" + base64 + "\n-----END CERTIFICATE-----";
        } catch (Exception e) {
            return "Error encoding certificate: " + e.getMessage();
        }
    }

    private String bytesToHex(byte[] bytes, int maxBytes) {
        StringBuilder sb = new StringBuilder();
        int limit = Math.min(bytes.length, maxBytes);
        for (int i = 0; i < limit; i++) {
            sb.append(String.format("%02X", bytes[i]));
            if (i < limit - 1) {
                sb.append(" ");
            }
        }
        if (bytes.length > maxBytes) {
            sb.append(" ...");
        }
        return sb.toString();
    }
}
