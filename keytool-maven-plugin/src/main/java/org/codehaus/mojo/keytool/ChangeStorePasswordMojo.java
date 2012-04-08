package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolChangeStorePasswordRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * To change the store password of a keystore.
 * <p/>
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -storepasswd} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal changeStorePassword
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class ChangeStorePasswordMojo
    extends AbstractKeyToolRequestWithKeyStoreParametersMojo<KeyToolChangeStorePasswordRequest>
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

    public ChangeStorePasswordMojo()
    {
        super( KeyToolChangeStorePasswordRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolChangeStorePasswordRequest prepareRequest()
    {
        KeyToolChangeStorePasswordRequest request = super.prepareRequest();
        request.setNewPassword( this.newPassword );
        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCommandlineInfo( Commandline commandLine )
    {
        String commandLineInfo = super.getCommandlineInfo( commandLine );

        commandLineInfo = StringUtils.replace( commandLineInfo, this.newPassword, "'*****'" );

        return commandLineInfo;
    }
}
