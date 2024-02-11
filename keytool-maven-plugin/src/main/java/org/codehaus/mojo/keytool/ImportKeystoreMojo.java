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
import org.apache.maven.shared.utils.cli.Commandline;
import org.codehaus.mojo.keytool.requests.KeyToolImportKeystoreRequest;
import org.codehaus.plexus.util.StringUtils;

/**
 * To import all entries of a keystore to another keystore.
 * Implemented as a wrapper around the SDK {@code keytool -importkeystore} command.
 * <strong>Note</strong> This operation was not implemented by the keytool before jdk 1.6.
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "importKeystore", requiresProject = true, threadSafe = true)
public class ImportKeystoreMojo extends AbstractKeyToolRequestMojo<KeyToolImportKeystoreRequest> {

    /**
     * Source keystore name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srckeystore;

    /**
     * Destination keystore name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destkeystore;

    /**
     * Source keystore type.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srcstoretype;

    /**
     * Destination keystore type.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String deststoretype;

    /**
     * Source keystore password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srcstorepass;

    /**
     * Destination keystore password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String deststorepass;

    /**
     * Source keystore password protected.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean srcprotected;

    /**
     * Source keystore provider name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srcprovidername;

    /**
     * Destination keystore provider name.
     * See <a hresf="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destprovidername;

    /**
     * Source alias.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srcalias;

    /**
     * Destination alias.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destalias;

    /**
     * Source key password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srckeypass;

    /**
     * Destination key password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destkeypass;

    /**
     * Do not prompt.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean noprompt;

    /**
     * Provider class name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providerclass;

    /**
     * Provider argument.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providerarg;

    /**
     * Provider classpath.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providerpath;

    /**
     * If value is {@code true}, then will do nothing if keystore already exists.
     *
     * @since 1.3
     */
    @Parameter
    private boolean skipIfExist;

    /**
     * Default contructor.
     */
    public ImportKeystoreMojo() {
        super(KeyToolImportKeystoreRequest.class);
    }

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {

        if (skipIfExist) {

            // check if keystore already exist
            File destinationKeystoreFile = new File(destkeystore);
            boolean keystoreFileExists = destinationKeystoreFile.exists();

            if (keystoreFileExists) {
                getLog().info("Skip execution, keystore already exists at " + destinationKeystoreFile);
                setSkip(true);
            }
        }
        super.execute();
    }

    /** {@inheritDoc} */
    @Override
    protected KeyToolImportKeystoreRequest createKeytoolRequest() {
        KeyToolImportKeystoreRequest request = super.createKeytoolRequest();

        request.setSrckeystore(this.srckeystore);
        request.setDestkeystore(this.destkeystore);
        request.setSrcstoretype(this.srcstoretype);
        request.setDeststoretype(this.deststoretype);
        request.setSrcstorepass(this.srcstorepass);
        request.setDeststorepass(this.deststorepass);
        request.setSrcprotected(this.srcprotected);
        request.setSrcprovidername(this.srcprovidername);
        request.setDestprovidername(this.destprovidername);
        request.setSrcalias(this.srcalias);
        request.setDestalias(this.destalias);
        request.setSrckeypass(this.srckeypass);
        request.setDestkeypass(this.destkeypass);
        request.setNoprompt(this.noprompt);
        request.setProviderclass(this.providerclass);
        request.setProviderarg(this.providerarg);
        request.setProviderpath(this.providerpath);
        return request;
    }

    /** {@inheritDoc} */
    @Override
    protected String getCommandlineInfo(Commandline commandLine) {
        String commandLineInfo = super.getCommandlineInfo(commandLine);

        commandLineInfo = StringUtils.replace(commandLineInfo, this.srckeypass, "'*****'");
        commandLineInfo = StringUtils.replace(commandLineInfo, this.destkeypass, "'*****'");
        commandLineInfo = StringUtils.replace(commandLineInfo, this.srcstorepass, "'*****'");
        commandLineInfo = StringUtils.replace(commandLineInfo, this.deststorepass, "'*****'");

        return commandLineInfo;
    }
}
