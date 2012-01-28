package org.codehaus.mojo.keytool;

/*
 * Copyright 2005-2011 The Codehaus
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

import org.codehaus.mojo.shared.keytool.KeyToolRequestWithKeyStoreAndAliasParameters;
import org.codehaus.mojo.shared.keytool.KeyToolRequestWithKeyStoreParameters;
import org.codehaus.mojo.shared.keytool.requests.KeyToolExportCertificateRequest;

/**
 * Reads (from the keystore) the certificate associated with <code>alias</code>, and stores it in the file
 * <code>cert_file</code>.
 * <p/>
 * Implemented as a wrapper around the SDK <code>keytool -export</code> command.
 * <pre>
 * -export   [-v] [-protected]
 * [-alias &lt;alias&gt;]
 * [-file &lt;file&gt;]
 * [-storetype &lt;storetype&gt;]
 * [-keystore &lt;keystore&gt;]
 * [-storepass &lt;storepass&gt;]
 * </pre>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author <a>Juergen Mayrbaeurl</a>
 * @version 1.0
 * @goal export
 * @phase package
 * @requiresProject
 */
public class ExportMojo
    extends AbstractCmdLineKeyToolMojo
{

    /**
     * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${file}"
     */
    private String file;

    /**
     * {@inheritDoc}
     */
    protected KeyToolRequestWithKeyStoreAndAliasParameters createRequest()
    {
        KeyToolExportCertificateRequest request = new KeyToolExportCertificateRequest();
        request.setFile( file );
        return request;
    }

    /**
     * @param file the file to set
     */
    public void setFile( String file )
    {
        this.file = file;
    }

}
