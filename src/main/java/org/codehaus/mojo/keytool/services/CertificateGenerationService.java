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
import javax.security.auth.x500.X500Principal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.MojoExecutionException;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for certificate and key pair generation using Bouncy Castle.
 * Follows SOLID principles - Single Responsibility: manages certificate generation operations.
 *
 * @since 2.0
 */
@Named
@Singleton
public class CertificateGenerationService {

    private static final Logger log = LoggerFactory.getLogger(CertificateGenerationService.class);
    private static final String DEFAULT_SIGNATURE_ALGORITHM = "SHA256WithRSA";
    private static final String BOUNCY_CASTLE_PROVIDER = "BC";

    /**
     * Generate a key pair and self-signed certificate.
     */
    public void generateKeyPair(
            File keystore,
            String storetype,
            char[] storepass,
            String alias,
            String keyalg,
            int keysize,
            String sigalg,
            String dname,
            int validity,
            char[] keypass,
            List<String> exts)
            throws MojoExecutionException {

        try {
            ensureBouncyCastleProvider();

            // Generate key pair
            log.info("Generating {} key pair with key size {}", keyalg, keysize);
            KeyPair keyPair = generateKeyPair(keyalg, keysize);

            // Determine signature algorithm
            String effectiveSigAlg = determineSignatureAlgorithm(keyalg, sigalg);

            // Generate self-signed certificate
            X509Certificate cert = generateSelfSignedCertificate(keyPair, dname, validity, effectiveSigAlg, exts);
            log.info("Generated self-signed certificate for: {}", dname);

            // Load or create keystore and store the key pair
            KeyStore ks = loadOrCreateKeyStore(keystore, storetype, storepass);
            Certificate[] chain = new Certificate[] {cert};
            ks.setKeyEntry(alias, keyPair.getPrivate(), keypass, chain);
            log.info("Stored key pair with alias: {}", alias);

            // Save keystore
            saveKeyStore(ks, keystore, storepass);
            log.info("Key pair generation completed successfully");

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate key pair", e);
        }
    }

    /**
     * Generate certificate request (CSR).
     */
    public void generateCertificateRequest(
            File keystore,
            String storetype,
            char[] storepass,
            String alias,
            char[] keypass,
            String dname,
            String sigalg,
            File outputFile,
            List<String> exts)
            throws MojoExecutionException {

        try {
            ensureBouncyCastleProvider();

            // Load keystore and retrieve key/certificate
            KeyStore ks = loadKeyStore(keystore, storetype, storepass);
            PrivateKey privateKey = getPrivateKey(ks, alias, keypass);
            Certificate cert = getCertificate(ks, alias);
            PublicKey publicKey = cert.getPublicKey();

            // Determine subject DN
            X500Name subject = determineSubjectDN(dname, cert);
            log.info("Subject Name in generateCertificateRequest(): {}", subject);

            // Build CSR with extensions
            PKCS10CertificationRequestBuilder csrBuilder = new JcaPKCS10CertificationRequestBuilder(subject, publicKey);

            addExtensionsToCSR(csrBuilder, exts, cert);

            // Sign and write CSR
            String signatureAlg = sigalg != null ? sigalg : DEFAULT_SIGNATURE_ALGORITHM;
            ContentSigner signer = new JcaContentSignerBuilder(signatureAlg).build(privateKey);
            PKCS10CertificationRequest csr = csrBuilder.build(signer);

            writePEMObject(outputFile, csr);
            log.info("Certificate request written to: {}", outputFile.getAbsolutePath());

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate certificate request", e);
        }
    }

    /**
     * Generate certificate from a certificate request.
     */
    public void generateCertificate(
            File keystore,
            String storetype,
            char[] storepass,
            String alias,
            char[] keypass,
            File infile,
            File outfile,
            String dname,
            int validity,
            String sigalg,
            boolean rfc,
            List<String> exts)
            throws MojoExecutionException {

        try {
            ensureBouncyCastleProvider();

            // Load keystore and signing credentials
            KeyStore ks = loadKeyStore(keystore, storetype, storepass);
            PrivateKey signingKey = getPrivateKey(ks, alias, keypass);
            Certificate signerCert = getCertificate(ks, alias);

            // Parse CSR
            PKCS10CertificationRequest csr = readCSR(infile);
            X500Name subject = csr.getSubject();
            log.info("DName from CSR: {}", subject);

            X500Name issuer = X500Name.getInstance(
                    ((X509Certificate) signerCert).getSubjectX500Principal().getEncoded());
            log.info("Issuer X500Name: {}", issuer);

            // Build certificate
            X509Certificate certificate = buildSignedCertificate(csr, issuer, signingKey, validity, sigalg, exts);

            log.info("Generated certificate for: {}", subject);

            // Write certificate
            writeCertificate(outfile, certificate, rfc);
            log.info("Certificate written to: {}", outfile.getAbsolutePath());

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate certificate", e);
        }
    }

