package org.codehaus.mojo.keytool.requests;

import junit.framework.Assert;
import org.codehaus.mojo.keytool.KeyToolResult;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link KeyToolPrintCertificateRequestRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolPrintCertificateRequestRequestIT
    extends AbstractKeyToolRequestIT
{

    public void testSimpleRequest()
        throws Exception
    {

        URL certificateURL = getCertificateRequestURL( "simple" );
        Assert.assertNotNull( certificateURL );
        File file = new File( workingDirectory, "testSimpleRequest-certificate" );
        Assert.assertFalse( file.exists() );
        copyURLToFile( certificateURL, file );
        Assert.assertTrue( file.exists() );

        KeyToolPrintCertificateRequestRequest request = new KeyToolPrintCertificateRequestRequest();

        request.setFile( file );
        request.setVerbose( true );

        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        assertKeyToolResult( keyToolResult, new String[]{ "-printcertreq", "-v", "-file", file.getAbsolutePath() } );
        //FIXME tchemit 2011-11-06 : this only works with jdk 7
//        ,0);
    }

}
