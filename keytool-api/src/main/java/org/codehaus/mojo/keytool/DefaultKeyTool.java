package org.codehaus.mojo.keytool;

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

import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Default implementation of component {@link KeyTool}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @version $Id$
 * @plexus.component role="org.codehaus.mojo.keytool.KeyTool" role-hint="default"
 * @since 1.1
 */
public class DefaultKeyTool
    extends AbstractLogEnabled
    implements KeyTool
{
    /**
     * The location of the keyTool executable file.
     */
    protected String keyToolFile;

    /**
     * Command line builder.
     *
     * @plexus.requirement
     */
    protected KeyToolCommandLineBuilder builder;

    /**
     * {@inheritDoc}
     */
    public KeyToolResult execute( KeyToolRequest request )
        throws KeyToolException
    {

        if ( keyToolFile == null )
        {

            // find the jar singer to use
            try
            {
                keyToolFile = findKeyToolExecutable();
            }
            catch ( IOException e )
            {
                throw new KeyToolException( "Error finding key tool executable. Reason: " + e.getMessage(), e );
            }
        }

        // creates the command line
        Commandline cli = createCommandLine( request );

        // execute it
        return executeCommandLine( cli, request );
    }

    /**
     * Creates the commandline given the request.
     *
     * @param request keytool request
     * @return the prepared command line object
     * @throws KeyToolException if could not find the keytool command
     */
    protected Commandline createCommandLine( KeyToolRequest request )
        throws KeyToolException
    {
        builder.setLogger( getLogger() );
        builder.setKeyToolFile( keyToolFile );
        Commandline cli;
        try
        {
            cli = builder.build( request );
        }
        catch ( CommandLineConfigurationException e )
        {
            throw new KeyToolException( "Error configuring command-line. Reason: " + e.getMessage(), e );
        }
        return cli;
    }

    /**
     * Launch execution of the given commandline
     *
     * @param cli     the commandline client ready to be executed
     * @param request the incoming keytool request
     * @return result of the execution of the commandline
     */
    protected KeyToolResult executeCommandLine( Commandline cli, KeyToolRequest request )
    {
        if ( getLogger().isDebugEnabled() )
        {
            getLogger().debug( "Executing: " + cli );
        }

        final boolean verbose = request.isVerbose();

        InputStream systemIn = new InputStream()
        {

            public int read()
            {
                return -1;
            }

        };
        StreamConsumer systemOut = request.getSystemOutStreamConsumer();

        if ( systemOut == null )
        {
            systemOut = new StreamConsumer()
            {

                public void consumeLine( final String line )
                {
                    if ( verbose )
                    {
                        getLogger().info( line );
                    }
                    else
                    {
                        getLogger().debug( line );
                    }
                }

            };
        }

        StreamConsumer systemErr = request.getSystemErrorStreamConsumer();

        if ( systemErr == null )
        {
            systemErr = new StreamConsumer()
            {

                public void consumeLine( final String line )
                {
                    getLogger().warn( line );
                }

            };
        }

        DefaultKeyToolResult result = new DefaultKeyToolResult();
        result.setCommandline( cli );

        try
        {
            int resultCode = CommandLineUtils.executeCommandLine( cli, systemIn, systemOut, systemErr );

            result.setExitCode( resultCode );
        }
        catch ( CommandLineException e )
        {
            result.setExecutionException( e );
        }

        return result;
    }

    /**
     * Finds the keeytool executable location.
     *
     * @return location of the keyttol executable
     * @throws java.io.IOException if could  not find the executable
     */
    protected String findKeyToolExecutable()
        throws IOException
    {
        String command = "keytool" + ( Os.isFamily( Os.FAMILY_WINDOWS ) ? ".exe" : "" );

        String executable =
            findExecutable( command, System.getProperty( "java.home" ), new String[]{ "../bin", "bin", "../sh" } );

        if ( executable == null )
        {
            try
            {
                Properties env = CommandLineUtils.getSystemEnvVars();

                String[] variables = { "JDK_HOME", "JAVA_HOME" };

                for ( int i = 0; i < variables.length && executable == null; i++ )
                {
                    executable =
                        findExecutable( command, env.getProperty( variables[i] ), new String[]{ "bin", "sh" } );
                }
            }
            catch ( IOException e )
            {
                if ( getLogger().isDebugEnabled() )
                {
                    getLogger().warn( "Failed to retrieve environment variables, cannot search for " + command, e );
                }
                else
                {
                    getLogger().warn( "Failed to retrieve environment variables, cannot search for " + command );
                }
            }
        }

        if ( executable == null )
        {
            executable = command;
        }

        return executable;
    }

    /**
     * Finds the specified command in any of the given sub directories of the specified JDK/JRE home directory.
     *
     * @param command The command to find, must not be <code>null</code>.
     * @param homeDir The home directory to search in, may be <code>null</code>.
     * @param subDirs The sub directories of the home directory to search in, must not be <code>null</code>.
     * @return The (absolute) path to the command if found, <code>null</code> otherwise.
     */
    protected String findExecutable( String command, String homeDir, String[] subDirs )
    {
        if ( StringUtils.isNotEmpty( homeDir ) )
        {
            for ( int i = 0; i < subDirs.length; i++ )
            {
                File file = new File( new File( homeDir, subDirs[i] ), command );

                if ( file.isFile() )
                {
                    return file.getAbsolutePath();
                }
            }
        }

        return null;
    }
}
