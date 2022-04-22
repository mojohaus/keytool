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
import org.codehaus.mojo.keytool.requests.KeyToolChangeAliasRequest;
import org.codehaus.plexus.util.StringUtils;

/**
 * To change an entry alias into a keystore.
 * <p/>
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -keyclone} (jdk 1.5) or {@code keytool -changealias} (jdk 1.6)
 * command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.2
 */
@Mojo( name = "changeAlias", requiresProject = true, threadSafe = true )
public class ChangeAliasMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolChangeAliasRequest>
{

    /**
     * Destination alias.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destalias;

    /**
     * Key password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keypass;

    /**
     * Default contructor.
     */
    public ChangeAliasMojo()
    {
        super( KeyToolChangeAliasRequest.class );
    }

    /**
     * Gets the value of the {@link #destalias} field.
     *
     * @return the value of the {@link #destalias} field
     */
    public String getDestalias()
    {
        return destalias;
    }

    /**
     * @param destalias value of the field {@link #destalias} to set
     */
    public void setDestalias( String destalias )
    {
        this.destalias = destalias;
    }

    /**
     * Gets the value of the {@code keypass} field.
     *
     * @return the value of the {@code keypass} field.
     */
    public String getKeypass()
    {
        return keypass;
    }

    /**
     * Sets the new given value to the field {@code keypass} of the request.
     *
     * @param keypass the new value of the field {@code keypass}.
     */
    public void setKeypass( String keypass )
    {
        this.keypass = keypass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolChangeAliasRequest createKeytoolRequest()
    {
        KeyToolChangeAliasRequest request = super.createKeytoolRequest();
        request.setDestalias( this.destalias );
        request.setKeypass( this.keypass );
        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCommandlineInfo( Commandline commandLine )
    {
        String commandLineInfo = super.getCommandlineInfo( commandLine );

        commandLineInfo = StringUtils.replace( commandLineInfo, this.keypass, "'*****'" );

        return commandLineInfo;
    }
}
