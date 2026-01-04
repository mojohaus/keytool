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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Request to do a Generate certificate request using the KeyTool tool.
 *
 * @author tchemit
 * @since 1.1
 */
public class KeyToolGenerateCertificateRequestRequest extends AbstractKeyToolRequestWithKeyStoreAndAliasParameters {
    /**
     * Signature algorithm name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String sigalg;

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
     * Output file name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private File file;

    /**
     * Key password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String keypass;

    /**
     * Gets the value of the {@link #file} field.
     *
     * @return the value of the {@link #file} field
     */
    public File getFile() {
        return file;
    }

    /**
     * <p>Setter for the field <code>file</code>.</p>
     *
     * @param file value of the field {@link #file} to set
     */
    public void setFile(File file) {
        this.file = file;
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
     * Gets the value of the {@link #exts} field.
     *
     * @return the value of the {@link #exts} field
     * @since 1.6
     */
    public String getExt() {
        return exts.isEmpty() ? null : exts.get(0);
    }

    /**
     * <p>setExt.</p>
     *
     * @param ext value of the field {@link #exts} to set
     * @since 1.6
     */
    public void setExt(String ext) {
        exts.clear();

        if (ext != null) {
            exts.add(ext);
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
        this.dname = dname;
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
