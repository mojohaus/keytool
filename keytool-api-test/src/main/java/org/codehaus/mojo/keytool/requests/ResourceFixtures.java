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

import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.junit.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Input resources fixtures.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.3
 */
public class ResourceFixtures
{

    protected final File workingDirectory;

    public ResourceFixtures( File workingDirectory )
    {
        this.workingDirectory = workingDirectory;
    }

    public File simpleDestKeyStoreFile( String name )
    {
        File file = new File( workingDirectory, name );

        Assert.assertFalse( file.exists() );

        return file;
    }

    public File simpleDestKeyStoreFile()
    {
        return simpleDestKeyStoreFile( "destkeystore" );
    }

    public File simpleKeyStore( String name, boolean copy )
        throws IOException
    {
        URL keyStoreURL = getKeyStoreURL( "simple" );
        File keyStore = new File( workingDirectory, name );
        if ( copy )
        {
            copyURLToFile( keyStoreURL, keyStore );
        }
        return keyStore;
    }

    public File simpleKeyStore()
        throws IOException
    {
        File keyStore = simpleKeyStore( true );
        return keyStore;
    }

    public File simpleKeyStore( boolean copy )
        throws IOException
    {
        File keyStore = simpleKeyStore( "keystore.jks", copy );
        FileUtils.forceMkdir( keyStore.getParentFile() );
        return keyStore;
    }

    public File simpleKeyStore( String name )
        throws IOException
    {
        File keyStore = simpleKeyStore( name, true );
        Assert.assertTrue( keyStore.exists() );
        return keyStore;
    }

    public File simpleCertificateRequest()
        throws IOException
    {
        URL certificateRequestURL = getCertificateRequestURL( "simple" );
        File inFile = new File( workingDirectory, "inFile" );
        Assert.assertFalse( inFile.exists() );
        copyURLToFile( certificateRequestURL, inFile );
        Assert.assertTrue( inFile.exists() );
        return inFile;
    }

    public File simpleCertificate()
        throws IOException
    {
        URL certificateURL = getCertificateURL( "simple" );
        File inFile = new File( workingDirectory, "inFile" );
        Assert.assertFalse( inFile.exists() );
        copyURLToFile( certificateURL, inFile );
        Assert.assertTrue( inFile.exists() );
        return inFile;
    }

    public File outputFile()
    {
        File outputFile = new File( workingDirectory, "outputFile" );
        Assert.assertFalse( outputFile.exists() );
        return outputFile;
    }

    protected URL getKeyStoreURL( String prefix )
    {
        return getClass().getResource( "/" + prefix + "-keystore" );
    }

    protected URL getCertificateRequestURL( String prefix )
    {
        return getClass().getResource( "/" + prefix + "-certificate-request" );
    }

    protected URL getCertificateURL( String prefix )
    {
        return getClass().getResource( "/" + prefix + "-certificate" );
    }

    protected void copyURLToFile( URL url, File dest )
        throws IOException
    {
        FileUtils.mkdir( dest.getParentFile().getAbsolutePath() );
        InputStream inputStream = url.openStream();
        try
        {
            OutputStream outputStream = new FileOutputStream( dest );
            try
            {
                IOUtil.copy( inputStream, outputStream );
            }
            finally
            {
                IOUtil.close( outputStream );
            }
        }
        finally
        {
            IOUtil.close( inputStream );
        }
    }
}
