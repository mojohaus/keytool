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

import org.codehaus.mojo.keytool.requests.KeyToolGenerateCertificateRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;

/**
 * To generate certificate from a certificate request from a keystore.
 * <p/>
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -gencert} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal generateCertificate
 * @requiresProject
 * @since 1.2
 */
public class GenerateCertificateMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolGenerateCertificateRequest>
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
     * Output in RFC style.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${rfc}"
     * @since 1.2
     */
    private boolean rfc;

    /**
     * input file name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${infile}"
     * @since 1.2
     */
    private File infile;

    /**
     * output file name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${outfile}"
     * @since 1.2
     */
    private File outfile;

    /**
     * Signature algorithm name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${sigalg}"
     * @since 1.2
     */
    private String sigalg;

    /**
     * Distinguished name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${dname}"
     * @since 1.2
     */
    private String dname;

    /**
     * Certificate validity start date/time.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${startdate}"
     * @since 1.2
     */
    private String startdate;

    /**
     * X.509 extension.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${ext}"
     * @since 1.2
     */
    private String ext;

    /**
     * Validity number of days.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${validity}"
     * @since 1.2
     */
    private String validity;

    /**
     * Default contructor.
     */
    public GenerateCertificateMojo()
    {
        super( KeyToolGenerateCertificateRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolGenerateCertificateRequest createKeytoolRequest()
    {
        KeyToolGenerateCertificateRequest request = super.createKeytoolRequest();

        request.setKeypass( this.keypass );
        request.setRfc( this.rfc );
        request.setInfile( this.infile );
        request.setOutfile( this.outfile );
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
