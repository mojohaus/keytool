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
import org.codehaus.plexus.util.cli.Commandline;

/**
 * To build the command line for a given {@link KeyToolRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @version $Id$
 * @since 1.0
 */
public interface KeyToolCommandLineBuilder
{
    /**
     * Plexus role name.
     */
    String ROLE = KeyToolCommandLineBuilder.class.getName();

    /**
     * Build the commandline given the incoming keytool request.
     *
     * @param request keytool request
     * @return the prepared commandline client ready to be executed
     * @throws CommandLineConfigurationException
     *          if could not find keytool executable
     */
    Commandline build( KeyToolRequest request )
        throws CommandLineConfigurationException;

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

}
