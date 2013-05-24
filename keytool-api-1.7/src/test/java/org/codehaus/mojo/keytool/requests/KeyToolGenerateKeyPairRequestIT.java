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
import org.junit.Assert;

import java.io.File;

/**
 * Test the {@link KeyToolGenerateKeyPairRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.1
 */
public class KeyToolGenerateKeyPairRequestIT
    extends AbstractKeyToolGenerateKeyPairRequestIT
{

    @Override
    protected void requestResult( KeyToolResult keyToolResult, File keyStore )
    {
        assertKeyToolResult( keyToolResult,
                             new String[]{ "-genkeypair", "-v", "-keystore", keyStore.getAbsolutePath(), "-storepass",
                                 "changeit", "-storetype", "jks", "-alias", "dest_foo_alias", "-dname",
                                 "CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France", "-keypass",
                                 "key-passwd", "-validity", "100", "-keyalg", "DSA", "-keysize", "1024", "-sigalg",
                                 "SHA1withDSA", "-startdate", "2011/11/11" }, 0 );

        // key store was created
        Assert.assertTrue( keyStore.exists() );
    }

}
