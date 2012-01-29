package org.codehaus.mojo.keytool.requests;

/*
 * Copyright 2005-2012 The Codehaus
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

import org.codehaus.mojo.keytool.KeyToolRequestWithKeyStoreAndAliasParameters;

/**
 * Specifies the commons parameters used to control a key tool invocation which have propviders options.
 *
 * @author tchemit <chemit@codelutin.com>
 * @version $Id$
 * @since 1.1
 */
public abstract class AbstractKeyToolRequestWithKeyStoreAndAliasParameters
    extends AbstractKeyToolRequestWithKeyStoreParameters
    implements KeyToolRequestWithKeyStoreAndAliasParameters
{

    /**
     * Password through protected mechanism.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private boolean passwordProtected;

    /**
     * Alias name of the entry to process.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    private String alias;

    /**
     * {@inheritDoc}
     */
    public final boolean isPasswordProtected()
    {
        return passwordProtected;
    }

    /**
     * {@inheritDoc}
     */
    public final void setPasswordProtected( boolean passwordProtected )
    {
        this.passwordProtected = passwordProtected;
    }

    /**
     * {@inheritDoc}
     */
    public final String getAlias()
    {
        return alias;
    }

    /**
     * {@inheritDoc}
     */
    public final void setAlias( String alias )
    {
        this.alias = alias;
    }

}
