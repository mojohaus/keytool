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

import org.codehaus.mojo.keytool.requests.KeyToolImportCertificateRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * To import a certificate into a keystore.
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -import} (jdk 1.5) or  {@code keytool -importcert} (jdk 1.6)
 * command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 * <p/>
 * <strong>Since version 1.2, this mojo replace the mojo import.</strong>
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal importCertificate
 * @requiresProject
 * @since 1.2
 */
public class ImportCertificateMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolImportCertificateRequest>
{

    /**
     * Key password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${keypass}"
     * @since 1.2
     */
    private String keypass;

    /**
     * Input file name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${file}"
     * @since 1.2
     */
    private String file;

    /**
     * Do not prompt.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${noprompt}"
     * @since 1.2
     */
    private boolean noprompt;

    /**
     * Trust certificates from cacerts.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${trustcacerts}"
     * @since 1.2
     */
    private boolean trustcacerts;

    /**
     * Default contructor.
     */
    public ImportCertificateMojo()
    {
        super( KeyToolImportCertificateRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolImportCertificateRequest createKeytoolRequest()
    {
        KeyToolImportCertificateRequest request = super.createKeytoolRequest();

        request.setKeypass( this.keypass );
        request.setFile( this.file );
        request.setNoprompt( this.noprompt );
        request.setTrustcacerts( this.trustcacerts );
        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCommandlineInfo( Commandline commandLine )
    {
        String commandLineInfo = super.getCommandlineInfo( commandLine );

        commandLineInfo = StringUtils.replace( commandLineInfo, this.keypass, "'*****'" );

        return commandLineInfo;
    }
}
