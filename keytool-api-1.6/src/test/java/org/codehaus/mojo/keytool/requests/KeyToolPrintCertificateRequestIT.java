package org.codehaus.mojo.keytool.requests;

import org.codehaus.mojo.keytool.KeyToolResult;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link KeyToolPrintCertificateRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolPrintCertificateRequestIT
    extends AbstractKeyToolRequestIT
{

    public void testSimpleRequest()
        throws Exception
    {

        URL certificateURL = getCertificateURL( "simple" );
        assertNotNull( certificateURL );
        File file = new File( workingDirectory, "testSimpleRequest-certificate" );
        assertFalse( file.exists() );
        copyURLToFile( certificateURL, file );
        assertTrue( file.exists() );

        KeyToolPrintCertificateRequest request = new KeyToolPrintCertificateRequest();

        request.setFile( file );

        request.setVerbose( true );
        request.setRfc( true );

        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        assertKeyToolResult( keyToolResult, new String[]{ "-printcert", "-v", "-rfc", "-file", file.getAbsolutePath() },
                             0 );


    }

}
