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
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.keytool.api.*;
import org.codehaus.mojo.keytool.api.requests.KeyToolListRequest;

/**
 * To list entries in a keystore.
 * Implemented as a wrapper around the SDK {@code keytool -list} (jdk 1.5) command.
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "list", requiresProject = true, threadSafe = true)
public class ListMojo extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolListRequest> {
    /**
     * Output in RFC style.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean rfc;

    /**
     * Default contructor.
     */
    public ListMojo() {
        super(KeyToolListRequest.class);
    }

    /** {@inheritDoc} */
    @Override
    protected KeyToolListRequest createKeytoolRequest() {
        KeyToolListRequest request = super.createKeytoolRequest();

        request.setRfc(this.rfc);
        return request;
    }
}
