package org.codehaus.mojo.shared.keytool;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.codehaus.mojo.shared.keytool.requests.*;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;

/**
 * To build the command line for a given {@link KeyToolRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @version $Id$
 * @since 1.0
 */
public class KeyToolCommandLineBuilder
{
    private static final Logger DEFAULT_LOGGER = new ConsoleLogger( 0, KeyToolCommandLineBuilder.class.getName() );

    private Logger logger = DEFAULT_LOGGER;

    /**
     * Keytool executable location.
     */
    private String keyToolFile;

    /**
     * Build the commandline given the incoming keytool request.
     *
     * @param request keytool request
     * @return the prepared commandline client ready to be executed
     * @throws CommandLineConfigurationException
     *          if could not find keytool executable
     */
    public Commandline build( KeyToolRequest request )
        throws CommandLineConfigurationException
    {
        checkRequiredState();

        Commandline cli = new Commandline();

        cli.setExecutable( keyToolFile );

        cli.setWorkingDirectory( request.getWorkingDirectory() );

        addArg( cli, request.getKeytoolCommand() );
        addArgIfTrue( cli, "-v", request.isVerbose() );

        if ( request instanceof KeyToolRequestWithKeyStoreParameters )
        {
            buildWithProviders( (KeyToolRequestWithKeyStoreParameters) request, cli );
        }
        if ( request instanceof KeyToolRequestWithKeyStoreAndAliasParameters )
        {
            buildWithProviders( (KeyToolRequestWithKeyStoreAndAliasParameters) request, cli );
        }
        if ( request instanceof KeyToolChangeAliasRequest )
        {
            build( (KeyToolChangeAliasRequest) request, cli );
        }
        if ( request instanceof KeyToolChangeKeyPasswordRequest )
        {
            build( (KeyToolChangeKeyPasswordRequest) request, cli );
        }
        if ( request instanceof KeyToolChangeStorePasswordRequest )
        {
            build( (KeyToolChangeStorePasswordRequest) request, cli );
        }
        if ( request instanceof KeyToolDeleteRequest )
        {
            build( (KeyToolDeleteRequest) request, cli );
        }
        if ( request instanceof KeyToolExportCertificateRequest )
        {
            build( (KeyToolExportCertificateRequest) request, cli );
        }
        if ( request instanceof KeyToolGenerateCertificateRequest )
        {
            build( (KeyToolGenerateCertificateRequest) request, cli );
        }
        if ( request instanceof KeyToolGenerateCertificateRequestRequest )
        {
            build( (KeyToolGenerateCertificateRequestRequest) request, cli );
        }
        if ( request instanceof KeyToolGenerateKeyPairRequest )
        {
            build( (KeyToolGenerateKeyPairRequest) request, cli );
        }
        if ( request instanceof KeyToolGenerateSecretKeyRequest )
        {
            build( (KeyToolGenerateSecretKeyRequest) request, cli );
        }
        if ( request instanceof KeyToolImportCertificateRequest )
        {
            build( (KeyToolImportCertificateRequest) request, cli );
        }
        if ( request instanceof KeyToolImportKeystoreRequest )
        {
            build( (KeyToolImportKeystoreRequest) request, cli );
        }
        if ( request instanceof KeyToolListRequest )
        {
            build( (KeyToolListRequest) request, cli );
        }
        if ( request instanceof KeyToolPrintCertificateRequest )
        {
            build( (KeyToolPrintCertificateRequest) request, cli );
        }
        if ( request instanceof KeyToolPrintCertificateRequestRequest )
        {
            build( (KeyToolPrintCertificateRequestRequest) request, cli );
        }
        if ( request instanceof KeyToolPrintCRLFileRequest )
        {
            build( (KeyToolPrintCRLFileRequest) request, cli );
        }

        String[] arguments = request.getArguments();
        if ( arguments != null )
        {
            cli.addArguments( arguments );
        }

        return cli;
    }

    /**
     * Sets the logger used by the builder.
     *
     * @param logger logger to use in this builder
     */
    public void setLogger( Logger logger )
    {
        this.logger = logger;
    }

    /**
     * Sets the keytool executable location.
     *
     * @param keyToolFile keytool executable location to use in this builder
     */
    public void setKeyToolFile( String keyToolFile )
    {
        this.keyToolFile = keyToolFile;
    }