    // ==================== Private Helper Methods ====================

    private void ensureBouncyCastleProvider() {
        if (java.security.Security.getProvider(BOUNCY_CASTLE_PROVIDER) == null) {
            java.security.Security.addProvider(new BouncyCastleProvider());
        }
    }

    private KeyPair generateKeyPair(String keyalg, int keysize) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyalg);
        keyGen.initialize(keysize, new SecureRandom());
        return keyGen.generateKeyPair();
    }

    private String determineSignatureAlgorithm(String keyalg, String sigalg) {
        if (sigalg != null) {
            return sigalg;
        }

        // Default signature algorithms based on key algorithm
        switch (keyalg.toUpperCase()) {
            case "EC":
                return "SHA256withECDSA";
            case "DSA":
                return "SHA256WithDSA";
            case "RSA":
            default:
                return "SHA256WithRSA";
        }
    }

    private X509Certificate generateSelfSignedCertificate(
            KeyPair keyPair, String dnName, int validity, String signatureAlgorithm, List<String> exts)
            throws Exception {

        X500Principal principal = new X500Principal(dnName);
        X500Name issuer = X500Name.getInstance(principal.getEncoded());
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = new Date();
        Date notAfter = new Date(notBefore.getTime() + ((long) validity * 24 * 60 * 60 * 1000));

        X509v3CertificateBuilder certBuilder =
                new JcaX509v3CertificateBuilder(issuer, serial, notBefore, notAfter, issuer, keyPair.getPublic());

        // Add extensions if specified
        addExtensionsToCertificate(certBuilder, exts);

        ContentSigner signer = new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());
        X509CertificateHolder certHolder = certBuilder.build(signer);

        return new JcaX509CertificateConverter()
                .setProvider(BOUNCY_CASTLE_PROVIDER)
                .getCertificate(certHolder);
    }

    private X509Certificate buildSignedCertificate(
            PKCS10CertificationRequest csr,
            X500Name issuer,
            PrivateKey signingKey,
            int validity,
            String sigalg,
            List<String> exts)
            throws Exception {

        X500Name subject = csr.getSubject();
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = new Date();
        Date notAfter = new Date(notBefore.getTime() + ((long) validity * 24 * 60 * 60 * 1000));

        // Extract public key from CSR
        SubjectPublicKeyInfo publicKeyInfo = csr.getSubjectPublicKeyInfo();
        PublicKey publicKey =
                new JcaPEMKeyConverter().setProvider(BOUNCY_CASTLE_PROVIDER).getPublicKey(publicKeyInfo);

        X509v3CertificateBuilder certBuilder =
                new JcaX509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject, publicKey);

        // Copy extensions from CSR
        copyExtensionsFromCSR(certBuilder, csr);

        // Add additional command-line extensions (override CSR extensions)
        addExtensionsToCertificate(certBuilder, exts);

        // Sign certificate
        String signatureAlg = sigalg != null ? sigalg : DEFAULT_SIGNATURE_ALGORITHM;
        ContentSigner signer = new JcaContentSignerBuilder(signatureAlg).build(signingKey);
        X509CertificateHolder certHolder = certBuilder.build(signer);

        return new JcaX509CertificateConverter()
                .setProvider(BOUNCY_CASTLE_PROVIDER)
                .getCertificate(certHolder);
    }

    private void addExtensionsToCertificate(X509v3CertificateBuilder certBuilder, List<String> exts) throws Exception {
        if (exts != null && !exts.isEmpty()) {
            log.info("Adding {} extension(s) to certificate", exts.size());
            for (String ext : exts) {
                parseAndAddExtension(certBuilder, ext);
            }
        }
    }

    private void addExtensionsToCSR(PKCS10CertificationRequestBuilder csrBuilder, List<String> exts, Certificate cert)
            throws Exception {

        if (exts != null && !exts.isEmpty()) {
            log.info("Adding {} extension(s) to certificate request", exts.size());
            ExtensionsGenerator extGen = new ExtensionsGenerator();

            for (String ext : exts) {
                log.info("Processing CSR extension: {}", ext);
                addExtensionToGenerator(extGen, ext);
            }

            csrBuilder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, extGen.generate());
        } else if (cert instanceof X509Certificate) {
            // Copy extensions from existing certificate if none specified
            copyExtensionsFromCertificateToCSR(csrBuilder, (X509Certificate) cert);
        }
    }

    private void copyExtensionsFromCSR(X509v3CertificateBuilder certBuilder, PKCS10CertificationRequest csr)
            throws Exception {

        org.bouncycastle.asn1.pkcs.Attribute[] attributes =
                csr.getAttributes(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest);

        if (attributes != null && attributes.length > 0) {
            org.bouncycastle.asn1.x509.Extensions extensions = org.bouncycastle.asn1.x509.Extensions.getInstance(
                    attributes[0].getAttrValues().getObjectAt(0));

            for (ASN1ObjectIdentifier oid : extensions.getExtensionOIDs()) {
                org.bouncycastle.asn1.x509.Extension ext = extensions.getExtension(oid);
                certBuilder.addExtension(oid, ext.isCritical(), ext.getParsedValue());
                log.info("Copied extension from CSR: {} (critical={})", oid, ext.isCritical());
            }
        }
    }

    private void copyExtensionsFromCertificateToCSR(PKCS10CertificationRequestBuilder csrBuilder, X509Certificate cert)
            throws Exception {

        Set<String> criticalOIDs = cert.getCriticalExtensionOIDs();
        Set<String> nonCriticalOIDs = cert.getNonCriticalExtensionOIDs();

        if ((criticalOIDs == null || criticalOIDs.isEmpty())
                && (nonCriticalOIDs == null || nonCriticalOIDs.isEmpty())) {
            log.info("No extensions found in existing certificate to copy");
            return;
        }

        ExtensionsGenerator extGen = new ExtensionsGenerator();
        int extensionCount = 0;

        // Copy critical extensions
        extensionCount += copyExtensionSet(extGen, cert, criticalOIDs, true);

        // Copy non-critical extensions
        extensionCount += copyExtensionSet(extGen, cert, nonCriticalOIDs, false);

        if (extensionCount > 0) {
            csrBuilder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, extGen.generate());
            log.info("Copied {} extension(s) from existing certificate to CSR", extensionCount);
        }
    }

    private int copyExtensionSet(ExtensionsGenerator extGen, X509Certificate cert, Set<String> oids, boolean critical)
            throws Exception {

        int count = 0;
        if (oids != null) {
            for (String oidStr : oids) {
                byte[] extValue = cert.getExtensionValue(oidStr);
                if (extValue != null) {
                    ASN1ObjectIdentifier oid = new ASN1ObjectIdentifier(oidStr);
                    ASN1OctetString oct = ASN1OctetString.getInstance(extValue);
                    extGen.addExtension(oid, critical, ASN1Primitive.fromByteArray(oct.getOctets()));
                    log.info("Copied {} extension: {}", critical ? "critical" : "non-critical", oidStr);
                    count++;
                }
            }
        }
        return count;
    }

    private void parseAndAddExtension(X509v3CertificateBuilder certBuilder, String extStr) throws Exception {
        ExtensionParser parser = new ExtensionParser(extStr);

        switch (parser.getType().toLowerCase()) {
            case "san":
            case "subjectalternativename":
                certBuilder.addExtension(
                        Extension.subjectAlternativeName, false, createGeneralNames(parser.getValue()));
                log.info("Added Subject Alternative Name extension");
                break;
            case "ian":
            case "issueralternativename":
                certBuilder.addExtension(Extension.issuerAlternativeName, false, createGeneralNames(parser.getValue()));
                log.info("Added Issuer Alternative Name extension");
                break;
            case "bc":
                certBuilder.addExtension(Extension.basicConstraints, true, createBasicConstraints(parser.getValue()));
                log.info("Added Basic Constraints extension");
                break;
            case "ku":
            case "keyusage":
                certBuilder.addExtension(Extension.keyUsage, true, createKeyUsage(parser.getValue()));
                log.info("Added Key Usage extension");
                break;
            default:
                log.warn("Unsupported extension type: {}", parser.getType());
        }
    }

    private void addExtensionToGenerator(ExtensionsGenerator extGen, String extStr) throws Exception {
        ExtensionParser parser = new ExtensionParser(extStr);

        switch (parser.getType().toLowerCase()) {
            case "san":
            case "subjectalternativename":
                extGen.addExtension(Extension.subjectAlternativeName, false, createGeneralNames(parser.getValue()));
                log.info("Added SAN extension to generator");
                break;
            case "ian":
            case "issueralternativename":
                extGen.addExtension(Extension.issuerAlternativeName, false, createGeneralNames(parser.getValue()));
                log.info("Added IAN extension to generator");
                break;
            case "bc":
                extGen.addExtension(Extension.basicConstraints, true, createBasicConstraints(parser.getValue()));
                log.info("Added Basic Constraints extension to generator");
                break;
            case "ku":
            case "keyusage":
                extGen.addExtension(Extension.keyUsage, true, createKeyUsage(parser.getValue()));
                log.info("Added Key Usage extension to generator");
                break;
            default:
                log.warn("Unsupported extension type: {}", parser.getType());
        }
    }

    private GeneralNames createGeneralNames(String value) {
        String[] names = value.split(",");
        GeneralName[] generalNames = new GeneralName[names.length];

        for (int i = 0; i < names.length; i++) {
            String[] nameParts = names[i].trim().split(":", 2);
            if (nameParts.length == 2) {
                String type = nameParts[0].trim().toLowerCase();
                String nameValue = nameParts[1].trim();

                switch (type) {
                    case "dns":
                        generalNames[i] = new GeneralName(GeneralName.dNSName, nameValue);
                        break;
                    case "ip":
                        generalNames[i] = new GeneralName(GeneralName.iPAddress, nameValue);
                        break;
                    case "email":
                        generalNames[i] = new GeneralName(GeneralName.rfc822Name, nameValue);
                        break;
                    case "uri":
                        generalNames[i] = new GeneralName(GeneralName.uniformResourceIdentifier, nameValue);
                        break;
                    default:
                        log.warn("Unsupported general name type: {}", type);
                }
            }
        }

        return new GeneralNames(generalNames);
    }

    private BasicConstraints createBasicConstraints(String value) {
        boolean isCa = false;
        int pathLen = -1;

        String[] parts = value.split(",");
        for (String part : parts) {
            String[] kv = part.trim().split(":", 2);
            if (kv.length == 2) {
                String key = kv[0].trim().toLowerCase();
                String val = kv[1].trim();

                if ("ca".equals(key)) {
                    isCa = Boolean.parseBoolean(val);
                } else if ("pathlen".equals(key)) {
                    pathLen = Integer.parseInt(val);
                }
            }
        }

        return pathLen >= 0 ? new BasicConstraints(pathLen) : new BasicConstraints(isCa);
    }

    private KeyUsage createKeyUsage(String value) {
        int usage = 0;
        String[] usages = value.split(",");

        for (String u : usages) {
            String usageType = u.trim().toLowerCase();
            switch (usageType) {
                case "digitalsignature":
                    usage |= KeyUsage.digitalSignature;
                    break;
                case "nonrepudiation":
                    usage |= KeyUsage.nonRepudiation;
                    break;
                case "keyencipherment":
                    usage |= KeyUsage.keyEncipherment;
                    break;
                case "dataencipherment":
                    usage |= KeyUsage.dataEncipherment;
                    break;
                case "keyagreement":
                    usage |= KeyUsage.keyAgreement;
                    break;
                case "keycertsign":
                    usage |= KeyUsage.keyCertSign;
                    break;
                case "crlsign":
                    usage |= KeyUsage.cRLSign;
                    break;
                case "encipheronly":
                    usage |= KeyUsage.encipherOnly;
                    break;
                case "decipheronly":
                    usage |= KeyUsage.decipherOnly;
                    break;
                default:
                    log.warn("Unknown key usage: {}", usageType);
            }
        }

        return new KeyUsage(usage);
    }

    private X500Name determineSubjectDN(String dname, Certificate cert) throws Exception {
        if (dname != null && !dname.isEmpty()) {
            X500Principal principal = new X500Principal(dname);
            return X500Name.getInstance(principal.getEncoded());
        } else if (cert instanceof X509Certificate) {
            return X500Name.getInstance(
                    ((X509Certificate) cert).getSubjectX500Principal().getEncoded());
        } else {
            throw new MojoExecutionException("DN not specified and cannot extract from certificate");
        }
    }

    private PKCS10CertificationRequest readCSR(File infile) throws Exception {
        try (InputStream in = new FileInputStream(infile);
                PEMParser pemParser = new PEMParser(new java.io.InputStreamReader(in))) {

            Object obj = pemParser.readObject();
            if (obj instanceof PKCS10CertificationRequest) {
                return (PKCS10CertificationRequest) obj;
            } else {
                throw new MojoExecutionException("Input file is not a valid CSR");
            }
        }
    }

    private void writeCertificate(File outfile, X509Certificate certificate, boolean rfc) throws Exception {
        if (rfc) {
            writePEMObject(outfile, certificate);
        } else {
            try (FileOutputStream fos = new FileOutputStream(outfile)) {
                fos.write(certificate.getEncoded());
            }
        }
    }

    private void writePEMObject(File file, Object obj) throws Exception {
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(file))) {
            pemWriter.writeObject(obj);
        }
    }

    private PrivateKey getPrivateKey(KeyStore ks, String alias, char[] keypass) throws Exception {
        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keypass);
        if (privateKey == null) {
            throw new MojoExecutionException("Private key not found for alias: " + alias);
        }
        return privateKey;
    }

    private Certificate getCertificate(KeyStore ks, String alias) throws Exception {
        Certificate cert = ks.getCertificate(alias);
        if (cert == null) {
            throw new MojoExecutionException("Certificate not found for alias: " + alias);
        }
        return cert;
    }

    private KeyStore loadOrCreateKeyStore(File keystoreFile, String storeType, char[] storePassword) throws Exception {
        String effectiveStoreType = storeType != null ? storeType : KeyStore.getDefaultType();
        KeyStore ks = KeyStore.getInstance(effectiveStoreType);

        if (keystoreFile != null && keystoreFile.exists()) {
            log.info("Loading existing keystore: {}", keystoreFile.getAbsolutePath());
            try (FileInputStream fis = new FileInputStream(keystoreFile)) {
                ks.load(fis, storePassword);
            }
        } else {
            log.info("Creating new keystore");
            ks.load(null, storePassword);

            if (keystoreFile != null && keystoreFile.getParentFile() != null) {
                keystoreFile.getParentFile().mkdirs();
            }
        }

        return ks;
    }

    private KeyStore loadKeyStore(File keystoreFile, String storeType, char[] storePassword) throws Exception {
        if (keystoreFile == null || !keystoreFile.exists()) {
            throw new MojoExecutionException("Keystore file not found: " + keystoreFile);
        }

        String effectiveStoreType = storeType != null ? storeType : KeyStore.getDefaultType();
        KeyStore ks = KeyStore.getInstance(effectiveStoreType);

        log.info("Loading keystore: {}", keystoreFile.getAbsolutePath());
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            ks.load(fis, storePassword);
        }

        return ks;
    }

    private void saveKeyStore(KeyStore ks, File keystoreFile, char[] storePassword) throws Exception {
        if (keystoreFile == null) {
            throw new IllegalArgumentException("Keystore file cannot be null");
        }

        log.info("Saving keystore to: {}", keystoreFile.getAbsolutePath());
        try (FileOutputStream fos = new FileOutputStream(keystoreFile)) {
            ks.store(fos, storePassword);
        }
    }

    // ==================== Inner Classes ====================

    /**
     * Helper class to parse extension strings in various formats.
     */
    private static class ExtensionParser {
        private final String type;
        private final String value;

        public ExtensionParser(String extStr) {
            int equalsIndex = extStr.indexOf('=');
            int colonIndex = extStr.indexOf(':');

            if (equalsIndex != -1 && (colonIndex == -1 || equalsIndex < colonIndex)) {
                // Format: "extensionName=value"
                this.type = extStr.substring(0, equalsIndex).trim();
                this.value = extStr.substring(equalsIndex + 1).trim();
            } else if (colonIndex != -1) {
                // Format: "extensionName:segment=value"
                this.type = extStr.substring(0, colonIndex).trim();
                String remaining = extStr.substring(colonIndex + 1).trim();

                int nextEqualsIndex = remaining.indexOf('=');
                this.value = nextEqualsIndex != -1
                        ? remaining.substring(nextEqualsIndex + 1).trim()
                        : remaining;
            } else {
                throw new IllegalArgumentException("Invalid extension format: " + extStr);
            }
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }
}
