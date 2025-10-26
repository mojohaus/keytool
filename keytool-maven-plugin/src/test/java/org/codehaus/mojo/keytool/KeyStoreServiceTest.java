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

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.mockito.Mockito.*;

/**
 * Test for KeyStoreService.
 */
public class KeyStoreServiceTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private Log log;
    private KeyStoreService service;

    @Before
    public void setUp() {
        log = mock(Log.class);
        service = new KeyStoreService(log);
    }

    @Test
    public void testServiceInstantiation() {
        // Just verify the service can be instantiated
        KeyStoreService testService = new KeyStoreService(log);
        assertNotNull("Service should not be null", testService);
    }

    @Test(expected = Exception.class)
    public void testImportCertificateWithNonExistentFile() throws Exception {
        File keystoreFile = new File("/tmp/nonexistent.jks");
        File certFile = new File("/tmp/nonexistent.cer");

        // This should throw an exception
        service.importCertificate(keystoreFile, "JKS", "testpass".toCharArray(), "testalias", certFile);
    }

    @Test
    public void testImportCertificateWithSkipIfAliasExists() throws Exception {
        // Copy test certificate from integration test resources
        File certFile = new File("src/it/import-certificate-keystore-api/testcert.cer");
        if (!certFile.exists()) {
            // Skip test if cert file not available
            return;
        }

        File keystoreFile = new File(tempFolder.getRoot(), "test-keystore.jks");

        String alias = "testalias";
        char[] password = "testpass".toCharArray();

        // First import should succeed
        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, false);
        verify(log).info("Certificate was added to keystore");

        // Reset mock to verify next call
        reset(log);

        // Second import with skipIfAliasExists=true should skip and log
        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, true);
        verify(log).info(contains("already exists"));
        verify(log, never()).info("Certificate was added to keystore");
    }

    @Test
    public void testImportCertificateOverwrite() throws Exception {
        // Copy test certificate from integration test resources
        File certFile = new File("src/it/import-certificate-keystore-api/testcert.cer");
        if (!certFile.exists()) {
            // Skip test if cert file not available
            return;
        }

        File keystoreFile = new File(tempFolder.getRoot(), "test-keystore-overwrite.jks");

        String alias = "testalias";
        char[] password = "testpass".toCharArray();

        // First import
        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, false);
        verify(log).info("Certificate was added to keystore");

        // Reset mock
        reset(log);

        // Second import with skipIfAliasExists=false should overwrite
        service.importCertificate(keystoreFile, "JKS", password, alias, certFile, false);
        verify(log).info("Certificate was added to keystore");
    }

    private static void assertNotNull(String message, Object object) {
        if (object == null) {
            throw new AssertionError(message);
        }
    }

    private static void assertTrue(String message, boolean condition) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    @Test
    public void testGenerateKeyPair() throws Exception {
        File keystoreFile = new File(tempFolder.getRoot(), "test-gen-keypair.jks");

        String alias = "mykey";
        char[] storePassword = "storepass".toCharArray();
        char[] keyPassword = "keypass".toCharArray();
        String dname = "CN=Test, OU=Dev, O=TestOrg, L=TestCity, ST=TestState, C=US";

        // Generate key pair
        service.generateKeyPair(
                keystoreFile, "JKS", storePassword, alias, keyPassword, dname, "RSA", 2048, "SHA256withRSA", 365);

        // Verify log message
        verify(log).info(contains("Generated key pair with alias 'mykey'"));

        // Verify keystore file was created
        assertTrue("Keystore file should exist", keystoreFile.exists());
    }

    @Test
    public void testGenerateKeyPairWithDefaults() throws Exception {
        File keystoreFile = new File(tempFolder.getRoot(), "test-gen-keypair-defaults.jks");

        String alias = "mykey2";
        char[] storePassword = "storepass".toCharArray();
        String dname = "CN=Test2, OU=Dev, O=TestOrg, L=TestCity, ST=TestState, C=US";

        // Generate key pair with default values (keyAlg=null, keySize=0, sigAlg=null, validity=0)
        service.generateKeyPair(keystoreFile, "JKS", storePassword, alias, null, dname, null, 0, null, 0);

        // Verify log message
        verify(log).info(contains("Generated key pair with alias 'mykey2'"));

        // Verify keystore file was created
        assertTrue("Keystore file should exist", keystoreFile.exists());
    }

    @Test
    public void testGenerateKeyPairPKCS12() throws Exception {
        File keystoreFile = new File(tempFolder.getRoot(), "test-gen-keypair.p12");

        String alias = "mykey3";
        char[] storePassword = "storepass".toCharArray();
        String dname = "CN=Test3, OU=Dev, O=TestOrg, L=TestCity, ST=TestState, C=US";

        // Generate key pair in PKCS12 keystore
        service.generateKeyPair(
                keystoreFile, "PKCS12", storePassword, alias, null, dname, "RSA", 2048, "SHA256withRSA", 365);

        // Verify log message
        verify(log).info(contains("Generated key pair with alias 'mykey3'"));

        // Verify keystore file was created
        assertTrue("Keystore file should exist", keystoreFile.exists());
    }
}
