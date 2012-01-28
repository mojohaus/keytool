package org.codehaus.mojo.shared.keytool.requests;

import junit.framework.Assert;
import org.codehaus.mojo.shared.keytool.KeyToolResult;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link KeyToolGenerateCertificateRequestRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolGenerateCertificateRequestRequestTest
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

        KeyToolGenerateCertificateRequestRequest request = new KeyToolGenerateCertificateRequestRequest();
        request.setAlias( "foo_alias" );
        request.setStoretype( "jks" );
        request.setStorepass( "changeit" );
        request.setKeystore( keyStore.getAbsolutePath() );
        request.setKeypass( "key-passwd" );
        request.setFile( outputFile );
        request.setSigalg( "SHA1withDSA" );
        request.setDname( "CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France" );
        request.setVerbose( true );
        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        assertKeyToolResult( keyToolResult,
                             new String[]{ "-certreq", "-v", "-keystore", keyStore.getAbsolutePath(), "-storepass",
                                 "changeit", "-storetype", "jks", "-alias", "foo_alias", "-sigalg", "SHA1withDSA",
                                 "-file", outputFile.getAbsolutePath(), "-keypass", "key-passwd", "-dname",
                                 "CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France" }, 0 );

        Assert.assertTrue( outputFile.exists() );

    }

}
