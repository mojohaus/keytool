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
import org.apache.maven.shared.utils.cli.Commandline;
import org.codehaus.mojo.keytool.requests.KeyToolGenerateSecretKeyRequest;
import org.codehaus.plexus.util.StringUtils;

/**
 * To generate a secret key into a keystore.
 * Implemented as a wrapper around the SDK {@code keytool -genseckey} command.
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 * @author tchemit
 * @since 1.2
 */
@Mojo( name = "generateSecretKey", requiresProject = true, threadSafe = true )
public class GenerateSecretKeyMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolGenerateSecretKeyRequest>
{

    /**
     * Key algorithm name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keyalg;

    /**
     * Key bit size.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keysize;

    /**
     * Key password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keypass;

    /**
     * Default contructor.
     */
    public GenerateSecretKeyMojo()
    {
        super( KeyToolGenerateSecretKeyRequest.class );
    }

    /** {@inheritDoc} */
    @Override
    protected KeyToolGenerateSecretKeyRequest createKeytoolRequest()
    {
        KeyToolGenerateSecretKeyRequest request = super.createKeytoolRequest();

        request.setKeyalg( this.keyalg );
        request.setKeysize( this.keysize );
        request.setKeypass( this.keypass );
        return request;
    }

    /** {@inheritDoc} */
    @Override
    protected String getCommandlineInfo( Commandline commandLine )
    {
        String commandLineInfo = super.getCommandlineInfo( commandLine );

        commandLineInfo = StringUtils.replace( commandLineInfo, this.keypass, "'*****'" );

        return commandLineInfo;
    }
}
