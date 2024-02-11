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

/**
 * Specifies the common parameters used to control a KeyTool tool invocation.
 *
 * @author tchemit
 * @since 1.1
 */
public interface KeyToolRequestWithKeyStoreParameters extends KeyToolRequest {

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
    void setKeystore(String keystore);

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
    void setStoretype(String storetype);

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
    void setStorepass(String storepass);

    /**
     * Gets the value of the {@code providername} field.
     *
     * @return the value of the {@code providername} field
     */
    String getProvidername();

    /**
     * <p>setProvidername.</p>
     *
     * @param providername value of the field {@code providername} to set
     */
    void setProvidername(String providername);

    /**
     * Gets the value of the {@code providerclass} field.
     *
     * @return the value of the {@code providerclass} field
     */
    String getProviderclass();

    /**
     * <p>setProviderclass.</p>
     *
     * @param providerclass value of the field {@code providerclass} to set
     */
    void setProviderclass(String providerclass);

    /**
     * Gets the value of the {@code providerarg} field.
     *
     * @return the value of the {@code providerarg} field
     */
    String getProviderarg();

    /**
     * <p>setProviderarg.</p>
     *
     * @param providerarg value of the field {@code providerarg} to set
     */
    void setProviderarg(String providerarg);

    /**
     * Gets the value of the {@code providerpath} field.
     *
     * @return the value of the {@code providerpath} field
     */
    String getProviderpath();

    /**
     * <p>setProviderpath.</p>
     *
     * @param providerpath value of the field {@code providerpath} to set
     */
    void setProviderpath(String providerpath);
}
