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

import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.mojo.keytool.requests.KeyToolDeleteRequest;

/**
 * To delete an entry alias from a keystore.
 * Implemented as a wrapper around the SDK {@code keytool -delete} command.
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo( name = "deleteAlias", requiresProject = true, threadSafe = true )
public class DeleteAliasMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolDeleteRequest>
{

    /**
     * Default contructor.
     */
    public DeleteAliasMojo()
    {
        super( KeyToolDeleteRequest.class );
    }

}
