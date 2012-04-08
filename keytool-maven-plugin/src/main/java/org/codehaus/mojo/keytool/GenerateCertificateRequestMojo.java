package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolGenerateCertificateRequestRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;

/**
 * To generate certificate request.
 * <p/>
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -certreq} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal exportCertificateRequest
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class GenerateCertificateRequestMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolGenerateCertificateRequestRequest>
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
     * Output file name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${infile}"
     * @since 1.2
     */
    private File file;

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

    public GenerateCertificateRequestMojo()
    {
        super( KeyToolGenerateCertificateRequestRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolGenerateCertificateRequestRequest prepareRequest()
    {
        KeyToolGenerateCertificateRequestRequest request = super.prepareRequest();

        request.setSigalg( this.sigalg );
        request.setDname( this.dname );
        request.setFile( this.file );
        request.setKeypass( this.keypass );
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
