package org.codehaus.mojo.keytool;

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

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.utils.cli.Commandline;
import org.apache.maven.shared.utils.cli.javatool.JavaToolException;
import org.apache.maven.shared.utils.cli.javatool.JavaToolResult;
import org.apache.maven.toolchain.Toolchain;
import org.apache.maven.toolchain.ToolchainManager;

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
     * <strong>Note: This parameter is left for compatibility reasons but
     * should be used as a last resort when parameters are not found in a
     * dedicated mojo due to possible side-effects on parameters
     * (see https://jira.codehaus.org/browse/MKEYTOOL-17)</strong>
     *
     * @since 1.1
     */
    @Parameter
    private String[] arguments;

    /**
     * Where to execute the keytool command.
     */
    @Parameter( defaultValue = "${basedir}", required = true, alias = "workingdir" )
    private File workingDirectory;

    /**
     * Keytool component.
     *
     * @since 1.2
     */
    @Component( role = KeyTool.class )
    private KeyTool keyTool;

    /**
     * To obtain a toolchain if possible.
     *
     * @since 1.4
     */
    @Component
    private ToolchainManager toolchainManager;

    /**
     * The current build session instance. This is used for
     * toolchain manager API calls.
     *
     * @since 1.4
     */
    @Component
    private MavenSession session;

    /**
     * Type of keytool request used by the mojo.
     */
    private final Class<R> requestType;

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
    public void execute()
        throws MojoExecutionException
    {

        if ( isSkip() )
        {
            getLog().info( getMessage( "disabled" ) );
        }
        else
        {

            // add toolchain support if found
            Toolchain toolchain = getToolchain();

            if ( toolchain != null )
            {
                getLog().info( "Toolchain in keytool-maven-plugin: " + toolchain );
                keyTool.setToolchain(toolchain);
            }

            //creates the keytool request
            R request = createKeytoolRequest();

            try
            {
                JavaToolResult result = keyTool.execute( request );

                consumeResult( result );
            }
            catch ( JavaToolException e )
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
    protected final void consumeResult( JavaToolResult result )
        throws MojoExecutionException
    {
        Commandline commandLine = result.getCommandline();

        int resultCode = result.getExitCode();

        if ( resultCode != 0 )
        {
            throw new MojoExecutionException( getMessage( "failure", getCommandlineInfo( commandLine ), resultCode ) );
        }
    }

    /**
     * FIXME tchemit-20123-11-13, need to find out how to do this...
     * TODO remove the part with ToolchainManager lookup once we depend on
     * 2.0.9 (have it as prerequisite). Define as regular component field then.
     *
     * @return Toolchain instance
     */
    private Toolchain getToolchain()
    {
        Toolchain tc = null;
        if ( toolchainManager != null )
        {
            tc = toolchainManager.getToolchainFromBuildContext( "jdk", session );
        }

        return tc;
    }
}
