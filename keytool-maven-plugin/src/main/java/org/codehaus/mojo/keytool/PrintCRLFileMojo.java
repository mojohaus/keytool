package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolPrintCRLFileRequest;

import java.io.File;

/**
 * To print the content of a CRL file.
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -printcrl} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal printCRLFile
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class PrintCRLFileMojo
    extends AbstractKeyToolRequestMojo<KeyToolPrintCRLFileRequest>
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

    public PrintCRLFileMojo()
    {
        super( KeyToolPrintCRLFileRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolPrintCRLFileRequest prepareRequest()
    {
        KeyToolPrintCRLFileRequest request = super.prepareRequest();

        request.setFile( this.file );

        return request;
    }

}
