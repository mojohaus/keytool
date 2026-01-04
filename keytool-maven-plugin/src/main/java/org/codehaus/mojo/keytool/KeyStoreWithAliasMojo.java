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

import org.apache.maven.plugins.annotations.Parameter;

/**
 * Base class for mojos that operate on a specific alias in a keystore.
 * Simplified from the old AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo.
 *
 * @author tchemit
 * @since 2.0
 */
public abstract class KeyStoreWithAliasMojo extends KeyStoreMojo {

    /**
     * Alias name of the entry to process.
     */
    @Parameter(required = true)
    protected String alias;

    /**
     * Password for the private key (if different from keystore password).
     */
    @Parameter
    protected String keypass;

    /**
     * Get the key password as a character array.
     * Falls back to keystore password if key password is not specified.
     *
     * @return key password
     */
    protected char[] getKeyPassword() {
        String password = keypass != null ? keypass : storepass;
        return password != null ? password.toCharArray() : null;
    }
}
