package org.codehaus.mojo.keytool.requests;

/*
 * Copyright 2005-2005-2013 The Codehaus
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

import org.apache.maven.shared.utils.cli.javatool.JavaToolResult;

import java.io.File;

/**
 * Test the {@link org.codehaus.mojo.keytool.requests.KeyToolImportKeystoreRequest}.
 *
 * @author tchemit
 * @since 1.3
 */
public abstract class AbstractKeyToolImportKeystoreRequestIT
    extends AbstractKeyToolRequestIT<KeyToolImportKeystoreRequest>
{

    /**
     * <p>Constructor for AbstractKeyToolImportKeystoreRequestIT.</p>
     */
    protected AbstractKeyToolImportKeystoreRequestIT()
    {
    }

    /**
     * <p>Constructor for AbstractKeyToolImportKeystoreRequestIT.</p>
     *
     * @param supportedRequest a boolean
     */
    protected AbstractKeyToolImportKeystoreRequestIT( boolean supportedRequest )
    {
        super( supportedRequest );
    }

    /** {@inheritDoc} */
    @Override
    public final void testRequest()
        throws Exception
    {

        File srcKeyStore = resourceFixtures.simpleKeyStore();

        File destKeyStore = resourceFixtures.simpleDestKeyStoreFile();

        KeyToolImportKeystoreRequest request =
            requestFixtures.createKeyToolImportKeystoreRequest( srcKeyStore, destKeyStore );

        JavaToolResult keyToolResult = consumeRequest( request );

        requestResult( keyToolResult, srcKeyStore, destKeyStore );
    }

    /**
     * <p>requestResult.</p>
     *
     * @param keyToolResult a {@link org.apache.maven.shared.utils.cli.javatool.JavaToolResult} object
     * @param srcKeyStore a {@link java.io.File} object
     * @param destKeyStore a {@link java.io.File} object
     */
    protected abstract void requestResult( JavaToolResult keyToolResult, File srcKeyStore, File destKeyStore );

}
