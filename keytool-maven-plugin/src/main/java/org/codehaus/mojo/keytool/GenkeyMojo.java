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

import org.codehaus.mojo.keytool.requests.KeyToolGenerateKeyPairRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Generates a keystore.
 * <p/>
 * Implemented as a wrapper around the SDK <code>keytool -genkey</code> command.
 * <pre>
 * -genkey   [-v] [-protected]
 * [-alias &lt;alias&gt;]
 * [-keyalg &lt;keyalg&gt;] [-keysize &lt;keysize&gt;]
 * [-sigalg &lt;sigalg&gt;] [-dname &lt;dname&gt;]
 * [-validity &lt;valDays&gt;] [-keypass &lt;keypass&gt;]
 * [-keystore &lt;keystore&gt;] [-storepass &lt;storepass&gt;]
 * [-storetype &lt;storetype&gt;] [-providerName &lt;name&gt;]
 * [-providerClass &lt;provider_class_name&gt; [-providerArg &lt;arg&gt;]] ...
 * </pre>
 * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author <a href="jerome@coffeebreaks.org">Jerome Lacoste</a>
 * @version $Id$
 * @goal genkey
 * @phase package
 * @requiresProject
 */
public class GenkeyMojo
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
     * @parameter expression="${keyalg}"
     */
    private String keyalg;

    /**
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${keysize}"
     */
    private String keysize;

    /**
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${sigalg}"
     */
    private String sigalg;

    /**
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${validity}"
     */
    private String validity;

    /**
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${dname}"
     */
    private String dname;

    /**
     * {@inheritDoc}
     */
    protected KeyToolRequestWithKeyStoreAndAliasParameters createRequest()
    {
        KeyToolGenerateKeyPairRequest request = new KeyToolGenerateKeyPairRequest();
        request.setKeyalg( keyalg );
        request.setKeysize( keysize );
        request.setSigalg( sigalg );
        request.setValidity( validity );
        request.setDname( dname );
        request.setKeypass( keypass );
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
     * @param keyalg value of the {@link #keyalg} field to set
     */
    public void setKeyalg( String keyalg )
    {
        this.keyalg = keyalg;
    }

    /**
     * @param sigalg value of the {@link #sigalg} field to set
     */
    public void setSigalg( String sigalg )
    {
        this.sigalg = sigalg;
    }

    /**
     * @param keysize value of the {@link #keysize} field to set
     */
    public void setKeysize( String keysize )
    {
        this.keysize = keysize;
    }

    /**
     * @param validity value of the {@link #validity} field to set
     */
    public void setValidity( String validity )
    {
        this.validity = validity;
    }

    /**
     * @param dname value of the {@link #dname} field to set
     */
    public void setDname( String dname )
    {
        this.dname = dname;
    }

    /**
     * @param keypass the keypass to set
     */
    public void setKeypass( String keypass )
    {
        this.keypass = keypass;
    }

}
