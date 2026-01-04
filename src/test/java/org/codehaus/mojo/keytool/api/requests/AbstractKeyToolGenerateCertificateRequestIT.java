package org.codehaus.mojo.keytool.api.requests;

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

import java.io.File;

import org.apache.maven.shared.utils.cli.javatool.JavaToolResult;

/**
 * Test the {@link KeyToolGenerateCertificateRequest}.
 *
 * @author tchemit
 * @since 1.1
 */
public abstract class AbstractKeyToolGenerateCertificateRequestIT
        extends AbstractKeyToolRequestIT<KeyToolGenerateCertificateRequest> {
    /**
     * <p>Constructor for AbstractKeyToolGenerateCertificateRequestIT.</p>
     */
    protected AbstractKeyToolGenerateCertificateRequestIT() {}

    /**
     * <p>Constructor for AbstractKeyToolGenerateCertificateRequestIT.</p>
     *
     * @param supportedRequest a boolean
     */
    protected AbstractKeyToolGenerateCertificateRequestIT(boolean supportedRequest) {
        super(supportedRequest);
    }

    /** {@inheritDoc} */
    @Override
    public final void testRequest() throws Exception {

        File keyStore = resourceFixtures.simpleKeyStore();

        File outputFile = resourceFixtures.outputFile();

        File inFile = resourceFixtures.simpleCertificateRequest();

        KeyToolGenerateCertificateRequest request =
                requestFixtures.createKeyToolGenerateCertificateRequest(keyStore, inFile, outputFile);

        JavaToolResult keyToolResult = consumeRequest(request);

        requestResult(keyToolResult, keyStore, inFile, outputFile);
    }

    /**
     * <p>requestResult.</p>
     *
     * @param keyToolResult a {@link org.apache.maven.shared.utils.cli.javatool.JavaToolResult} object
     * @param keyStore a {@link java.io.File} object
     * @param inFile a {@link java.io.File} object
     * @param outputFile a {@link java.io.File} object
     */
    protected abstract void requestResult(JavaToolResult keyToolResult, File keyStore, File inFile, File outputFile);
}
