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
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * To generate a key pair into a keystore using Java KeyStore API.
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "generateKeyPair", requiresProject = true, threadSafe = true)
public class GenerateKeyPairMojo extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo {

    /**
     * Key algorithm name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keyalg;

    /**
     * Key bit size.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keysize;

    /**
     * Key password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keypass;

    /**
     * Signature algorithm name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String sigalg;

    /**
     * Validity number of days.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String validity;

    /**
     * Certificate validity start date/time.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String startdate;

    /**
     * X.509 extension.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     *
     * @deprecated Use {@link #exts instead}.
     */
    @Deprecated
    @Parameter
    private String ext;

    /**
     * X.509 extension.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.6
     */
    @Parameter
    private List<String> exts;

    /**
     * Distinguished name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String dname;

    /**
     * If value is {@code true}, then will do nothing if keystore already exists.
     *
     * @since 1.3
     */
    @Parameter
    private boolean skipIfExist;

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
            if (getAlias() == null || getAlias().isEmpty()) {
                throw new MojoExecutionException("Alias is required");
            }

            if (dname == null || dname.isEmpty()) {
                throw new MojoExecutionException("Distinguished name (dname) is required");
            }

            // Get passwords as char arrays
            char[] storePassword = (getStorepass() != null) ? getStorepass().toCharArray() : null;
            char[] keyPassword = (keypass != null) ? keypass.toCharArray() : null;

            // Parse keysize
            int keySizeInt = 0;
            if (keysize != null && !keysize.isEmpty()) {
                try {
                    keySizeInt = Integer.parseInt(keysize);
                } catch (NumberFormatException e) {
                    throw new MojoExecutionException("Invalid keysize: " + keysize, e);
                }
            }

            // Parse validity
            int validityInt = 0;
            if (validity != null && !validity.isEmpty()) {
                try {
                    validityInt = Integer.parseInt(validity);
                } catch (NumberFormatException e) {
                    throw new MojoExecutionException("Invalid validity: " + validity, e);
                }
            }

            // Create KeyStore service and generate key pair
            KeyStoreService keyStoreService = new KeyStoreService(getLog());
            keyStoreService.generateKeyPair(
                    keystoreFile,
                    getStoretype(),
                    storePassword,
                    getAlias(),
                    keyPassword,
                    dname,
                    keyalg,
                    keySizeInt,
                    sigalg,
                    validityInt);

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate key pair: " + e.getMessage(), e);
        }
    }
}
