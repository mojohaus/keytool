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
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Import a certificate into a keystore using Java KeyStore API.
 *
 * @author tchemit
 * @since 2.0
 */
@Mojo(name = "importCertificate", requiresProject = true, threadSafe = true)
public class ImportCertificateMojo extends KeyStoreWithAliasMojo {

    /**
     * Input file name (certificate file to import).
     */
    @Parameter(required = true)
    private String file;

    /**
     * If value is {@code true}, then will do nothing if keystore already exists.
     */
    @Parameter(defaultValue = "false")
    private boolean skipIfExist;

    /**
     * If value is {@code true}, skip the import silently if the alias already exists in the keystore.
     */
    @Parameter(defaultValue = "false")
    private boolean skipIfAliasExists;

    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            getLog().info(getMessage("disabled"));
            return;
        }

        File keystoreFile = getKeystoreFile();

        if (skipIfExist && keystoreFile.exists()) {
            getLog().info("Skip execution, keystore already exists at " + keystoreFile);
            return;
        }

        // Validate parameters
        if (file == null || file.isEmpty()) {
            throw new MojoExecutionException("Certificate file is required");
        }

        if (alias == null || alias.isEmpty()) {
            throw new MojoExecutionException("Alias is required");
        }

        File certFile = new File(file);
        if (!certFile.exists()) {
            throw new MojoExecutionException("Certificate file does not exist: " + certFile);
        }

        KeyStoreService service = new KeyStoreService(getLog());
        service.importCertificate(keystoreFile, storetype, getKeystorePassword(), alias, certFile, skipIfAliasExists);
    }
}
