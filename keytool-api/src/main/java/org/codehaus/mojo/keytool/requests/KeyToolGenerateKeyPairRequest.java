package org.codehaus.mojo.keytool.requests;

import java.util.ArrayList;
import java.util.List;

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
 * Request to generate a key pair using the KeyTool tool.
 *
 * @author tchemit
 * @since 1.1
 */
public class KeyToolGenerateKeyPairRequest extends AbstractKeyToolRequestWithKeyStoreAndAliasParameters {

    /**
     * Key algorithm name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String keyalg;

    /**
     * Key bit size.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String keysize;

    /**
     * Key password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String keypass;

    /**
     * Signature algorithm name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String sigalg;

    /**
     * Validity number of days.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String validity;

    /**
     * Certificate validity start date/time.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String startdate;

    /**
     * X.509 extension.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private List<String> exts = new ArrayList<>();

    /**
     * Distinguished name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String dname;

    /**
     * Gets the value of the {@link #keyalg} field.
     *
     * @return the value of the {@link #keyalg} field
     */
    public String getKeyalg() {
        return keyalg;
    }

    /**
     * <p>Setter for the field <code>keyalg</code>.</p>
     *
     * @param keyalg value of the field {@link #keyalg} to set
     */
    public void setKeyalg(String keyalg) {
        this.keyalg = keyalg;
    }

    /**
     * Gets the value of the {@link #keysize} field.
     *
     * @return the value of the {@link #keysize} field
     */
    public String getKeysize() {
        return keysize;
    }

    /**
     * <p>Setter for the field <code>keysize</code>.</p>
     *
     * @param keysize value of the field {@link #keysize} to set
     */
    public void setKeysize(String keysize) {
        this.keysize = keysize;
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

    /**
     * Gets the value of the {@link #sigalg} field.
     *
     * @return the value of the {@link #sigalg} field
     */
    public String getSigalg() {
        return sigalg;
    }

    /**
     * <p>Setter for the field <code>sigalg</code>.</p>
     *
     * @param sigalg value of the field {@link #sigalg} to set
     */
    public void setSigalg(String sigalg) {
        this.sigalg = sigalg;
    }

    /**
     * Gets the value of the {@link #validity} field.
     *
     * @return the value of the {@link #validity} field
     */
    public String getValidity() {
        return validity;
    }

    /**
     * <p>Setter for the field <code>validity</code>.</p>
     *
     * @param validity value of the field {@link #validity} to set
     */
    public void setValidity(String validity) {
        this.validity = validity;
    }

    /**
     * Gets the value of the {@link #dname} field.
     *
     * @return the value of the {@link #dname} field
     */
    public String getDname() {
        return dname;
    }

    /**
     * <p>Setter for the field <code>dname</code>.</p>
     *
     * @param dname value of the field {@link #dname} to set
     */
    public void setDname(String dname) {
        // Remove any extra spaces, see http://jira.codehaus.org/browse/MKEYTOOL-10
        this.dname = dname.replaceAll("\\s+", " ");
    }

    /**
     * Gets the value of the {@link #startdate} field.
     *
     * @return the value of the {@link #startdate} field
     */
    public String getStartdate() {
        return startdate;
    }

    /**
     * <p>Setter for the field <code>startdate</code>.</p>
     *
     * @param startdate value of the field {@link #startdate} to set
     */
    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    /**
     * Gets the value of the {@link #exts} field.
     *
     * @return the value of the {@link #exts} field
     */
    public String getExt() {
        return exts.isEmpty() ? null : exts.get(0);
    }

    /**
     * <p>setExt.</p>
     *
     * @param ext value of the field {@link #exts} to set
     */
    public void setExt(String ext) {
        this.exts.clear();

        if (ext != null) {
            this.exts.add(ext);
        }
    }

    /**
     * <p>Setter for the field <code>exts</code>.</p>
     *
     * @param exts values of the field {@link #exts} to set
     * @since 1.6
     */
    public void setExts(List<String> exts) {
        this.exts.clear();

        if (exts != null) {
            this.exts.addAll(exts);
        }
    }

    /**
     * Gets the values of the {@link #exts} field.
     *
     * @return the values of the {@link #exts} field
     * @since 1.6
     */
    public List<String> getExts() {
        return exts;
    }
}
