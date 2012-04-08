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

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;

/**
 * Abstract keytool mojo implementing the {@link KeyToolRequest}.
 *
 * @param <R> generic type of request used by the mojo
 * @author tchemit <chemit@codelutin.com>
 * @since 1.2
 */
public abstract class AbstractKeyToolRequestMojo<R extends KeyToolRequest>
    extends AbstractKeyToolMojo
{

    /**
     * List of additional arguments to append to the keytool command line.
     * <p/>
     * <strong>Note: This parameter is left for compatibility reason but
     * should be used at a last resort whenparameters are not found in
     * dedicated mojo due to possible side-effects on parameters
     * (see https://jira.codehaus.org/browse/MKEYTOOL-17)</strong>
     *
     * @parameter expression="${keytool.arguments}"
     * @since 1.1
     */
    private String[] arguments;

    /**
     * Where to execute the keytool command.
     *
     * @parameter expression="${keytool.workingdir}" default-value="${basedir}" alias="workingdir"
     * @required
     */
    private File workingDirectory;

    /**
     * Type of keytool request used by the mojo.
     */
    private final Class<R> requestType;

    /**
     * Keytool component.
     *
     * @component role="org.codehaus.mojo.keytool.KeyTool"
     * @since 1.2
     */
    private KeyTool keyTool;

    /**
     * Constructor of abstract mojo.
     *
     * @param requestType type of keytool request used by the mojo
     */
    protected AbstractKeyToolRequestMojo( Class<R> requestType )
    {
        this.requestType = requestType;
    }

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

            //creates the keytool request
            R request = createKeytoolRequest();

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
     * To prepare the incoming request, says fill it with mojo parameters.
     *
     * @return the created keytool request
     * @see KeyToolRequest
     */
    protected R createKeytoolRequest()
    {
        R request;
        try
        {
            request = requestType.getConstructor().newInstance();
        }
        catch ( Exception e )
        {
            // just throw a runtime exception : this should never happen!
            throw new RuntimeException( "Failed to create keytool request ", e );
        }

        // add default parameters
        request.setVerbose( isVerbose() );
        request.setWorkingDirectory( this.workingDirectory );
        request.setArguments( this.arguments );
        return request;
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
        return commandLine.toString();
    }

    /**
     * To consume the keytool comand execution.
     *
     * @param result result of the command line action
     * @throws MojoExecutionException if the result is not 0 (means something bad occurs)
     */
    protected final void consumeResult( KeyToolResult result )
        throws MojoExecutionException
    {
        Commandline commandLine = result.getCommandline();

        int resultCode = result.getExitCode();

        if ( resultCode != 0 )
        {
            throw new MojoExecutionException( getMessage( "failure", getCommandlineInfo( commandLine ), resultCode ) );
        }
    }

}
