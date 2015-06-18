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

import org.apache.maven.shared.utils.cli.Commandline;
import org.codehaus.plexus.logging.Logger;

/**
 * To build the command line for a given {@link KeyToolRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @version $Id$
 * @since 1.1
 */
public interface KeyToolCommandLineBuilder
{
    /**
     * Plexus role name.
     */
    String ROLE = KeyToolCommandLineBuilder.class.getName();

    /**
     * Test if given request type is supported by the underlined keytool implementation.
     * <p/>
     * <strong>Note:</strong> a request of a none supported type will then thrown a {@link UnsupportedKeyToolRequestException} in method {@link #build(KeyToolRequest)}
     *
     * @param requestType type of request to test
     * @param <R>         type of request to test
     * @return {@code true} if can create a such request type, {@code false} otherwise.
     * @since 1.3
     */
    <R extends KeyToolRequest> boolean supportRequestType( Class<R> requestType );

    /**
     * Build the commandline given the incoming keytool request.
     *
     * @param request keytool request
     * @return the prepared commandline client ready to be executed
     * @throws CommandLineConfigurationException
     *          if could not find keytool executable
     */
    Commandline build( KeyToolRequest request )
        throws CommandLineConfigurationException, UnsupportedKeyToolRequestException;

    /**
     * Sets the logger used by the builder.
     *
     * @param logger logger to use in this builder
     */
    void setLogger( Logger logger );

    /**
     * Sets the keytool executable location.
     *
     * @param keyToolFile keytool executable location to use in this builder
     */
    void setKeyToolFile( String keyToolFile );

    /**
     * Checks that builder is ready to produce commandline from incoming request.
     * <p/>
     * Says a logger is set and a keytool executable location is setted.
     */
    void checkRequiredState();

    /**
     * Checks that builder can build the given type of request.
     *
     * @param request request to test
     * @since 1.3
     */
    void checkSupportedRequest( KeyToolRequest request )
        throws UnsupportedKeyToolRequestException;

}
