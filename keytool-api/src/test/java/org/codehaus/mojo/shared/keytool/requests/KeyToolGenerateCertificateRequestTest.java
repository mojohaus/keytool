package org.codehaus.mojo.shared.keytool.requests;

import org.codehaus.mojo.shared.keytool.KeyToolResult;
import org.junit.Assert;

import java.io.File;
import java.net.URL;

/**
 * Test the {@link KeyToolGenerateCertificateRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolGenerateCertificateRequestTest
    extends AbstractKeyToolRequestTest
{

    //FIXME : tchemit 2011-11-06 : find a way to check jdk >= 1.7
    public void testSimpleRequest()
        throws Exception
    {

//        String javaVersion = System.getProperty( "java.version" );

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

        KeyToolResult keyToolResult = executeKeyToolRequest( request );

        System.out.println( keyToolResult.getCommandline().toString() );
        assertKeyToolResult( keyToolResult,
                             new String[]{ "-gencert", "-v", "-keystore", keyStore.getAbsolutePath(), "-storepass",
                                 "changeit", "-storetype", "jks", "-alias", "foo_alias", "-rfc", "-infile",
                                 inFile.getAbsolutePath(), "-outfile", outputFile.getAbsolutePath(), "-sigalg",
                                 "SHA1withDSA", "-dname",
                                 "CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France", "-startdate",
                                 "2011/11/11", "-validity", "100", "-keypass", "key-passwd" } );
        //FIXME : tchemit 2011-11-06 : find a way to check jdk >= 1.7
//        , 0);
//        Assert.assertTrue( outputFile.exists() );

    }

}
