package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolPrintCertificateRequestRequest;

import java.io.File;

/**
 * To print the content of a certificate request of a key store.
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -printcertreq} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal printCertificateRequest
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class PrintCertificateRequestMojo
    extends AbstractKeyToolRequestMojo<KeyToolPrintCertificateRequestRequest>
{

    /**
     * Input file name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${file}"
     * @since 1.2
     */
    private File file;

    public PrintCertificateRequestMojo()
    {
        super( KeyToolPrintCertificateRequestRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolPrintCertificateRequestRequest prepareRequest()
    {
        KeyToolPrintCertificateRequestRequest request = super.prepareRequest();

        request.setFile( this.file );
        return request;
    }

}
