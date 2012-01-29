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

import org.codehaus.mojo.keytool.KeyToolResult;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link KeyToolListRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.1
 */
public class KeyToolListRequestIT
    extends AbstractKeyToolRequestIT
{

    public void testSimpleRequest()
        throws Exception
    {

        URL keyStoreURL = getKeyStoreURL( "simple" );
        File keyStore = new File( workingDirectory, "testSimpleRequest-keystore" );
        copyURLToFile( keyStoreURL, keyStore );
        assertTrue( keyStore.exists() );

        KeyToolListRequest request = new KeyToolListRequest();
        request.setAlias( "foo_alias" );

        request.setStoretype( "jks" );
        request.setStorepass( "changeit" );
        request.setKeystore( keyStore.getAbsolutePath() );
        request.setVerbose( true );
        request.setRfc( false );

        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        assertKeyToolResult( keyToolResult,
                             new String[]{ "-list", "-v", "-keystore", keyStore.getAbsolutePath(), "-storepass",
                                 "changeit", "-storetype", "jks", "-alias", "foo_alias" }, 0 );

    }

}
