package org.codehaus.mojo.keytool;

import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;

/**
 * Abstract mojo to execute a {@link KeyToolRequestWithKeyStoreParameters} request.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.2
 */
public abstract class AbstractKeyToolRequestWithKeyStoreParametersMojo<R extends KeyToolRequestWithKeyStoreParameters>
    extends AbstractKeyToolRequestMojo<R>
{

    /**
     * Keystore location.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${keystore}"
     */
    private String keystore;

    /**
     * Keystore type.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${storetype}"
     */
    private String storetype;

    /**
     * Keystore password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${storepass}" alias="storepass"
     */
    private String storepass;

    /**
     * Provider name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${providername}"
     * @since 1.2
     */
    private String providername;

    /**
     * Provider class name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${providerclass}"
     * @since 1.2
     */
    private String providerclass;

    /**
     * Provider argument.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${providerarg}"
     * @since 1.2
     */
    private String providerarg;

    /**
     * Provider classpath.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${providerpath}"
     * @since 1.2
     */
    private String providerpath;

    public AbstractKeyToolRequestWithKeyStoreParametersMojo( Class<R> requestType )
    {
        super( requestType );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected R prepareRequest()
    {
        R request = super.prepareRequest();

        String keystoreFile = keystore;

        if ( StringUtils.isNotEmpty( keystore ) )
        {
            // make sure the parent directory of the key store exists
            createParentDirIfNecessary( keystore );

            // force to not use this parameter
            request.setKeystore( keystore );
        }

        request.setProviderarg( providerarg );
        request.setProviderclass( providerclass );
        request.setProvidername( providername );
        request.setProviderpath( providerpath );
        request.setStorepass( storepass );
        request.setStoretype( storetype );
        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCommandlineInfo( final Commandline commandLine )
    {
        String commandLineInfo = super.getCommandlineInfo( commandLine );

        commandLineInfo = StringUtils.replace( commandLineInfo, this.storepass, "'*****'" );

        return commandLineInfo;
    }

    /**
     * Create the parent directory of the given file location.
     *
     * @param file file location to check
     */
    protected final void createParentDirIfNecessary( final String file )
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
