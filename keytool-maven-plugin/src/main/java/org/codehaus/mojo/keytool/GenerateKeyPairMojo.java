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
 * Generate a key pair into a keystore using Java KeyStore API.
 *
 * @author tchemit
 * @since 2.0
 */
@Mojo(name = "generateKeyPair", requiresProject = true, threadSafe = true)
public class GenerateKeyPairMojo extends KeyStoreWithAliasMojo {

    /**
     * Key algorithm name (e.g., "RSA", "DSA", "EC").
     */
    @Parameter(defaultValue = "RSA")
    private String keyalg;

    /**
     * Key bit size.
     */
    @Parameter(defaultValue = "2048")
    private String keysize;

    /**
     * Signature algorithm name (e.g., "SHA256WithRSA").
     */
    @Parameter(defaultValue = "SHA256WithRSA")
    private String sigalg;

    /**
     * Validity number of days.
     */
    @Parameter(defaultValue = "90")
    private String validity;

    /**
     * Distinguished name (e.g., "CN=Test, OU=Dev, O=Company, C=US").
     */
    @Parameter(required = true)
    private String dname;

    /**
     * If value is {@code true}, then will do nothing if keystore already exists.
     */
    @Parameter(defaultValue = "false")
    private boolean skipIfExist;

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
        if (alias == null || alias.isEmpty()) {
            throw new MojoExecutionException("Alias is required");
        }

        if (dname == null || dname.isEmpty()) {
            throw new MojoExecutionException("Distinguished name (dname) is required");
        }

        try {
            int keySizeInt = Integer.parseInt(keysize);
            int validityDays = Integer.parseInt(validity);

            KeyStoreService service = new KeyStoreService(getLog());
            service.generateKeyPair(
                    keystoreFile,
                    storetype,
                    getKeystorePassword(),
                    alias,
                    getKeyPassword(),
                    keyalg,
                    keySizeInt,
                    sigalg,
                    dname,
                    validityDays);

        } catch (NumberFormatException e) {
            throw new MojoExecutionException("Invalid number format: " + e.getMessage(), e);
        }
    }
}
