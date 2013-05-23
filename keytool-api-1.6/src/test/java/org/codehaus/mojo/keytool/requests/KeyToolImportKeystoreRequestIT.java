package org.codehaus.mojo.keytool.requests;

/*
 * Copyright 2005-2013 The Codehaus
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
 * Test the {@link org.codehaus.mojo.keytool.requests.KeyToolImportKeystoreRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.3
 */
public class KeyToolImportKeystoreRequestIT
    extends AbstractKeyToolRequestIT
{

    public void testSimpleRequest()
        throws Exception
    {

        URL keyStoreURL = getKeyStoreURL( "simple" );
        File srcKeyStore = new File( workingDirectory, "testSimpleRequest-srckeystore" );
        File destKeyStore = new File( workingDirectory, "testSimpleRequest-destkeystore" );
        copyURLToFile( keyStoreURL, srcKeyStore );
        assertTrue( srcKeyStore.exists() );
        assertFalse( destKeyStore.exists() );

        KeyToolImportKeystoreRequest request = new KeyToolImportKeystoreRequest();

        request.setSrcalias( "foo_alias" );
        request.setDestalias( "new_alias" );

        request.setSrcstoretype( "jks" );
        request.setDeststoretype( "jks" );

        request.setSrcstorepass( "changeit" );
        request.setDeststorepass( "changeit" );

        request.setSrckeystore( srcKeyStore.getAbsolutePath() );
        request.setDestkeystore( destKeyStore.getAbsolutePath() );

        request.setSrckeypass( "key-passwd" );
        request.setDestkeypass( "key-passwd" );
        request.setVerbose( true );
        request.setNoprompt( true );

        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        assertKeyToolResult( keyToolResult, new String[]{ "-importkeystore", "-v", "-noprompt", "-srckeystore",
            srcKeyStore.getAbsolutePath(), "-destkeystore", destKeyStore.getAbsolutePath(), "-srcstoretype", "jks",
            "-deststoretype", "jks", "-srcstorepass", "changeit", "-deststorepass", "changeit", "-srcalias",
            "foo_alias", "-destalias", "new_alias", "-srckeypass", "key-passwd", "-destkeypass", "key-passwd", }, 0 );

        assertTrue( destKeyStore.exists() );
    }
}