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
import org.codehaus.mojo.keytool.requests.KeyToolImportCertificateRequest;

/**
 * To import a certificate into a keystore using Java KeyStore API.
 * See <a href="https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html">keytool documentation</a>.
 * <strong>Since version 1.2, this mojo replace the mojo import.</strong>
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "importCertificate", requiresProject = true, threadSafe = true)
public class ImportCertificateMojo
        extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolImportCertificateRequest> {

    /**
     * Key password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keypass;

    /**
     * Input file name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String file;

    /**
     * Do not prompt.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean noprompt;

    /**
     * Trust certificates from cacerts.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean trustcacerts;

    /**
     * If value is {@code true}, then will do nothing if keystore already exists.
     *
     * @since 1.3
     */
    @Parameter
    private boolean skipIfExist;

    /**
     * If value is {@code true}, skip the import silently if the alias already exists in the keystore.
     * This is useful when running multiple executions without clean, allowing idempotent builds.
     * When {@code false} (default), the certificate entry will be overwritten if it exists.
     *
     * @since 1.8
     */
    @Parameter(defaultValue = "false")
    private boolean skipIfAliasExists;

    /**
     * Constructor.
     */
    public ImportCertificateMojo() {
        super(KeyToolImportCertificateRequest.class);
    }

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {

        if (isSkip()) {
            getLog().info(getMessage("disabled"));
            return;
        }

        if (skipIfExist) {
            // check if keystore already exist
            File keystoreFile = getKeystoreFile();
            boolean keystoreFileExists = keystoreFile.exists();

            if (keystoreFileExists) {
                getLog().info("Skip execution, keystore already exists at " + keystoreFile);
                return;
            }
        }

        try {
            // Get parameters
            File keystoreFile = getKeystoreFile();

            // Validate required parameters
            if (file == null || file.isEmpty()) {
                throw new MojoExecutionException("Certificate file is required");
            }

            if (getAlias() == null || getAlias().isEmpty()) {
                throw new MojoExecutionException("Alias is required");
            }

            File certFile = new File(file);
            if (!certFile.exists()) {
                throw new MojoExecutionException("Certificate file does not exist: " + certFile);
            }

            // Get password as char array
            char[] password = (getStorepass() != null) ? getStorepass().toCharArray() : null;

            // Create KeyStore service and import certificate
            KeyStoreService keyStoreService = new KeyStoreService(getLog());
            keyStoreService.importCertificate(
                    keystoreFile, getStoretype(), password, getAlias(), certFile, skipIfAliasExists);

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to import certificate: " + e.getMessage(), e);
        }
    }
}
