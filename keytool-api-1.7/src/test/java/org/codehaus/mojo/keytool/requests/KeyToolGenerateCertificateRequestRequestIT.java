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

import org.apache.maven.shared.utils.cli.javatool.JavaToolResult;

import java.io.File;

/**
 * Test the {@link KeyToolGenerateCertificateRequestRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.1
 */
public class KeyToolGenerateCertificateRequestRequestIT
    extends AbstractKeyToolGenerateCertificateRequestRequestIT
{

    protected void requestResult( JavaToolResult keyToolResult, File keyStore, File outputFile )
    {
        assertKeyToolResult( keyToolResult,
                             new String[]{ "-certreq", "-v", "-keystore", keyStore.getAbsolutePath(), "-storepass",
                                 "changeit", "-storetype", "jks", "-alias", "foo_alias", "-sigalg", "SHA1withDSA",
                                 "-file", outputFile.getAbsolutePath(), "-keypass", "key-passwd", "-dname",
                                 "CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France" }, 0 );

        assertTrue( outputFile.exists() );
    }
}
