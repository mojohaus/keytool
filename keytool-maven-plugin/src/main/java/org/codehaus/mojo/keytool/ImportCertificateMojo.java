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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.utils.cli.Commandline;
import org.codehaus.mojo.keytool.requests.KeyToolImportCertificateRequest;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;

/**
 * To import a certificate into a keystore.
 * Implemented as a wrapper around the SDK {@code keytool -import} (jdk 1.5) or  {@code keytool -importcert} (jdk 1.6)
 * command.
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 * <strong>Since version 1.2, this mojo replace the mojo import.</strong>
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo( name = "importCertificate", requiresProject = true, threadSafe = true )
public class ImportCertificateMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolImportCertificateRequest>
{

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
     * Default contructor.
     */
    public ImportCertificateMojo()
    {
        super( KeyToolImportCertificateRequest.class );
    }

    /** {@inheritDoc} */
    @Override
    public void execute()
        throws MojoExecutionException
    {

        if ( skipIfExist )
        {

            // check if keystore already exist
            File keystoreFile = getKeystoreFile();
            boolean keystoreFileExists = keystoreFile.exists();

            if ( keystoreFileExists )
            {
                getLog().info( "Skip execution, keystore already exists at " + keystoreFile );
                setSkip( true );
            }

        }
        super.execute();
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    protected String getCommandlineInfo( Commandline commandLine )
    {
        String commandLineInfo = super.getCommandlineInfo( commandLine );

        commandLineInfo = StringUtils.replace( commandLineInfo, this.keypass, "'*****'" );

        return commandLineInfo;
    }
}
