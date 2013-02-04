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

import org.codehaus.mojo.keytool.requests.KeyToolChangeAliasRequest;
import org.codehaus.mojo.keytool.requests.KeyToolChangeKeyPasswordRequest;
import org.codehaus.mojo.keytool.requests.KeyToolChangeStorePasswordRequest;
import org.codehaus.mojo.keytool.requests.KeyToolDeleteRequest;
import org.codehaus.mojo.keytool.requests.KeyToolExportCertificateRequest;
import org.codehaus.mojo.keytool.requests.KeyToolGenerateCertificateRequest;
import org.codehaus.mojo.keytool.requests.KeyToolGenerateCertificateRequestRequest;
import org.codehaus.mojo.keytool.requests.KeyToolGenerateKeyPairRequest;
import org.codehaus.mojo.keytool.requests.KeyToolGenerateSecretKeyRequest;
import org.codehaus.mojo.keytool.requests.KeyToolImportCertificateRequest;
import org.codehaus.mojo.keytool.requests.KeyToolImportKeystoreRequest;
import org.codehaus.mojo.keytool.requests.KeyToolListRequest;
import org.codehaus.mojo.keytool.requests.KeyToolPrintCRLFileRequest;
import org.codehaus.mojo.keytool.requests.KeyToolPrintCertificateRequest;
import org.codehaus.mojo.keytool.requests.KeyToolPrintCertificateRequestRequest;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * To build the command line for a given {@link KeyToolRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @version $Id$
 * @plexus.component role="org.codehaus.mojo.keytool.KeyToolCommandLineBuilder" role-hint="default"
 * @since 1.1
 */
public class DefaultKeyToolCommandLineBuilder
    extends AbstractKeyToolCommandLineBuilder
{

    /**
     * {@inheritDoc}
     */
    public Commandline build( KeyToolRequest request )
        throws CommandLineConfigurationException
    {
        checkRequiredState();

        Commandline cli = new Commandline();

        cli.setExecutable( getKeyToolFile() );

        cli.setWorkingDirectory( request.getWorkingDirectory() );

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
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolChangeAliasRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-changealias", request );
        addArgIfNotEmpty( cli, "-destalias", request.getDestalias() );
        addArgIfNotEmpty( cli, "-keypass", request.getKeypass() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolChangeKeyPasswordRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-keypasswd", request );
        addArgIfNotEmpty( cli, "-keypass", request.getKeypass() );
        addArgIfNotEmpty( cli, "-new", request.getNewPassword() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolChangeStorePasswordRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-storepasswd", request );
        addArgIfNotEmpty( cli, "-new", request.getNewPassword() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolDeleteRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-delete", request );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolExportCertificateRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-export", request );
        addArgIfTrue( cli, "-rfc", request.isRfc() );
        addArgIfNotEmpty( cli, "-file", request.getFile() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolGenerateCertificateRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-gencert", request );
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
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolGenerateCertificateRequestRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-certreq", request );
        addArgIfNotEmpty( cli, "-sigalg", request.getSigalg() );
        addArgIfNotEmpty( cli, "-file", request.getFile() );
        addArgIfNotEmpty( cli, "-keypass", request.getKeypass() );
        addArgIfNotEmpty( cli, "-dname", request.getDname() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolGenerateKeyPairRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-genkeypair", request );
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
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolGenerateSecretKeyRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-genseckey", request );
        addArgIfNotEmpty( cli, "-keypass", request.getKeypass() );
        addArgIfNotEmpty( cli, "-keyalg", request.getKeyalg() );
        addArgIfNotEmpty( cli, "-keysize", request.getKeysize() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolImportCertificateRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-importcert", request );
        addArgIfTrue( cli, "-noprompt", request.isNoprompt() );
        addArgIfTrue( cli, "-trustcacerts", request.isTrustcacerts() );
        addArgIfNotEmpty( cli, "-file", request.getFile() );
        addArgIfNotEmpty( cli, "-keypass", request.getKeypass() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolImportKeystoreRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-importkeystore", request );
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
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolListRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-list", request );
        addArgIfTrue( cli, "-rfc", request.isRfc() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolPrintCertificateRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-printcert", request );
        addArgIfTrue( cli, "-rfc", request.isRfc() );
        addArgIfNotEmpty( cli, "-file", request.getFile() );
        addArgIfNotEmpty( cli, "-sslserver", request.getSslserver() );
        addArgIfNotEmpty( cli, "-jarfile", request.getJarfile() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolPrintCertificateRequestRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-printcertreq", request );
        addArgIfNotEmpty( cli, "-file", request.getFile() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool request to consume
     * @param cli     the commandline client to prepare
     */
    protected void build( KeyToolPrintCRLFileRequest request, Commandline cli )
    {
        addKeytoolCommandAndDefaultoptions( cli, "-printcrl", request );
        addArgIfNotEmpty( cli, "-file", request.getFile() );
    }

}
