package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolGenerateKeyPairRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * To generate a key pair.
 * <p/>
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -genkey} (jdk 1.5) {@code keytool -genkeypair} (jdk 1.6)
 * command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal generateKeyPair
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class GenerateKeyPairMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolGenerateKeyPairRequest>
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

    /**
     * Signature algorithm name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${sigalg}"
     * @since 1.2
     */
    private String sigalg;

    /**
     * Validity number of days.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${validity}"
     * @since 1.2
     */
    private String validity;

    /**
     * Certificate validity start date/time.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${startdate}"
     * @since 1.2
     */
    private String startdate;

    /**
     * X.509 extension.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${ext}"
     * @since 1.2
     */
    private String ext;

    /**
     * Distinguished name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${dname}"
     * @since 1.2
     */
    private String dname;

    public GenerateKeyPairMojo()
    {
        super( KeyToolGenerateKeyPairRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolGenerateKeyPairRequest prepareRequest()
    {
        KeyToolGenerateKeyPairRequest request = super.prepareRequest();

        request.setKeyalg( this.keyalg );
        request.setKeysize( this.keysize );
        request.setKeypass( this.keypass );
        request.setSigalg( this.sigalg );
        request.setDname( this.dname );
        request.setStartdate( this.startdate );
        request.setExt( this.ext );
        request.setValidity( this.validity );
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
