package org.codehaus.mojo.keytool.requests;

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

import org.codehaus.mojo.keytool.KeyToolRequestWithKeyStoreParameters;

/**
 * Specifies the commons parameters used to control a key tool invocation which have propviders options.
 *
 * @author tchemit <chemit@codelutin.com>
 * @version $Id$
 * @since 1.0
 */
public abstract class AbstractKeyToolRequestWithKeyStoreParameters
    extends AbstractKeyToolRequest
    implements KeyToolRequestWithKeyStoreParameters
{

    /**
     * Provider name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String providername;

    /**
     * Provider class name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String providerclass;

    /**
     * Provider argument.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String providerarg;

    /**
     * Provider classpath.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String providerpath;

    /**
     * Keystore name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String keystore;

    /**
     * Keystore type.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String storetype;

    /**
     * Keystore password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String storepass;

    /**
     * {@inheritDoc}
     */
    public final String getProvidername()
    {
        return providername;
    }

    /**
     * {@inheritDoc}
     */
    public final void setProvidername( String providername )
    {
        this.providername = providername;
    }

    /**
     * {@inheritDoc}
     */
    public final String getProviderclass()
    {
        return providerclass;
    }

    /**
     * {@inheritDoc}
     */
    public final void setProviderclass( String providerclass )
    {
        this.providerclass = providerclass;
    }

    /**
     * {@inheritDoc}
     */
    public final String getProviderarg()
    {
        return providerarg;
    }

    /**
     * {@inheritDoc}
     */
    public final void setProviderarg( String providerarg )
    {
        this.providerarg = providerarg;
    }

    /**
     * {@inheritDoc}
     */
    public final String getProviderpath()
    {
        return providerpath;
    }

    /**
     * {@inheritDoc}
     */
    public final void setProviderpath( String providerpath )
    {
        this.providerpath = providerpath;
    }

    /**
     * {@inheritDoc}
     */
    public final String getKeystore()
    {
        return keystore;
    }

    /**
     * {@inheritDoc}
     */
    public final void setKeystore( String keystore )
    {
        this.keystore = keystore;
    }

    /**
     * {@inheritDoc}
     */
    public final String getStoretype()
    {
        return storetype;
    }

    /**
     * {@inheritDoc}
     */
    public final void setStoretype( String storetype )
    {
        this.storetype = storetype;
    }

    /**
     * {@inheritDoc}
     */
    public final String getStorepass()
    {
        return storepass;
    }

    /**
     * {@inheritDoc}
     */
    public final void setStorepass( String storepass )
    {
        this.storepass = storepass;
    }

}
