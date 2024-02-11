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

import java.io.File;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.keytool.requests.KeyToolPrintCertificateRequestRequest;

/**
 * To print the content of a certificate request.
 * Implemented as a wrapper around the SDK {@code keytool -printcertreq} command.
 * <strong>Note</strong> This operation was not implemented by the keytool before jdk 1.7.
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "printCertificateRequest", requiresProject = true, threadSafe = true)
public class PrintCertificateRequestMojo extends AbstractKeyToolRequestMojo<KeyToolPrintCertificateRequestRequest> {

    /**
     * Input file name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private File file;

    /**
     * Default contructor.
     */
    public PrintCertificateRequestMojo() {
        super(KeyToolPrintCertificateRequestRequest.class);
    }

    /** {@inheritDoc} */
    @Override
    protected KeyToolPrintCertificateRequestRequest createKeytoolRequest() {
        KeyToolPrintCertificateRequestRequest request = super.createKeytoolRequest();

        request.setFile(this.file);
        return request;
    }
}
