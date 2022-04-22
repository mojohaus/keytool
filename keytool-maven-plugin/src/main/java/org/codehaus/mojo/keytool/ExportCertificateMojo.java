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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.keytool.requests.KeyToolExportCertificateRequest;

/**
 * To export a certificate from a keystore.
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -export} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.2
 */
@Mojo( name = "exportCertificate", requiresProject = true, threadSafe = true )
public class ExportCertificateMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolExportCertificateRequest>
{

    /**
     * Output in RFC style.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean rfc;

    /**
     * Output file name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String file;

    /**
     * Default contructor.
     */
    public ExportCertificateMojo()
    {
        super( KeyToolExportCertificateRequest.class );
    }

    @Override
    public void execute() throws MojoExecutionException {
        createParentDirIfNecessary(file);
        super.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolExportCertificateRequest createKeytoolRequest()
    {
        KeyToolExportCertificateRequest request = super.createKeytoolRequest();

        request.setFile( this.file );
        request.setRfc( this.rfc );
        return request;
    }
}
