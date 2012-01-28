package org.codehaus.mojo.shared.keytool.requests;

import junit.framework.Assert;
import org.codehaus.mojo.shared.keytool.KeyToolResult;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link KeyToolPrintCertificateRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolPrintCertificateRequestTest
    extends AbstractKeyToolRequestTest
{

    public void testSimpleRequest()
        throws Exception
    {

        URL certificateURL = getCertificateURL( "simple" );
        Assert.assertNotNull( certificateURL );
        File file = new File( workingDirectory, "testSimpleRequest-certificate" );
        Assert.assertFalse( file.exists() );
        copyURLToFile( certificateURL, file );
        Assert.assertTrue( file.exists() );

        KeyToolPrintCertificateRequest request = new KeyToolPrintCertificateRequest();

        request.setFile( file );

        request.setVerbose( true );
        request.setRfc( true );

        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        assertKeyToolResult( keyToolResult, new String[]{ "-printcert", "-v", "-rfc", "-file", file.getAbsolutePath() },
                             0 );


    }

}
