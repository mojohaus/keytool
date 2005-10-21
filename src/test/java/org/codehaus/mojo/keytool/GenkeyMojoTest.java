package org.codehaus.mojo.keytool;

/*
 * Copyright 2005 The Codehaus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

import junit.framework.TestCase;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
 * @version $Id:$
 */
public class GenkeyMojoTest
    extends TestCase
{
    private MockGenkeyMojo mojo;

    static class MockGenkeyMojo
        extends GenkeyMojo
    {
        public int executeResult;

        public List commandLines = new ArrayList();

        public String failureMsg;

        public Map systemProperties = new HashMap();

        protected int executeCommandLine( Commandline commandLine, InputStream inputStream, StreamConsumer stream1,
                                          StreamConsumer stream2 )
            throws CommandLineException
        {
            commandLines.add( commandLine );
            if ( failureMsg != null )
            {
                throw new CommandLineException( failureMsg );
            }
            return executeResult;
        }

        protected String getSystemProperty( String key )
        {
            return (String) systemProperties.get( key );
        }
    }


    public void setUp()
        throws IOException
    {
        mojo = new MockGenkeyMojo();
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

    /**
     */
    public void testRunOKMinimumNumberOfParameters()
        throws MojoExecutionException
    {
        mojo.execute();

        String[] expectedArguments = {"-genkey", "-dname",
                                      "cn=www.example.com, ou=None, L=Seattle, ST=Washington, o=ExampleOrg, c=US",
                                      "-keypass", "secretpassword", "-storepass", "secretpassword2"};

        checkMojo( expectedArguments );
    }

    /**
     */
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

        String[] expectedArguments = {"-genkey", "-v", "-dname",
                                      "cn=www.example.com, ou=None, L=Seattle, ST=Washington, o=ExampleOrg, c=US",
                                      "-alias", "alias", "-keypass", "secretpassword", "-keystore", "/tmp/keystore",
                                      "-storepass", "secretpassword2", "-validity", "90", "-keyalg", "DSA", "-keysize",
                                      "1024", "-sigalg", "SHA1withDSA", "-storetype", "jks", };

        checkMojo( expectedArguments );
    }

    /**
     */
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

        String[] expectedArguments = {"-genkey", "-dname",
                                      "cn=www.example.com, ou=None, L=Seattle, ST=Washington, o=ExampleOrg, c=US",
                                      "-keypass", "secretpassword", "-keystore", "/invalid/path", "-storepass",
                                      "secretpassword2"};

        checkMojo( expectedArguments );
    }

    /**
     */
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
            assertEquals( "command execution failed", e.getMessage() );
        }

        String[] expectedArguments = {"-genkey", "-dname",
                                      "cn=www.example.com, ou=None, L=Seattle, ST=Washington, o=ExampleOrg, c=US",
                                      "-keypass", "secretpassword", "-storepass", "secretpassword2"};

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
