package org.codehaus.mojo.keytool.requests;

import junit.framework.Assert;
import org.codehaus.mojo.keytool.KeyToolResult;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link KeyToolGenerateKeyPairRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolGenerateKeyPairRequestIT
    extends AbstractKeyToolRequestIT
{

    public void testSimpleRequest()
        throws Exception
    {

        URL keyStoreURL = getKeyStoreURL( "simple" );
        File keyStore = new File( workingDirectory, "testSimpleRequest-keystore" );
        copyURLToFile( keyStoreURL, keyStore );
        Assert.assertTrue( keyStore.exists() );

        KeyToolGenerateKeyPairRequest request = new KeyToolGenerateKeyPairRequest();
        request.setAlias( "dest_foo_alias" );
        request.setStoretype( "jks" );
        request.setStorepass( "changeit" );
        request.setKeystore( keyStore.getAbsolutePath() );
        request.setKeypass( "key-passwd" );
        request.setSigalg( "SHA1withDSA" );
        request.setDname( "CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France" );
        request.setVerbose( true );
        request.setValidity( "100" );
        request.setStartdate( "2011/11/11" );
        request.setKeyalg( "DSA" );
        request.setKeysize( "1024" );
        request.setExt( "" );

        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        System.out.println( keyToolResult.getCommandline().toString() );

        assertKeyToolResult( keyToolResult,
                             new String[]{ "-genkeypair", "-v", "-keystore", keyStore.getAbsolutePath(), "-storepass",
                                 "changeit", "-storetype", "jks", "-alias", "dest_foo_alias", "-dname",
                                 "CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France", "-keypass",
                                 "key-passwd", "-validity", "100", "-keyalg", "DSA", "-keysize", "1024", "-sigalg",
                                 "SHA1withDSA", "-startdate", "2011/11/11" }, 0 );

    }

}
