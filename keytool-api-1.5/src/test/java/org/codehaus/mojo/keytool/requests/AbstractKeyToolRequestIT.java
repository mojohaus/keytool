package org.codehaus.mojo.keytool.requests;

import junit.framework.Assert;
import org.codehaus.mojo.keytool.KeyTool;
import org.codehaus.mojo.keytool.KeyToolException;
import org.codehaus.mojo.keytool.KeyToolRequest;
import org.codehaus.mojo.keytool.KeyToolResult;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;

/**
 * abstract test of a keytool request.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public abstract class AbstractKeyToolRequestIT
    extends PlexusTestCase
{

    private static final long BUILD_TIMESTAMP = System.nanoTime();

    /**
     * KeyTool to test keyTool requests.
     */
    protected KeyTool tool;

    protected File workingDirectory;

    /**
     * {@inheritDoc}
     */
    public void setUp()
        throws Exception
    {
        super.setUp();

        tool = (KeyTool) lookup( KeyTool.ROLE );
        Assert.assertNotNull( tool );

        String basedir = getBasedir();
        workingDirectory = new File( basedir, "target" + File.separator + "surefire-workdir" + File.separator +
            getClass().getName() + "_" + BUILD_TIMESTAMP );
    }

    /**
     * {@inheritDoc}
     */
    public void tearDown()
        throws Exception
    {
        super.tearDown();
        tool = null;
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

    protected KeyToolResult executeKeyToolRequest( KeyToolRequest request )
        throws KeyToolException
    {
        Assert.assertNotNull( request );
        KeyToolResult result = tool.execute( request );
        Assert.assertNotNull( result );
        return result;
    }

    protected void assertKeyToolResult( KeyToolResult result, String[] expectedCommandLineArguments,
                                        int expectedExitCode )
    {
        assertKeyToolResult( result, expectedCommandLineArguments );

        assertEquals( "Differing exit code , required " + expectedExitCode + " but had : " + result.getExitCode(),
                      expectedExitCode, result.getExitCode() );
    }

    protected void assertKeyToolResult( KeyToolResult result, String[] expectedCommandLineArguments )
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
}
