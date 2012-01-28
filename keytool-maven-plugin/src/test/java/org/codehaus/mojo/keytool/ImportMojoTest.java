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

import org.apache.commons.lang.SystemUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.mojo.shared.keytool.KeyTool;
import org.codehaus.mojo.shared.keytool.KeyToolResult;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;
import java.util.*;

/**
 * These unit tests only check whether the generated command lines are correct. Really running the command would mean
 * checking the results, which is too painful and not really a unit test.
 *
 * @author Juergen Mayrbaeurl <j.mayrbaeurl@yahoo.de>
 * @version 1.0 2008-02-03
 */
public class ImportMojoTest
    extends AbstractMojoTestCase
{

    private MockImportMojo mojo;

    static class MockImportMojo
        extends ImportMojo
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
    }

    public void setUp()
        throws Exception
    {
        super.setUp();
        mojo = new MockImportMojo();
        KeyTool keyTool = (KeyTool) lookup( KeyTool.ROLE );
        mojo.setKeyTool( keyTool );
        mojo.executeResult = 0;
        // it doesn't really matter if the paths are not cross-platform, we don't execute the command lines anyway
        File workingdir = new File( System.getProperty( "java.io.tmpdir" ) );
        mojo.setWorkingDir( workingdir );
        mojo.setKeypass( "secretpassword" );
        mojo.setStorepass( "secretpassword2" );
    }

    public void tearDown()
    {
        mojo = null;
    }

    public void testRunOKMinimumNumberOfParameters()
        throws MojoExecutionException
    {
        mojo.execute();

        String[] expectedArguments =
            { "-importcert", "-storepass", "secretpassword2", "-noprompt", "-keypass", "secretpassword" };

        checkMojo( expectedArguments );
    }

    public void testRunOKImportJRECaCerts()
        throws MojoExecutionException
    {
        mojo.setFile( "certsfile" );
        mojo.setUseJREcacerts( true );
        File cacertsFile = new File( SystemUtils.getJavaHome() + "/" + "lib/security/cacerts" );

        mojo.execute();

        String[] expectedArguments =
            { "-importcert", "-keystore", cacertsFile.getAbsolutePath(), "-storepass", "secretpassword2" ,
                "-noprompt", "-file", "certsfile", "-keypass",
                "secretpassword"};

        checkMojo( expectedArguments );
    }

    private void checkMojo( String[] expectedCommandLineArguments )
    {
        assertEquals( 1, mojo.commandLines.size() );
        Commandline commandline = (Commandline) mojo.commandLines.get( 0 );
        String[] arguments = commandline.getArguments();

        assertEquals(
            "Differing number of arguments, required " + Arrays.asList( expectedCommandLineArguments ) + " but had : " +
                Arrays.asList( arguments ), expectedCommandLineArguments.length, arguments.length );
        for ( int i = 0; i < arguments.length; i++ )
        {
            assertEquals( expectedCommandLineArguments[i], arguments[i] );
        }
    }
}
