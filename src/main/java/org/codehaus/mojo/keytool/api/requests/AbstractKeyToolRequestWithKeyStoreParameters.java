package org.codehaus.mojo.keytool.api.requests;

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

import org.codehaus.mojo.keytool.api.KeyToolRequestWithKeyStoreParameters;

/**
 * Specifies the commons parameters used to control a key tool invocation which have propviders options.
 *
 * @author tchemit
 * @since 1.1
 */
public abstract class AbstractKeyToolRequestWithKeyStoreParameters extends AbstractKeyToolRequest
        implements KeyToolRequestWithKeyStoreParameters {

    /**
     * Provider name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String providername;

    /**
     * Provider class name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String providerclass;

    /**
     * Provider argument.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String providerarg;

    /**
     * Provider classpath.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String providerpath;

    /**
     * Keystore name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String keystore;

    /**
     * Keystore type.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String storetype;

    /**
     * Keystore password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String storepass;

    /**
     * {@inheritDoc}
     *
     * @return a {@link java.lang.String} object
     */
    public final String getProvidername() {
        return providername;
    }

    /** {@inheritDoc} */
    public final void setProvidername(String providername) {
        this.providername = providername;
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@link java.lang.String} object
     */
    public final String getProviderclass() {
        return providerclass;
    }

    /** {@inheritDoc} */
    public final void setProviderclass(String providerclass) {
        this.providerclass = providerclass;
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@link java.lang.String} object
     */
    public final String getProviderarg() {
        return providerarg;
    }

    /** {@inheritDoc} */
    public final void setProviderarg(String providerarg) {
        this.providerarg = providerarg;
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@link java.lang.String} object
     */
    public final String getProviderpath() {
        return providerpath;
    }

    /** {@inheritDoc} */
    public final void setProviderpath(String providerpath) {
        this.providerpath = providerpath;
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@link java.lang.String} object
     */
    public final String getKeystore() {
        return keystore;
    }

    /** {@inheritDoc} */
    public final void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@link java.lang.String} object
     */
    public final String getStoretype() {
        return storetype;
    }

    /** {@inheritDoc} */
    public final void setStoretype(String storetype) {
        this.storetype = storetype;
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@link java.lang.String} object
     */
    public final String getStorepass() {
        return storepass;
    }

    /** {@inheritDoc} */
    public final void setStorepass(String storepass) {
        this.storepass = storepass;
    }
}
