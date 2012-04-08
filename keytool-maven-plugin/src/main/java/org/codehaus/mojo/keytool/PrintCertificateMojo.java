package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolPrintCertificateRequest;

import java.io.File;

/**
 * To print the content of a certificate of a key store.
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -printcert} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 * <p/>
 * <strong>Since version 1.2, this mojo replace the mojo import.</strong>
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal printCertificate
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class PrintCertificateMojo
    extends AbstractKeyToolRequestMojo<KeyToolPrintCertificateRequest>
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
     * Input file name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${file}"
     * @since 1.2
     */
    private File file;

    /**
     * SSL server host and port.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${sslserver}"
     * @since 1.2
     */
    private String sslserver;

    /**
     * Signed jar file.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${jarfile}"
     * @since 1.2
     */
    private File jarfile;

    public PrintCertificateMojo()
    {
        super( KeyToolPrintCertificateRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolPrintCertificateRequest prepareRequest()
    {
        KeyToolPrintCertificateRequest request = super.prepareRequest();

        request.setRfc( this.rfc );
        request.setFile( this.file );
        request.setSslserver( this.sslserver );
        request.setJarfile( this.jarfile );
        return request;
    }

}
