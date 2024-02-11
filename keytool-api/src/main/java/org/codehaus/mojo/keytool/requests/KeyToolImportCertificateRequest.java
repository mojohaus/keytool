package org.codehaus.mojo.keytool.requests;

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
 * Request to import a certificate using the KeyTool tool.
 *
 * @author tchemit
 * @since 1.1
 */
public class KeyToolImportCertificateRequest extends AbstractKeyToolRequestWithKeyStoreAndAliasParameters {
    /**
     * Key password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String keypass;

    /**
     * Input file name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String file;

    /**
     * Do not prompt.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private boolean noprompt;

    /**
     * Trust certificates from cacerts.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private boolean trustcacerts;

    /**
     * Gets the value of the {@link #file} field.
     *
     * @return the value of the {@link #file} field
     */
    public String getFile() {
        return file;
    }

    /**
     * <p>Setter for the field <code>file</code>.</p>
     *
     * @param file value of the field {@link #file} to set
     */
    public void setFile(String file) {
        this.file = file;
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
     * Gets the value of the {@link #trustcacerts} field.
     *
     * @return the value of the {@link #trustcacerts} field
     */
    public boolean isTrustcacerts() {
        return trustcacerts;
    }

    /**
     * <p>Setter for the field <code>trustcacerts</code>.</p>
     *
     * @param trustcacerts value of the field {@link #trustcacerts} to set
     */
    public void setTrustcacerts(boolean trustcacerts) {
        this.trustcacerts = trustcacerts;
    }

    /**
     * Gets the value of the {@code keypass} field.
     *
     * @return the value of the {@code keypass} field.
     */
    public String getKeypass() {
        return keypass;
    }

    /**
     * Sets the new given value to the field {@code keypass} of the request.
     *
     * @param keypass the new value of the field {@code keypass}.
     */
    public void setKeypass(String keypass) {
        this.keypass = keypass;
    }
}
