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
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;

/**
 * @author <a>Juergen Mayrbaeurl</a>
 * @version 1.0 2008-02-03
 */
public abstract class AbstractCmdLineKeyToolMojo
    extends AbstractKeyToolMojo
{

    /**
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${storetype}"
     */
    private String storetype;

    /**
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${storepass}"
     */
    private String storepass;

    /**
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${alias}"
     */
    private String alias;

    /**
     * List of additional arguments to append to the jarsigner command line.
     *
     * @parameter expression="${jarsigner.arguments}"
     */
    private String[] arguments;

    /**
     * Enable verbose.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${verbose}" default-value="false"
     */
    private boolean verbose;

    /**
     * @component role="org.codehaus.mojo.keytool.KeyTool"
     */
    private KeyTool keyTool;

    /**
     * Creates the key tool request to execute.
     *
     * @return the request
     * @since 1.1
     */
    protected abstract KeyToolRequestWithKeyStoreAndAliasParameters createRequest();

    /**
     * {@inheritDoc}
     */
    public final void execute()
        throws MojoExecutionException
    {

        if ( isSkip() )
        {
            getLog().info( getMessage( "disabled" ) );
        }
        else
        {
            String keystoreFile = getKeystore();

            createParentDirIfNecessary( keystoreFile );

            KeyToolRequestWithKeyStoreAndAliasParameters request = createRequest();

            request.setWorkingDirectory( getWorkingDir() );

            if ( StringUtils.isNotEmpty( keystoreFile ) )
            {
                request.setKeystore( keystoreFile );
            }
            request.setStoretype( storetype );
            request.setStorepass( storepass );
            request.setAlias( alias );
            request.setArguments( arguments );
            request.setVerbose( verbose );

            try
            {
                KeyToolResult result = keyTool.execute( request );

                consumeResult( result );

            }
            catch ( KeyToolException e )
            {
                throw new MojoExecutionException( getMessage( "commandLineException", e.getMessage() ), e );
            }

        }
    }

    /**
     * Gets a string representation of a {@code Commandline}.
     * <p>This method creates the string representation by calling {@code commandLine.toString()} by default.</p>
     *
     * @param commandLine The {@code Commandline} to get a string representation of (can not be null).
     * @return The string representation of {@code commandLine}.
     */
    protected String getCommandlineInfo( final Commandline commandLine )
    {
        if ( commandLine == null )
        {
            throw new NullPointerException( "commandLine" );
        }
        String commandLineInfo = commandLine.toString();

        commandLineInfo = StringUtils.replace( commandLineInfo, this.storepass, "'*****'" );

        return commandLineInfo;
    }

    /**
     * To consume the keytool comand execution.
     *
     * @param result result of the command line action
     * @throws MojoExecutionException if the result is not 0 (means something bad occurs)
     */
    protected void consumeResult( KeyToolResult result )
        throws MojoExecutionException
    {
        Commandline commandLine = result.getCommandline();

        int resultCode = result.getExitCode();

        if ( resultCode != 0 )
        {
            throw new MojoExecutionException(
                getMessage( "failure", getCommandlineInfo( commandLine ), new Integer( resultCode ) ) );
        }
    }

    /**
     * @param storetype the storetype to set
     */
    public void setStoretype( String storetype )
    {
        this.storetype = storetype;
    }

    /**
     * @param storepass the storepass to set
     */
    public void setStorepass( String storepass )
    {
        this.storepass = storepass;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias( String alias )
    {
        this.alias = alias;
    }

    /**
     * @param verbose the verbose to set
     */
    public void setVerbose( boolean verbose )
    {
        this.verbose = verbose;
    }

    /**
     * @param arguments the arguments to set
     */
    public void setArguments( String[] arguments )
    {
        this.arguments = arguments;
    }

    /**
     * @param keyTool keyTool to set
     */
    protected void setKeyTool( KeyTool keyTool )
    {
        this.keyTool = keyTool;
    }

    /**
     * Create the parent directory of the given file location.
     *
     * @param file file location to check
     */
    protected void createParentDirIfNecessary( final String file )
    {
        if ( file != null )
        {
            final File fileDir = new File( file ).getParentFile();

            if ( fileDir != null )
            {
                // not a relative path
                boolean mkdirs = fileDir.mkdirs();
                getLog().debug( "mdkirs: " + mkdirs + " " + fileDir );
            }
        }
    }
}
