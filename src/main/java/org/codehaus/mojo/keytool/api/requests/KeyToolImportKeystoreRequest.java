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

/**
 * Request to import all entries of a keystore to another keystore using the KeyTool tool.
 *
 * @author tchemit
 * @since 1.1
 */
public class KeyToolImportKeystoreRequest extends AbstractKeyToolRequest {

    /**
     * Source keystore name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String srckeystore;

    /**
     * Destination keystore name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String destkeystore;

    /**
     * Source keystore type.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String srcstoretype;

    /**
     * Destination keystore type.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String deststoretype;

    /**
     * Source keystore password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String srcstorepass;

    /**
     * Destination keystore password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String deststorepass;

    /**
     * Source keystore password protected.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private boolean srcprotected;

    /**
     * Source keystore provider name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String srcprovidername;

    /**
     * Destination keystore provider name.
     * See <a hresf="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String destprovidername;

    /**
     * Source alias.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String srcalias;

    /**
     * Destination alias.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String destalias;

    /**
     * Source key password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String srckeypass;

    /**
     * Destination key password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String destkeypass;

    /**
     * Do not prompt.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private boolean noprompt;

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
     * Gets the value of the {@link #srckeystore} field.
     *
     * @return the value of the {@link #srckeystore} field
     */
    public String getSrckeystore() {
        return srckeystore;
    }

    /**
     * <p>Setter for the field <code>srckeystore</code>.</p>
     *
     * @param srckeystore value of the field {@link #srckeystore} to set
     */
    public void setSrckeystore(String srckeystore) {
        this.srckeystore = srckeystore;
    }

    /**
     * Gets the value of the {@link #destkeystore} field.
     *
     * @return the value of the {@link #destkeystore} field
     */
    public String getDestkeystore() {
        return destkeystore;
    }

    /**
     * <p>Setter for the field <code>destkeystore</code>.</p>
     *
     * @param destkeystore value of the field {@link #destkeystore} to set
     */
    public void setDestkeystore(String destkeystore) {
        this.destkeystore = destkeystore;
    }

    /**
     * Gets the value of the {@link #srcstoretype} field.
     *
     * @return the value of the {@link #srcstoretype} field
     */
    public String getSrcstoretype() {
        return srcstoretype;
    }

    /**
     * <p>Setter for the field <code>srcstoretype</code>.</p>
     *
     * @param srcstoretype value of the field {@link #srcstoretype} to set
     */
    public void setSrcstoretype(String srcstoretype) {
        this.srcstoretype = srcstoretype;
    }

    /**
     * Gets the value of the {@link #deststoretype} field.
     *
     * @return the value of the {@link #deststoretype} field
     */
    public String getDeststoretype() {
        return deststoretype;
    }

    /**
     * <p>Setter for the field <code>deststoretype</code>.</p>
     *
     * @param deststoretype value of the field {@link #deststoretype} to set
     */
    public void setDeststoretype(String deststoretype) {
        this.deststoretype = deststoretype;
    }

    /**
     * Gets the value of the {@link #srcstorepass} field.
     *
     * @return the value of the {@link #srcstorepass} field
     */
    public String getSrcstorepass() {
        return srcstorepass;
    }

    /**
     * <p>Setter for the field <code>srcstorepass</code>.</p>
     *
     * @param srcstorepass value of the field {@link #srcstorepass} to set
     */
    public void setSrcstorepass(String srcstorepass) {
        this.srcstorepass = srcstorepass;
    }

    /**
     * Gets the value of the {@link #deststorepass} field.
     *
     * @return the value of the {@link #deststorepass} field
     */
    public String getDeststorepass() {
        return deststorepass;
    }

    /**
     * <p>Setter for the field <code>deststorepass</code>.</p>
     *
     * @param deststorepass value of the field {@link #deststorepass} to set
     */
    public void setDeststorepass(String deststorepass) {
        this.deststorepass = deststorepass;
    }

    /**
     * Gets the value of the {@link #srcprotected} field.
     *
     * @return the value of the {@link #srcprotected} field
     */
    public boolean isSrcprotected() {
        return srcprotected;
    }

    /**
     * <p>Setter for the field <code>srcprotected</code>.</p>
     *
     * @param srcprotected value of the field {@link #srcprotected} to set
     */
    public void setSrcprotected(boolean srcprotected) {
        this.srcprotected = srcprotected;
    }

