package org.codehaus.mojo.shared.keytool.requests;

import junit.framework.Assert;
import org.codehaus.mojo.shared.keytool.KeyToolResult;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link KeyToolExportCertificateRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolExportCertificateRequestTest
    extends AbstractKeyToolRequestTest
{

    public void testSimpleRequest()
        throws Exception
    {

        URL keyStoreURL = getKeyStoreURL( "simple" );
        File keyStore = new File( workingDirectory, "testSimpleRequest-keystore" );
        copyURLToFile( keyStoreURL, keyStore );
        Assert.assertTrue( keyStore.exists() );

        File outputFile = new File( workingDirectory, "outputFile" );
        Assert.assertFalse( outputFile.exists() );

        KeyToolExportCertificateRequest request = new KeyToolExportCertificateRequest();
        request.setAlias( "foo_alias" );
        request.setStoretype( "jks" );
        request.setStorepass( "changeit" );
        request.setKeystore( keyStore.getAbsolutePath() );
        request.setRfc( true );
        
        request.setFile( outputFile.getAbsolutePath() );
        request.setVerbose( true );

        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        assertKeyToolResult( keyToolResult,
                             new String[]{ "-export", "-v", "-keystore", keyStore.getAbsolutePath(), "-storepass",
                                 "changeit", "-storetype", "jks", "-alias", "foo_alias", "-rfc", "-file",
                                 outputFile.getAbsolutePath() }, 0 );

        Assert.assertTrue( outputFile.exists() );

    }

}
