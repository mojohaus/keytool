package org.codehaus.mojo.keytool;

/*
 * Copyright 2005-2011 The Codehaus
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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.mojo.shared.keytool.KeyTool;
import org.codehaus.mojo.shared.keytool.KeyToolResult;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * These unit tests only check whether the generated command lines are correct.
 * Really running the command would mean checking the results, which is too painful and not really a unit test.
 * It would probably require to 'jarsigner -verify' the resulting signed webstart and I believe it would make the code
 * too complex with very few benefits.
 *
 * @author Jerome Lacoste <jerome@coffeebreaks.org>
 * @version $Id$
 */
public class GenkeyMojoTest
    extends AbstractMojoTestCase
{
    private MockGenkeyMojo mojo;

    static class MockGenkeyMojo
        extends GenkeyMojo
    {
        public int executeResult;

        public List commandLines = new ArrayList();

        public String failureMsg;

        public Map systemProperties = new HashMap();

        protected void consumeResult( KeyToolResult result )
            throws MojoExecutionException
        {
            commandLines.add( result.getCommandline() );
            if ( failureMsg != null )
            {
                throw new MojoExecutionException( failureMsg );
            }
            if ( executeResult != 0 )
            {
                throw new MojoExecutionException( "Result of " );
            }
        }

        protected String getSystemProperty( String key )
        {
            return (String) systemProperties.get( key );
        }
    }


    public void setUp()
        throws Exception
    {
        super.setUp();

        mojo = new MockGenkeyMojo();
        KeyTool keyTool = (KeyTool) lookup( KeyTool.ROLE );
        mojo.setKeyTool( keyTool );
        mojo.executeResult = 0;
        // it doesn't really matter if the paths are not cross-platform, we don't execute the command lines anyway
        File workingdir = new File( System.getProperty( "java.io.tmpdir" ) );
        mojo.setWorkingDir( workingdir );
        mojo.setDname( "cn=www.example.com, ou=None, L=Seattle, ST=Washington, o=ExampleOrg, c=US" );
        mojo.setKeypass( "secretpassword" );
        mojo.setStorepass( "secretpassword2" );
    }

    public void tearDown()
    {
        mojo = null;
    }

    public void testPleaseMaven()
    {
        assertTrue( true );
    }

    public void testRunOKMinimumNumberOfParameters()
        throws MojoExecutionException
    {
        mojo.execute();

        String[] expectedArguments =
            { "-genkey", "-storepass", "secretpassword2" ,"-dname", "cn=www.example.com, ou=None, L=Seattle, ST=Washington, o=ExampleOrg, c=US",
                "-keypass", "secretpassword", };

        checkMojo( expectedArguments );
    }

    public void testRunOKMaximumNumberOfParameters()
        throws MojoExecutionException
    {
        mojo.setVerbose( true );
        mojo.setAlias( "alias" );
        mojo.setKeystore( "/tmp/keystore" );
        mojo.setValidity( "90" );
        mojo.setKeyalg( "DSA" );
        mojo.setKeysize( "1024" );
        mojo.setSigalg( "SHA1withDSA" );
        mojo.setStoretype( "jks" /* java.security.KeyStore.getDefaultType() */ );

        mojo.execute();

        String[] expectedArguments =
            { "-genkey", "-v","-keystore", "/tmp/keystore", "-storepass", "secretpassword2","-storetype", "jks",
                "-alias", "alias", "-dname", "cn=www.example.com, ou=None, L=Seattle, ST=Washington, o=ExampleOrg, c=US",
                "-keypass", "secretpassword", "-validity", "90", "-keyalg", "DSA", "-keysize", "1024", "-sigalg", "SHA1withDSA",
                 };

        checkMojo( expectedArguments );
    }

    public void testRunFailure()
    {
        mojo.executeResult = 1;

        // any missing argument should produce this. Let's simulate a missing alias
        mojo.setKeystore( "/invalid/path" );

        try
        {
            mojo.execute();
            fail( "expected failure" );
        }
        catch ( MojoExecutionException e )
        {
            assertTrue( e.getMessage().startsWith( "Result of " ) );
        }

        String[] expectedArguments =
            { "-genkey", "-keystore", "/invalid/path","-storepass", "secretpassword2" ,
                "-dname", "cn=www.example.com, ou=None, L=Seattle, ST=Washington, o=ExampleOrg, c=US",
                "-keypass", "secretpassword"};

        checkMojo( expectedArguments );
    }

    public void testRunError()
    {
        mojo.failureMsg = "simulated failure";

        try
        {
            mojo.execute();
            fail( "expected failure" );
        }
        catch ( MojoExecutionException e )
        {
            assertEquals( "simulated failure", e.getMessage() );
        }

        String[] expectedArguments =
            { "-genkey", "-storepass", "secretpassword2" ,
                "-dname", "cn=www.example.com, ou=None, L=Seattle, ST=Washington, o=ExampleOrg, c=US",
                "-keypass", "secretpassword"};

        checkMojo( expectedArguments );
    }

    private void checkMojo( String[] expectedCommandLineArguments )
    {
        assertEquals( 1, mojo.commandLines.size() );
        Commandline commandline = (Commandline) mojo.commandLines.get( 0 );
        String[] arguments = commandline.getArguments();
        // isn't there an assertEquals for arrays?
        /*
        for (int i = 0; i < arguments.length; i++ ) {
            System.out.println( arguments[ i ] );
        }
        */
        assertEquals( "Differing number of arguments", expectedCommandLineArguments.length, arguments.length );
        for ( int i = 0; i < arguments.length; i++ )
        {
            assertEquals( expectedCommandLineArguments[i], arguments[i] );
        }
    }
}
