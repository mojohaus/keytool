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
import org.apache.maven.shared.utils.cli.Commandline;
import org.codehaus.mojo.keytool.requests.KeyToolGenerateKeyPairRequest;
import org.codehaus.plexus.util.StringUtils;

/**
 * To generate a key pair into a keystore.
 * Implemented as a wrapper around the SDK {@code keytool -genkey} (jdk 1.5) {@code keytool -genkeypair} (jdk 1.6)
 * command.
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "generateKeyPair", requiresProject = true, threadSafe = true)
public class GenerateKeyPairMojo
        extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolGenerateKeyPairRequest> {

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

    /**
     * If value is {@code true}, use Java KeyStore API directly instead of invoking external keytool command.
     * This provides better logging and error handling.
     *
     * @since 1.8
     */
    @Parameter(defaultValue = "true")
    private boolean useKeyStoreAPI;

    /**
     * Default contructor.
     */
    public GenerateKeyPairMojo() {
        super(KeyToolGenerateKeyPairRequest.class);
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

        if (useKeyStoreAPI) {
            executeWithKeyStoreAPI();
        } else {
            super.execute();
        }
    }

    /**
     * Execute the key pair generation using Java KeyStore API directly.
     *
     * @throws MojoExecutionException if operation fails
     */
    private void executeWithKeyStoreAPI() throws MojoExecutionException {
        try {
            // Get parameters
            File keystoreFile = getKeystoreFile();
            KeyToolGenerateKeyPairRequest request = createKeytoolRequest();

            // Validate required parameters
            if (request.getAlias() == null || request.getAlias().isEmpty()) {
                throw new MojoExecutionException("Alias is required");
            }

            if (dname == null || dname.isEmpty()) {
                throw new MojoExecutionException("Distinguished name (dname) is required");
            }

            // Get passwords as char arrays
            char[] storePassword =
                    (request.getStorepass() != null) ? request.getStorepass().toCharArray() : null;
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
                    request.getStoretype(),
                    storePassword,
                    request.getAlias(),
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

    /** {@inheritDoc} */
    @Override
    protected KeyToolGenerateKeyPairRequest createKeytoolRequest() {
        KeyToolGenerateKeyPairRequest request = super.createKeytoolRequest();

        request.setKeyalg(this.keyalg);
        request.setKeysize(this.keysize);
        request.setKeypass(this.keypass);
        request.setSigalg(this.sigalg);
        request.setDname(this.dname);
        request.setStartdate(this.startdate);
        if (this.exts != null && !this.exts.isEmpty()) {
            request.setExts(exts);
        } else {
            request.setExt(ext);
        }
        request.setValidity(this.validity);
        return request;
    }

    /** {@inheritDoc} */
    @Override
    protected String getCommandlineInfo(Commandline commandLine) {
        String commandLineInfo = super.getCommandlineInfo(commandLine);

        commandLineInfo = StringUtils.replace(commandLineInfo, this.keypass, "'*****'");

        return commandLineInfo;
    }
}
