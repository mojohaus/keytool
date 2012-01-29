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

/**
 * Specifies the common parameters used to control a KeyTool tool invocation.
 *
 * @author tchemit <chemit@codelutin.com>
 * @version $Id$
 * @since 1.0
 */
public interface KeyToolRequestWithKeyStoreAndAliasParameters
    extends KeyToolRequestWithKeyStoreParameters
{

    /**
     * Gets the value of the {@code alias} field.
     *
     * @return the value of the {@code alias} field.
     */
    String getAlias();

    /**
     * Sets the new given value to the field {@code alias} of the request.
     *
     * @param alias the new value of the field {@code alias}.
     */
    void setAlias( String alias );

    /**
     * Gets the value of the {@code passwordProtected} field.
     *
     * @return the value of the {@code passwordProtected} field
     */
    boolean isPasswordProtected();

    /**
     * @param passwordProtected value of the field {@code passwordProtected} to set
     */
    void setPasswordProtected( boolean passwordProtected );

}
