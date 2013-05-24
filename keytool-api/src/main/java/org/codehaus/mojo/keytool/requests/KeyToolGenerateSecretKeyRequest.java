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
 * Request to generate a secret key using the KeyTool tool.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.1
 */
public class KeyToolGenerateSecretKeyRequest
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParameters
{

    /**
     * Key algorithm name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String keyalg;

    /**
     * Key bit size.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String keysize;

    /**
     * Key password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String keypass;

    /**
     * Gets the value of the {@link #keyalg} field.
     *
     * @return the value of the {@link #keyalg} field
     */
    public String getKeyalg()
    {
        return keyalg;
    }

    /**
     * @param keyalg value of the field {@link #keyalg} to set
     */
    public void setKeyalg( String keyalg )
    {
        this.keyalg = keyalg;
    }

    /**
     * Gets the value of the {@link #keysize} field.
     *
     * @return the value of the {@link #keysize} field
     */
    public String getKeysize()
    {
        return keysize;
    }

    /**
     * @param keysize value of the field {@link #keysize} to set
     */
    public void setKeysize( String keysize )
    {
        this.keysize = keysize;
    }

    /**
     * Gets the value of the {@link #keypass} field.
     *
     * @return the value of the {@link #keypass} field
     */
    public String getKeypass()
    {
        return keypass;
    }

    /**
     * @param keypass value of the field {@link #keypass} to set
     */
    public void setKeypass( String keypass )
    {
        this.keypass = keypass;
    }

}
