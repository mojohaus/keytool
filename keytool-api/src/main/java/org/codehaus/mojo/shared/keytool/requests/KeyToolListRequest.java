package org.codehaus.mojo.shared.keytool.requests;

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
 * Request to list entries in a keystore using the KeyTool tool.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.0
 */
public class KeyToolListRequest
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParameters
{

    /**
     * Output in RFC style.
     * <p/>
     * See <a href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private boolean rfc;

    public KeyToolListRequest()
    {
        super( "-list" );
    }

    /**
     * Gets the value of the {@link #rfc} field.
     *
     * @return the value of the {@link #rfc} field
     */
    public boolean isRfc()
    {
        return rfc;
    }

    /**
     * @param rfc value of the field {@link #rfc} to set
     */
    public void setRfc( boolean rfc )
    {
        this.rfc = rfc;
    }

}
