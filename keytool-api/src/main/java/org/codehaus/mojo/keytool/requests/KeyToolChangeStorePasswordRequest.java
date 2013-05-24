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
 * Request to change the store password of a keystore using the KeyTool tool.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.1
 */
public class KeyToolChangeStorePasswordRequest
    extends AbstractKeyToolRequestWithKeyStoreParameters
{

    /**
     * New password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String newPassword;

    /**
     * Gets the value of the {@link #newPassword} field.
     *
     * @return the value of the {@link #newPassword} field
     */
    public String getNewPassword()
    {
        return newPassword;
    }

    /**
     * @param newPassword value of the field {@link #newPassword} to set
     */
    public void setNewPassword( String newPassword )
    {
        this.newPassword = newPassword;
    }

}
