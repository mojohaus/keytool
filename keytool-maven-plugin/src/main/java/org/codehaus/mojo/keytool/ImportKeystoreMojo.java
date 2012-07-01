package org.codehaus.mojo.keytool;

/*
 * Copyright 2005-2012 The Codehaus
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

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.keytool.requests.KeyToolImportKeystoreRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * To import all entries of a keystore to another keystore.
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -importkeystore} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.2
 */
@Mojo( name = "importKeystore", requiresProject = true )
public class ImportKeystoreMojo
    extends AbstractKeyToolRequestMojo<KeyToolImportKeystoreRequest>
{

    /**
     * Source keystore name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srckeystore;

    /**
     * Destination keystore name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destkeystore;

    /**
     * Source keystore type.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srcstoretype;

    /**
     * Destination keystore type.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String deststoretype;

    /**
     * Source keystore password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srcstorepass;

    /**
     * Destination keystore password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String deststorepass;

    /**
     * Source keystore password protected.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean srcprotected;

    /**
     * Source keystore provider name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srcprovidername;

    /**
     * Destination keystore provider name.
     * <p/>
     * See <a hresf="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destprovidername;

    /**
     * Source alias.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srcalias;

    /**
     * Destination alias.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destalias;

    /**
     * Source key password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srckeypass;

    /**
     * Destination key password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destkeypass;

    /**
     * Do not prompt.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean noprompt;

    /**
     * Provider class name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providerclass;

    /**
     * Provider argument.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providerarg;

    /**
     * Provider classpath.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providerpath;

    /**
     * Default contructor.
     */
    public ImportKeystoreMojo()
    {
        super( KeyToolImportKeystoreRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolImportKeystoreRequest createKeytoolRequest()
    {
        KeyToolImportKeystoreRequest request = super.createKeytoolRequest();

        request.setSrckeystore( this.srckeystore );
        request.setDestkeystore( this.destkeystore );
        request.setSrcstoretype( this.srcstoretype );
        request.setDeststoretype( this.deststoretype );
        request.setSrcstorepass( this.srcstorepass );
        request.setDeststorepass( this.deststorepass );
        request.setSrcprotected( this.srcprotected );
        request.setSrcprovidername( this.srcprovidername );
        request.setDestprovidername( this.destprovidername );
        request.setSrcalias( this.srcalias );
        request.setDestalias( this.destalias );
        request.setSrckeypass( this.srckeypass );
        request.setDestkeypass( this.destkeypass );
        request.setNoprompt( this.noprompt );
        request.setProviderclass( this.providerclass );
        request.setProviderarg( this.providerarg );
        request.setProviderpath( this.providerpath );
        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCommandlineInfo( Commandline commandLine )
    {
        String commandLineInfo = super.getCommandlineInfo( commandLine );

        commandLineInfo = StringUtils.replace( commandLineInfo, this.srckeypass, "'*****'" );
        commandLineInfo = StringUtils.replace( commandLineInfo, this.destkeypass, "'*****'" );
        commandLineInfo = StringUtils.replace( commandLineInfo, this.srcstorepass, "'*****'" );
        commandLineInfo = StringUtils.replace( commandLineInfo, this.deststorepass, "'*****'" );

        return commandLineInfo;
    }
}
