package org.codehaus.mojo.shared.keytool;

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

import org.codehaus.plexus.util.cli.StreamConsumer;

import java.io.File;


/**
 * Specifies the common parameters used to control a KeyTool tool invocation.
 *
 * @author tchemit <chemit@codelutin.com>
 * @version $Id$
 * @since 1.0
 */
public interface KeyToolRequest
{

    /**
     * Gets the keytool command used by the tool.
     * <p/>
     * This is the very first argument passed to the command line tool.
     *
     * @return the keytool command used by the tool.
     */

    String getKeytoolCommand();

    /**
     * Gets the value of the {@code verbose} field.
     *
     * @return the value of the {@code verbose} field.
     */

    boolean isVerbose();

    /**
     * Gets the value of the {@code maxMemory} field.
     *
     * @return the value of the {@code maxMemory} field.
     */
    String[] getArguments();

//    /**
//     * Gets the value of the {@code keystore} field.
//     *
//     * @return the value of the {@code keystore} field.
//     */
//    String getKeystore();
//
//    /**
//     * Gets the value of the {@code storetype} field.
//     *
//     * @return the value of the {@code storetype} field.
//     */
//    String getStoretype();
//
//    /**
//     * Gets the value of the {@code storepass} field.
//     *
//     * @return the value of the {@code storepass} field.
//     */
//    String getStorepass();
//
//    /**
//     * Gets the value of the {@code alias} field.
//     *
//     * @return the value of the {@code alias} field.
//     */
//    String getAlias();

//    /**
//     * Gets the value of the {@code keypass} field.
//     *
//     * @return the value of the {@code keypass} field.
//     */
//    String getKeypass();

    /**
     * Gets the value of the {@code workingDirectory} field.
     *
     * @return the value of the {@code workingDirectory} field.
     */
    File getWorkingDirectory();

    /**
     * Gets the value of the {@code systemOutStreamConsumer} field.
     * <p/>
     * This option field if filled is used by the commandline tool to consume system ouput stream of the jarsigner
     * command.
     *
     * @return the value of the {@code systemOutStreamConsumer} field.
     */
    StreamConsumer getSystemOutStreamConsumer();

    /**
     * Gets the value of the {@code systemErrorStreamConsumer} field.
     * <p/>
     * This option field if filled is used by the commandline tool to consume system error stream of the jarsigner
     * command.
     *
     * @return the value of the {@code systemErrorStreamConsumer} field.
     */
    StreamConsumer getSystemErrorStreamConsumer();

    /**
     * Sets the new given value to the field {@code verbose} of the request.
     *
     * @param verbose the new value of the field {@code verbose}.
     */
    void setVerbose( boolean verbose );

    /**
     * Sets the new given value to the field {@code arguments} of the request.
     *
     * @param arguments the new value of the field {@code arguments}.
     */
    void setArguments( String[] arguments );

//    /**
//     * Sets the new given value to the field {@code keystore} of the request.
//     *
//     * @param keystore the new value of the field {@code keystore}.
//     */
//    void setKeystore( String keystore );
//
//    /**
//     * Sets the new given value to the field {@code storetype} of the request.
//     *
//     * @param storetype the new value of the field {@code storetype}.
//     */
//    void setStoretype( String storetype );
//
//    /**
//     * Sets the new given value to the field {@code storepass} of the request.
//     *
//     * @param storepass the new value of the field {@code storepass}.
//     */
//    void setStorepass( String storepass );
//
//    /**
//     * Sets the new given value to the field {@code alias} of the request.
//     *
//     * @param alias the new value of the field {@code alias}.
//     */
//    void setAlias( String alias );

//    /**
//     * Sets the new given value to the field {@code keypass} of the request.
//     *
//     * @param keypass the new value of the field {@code keypass}.
//     */
//    void setKeypass( String keypass );

    /**
     * Sets the new given value to the field {@code workingDirectory} of the request.
     *
     * @param workingDirectory the new value of the field {@code workingDirectory}.
     */
    void setWorkingDirectory( File workingDirectory );

    /**
     * Sets the new given value to the field {@code systemOutStreamConsumer} of the request.
     *
     * @param systemOutStreamConsumer the new value of the field {@code systemOutStreamConsumer}.
     */
    void setSystemOutStreamConsumer( StreamConsumer systemOutStreamConsumer );

    /**
     * Sets the new given value to the field {@code systemErrorStreamConsumer} of the request.
     *
     * @param systemErrorStreamConsumer the new value of the field {@code systemErrorStreamConsumer}.
     */
    void setSystemErrorStreamConsumer( StreamConsumer systemErrorStreamConsumer );
}
