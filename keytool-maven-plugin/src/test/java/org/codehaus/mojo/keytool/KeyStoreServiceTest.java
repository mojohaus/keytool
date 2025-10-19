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
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Test for KeyStoreService.
 */
public class KeyStoreServiceTest {

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

    private static void assertNotNull(String message, Object object) {
        if (object == null) {
            throw new AssertionError(message);
        }
    }
}
