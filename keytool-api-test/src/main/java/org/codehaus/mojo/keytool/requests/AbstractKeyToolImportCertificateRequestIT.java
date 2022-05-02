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

import org.apache.maven.shared.utils.cli.javatool.JavaToolResult;

import java.io.File;

/**
 * Test the {@link org.codehaus.mojo.keytool.requests.KeyToolImportCertificateRequest}.
 *
 * @author tchemit
 * @since 1.1
 */
public abstract class AbstractKeyToolImportCertificateRequestIT
    extends AbstractKeyToolRequestIT<KeyToolImportCertificateRequest>
{
    /**
     * <p>Constructor for AbstractKeyToolImportCertificateRequestIT.</p>
     */
    protected AbstractKeyToolImportCertificateRequestIT()
    {
    }

    /**
     * <p>Constructor for AbstractKeyToolImportCertificateRequestIT.</p>
     *
     * @param supportedRequest a boolean
     */
    protected AbstractKeyToolImportCertificateRequestIT( boolean supportedRequest )
    {
        super( supportedRequest );
    }

    /** {@inheritDoc} */
    @Override
    public final void testRequest()
        throws Exception
    {

        File keyStore = resourceFixtures.simpleKeyStore( false );

        File file = resourceFixtures.simpleCertificate();

        KeyToolImportCertificateRequest request =
            requestFixtures.createKeyToolImportCertificateRequest( keyStore, file );

        JavaToolResult keyToolResult = consumeRequest( request );

        requestResult( keyToolResult, keyStore, file );
    }

    /**
     * <p>requestResult.</p>
     *
     * @param keyToolResult a {@link org.apache.maven.shared.utils.cli.javatool.JavaToolResult} object
     * @param keyStore a {@link java.io.File} object
     * @param file a {@link java.io.File} object
     */
    protected abstract void requestResult( JavaToolResult keyToolResult, File keyStore, File file );
}
