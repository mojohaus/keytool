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

import org.apache.maven.shared.utils.cli.Commandline;
import org.apache.maven.shared.utils.cli.javatool.JavaToolException;
import org.apache.maven.shared.utils.cli.javatool.JavaToolResult;
import org.codehaus.mojo.keytool.KeyTool;
import org.codehaus.mojo.keytool.KeyToolRequest;
import org.codehaus.mojo.keytool.UnsupportedKeyToolRequestException;
import org.codehaus.plexus.PlexusTestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.File;
import java.util.Arrays;

/**
 * abstract test of a keytool request.
 *
 * @author tchemit
 * @since 1.1
 */
public abstract class AbstractKeyToolRequestIT<R extends KeyToolRequest>
    extends PlexusTestCase
{

    private static final long BUILD_TIMESTAMP = System.nanoTime();

    /**
     * KeyTool to test keyTool requests.
     */
    protected KeyTool tool;

    protected File workingDirectory;

    protected KeyToolRequestFixtures requestFixtures;

    protected ResourceFixtures resourceFixtures;

    private final boolean supportedRequest;

    /**
     * <p>Constructor for AbstractKeyToolRequestIT.</p>
     */
    protected AbstractKeyToolRequestIT()
    {
        this( true );
    }

    /**
     * <p>Constructor for AbstractKeyToolRequestIT.</p>
     *
     * @param supportedRequest a boolean
     */
    protected AbstractKeyToolRequestIT( boolean supportedRequest )
    {
        this.supportedRequest = supportedRequest;
    }

    /**
     * <p>testRequest.</p>
     *
     * @throws java.lang.Exception if any.
     */
    public abstract void testRequest()
        throws Exception;

    /**
     * <p>consumeRequest.</p>
     *
     * @param request a R object
     * @return a {@link org.apache.maven.shared.utils.cli.javatool.JavaToolResult} object
     * @throws org.apache.maven.shared.utils.cli.javatool.JavaToolException if any.
     */
    protected final JavaToolResult consumeRequest( R request )
        throws JavaToolException
    {

        JavaToolResult result = null;

        if ( supportedRequest )
        {
            result = executeKeyToolRequest( request );
        }
        else
        {
            executeUnsupportedKeyToolRequest( request );
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @throws java.lang.Exception if any.
     */
    @Before
    public void setUp()
        throws Exception
    {
        super.setUp();

        String basedir = getBasedir();
        workingDirectory = new File( basedir, "target" + File.separator + "surefire-workdir" + File.separator +
            getClass().getName() + "_" + BUILD_TIMESTAMP );

        tool = (KeyTool) lookup( KeyTool.ROLE );

        Assert.assertNotNull( tool );

        requestFixtures = new KeyToolRequestFixtures();
        resourceFixtures = new ResourceFixtures( workingDirectory );
    }

    /**
     * {@inheritDoc}
     *
     * @throws java.lang.Exception if any.
     */
    @After
    public void tearDown()
        throws Exception
    {
        super.tearDown();
        tool = null;
        requestFixtures = null;
        resourceFixtures = null;
    }

    /**
     * <p>executeKeyToolRequest.</p>
     *
     * @param request a {@link org.codehaus.mojo.keytool.KeyToolRequest} object
     * @return a {@link org.apache.maven.shared.utils.cli.javatool.JavaToolResult} object
     * @throws org.apache.maven.shared.utils.cli.javatool.JavaToolException if any.
     */
    protected JavaToolResult executeKeyToolRequest( KeyToolRequest request )
        throws JavaToolException
    {
        Assert.assertNotNull( request );
        JavaToolResult result = tool.execute( request );
        System.out.println( result.getCommandline().toString() );
        Assert.assertNotNull( result );
        return result;
    }

    /**
     * <p>assertKeyToolResult.</p>
     *
     * @param result a {@link org.apache.maven.shared.utils.cli.javatool.JavaToolResult} object
     * @param expectedCommandLineArguments an array of {@link java.lang.String} objects
     * @param expectedExitCode a int
     */
    protected void assertKeyToolResult( JavaToolResult result, String[] expectedCommandLineArguments,
                                        int expectedExitCode )
    {
        assertKeyToolResult( result, expectedCommandLineArguments );

        assertEquals( "Differing exit code , required " + expectedExitCode + " but had : " + result.getExitCode(),
                      expectedExitCode, result.getExitCode() );
    }

    /**
     * <p>assertKeyToolResult.</p>
     *
     * @param result a {@link org.apache.maven.shared.utils.cli.javatool.JavaToolResult} object
     * @param expectedCommandLineArguments an array of {@link java.lang.String} objects
     */
    protected void assertKeyToolResult( JavaToolResult result, String[] expectedCommandLineArguments )
    {

        Commandline commandline = result.getCommandline();
        String[] arguments = commandline.getArguments();

        assertEquals(
            "Differing number of arguments, required " + Arrays.asList( expectedCommandLineArguments ) + " but had : " +
                Arrays.asList( arguments ), expectedCommandLineArguments.length, arguments.length );
        for ( int i = 0; i < arguments.length; i++ )
        {
            assertEquals( expectedCommandLineArguments[i], arguments[i] );
        }
    }

    /**
     * <p>executeUnsupportedKeyToolRequest.</p>
     *
     * @param request a {@link org.codehaus.mojo.keytool.KeyToolRequest} object
     * @throws org.apache.maven.shared.utils.cli.javatool.JavaToolException if any.
     */
    protected void executeUnsupportedKeyToolRequest( KeyToolRequest request )
        throws JavaToolException
    {
        Assert.assertNotNull( request );
        try
        {
            tool.execute( request );
            Assert.fail( "Request of type " + request.getClass().getName() + " is not supported." );
        }
        catch ( UnsupportedKeyToolRequestException e )
        {
            Assert.assertTrue( true );
        }
    }
}
