package org.codehaus.mojo.keytool.requests;

/*
 * Copyright 2005-2012 The Codehaus
 *
 * Licensed under the Apache License, Version 2.0 (the "License" );
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.Assert;
import org.codehaus.mojo.keytool.KeyToolResult;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link KeyToolGenerateKeyPairRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.1
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
