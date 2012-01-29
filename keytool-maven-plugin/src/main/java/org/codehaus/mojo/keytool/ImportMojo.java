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

import org.codehaus.mojo.keytool.requests.KeyToolImportCertificateRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;

/**
 * Reads the certificate or certificate chain (where the latter is supplied in a PKCS#7 formatted reply) from the file
 * <code>cert_file</code>, and stores it in the keystore entry identified by <code>alias</code>.
 * <p/>
 * Implemented as a wrapper around the SDK <code>keytool -import</code> command.
 * <pre>
 * -import   [-v] [-protected]
 * [-alias &lt;alias&gt;]
 * [-file &lt;file&gt;]
 * [-keypass &lt;keypass&gt;]
 * [-storepass &lt;storepass&gt;]
 * [-storetype &lt;storetype&gt;]
 * [-keystore &lt;keystore&gt;]
 * </pre>
 * <p/>
 * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author <a>Juergen Mayrbaeurl</a>
 * @version 1.0
 * @goal import
 * @phase package
 * @requiresProject
 */
public class ImportMojo
    extends AbstractCmdLineKeyToolMojo
{

    /**
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${keypass}"
     */
    private String keypass;

    /**
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${file}"
     */
    private String file;

    /**
     * Imports Cert in current Java Runtime lib/security/cacerts.
     *
     * @parameter expression="${useJREcacerts}" default-value="false"
     */
    private boolean useJREcacerts;

    /**
     * {@inheritDoc}
     */
    protected KeyToolRequestWithKeyStoreAndAliasParameters createRequest()
    {
        KeyToolImportCertificateRequest request = new KeyToolImportCertificateRequest();
        request.setNoprompt( true );
        request.setFile( file );
        request.setKeypass( keypass );

        if ( StringUtils.isEmpty( getKeystore() ) && useJREcacerts )
        {
            // use the JRE certificates keystore, not any one specified in the keystore parameter
            File keystoreFile = KeyToolUtil.getJRECACerts();
            request.setKeystore( keystoreFile.getAbsolutePath() );
        }
        return request;
    }

    /**
     * {@inheritDoc}
     */
    protected String getCommandlineInfo( final Commandline commandLine )
    {
        String commandLineInfo = super.getCommandlineInfo( commandLine );

        commandLineInfo = StringUtils.replace( commandLineInfo, this.keypass, "'*****'" );

        return commandLineInfo;
    }

    /**
     * @param file the file to set
     */
    public void setFile( String file )
    {
        this.file = file;
    }

    /**
     * @param useJREcacerts the useJREcacerts flag to set
     */
    public void setUseJREcacerts( boolean useJREcacerts )
    {
        this.useJREcacerts = useJREcacerts;
    }

    /**
     * @param keypass the keypass to set
     */
    public void setKeypass( String keypass )
    {
        this.keypass = keypass;
    }

}
