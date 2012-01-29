package org.codehaus.mojo.keytool;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;

/**
 * To build the command line for a given {@link org.codehaus.mojo.keytool.KeyToolRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @version $Id: KeyToolCommandLineBuilder.java 15792 2012-01-28 21:06:10Z tchemit $
 * @since 1.0
 */
public abstract class AbstractKeyToolCommandLineBuilder
    implements KeyToolCommandLineBuilder
{

    private Logger logger;

    /**
     * Keytool executable location.
     */
    private String keyToolFile;

    /**
     * Sets the logger used by the builder.
     *
     * @param logger logger to use in this builder
     */
    public final void setLogger( Logger logger )
    {
        this.logger = logger;
    }

    /**
     * Sets the keytool executable location.
     *
     * @param keyToolFile keytool executable location to use in this builder
     */
    public final void setKeyToolFile( String keyToolFile )
    {
        this.keyToolFile = keyToolFile;
    }

    /**
     * Checks that builder is ready to produce commandline from incoming request.
     * <p/>
     * Says a logger is set and a keytool executable location is setted.
     */
    public final void checkRequiredState()
    {
        if ( logger == null )
        {
            throw new IllegalStateException( "A logger instance is required." );
        }

        if ( keyToolFile == null )
        {
            throw new IllegalStateException( "A keyTool file is required." );
        }
    }

    protected final Logger getLogger()
    {
        return logger;
    }

    protected final String getKeyToolFile()
    {
        return keyToolFile;
    }

    protected final void addKeytoolCommandAndDefaultoptions( Commandline cli, String keytoolcommand,
                                                             KeyToolRequest request )
    {
        addArg( cli, keytoolcommand );
        addArgIfTrue( cli, "-v", request.isVerbose() );
        if ( request instanceof KeyToolRequestWithKeyStoreParameters )
        {
            buildWithKeyStoreParameters( (KeyToolRequestWithKeyStoreParameters) request, cli );
        }
        if ( request instanceof KeyToolRequestWithKeyStoreAndAliasParameters )
        {
            buildWithKeyStoreAndAliasParameters( (KeyToolRequestWithKeyStoreAndAliasParameters) request, cli );
        }
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool import request
     * @param cli     the commandline client to prepare
     */
    protected void buildWithKeyStoreParameters( KeyToolRequestWithKeyStoreParameters request, Commandline cli )
    {
        addArgIfNotEmpty( cli, "-keystore", request.getKeystore() );
        addArgIfNotEmpty( cli, "-storepass", request.getStorepass() );
        addArgIfNotEmpty( cli, "-storetype", request.getStoretype() );
        addArgIfNotEmpty( cli, "-providername", request.getProvidername() );
        addArgIfNotEmpty( cli, "-providerclass", request.getProviderclass() );
        addArgIfNotEmpty( cli, "-providerarg", request.getProviderarg() );
        addArgIfNotEmpty( cli, "-providerpath", request.getProviderpath() );
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool import request
     * @param cli     the commandline client to prepare
     */
    protected void buildWithKeyStoreAndAliasParameters( KeyToolRequestWithKeyStoreAndAliasParameters request,
                                                        Commandline cli )
    {
        addArgIfNotEmpty( cli, "-protected", request.isPasswordProtected() ? Boolean.TRUE.toString() : "" );
        addArgIfNotEmpty( cli, "-alias", request.getAlias() );
    }

    /**
     * Convenience method to add an argument to the <code>command line</code> if the the value is not null or empty.
     *
     * @param cli   command line to fill
     * @param key   the argument name.
     * @param value the argument value to be added.
     */
    protected final void addArgIfNotEmpty( Commandline cli, String key, String value )
    {
        if ( !StringUtils.isEmpty( value ) )
        {
            addArg( cli, key );
            addArg( cli, value );
        }
    }

    /**
     * Convenience method to add an argument to the <code>command line</code> if the the value is not null or empty.
     *
     * @param cli   command line to fill
     * @param key   the argument name.
     * @param value the argument value to be added.
     */
    protected final void addArgIfNotEmpty( Commandline cli, String key, File value )
    {
        if ( value != null )
        {
            addArg( cli, key );
            addArg( cli, value );
        }
    }

    /**
     * Convenience method to add an argument to the <code>command line</code> if the the value is true.
     *
     * @param cli   command line to fill
     * @param key   the argument name.
     * @param value the argument value to be test.
     */
    protected final void addArgIfTrue( Commandline cli, String key, boolean value )
    {
        if ( value )
        {
            addArg( cli, key );
        }
    }

    /**
     * Convinience method to add an argument to the {@code command line}.
     *
     * @param cli   command line to fill
     * @param value the argument value to be added
     */
    protected final void addArg( Commandline cli, String value )
    {
        cli.createArg().setValue( value );
    }

    /**
     * Convinience method to add a file argument to the {@code command line}.
     *
     * @param cli   command line to fill
     * @param value the file argument value to be added
     */
    protected final void addArg( Commandline cli, File value )
    {
        cli.createArg().setFile( value );
    }


}
