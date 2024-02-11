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
import org.junit.Assert;

import java.io.File;

/**
 * Test the {@link KeyToolGenerateCertificateRequest}.
 *
 * @author tchemit
 * @since 1.1
 */
public class KeyToolGenerateCertificateRequestIT
    extends AbstractKeyToolGenerateCertificateRequestIT
{

    @Override
    protected void requestResult( JavaToolResult keyToolResult, File keyStore, File inFile, File outputFile )
    {
        assertKeyToolResult( keyToolResult,
                             new String[]{ "-gencert", "-v", "-keystore", keyStore.getAbsolutePath(), "-storepass",
                                 "changeit", "-storetype", "jks", "-alias", "foo_alias", "-rfc", "-infile",
                                 inFile.getAbsolutePath(), "-outfile", outputFile.getAbsolutePath(), "-sigalg",
                                 "SHA1withDSA", "-dname",
                                 "CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France", "-startdate",
                                 "2011/11/11", "-validity", "100", "-keypass", "key-passwd" }, 0 );
        Assert.assertTrue( outputFile.exists() );
    }

}
