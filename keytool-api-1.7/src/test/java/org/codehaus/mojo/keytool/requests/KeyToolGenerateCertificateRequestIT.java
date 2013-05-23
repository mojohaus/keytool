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
import org.junit.Assert;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link KeyToolGenerateCertificateRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.1
 */
public class KeyToolGenerateCertificateRequestIT
    extends AbstractKeyToolRequestIT
{

    public void testSimpleRequest()
        throws Exception
    {

        URL keyStoreURL = getKeyStoreURL( "simple" );
        File keyStore = new File( workingDirectory, "testSimpleRequest-keystore" );
        Assert.assertFalse( keyStore.exists() );
        copyURLToFile( keyStoreURL, keyStore );
        Assert.assertTrue( keyStore.exists() );

        File outputFile = new File( workingDirectory, "outputFile" );
        Assert.assertFalse( outputFile.exists() );

        URL certificateRequestURL = getCertificateRequestURL( "simple" );
        File inFile = new File( workingDirectory, "inFile" );
        Assert.assertFalse( inFile.exists() );
        copyURLToFile( certificateRequestURL, inFile );
        Assert.assertTrue( inFile.exists() );

        KeyToolGenerateCertificateRequest request = new KeyToolGenerateCertificateRequest();
        request.setAlias( "foo_alias" );
        request.setStoretype( "jks" );
        request.setStorepass( "changeit" );
        request.setKeystore( keyStore.getAbsolutePath() );
        request.setKeypass( "key-passwd" );
        request.setRfc( true );

        request.setInfile( inFile );
        request.setOutfile( outputFile );
        request.setSigalg( "SHA1withDSA" );
        request.setDname( "CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France" );
        request.setStartdate( "2011/11/11" );
        request.setExt( "" );
        request.setValidity( "100" );
        request.setVerbose( true );

        Assert.assertFalse( outputFile.exists() );

        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        System.out.println( keyToolResult.getCommandline().toString() );
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
