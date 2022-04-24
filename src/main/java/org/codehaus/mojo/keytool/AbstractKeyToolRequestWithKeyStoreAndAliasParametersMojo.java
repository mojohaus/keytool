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
 * Abstract mojo to execute a store with alias parameters request.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.2
 */
public abstract class AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo
        extends AbstractKeyToolRequestWithKeyStoreParametersMojo
{
    /**
     * Password through protected mechanism.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean passwordProtected;

    /**
     * Alias name of the entry to process.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    @Parameter
    private String alias;

    /**
     * Constructor of abstract mojo.
     */
    protected AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo()
    {
    }

    public boolean isPasswordProtected()
    {
        return passwordProtected;
    }

    public String getAlias()
    {
        return alias;
    }
}
