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

/**
 * Specifies the common parameters used to control a KeyTool tool invocation.
 *
 * @author tchemit <chemit@codelutin.com>
 * @version $Id$
 * @since 1.0
 */
public interface KeyToolRequestWithKeyStoreParameters
    extends KeyToolRequest
{

    /**
     * Gets the value of the {@code keystore} field.
     *
     * @return the value of the {@code keystore} field.
     */
    String getKeystore();

    /**
     * Sets the new given value to the field {@code keystore} of the request.
     *
     * @param keystore the new value of the field {@code keystore}.
     */
    void setKeystore( String keystore );

    /**
     * Gets the value of the {@code storetype} field.
     *
     * @return the value of the {@code storetype} field.
     */
    String getStoretype();

    /**
     * Sets the new given value to the field {@code storetype} of the request.
     *
     * @param storetype the new value of the field {@code storetype}.
     */
    void setStoretype( String storetype );

    /**
     * Gets the value of the {@code storepass} field.
     *
     * @return the value of the {@code storepass} field.
     */
    String getStorepass();

    /**
     * Sets the new given value to the field {@code storepass} of the request.
     *
     * @param storepass the new value of the field {@code storepass}.
     */
    void setStorepass( String storepass );

//    /**
//     * Gets the value of the {@code alias} field.
//     *
//     * @return the value of the {@code alias} field.
//     */
//    String getAlias();
//
//    /**
//     * Sets the new given value to the field {@code alias} of the request.
//     *
//     * @param alias the new value of the field {@code alias}.
//     */
//    void setAlias( String alias );

//    /**
//     * Gets the value of the {@code passwordProtected} field.
//     *
//     * @return the value of the {@code passwordProtected} field
//     */
//    boolean isPasswordProtected();
//
//    /**
//     * @param passwordProtected value of the field {@code passwordProtected} to set
//     */
//    void setPasswordProtected( boolean passwordProtected );

    /**
     * Gets the value of the {@code providername} field.
     *
     * @return the value of the {@code providername} field
     */
    String getProvidername();

    /**
     * @param providername value of the field {@code providername} to set
     */
    void setProvidername( String providername );

    /**
     * Gets the value of the {@code providerclass} field.
     *
     * @return the value of the {@code providerclass} field
     */
    String getProviderclass();

    /**
     * @param providerclass value of the field {@code providerclass} to set
     */
    void setProviderclass( String providerclass );

    /**
     * Gets the value of the {@code providerarg} field.
     *
     * @return the value of the {@code providerarg} field
     */
    String getProviderarg();

    /**
     * @param providerarg value of the field {@code providerarg} to set
     */
    void setProviderarg( String providerarg );

    /**
     * Gets the value of the {@code providerpath} field.
     *
     * @return the value of the {@code providerpath} field
     */
    String getProviderpath();

    /**
     * @param providerpath value of the field {@code providerpath} to set
     */
    void setProviderpath( String providerpath );

}
