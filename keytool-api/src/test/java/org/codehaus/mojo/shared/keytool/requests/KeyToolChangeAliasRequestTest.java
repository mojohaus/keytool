package org.codehaus.mojo.shared.keytool.requests;

import junit.framework.Assert;
import org.codehaus.mojo.shared.keytool.KeyToolResult;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link KeyToolChangeAliasRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolChangeAliasRequestTest
    extends AbstractKeyToolRequestTest
{

    public void testSimpleRequest()
        throws Exception
    {

        URL keyStoreURL = getKeyStoreURL( "simple" );
        File keyStore = new File( workingDirectory, "testSimpleRequest-keystore" );
        copyURLToFile( keyStoreURL, keyStore );
        Assert.assertTrue( keyStore.exists() );

        KeyToolChangeAliasRequest request = new KeyToolChangeAliasRequest();
        request.setAlias( "foo_alias" );
        request.setDestalias( "new_alias" );
        request.setStoretype( "jks" );
        request.setStorepass( "changeit" );
        request.setKeystore( keyStore.getAbsolutePath() );
        request.setKeypass( "key-passwd" );
        request.setVerbose( true );

        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        assertKeyToolResult( keyToolResult,
                             new String[]{ "-changealias", "-v", "-keystore", keyStore.getAbsolutePath(), "-storepass",
                                 "changeit", "-storetype", "jks", "-alias", "foo_alias", "-destalias", "new_alias",
                                 "-keypass", "key-passwd" }, 0 );

    }

}