    /**
     * Checks that builder is ready to produce commandline from incoming request.
     * <p/>
     * Says a logger is set and a keytool executable location is setted.
     */
    protected void checkRequiredState()
    {
        if ( logger == null )
        {
            throw new IllegalStateException( "A logger instance is required." );
        }

        if ( keyToolFile == null )
        {
            throw new IllegalStateException( "A keyTool file is required." );
        }
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool generate key request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolChangeAliasRequest request, Commandline cli )
    {
        addArgIfNotEmpty( cli, "-destalias", request.getDestalias() );
        addArgIfNotEmpty( cli, "-keypass", request.getKeypass() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool generate key request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolChangeKeyPasswordRequest request, Commandline cli )
    {
        addArgIfNotEmpty( cli, "-keypass", request.getKeypass() );
        addArgIfNotEmpty( cli, "-new", request.getNewPassword() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool generate key request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolChangeStorePasswordRequest request, Commandline cli )
    {
        addArgIfNotEmpty( cli, "-new", request.getNewPassword() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool generate key request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolDeleteRequest request, Commandline cli )
    {
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool export key request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolExportCertificateRequest request, Commandline cli )
    {
        addArgIfTrue( cli, "-rfc", request.isRfc() );
        addArgIfNotEmpty( cli, "-file", request.getFile() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool generate certificate request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolGenerateCertificateRequest request, Commandline cli )
    {
        addArgIfTrue( cli, "-rfc", request.isRfc() );
        addArgIfNotEmpty( cli, "-infile", request.getInfile() );
        addArgIfNotEmpty( cli, "-outfile", request.getOutfile() );
        addArgIfNotEmpty( cli, "-sigalg", request.getSigalg() );
        addArgIfNotEmpty( cli, "-dname", request.getDname() );
        addArgIfNotEmpty( cli, "-startdate", request.getStartdate() );
        addArgIfNotEmpty( cli, "-ext", request.getExt() );
        addArgIfNotEmpty( cli, "-validity", request.getValidity() );
        addArgIfNotEmpty( cli, "-keypass", request.getKeypass() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool generate certificate request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolGenerateCertificateRequestRequest request, Commandline cli )
    {
        addArgIfNotEmpty( cli, "-sigalg", request.getSigalg() );
        addArgIfNotEmpty( cli, "-file", request.getFile() );
        addArgIfNotEmpty( cli, "-keypass", request.getKeypass() );
        addArgIfNotEmpty( cli, "-dname", request.getDname() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool generate key request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolGenerateKeyPairRequest request, Commandline cli )
    {
        addArgIfNotEmpty( cli, "-dname", request.getDname() );
        addArgIfNotEmpty( cli, "-keypass", request.getKeypass() );
        addArgIfNotEmpty( cli, "-validity", request.getValidity() );
        addArgIfNotEmpty( cli, "-keyalg", request.getKeyalg() );
        addArgIfNotEmpty( cli, "-keysize", request.getKeysize() );
        addArgIfNotEmpty( cli, "-sigalg", request.getSigalg() );
        addArgIfNotEmpty( cli, "-startdate", request.getStartdate() );
        addArgIfNotEmpty( cli, "-ext", request.getExt() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool generate key request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolGenerateSecretKeyRequest request, Commandline cli )
    {
        addArgIfNotEmpty( cli, "-keypass", request.getKeypass() );
        addArgIfNotEmpty( cli, "-keyalg", request.getKeyalg() );
        addArgIfNotEmpty( cli, "-keysize", request.getKeysize() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool import request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolImportCertificateRequest request, Commandline cli )
    {
        addArgIfTrue( cli, "-noprompt", request.isNoprompt() );
        addArgIfTrue( cli, "-trustcacerts", request.isTrustcacerts() );
        addArgIfNotEmpty( cli, "-file", request.getFile() );
        addArgIfNotEmpty( cli, "-keypass", request.getKeypass() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool import request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolImportKeystoreRequest request, Commandline cli )
    {
        addArgIfTrue( cli, "-noprompt", request.isNoprompt() );
        addArgIfNotEmpty( cli, "-srcprotected", request.isSrcprotected() ? Boolean.TRUE.toString() : "" );
        addArgIfNotEmpty( cli, "-srckeystore", request.getSrckeystore() );
        addArgIfNotEmpty( cli, "-destkeystore", request.getDestkeystore() );
        addArgIfNotEmpty( cli, "-srcstoretype", request.getSrcstoretype() );
        addArgIfNotEmpty( cli, "-deststoretype", request.getDeststoretype() );
        addArgIfNotEmpty( cli, "-srcstorepass", request.getSrckeypass() );
        addArgIfNotEmpty( cli, "-deststorepass", request.getDestkeypass() );
        addArgIfNotEmpty( cli, "-srcprovidername", request.getSrcprovidername() );
        addArgIfNotEmpty( cli, "-destprovidername", request.getDestprovidername() );
        addArgIfNotEmpty( cli, "-srcalias", request.getSrcalias() );
        addArgIfNotEmpty( cli, "-destalias", request.getDestalias() );
        addArgIfNotEmpty( cli, "-srckeypass", request.getSrckeypass() );
        addArgIfNotEmpty( cli, "-destkeypass", request.getDestkeypass() );
        addArgIfNotEmpty( cli, "-providerclass", request.getProviderclass() );
        addArgIfNotEmpty( cli, "-providerarg", request.getProviderarg() );
        addArgIfNotEmpty( cli, "-providerpath", request.getProviderpath() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool import request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolListRequest request, Commandline cli )
    {
        addArgIfTrue( cli, "-rfc", request.isRfc() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool import request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolPrintCertificateRequest request, Commandline cli )
    {
        addArgIfTrue( cli, "-rfc", request.isRfc() );
        addArgIfNotEmpty( cli, "-file", request.getFile() );
        addArgIfNotEmpty( cli, "-sslserver", request.getSslserver() );
        addArgIfNotEmpty( cli, "-jarfile", request.getJarfile() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool import request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolPrintCertificateRequestRequest request, Commandline cli )
    {
        addArgIfNotEmpty( cli, "-file", request.getFile() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool import request
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolPrintCRLFileRequest request, Commandline cli )
    {
        addArgIfNotEmpty( cli, "-file", request.getFile() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool import request
     * @param cli     the commandline client to prepare
     */
    protected void buildWithProviders( KeyToolRequestWithKeyStoreParameters request, Commandline cli )
    {
        addArgIfNotEmpty( cli, "-keystore", request.getKeystore() );
        addArgIfNotEmpty( cli, "-storepass", request.getStorepass() );
        addArgIfNotEmpty( cli, "-storetype", request.getStoretype() );
        addArgIfNotEmpty( cli, "-providername", request.getProvidername() );
        addArgIfNotEmpty( cli, "-providerclass", request.getProviderclass() );
        addArgIfNotEmpty( cli, "-providerarg", request.getProviderarg() );
        addArgIfNotEmpty( cli, "-providerpath", request.getProviderpath() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool import request
     * @param cli     the commandline client to prepare
     */
    protected void buildWithProviders( KeyToolRequestWithKeyStoreAndAliasParameters request, Commandline cli )
    {
        addArgIfNotEmpty( cli, "-protected", request.isPasswordProtected() ? Boolean.TRUE.toString() : "" );
        addArgIfNotEmpty( cli, "-alias", request.getAlias() );
    }

    /**
     * Convenience method to add an argument to the <code>command line</code> if the the value is not null or empty.
     *
     * @param cli   command line to fill
     * @param key   the argument name.
     * @param value the argument value to be added.
     */
    protected void addArgIfNotEmpty( Commandline cli, String key, String value )
    {
        if ( !StringUtils.isEmpty( value ) )
        {
            addArg( cli, key );
            addArg( cli, value );
        }
    }

    /**
     * Convenience method to add an argument to the <code>command line</code> if the the value is not null or empty.
     *
     * @param cli   command line to fill
     * @param key   the argument name.
     * @param value the argument value to be added.
     */
    protected void addArgIfNotEmpty( Commandline cli, String key, File value )
    {
        if ( value != null )
        {
            addArg( cli, key );
            addArg( cli, value );
        }
    }

    /**
     * Convenience method to add an argument to the <code>command line</code> if the the value is true.
     *
     * @param cli   command line to fill
     * @param key   the argument name.
     * @param value the argument value to be test.
     */
    protected void addArgIfTrue( Commandline cli, String key, boolean value )
    {
        if ( value )
        {
            addArg( cli, key );
        }
    }

    /**
     * Convinience method to add an argument to the {@code command line}.
     *
     * @param cli   command line to fill
     * @param value the argument value to be added
     */
    protected void addArg( Commandline cli, String value )
    {
        cli.createArg().setValue( value );
    }

    /**
     * Convinience method to add a file argument to the {@code command line}.
     *
     * @param cli   command line to fill
     * @param value the file argument value to be added
     */
    protected void addArg( Commandline cli, File value )
    {
        cli.createArg().setFile( value );
    }


}
