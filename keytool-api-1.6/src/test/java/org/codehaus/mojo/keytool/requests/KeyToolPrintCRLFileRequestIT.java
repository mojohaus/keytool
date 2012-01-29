package org.codehaus.mojo.keytool.requests;

import org.codehaus.mojo.keytool.KeyToolResult;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link org.codehaus.mojo.keytool.requests.KeyToolPrintCRLFileRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolPrintCRLFileRequestIT
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

        KeyToolPrintCRLFileRequest request = new KeyToolPrintCRLFileRequest();

        request.setFile( file );
        request.setVerbose( true );

        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        assertKeyToolResult( keyToolResult, new String[]{ "-printcrl", "-v", "-file", file.getAbsolutePath() } );

        //FIXME tchemit 2011-11-06 : this only works with jdk 7
//        ,0);
    }

}
