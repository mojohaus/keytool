package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolExportCertificateRequest;

/**
 * To export a certificate from a keystore.
 * <p/>
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -export} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal exportCertificate
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class ExportCertificateMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolExportCertificateRequest>
{

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
     * Output file name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${file}"
     * @since 1.2
     */
    private String file;

    public ExportCertificateMojo()
    {
        super( KeyToolExportCertificateRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolExportCertificateRequest prepareRequest()
    {
        KeyToolExportCertificateRequest request = super.prepareRequest();

        request.setFile( this.file );
        request.setRfc( this.rfc );
        return request;
    }
}
