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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * To delete an entry alias from a keystore using Java KeyStore API.
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "deleteAlias", requiresProject = true, threadSafe = true)
public class DeleteAliasMojo extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo {

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {

        if (isSkip()) {
            getLog().info(getMessage("disabled"));
            return;
        }

        try {
            // Get parameters
            File keystoreFile = getKeystoreFile();

            // Validate required parameters
            if (getAlias() == null || getAlias().isEmpty()) {
                throw new MojoExecutionException("Alias is required");
            }

            // Get password as char array
            char[] storePassword = (getStorepass() != null) ? getStorepass().toCharArray() : null;

            // Create KeyStore service and delete entry
            KeyStoreService keyStoreService = new KeyStoreService(getLog());
            keyStoreService.deleteEntry(keystoreFile, getStoretype(), storePassword, getAlias());

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to delete alias: " + e.getMessage(), e);
        }
    }
}
