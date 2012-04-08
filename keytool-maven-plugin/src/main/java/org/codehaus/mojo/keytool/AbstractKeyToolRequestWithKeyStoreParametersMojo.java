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

import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;

/**
 * Abstract mojo to execute a {@link KeyToolRequestWithKeyStoreParameters} request.
 *
 * @param <R> generic type of request used by the mojo
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

    /**
     * Constructor of abstract mojo.
     *
     * @param requestType type of keytool request used by the mojo
     */
    public AbstractKeyToolRequestWithKeyStoreParametersMojo( Class<R> requestType )
    {
        super( requestType );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected R createKeytoolRequest()
    {
        R request = super.createKeytoolRequest();

        String keystoreFile = keystore;

        if ( StringUtils.isNotEmpty( keystore ) )
        {
            // make sure the parent directory of the keystore exists
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