    /**
     * Gets the value of the {@link #srcprovidername} field.
     *
     * @return the value of the {@link #srcprovidername} field
     */
    public String getSrcprovidername() {
        return srcprovidername;
    }

    /**
     * <p>Setter for the field <code>srcprovidername</code>.</p>
     *
     * @param srcprovidername value of the field {@link #srcprovidername} to set
     */
    public void setSrcprovidername(String srcprovidername) {
        this.srcprovidername = srcprovidername;
    }

    /**
     * Gets the value of the {@link #destprovidername} field.
     *
     * @return the value of the {@link #destprovidername} field
     */
    public String getDestprovidername() {
        return destprovidername;
    }

    /**
     * <p>Setter for the field <code>destprovidername</code>.</p>
     *
     * @param destprovidername value of the field {@link #destprovidername} to set
     */
    public void setDestprovidername(String destprovidername) {
        this.destprovidername = destprovidername;
    }

    /**
     * Gets the value of the {@link #srcalias} field.
     *
     * @return the value of the {@link #srcalias} field
     */
    public String getSrcalias() {
        return srcalias;
    }

    /**
     * <p>Setter for the field <code>srcalias</code>.</p>
     *
     * @param srcalias value of the field {@link #srcalias} to set
     */
    public void setSrcalias(String srcalias) {
        this.srcalias = srcalias;
    }

    /**
     * Gets the value of the {@link #destalias} field.
     *
     * @return the value of the {@link #destalias} field
     */
    public String getDestalias() {
        return destalias;
    }

    /**
     * <p>Setter for the field <code>destalias</code>.</p>
     *
     * @param destalias value of the field {@link #destalias} to set
     */
    public void setDestalias(String destalias) {
        this.destalias = destalias;
    }

    /**
     * Gets the value of the {@link #srckeypass} field.
     *
     * @return the value of the {@link #srckeypass} field
     */
    public String getSrckeypass() {
        return srckeypass;
    }

    /**
     * <p>Setter for the field <code>srckeypass</code>.</p>
     *
     * @param srckeypass value of the field {@link #srckeypass} to set
     */
    public void setSrckeypass(String srckeypass) {
        this.srckeypass = srckeypass;
    }

    /**
     * Gets the value of the {@link #destkeypass} field.
     *
     * @return the value of the {@link #destkeypass} field
     */
    public String getDestkeypass() {
        return destkeypass;
    }

    /**
     * <p>Setter for the field <code>destkeypass</code>.</p>
     *
     * @param destkeypass value of the field {@link #destkeypass} to set
     */
    public void setDestkeypass(String destkeypass) {
        this.destkeypass = destkeypass;
    }

    /**
     * Gets the value of the {@link #noprompt} field.
     *
     * @return the value of the {@link #noprompt} field
     */
    public boolean isNoprompt() {
        return noprompt;
    }

    /**
     * <p>Setter for the field <code>noprompt</code>.</p>
     *
     * @param noprompt value of the field {@link #noprompt} to set
     */
    public void setNoprompt(boolean noprompt) {
        this.noprompt = noprompt;
    }

    /**
     * Gets the value of the {@link #providerclass} field.
     *
     * @return the value of the {@link #providerclass} field
     */
    public String getProviderclass() {
        return providerclass;
    }

    /**
     * <p>Setter for the field <code>providerclass</code>.</p>
     *
     * @param providerclass value of the field {@link #providerclass} to set
     */
    public void setProviderclass(String providerclass) {
        this.providerclass = providerclass;
    }

    /**
     * Gets the value of the {@link #providerarg} field.
     *
     * @return the value of the {@link #providerarg} field
     */
    public String getProviderarg() {
        return providerarg;
    }

    /**
     * <p>Setter for the field <code>providerarg</code>.</p>
     *
     * @param providerarg value of the field {@link #providerarg} to set
     */
    public void setProviderarg(String providerarg) {
        this.providerarg = providerarg;
    }

    /**
     * Gets the value of the {@link #providerpath} field.
     *
     * @return the value of the {@link #providerpath} field
     */
    public String getProviderpath() {
        return providerpath;
    }

    /**
     * <p>Setter for the field <code>providerpath</code>.</p>
     *
     * @param providerpath value of the field {@link #providerpath} to set
     */
    public void setProviderpath(String providerpath) {
        this.providerpath = providerpath;
    }
}
