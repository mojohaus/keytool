package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolImportKeystoreRequest;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * To import all entries of a keystore to another keystore.
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -importkeystore} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 * <p/>
 * <strong>Since version 1.2, this mojo replace the mojo import.</strong>
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal importKeystore
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class ImportKeystoreMojo
    extends AbstractKeyToolRequestMojo<KeyToolImportKeystoreRequest>
{

    /**
     * Source keystore name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${srckeystore}"
     * @since 1.2
     */
    private String srckeystore;

    /**
     * Destination keystore name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${destkeystore}"
     * @since 1.2
     */
    private String destkeystore;

    /**
     * Source keystore type.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${srcstoretype}"
     * @since 1.2
     */
    private String srcstoretype;

    /**
     * Destination keystore type.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${deststoretype}"
     * @since 1.2
     */
    private String deststoretype;

    /**
     * Source keystore password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${srcstorepass}"
     * @since 1.2
     */
    private String srcstorepass;

    /**
     * Destination keystore password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${deststorepass}"
     * @since 1.2
     */
    private String deststorepass;

    /**
     * Source keystore password protected.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${srcprotected}"
     * @since 1.2
     */
    private boolean srcprotected;

    /**
     * Source keystore provider name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${srcprovidername}"
     * @since 1.2
     */
    private String srcprovidername;

    /**
     * Destination keystore provider name.
     * <p/>
     * See <a hresf="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${destprovidername}"
     * @since 1.2
     */
    private String destprovidername;

    /**
     * Source alias.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${srcalias}"
     * @since 1.2
     */
    private String srcalias;

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
     * Source key password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${srckeypass}"
     * @since 1.2
     */
    private String srckeypass;

    /**
     * Destination key password.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${destkeypass}"
     * @since 1.2
     */
    private String destkeypass;

    /**
     * Do not prompt.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${noprompt}"
     * @since 1.2
     */
    private boolean noprompt;

    /**
     * Provider class name.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${providerclass}"
     * @since 1.2
     */
    private String providerclass;

    /**
     * Provider argument.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${providerarg}"
     * @since 1.2
     */
    private String providerarg;

    /**
     * Provider classpath.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${providerpath}"
     * @since 1.2
     */
    private String providerpath;

    public ImportKeystoreMojo()
    {
        super( KeyToolImportKeystoreRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolImportKeystoreRequest prepareRequest()
    {
        KeyToolImportKeystoreRequest request = super.prepareRequest();

        request.setSrckeystore( this.srckeystore );
        request.setDestkeystore( this.destkeystore );
        request.setSrcstoretype( this.srcstoretype );
        request.setDeststoretype( this.deststoretype );
        request.setSrcstorepass( this.srcstorepass );
        request.setDeststorepass( this.deststorepass );
        request.setSrcprotected( this.srcprotected );
        request.setSrcprovidername( this.srcprovidername );
        request.setDestprovidername( this.destprovidername );
        request.setSrcalias( this.srcalias );
        request.setDestalias( this.destalias );
        request.setSrckeypass( this.srckeypass );
        request.setDestkeypass( this.destkeypass );
        request.setNoprompt( this.noprompt );
        request.setProviderclass( this.providerclass );
        request.setProviderarg( this.providerarg );
        request.setProviderpath( this.providerpath );
        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCommandlineInfo( Commandline commandLine )
    {
        String commandLineInfo = super.getCommandlineInfo( commandLine );

        commandLineInfo = StringUtils.replace( commandLineInfo, this.srckeypass, "'*****'" );
        commandLineInfo = StringUtils.replace( commandLineInfo, this.destkeypass, "'*****'" );
        commandLineInfo = StringUtils.replace( commandLineInfo, this.srcstorepass, "'*****'" );
        commandLineInfo = StringUtils.replace( commandLineInfo, this.deststorepass, "'*****'" );

        return commandLineInfo;
    }
}
