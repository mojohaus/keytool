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

import org.apache.maven.plugins.annotations.Parameter;

/**
 * Base class for mojos that operate on a keystore.
 * Simplified from the old AbstractKeyToolRequestWithKeyStoreParametersMojo to use only Java KeyStore API.
 *
 * @author tchemit
 * @since 2.0
 */
public abstract class KeyStoreMojo extends AbstractKeyToolMojo {

    /**
     * Keystore location.
     */
    @Parameter(required = true)
    protected String keystore;

    /**
     * Keystore type (e.g., "JKS", "PKCS12").
     */
    @Parameter(defaultValue = "JKS")
    protected String storetype;

    /**
     * Keystore password.
     */
    @Parameter(alias = "storepass")
    protected String storepass;

    /**
     * Provider name.
     */
    @Parameter
    protected String providername;

    /**
     * Get the keystore file.
     *
     * @return keystore file
     */
    protected File getKeystoreFile() {
        File file = new File(keystore);
        // Ensure parent directory exists
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (isVerbose()) {
                getLog().debug("Created parent directory: " + created + " " + parentDir);
            }
        }
        return file;
    }

    /**
     * Get the keystore password as a character array.
     *
     * @return keystore password
     */
    protected char[] getKeystorePassword() {
        return storepass != null ? storepass.toCharArray() : null;
    }

    /**
     * Create the parent directory of the given file if necessary.
     *
     * @param file file path
     */
    protected void createParentDirIfNecessary(String file) {
        if (file != null) {
            File fileDir = new File(file).getParentFile();
            if (fileDir != null && !fileDir.exists()) {
                boolean created = fileDir.mkdirs();
                if (isVerbose()) {
                    getLog().debug("Created directory: " + created + " " + fileDir);
                }
            }
        }
    }
}
