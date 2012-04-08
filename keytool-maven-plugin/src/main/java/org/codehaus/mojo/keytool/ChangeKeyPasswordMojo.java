package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolChangeKeyPasswordRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * To change the key password of an entry of a key store.
 * <p/>
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -keypasswd} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal changeKeyPassword
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class ChangeKeyPasswordMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolChangeKeyPasswordRequest>
{

    /**
     * New password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${newPassword}"
     * @since 1.2
     */
    private String newPassword;

    /**
     * Key password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${keypass}"
     * @since 1.2
     */
    private String keypass;

    public ChangeKeyPasswordMojo()
    {
        super( KeyToolChangeKeyPasswordRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolChangeKeyPasswordRequest prepareRequest()
    {
        KeyToolChangeKeyPasswordRequest request = super.prepareRequest();
        request.setNewPassword( this.newPassword );
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
        commandLineInfo = StringUtils.replace( commandLineInfo, this.newPassword, "'*****'" );

        return commandLineInfo;
    }
}
