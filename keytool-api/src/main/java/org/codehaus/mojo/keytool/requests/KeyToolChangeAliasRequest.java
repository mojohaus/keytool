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
 * Request to change an entry alias using the KeyTool tool.
 *
 * @author tchemit
 * @since 1.1
 */
public class KeyToolChangeAliasRequest
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParameters
{

    /**
     * Destination alias.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String destalias;

    /**
     * Key password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String keypass;

    /**
     * Gets the value of the {@link #destalias} field.
     *
     * @return the value of the {@link #destalias} field
     */
    public String getDestalias()
    {
        return destalias;
    }

    /**
     * <p>Setter for the field <code>destalias</code>.</p>
     *
     * @param destalias value of the field {@link #destalias} to set
     */
    public void setDestalias( String destalias )
    {
        this.destalias = destalias;
    }

    /**
     * Gets the value of the {@code keypass} field.
     *
     * @return the value of the {@code keypass} field.
     */
    public String getKeypass()
    {
        return keypass;
    }

    /**
     * Sets the new given value to the field {@code keypass} of the request.
     *
     * @param keypass the new value of the field {@code keypass}.
     */
    public void setKeypass( String keypass )
    {
        this.keypass = keypass;
    }

}
