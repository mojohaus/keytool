package org.codehaus.mojo.shared.keytool.requests;

import junit.framework.Assert;
import org.codehaus.mojo.shared.keytool.KeyToolResult;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link KeyToolDeleteRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolDeleteRequestTest
    extends AbstractKeyToolRequestTest
{

    public void testSimpleRequest()
        throws Exception
    {

        URL keyStoreURL = getKeyStoreURL( "simple" );
        File keyStore = new File( workingDirectory, "testSimpleRequest-keystore" );
        copyURLToFile( keyStoreURL, keyStore );
        Assert.assertTrue( keyStore.exists() );

        KeyToolDeleteRequest request = new KeyToolDeleteRequest();
        request.setAlias( "foo_alias" );
        request.setStoretype( "jks" );
        request.setStorepass( "changeit" );
        request.setKeystore( keyStore.getAbsolutePath() );
        request.setVerbose( true );

        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        assertKeyToolResult( keyToolResult,
                             new String[]{ "-delete", "-v", "-keystore", keyStore.getAbsolutePath(), "-storepass",
                                 "changeit", "-storetype", "jks", "-alias", "foo_alias" }, 0 );

    }

}
