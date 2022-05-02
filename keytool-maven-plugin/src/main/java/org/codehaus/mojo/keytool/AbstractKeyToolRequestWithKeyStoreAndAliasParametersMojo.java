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

import org.apache.maven.plugins.annotations.Parameter;

/**
 * Abstract mojo to execute a {@link org.codehaus.mojo.keytool.KeyToolRequestWithKeyStoreAndAliasParameters} request.
 *
 * @param <R> generic type of request used by the mojo
 * @author tchemit
 * @since 1.2
 */
public abstract class AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<R extends KeyToolRequestWithKeyStoreAndAliasParameters>
    extends AbstractKeyToolRequestWithKeyStoreParametersMojo<R>
{
    /**
     * Password through protected mechanism.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean passwordProtected;

    /**
     * Alias name of the entry to process.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    @Parameter
    private String alias;

    /**
     * Constructor of abstract mojo.
     *
     * @param requestType type of keytool request used by the mojo
     */
    protected AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo( Class<R> requestType )
    {
        super( requestType );
    }

    /** {@inheritDoc} */
    @Override
    protected R createKeytoolRequest()
    {
        R request = super.createKeytoolRequest();

        request.setPasswordProtected( this.passwordProtected );
        request.setAlias( this.alias );
        return request;
    }
}
