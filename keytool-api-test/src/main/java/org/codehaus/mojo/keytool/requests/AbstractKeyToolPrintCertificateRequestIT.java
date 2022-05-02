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
 * Test the {@link org.codehaus.mojo.keytool.requests.KeyToolPrintCertificateRequest}.
 *
 * @author tchemit
 * @since 1.1
 */
public abstract class AbstractKeyToolPrintCertificateRequestIT
    extends AbstractKeyToolRequestIT<KeyToolPrintCertificateRequest>
{

    /**
     * <p>Constructor for AbstractKeyToolPrintCertificateRequestIT.</p>
     */
    protected AbstractKeyToolPrintCertificateRequestIT()
    {
    }

    /**
     * <p>Constructor for AbstractKeyToolPrintCertificateRequestIT.</p>
     *
     * @param supportedRequest a boolean
     */
    protected AbstractKeyToolPrintCertificateRequestIT( boolean supportedRequest )
    {
        super( supportedRequest );
    }

    /** {@inheritDoc} */
    @Override
    public final void testRequest()
        throws Exception
    {

        File file = resourceFixtures.simpleCertificate();

        KeyToolPrintCertificateRequest request = requestFixtures.createKeyToolPrintCertificateRequest( file );

        JavaToolResult keyToolResult = consumeRequest( request );

        requestResult( keyToolResult, file );
    }

    /**
     * <p>requestResult.</p>
     *
     * @param keyToolResult a {@link org.apache.maven.shared.utils.cli.javatool.JavaToolResult} object
     * @param file a {@link java.io.File} object
     */
    protected abstract void requestResult( JavaToolResult keyToolResult, File file );
}
