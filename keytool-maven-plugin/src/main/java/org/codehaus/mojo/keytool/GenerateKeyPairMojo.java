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
import org.codehaus.mojo.keytool.requests.KeyToolGenerateKeyPairRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;

/**
 * To generate a key pair into a keystore.
 * <p/>
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -genkey} (jdk 1.5) {@code keytool -genkeypair} (jdk 1.6)
 * command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.2
 */
@Mojo(name = "generateKeyPair", requiresProject = true)
public class GenerateKeyPairMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolGenerateKeyPairRequest>
{

    /**
     * Key algorithm name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keyalg;

    /**
     * Key bit size.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keysize;

    /**
     * Key password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keypass;

    /**
     * Signature algorithm name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String sigalg;

    /**
     * Validity number of days.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String validity;

    /**
     * Certificate validity start date/time.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String startdate;

    /**
     * X.509 extension.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String ext;

    /**
     * Distinguished name.
     * <p/>
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
     * Default contructor.
     */
    public GenerateKeyPairMojo()
    {
        super( KeyToolGenerateKeyPairRequest.class );
    }

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

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolGenerateKeyPairRequest createKeytoolRequest()
    {
        KeyToolGenerateKeyPairRequest request = super.createKeytoolRequest();

        request.setKeyalg( this.keyalg );
        request.setKeysize( this.keysize );
        request.setKeypass( this.keypass );
        request.setSigalg( this.sigalg );
        request.setDname( this.dname );
        request.setStartdate( this.startdate );
        request.setExt( this.ext );
        request.setValidity( this.validity );
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
