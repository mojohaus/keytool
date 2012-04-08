package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolGenerateSecretKeyRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * To generate a secret key.
 * <p/>
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -genseckey} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal generateSecretKey
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class GenerateSecretKeyMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolGenerateSecretKeyRequest>
{

    /**
     * Key algorithm name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${keyalg}"
     * @since 1.2
     */
    private String keyalg;

    /**
     * Key bit size.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${keysize}"
     * @since 1.2
     */
    private String keysize;

    /**
     * Key password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${keypass}"
     * @since 1.2
     */
    private String keypass;

    public GenerateSecretKeyMojo()
    {
        super( KeyToolGenerateSecretKeyRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolGenerateSecretKeyRequest prepareRequest()
    {
        KeyToolGenerateSecretKeyRequest request = super.prepareRequest();

        request.setKeyalg( this.keyalg );
        request.setKeysize( this.keysize );
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
