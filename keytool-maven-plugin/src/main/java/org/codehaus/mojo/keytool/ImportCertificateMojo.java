package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolImportCertificateRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * To import a certificate into a key store.
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
 * @phase package
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

    public ImportCertificateMojo()
    {
        super( KeyToolImportCertificateRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolImportCertificateRequest prepareRequest()
    {
        KeyToolImportCertificateRequest request = super.prepareRequest();

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
