package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolChangeAliasRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * To change an entry alias into a key store.
 * <p/>
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -keyclone} (jdk 1.5) or {@code keytool -changealias} (jdk 1.6)
 * command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal changeAlias
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class ChangeAliasMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolChangeAliasRequest>
{

    /**
     * Destination alias.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${destalias}"
     * @since 1.2
     */
    private String destalias;

    /**
     * Key password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${keypass}"
     * @since 1.2
     */
    private String keypass;

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
    protected KeyToolChangeAliasRequest prepareRequest()
    {
        KeyToolChangeAliasRequest request = super.prepareRequest();
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
