package org.codehaus.mojo.keytool;

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

import org.codehaus.mojo.keytool.requests.KeyToolPrintCertificateRequestRequest;

import java.io.File;

/**
 * To print the content of a certificate request.
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -printcertreq} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal printCertificateRequest
 * @requiresProject
 * @since 1.2
 */
public class PrintCertificateRequestMojo
    extends AbstractKeyToolRequestMojo<KeyToolPrintCertificateRequestRequest>
{

    /**
     * Input file name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${file}"
     * @since 1.2
     */
    private File file;

    /**
     * Default contructor.
     */
    public PrintCertificateRequestMojo()
    {
        super( KeyToolPrintCertificateRequestRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolPrintCertificateRequestRequest createKeytoolRequest()
    {
        KeyToolPrintCertificateRequestRequest request = super.createKeytoolRequest();

        request.setFile( this.file );
        return request;
    }

}
