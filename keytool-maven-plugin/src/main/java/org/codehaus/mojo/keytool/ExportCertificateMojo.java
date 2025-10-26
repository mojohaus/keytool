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
 * To export a certificate from a keystore using Java KeyStore API.
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "exportCertificate", requiresProject = true, threadSafe = true)
public class ExportCertificateMojo extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo {

    /**
     * Output in RFC style.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean rfc;

    /**
     * Output file name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String file;

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

            if (file == null || file.isEmpty()) {
                throw new MojoExecutionException("Output file is required");
            }

            File outputFile = new File(file);

            // Get password as char array
            char[] storePassword = (getStorepass() != null) ? getStorepass().toCharArray() : null;

            // Create KeyStore service and export certificate
            KeyStoreService keyStoreService = new KeyStoreService(getLog());
            keyStoreService.exportCertificate(keystoreFile, getStoretype(), storePassword, getAlias(), outputFile, rfc);

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to export certificate: " + e.getMessage(), e);
        }
    }
}
