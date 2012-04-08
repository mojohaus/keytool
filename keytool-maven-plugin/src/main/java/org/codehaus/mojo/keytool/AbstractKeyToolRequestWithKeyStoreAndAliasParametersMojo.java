package org.codehaus.mojo.keytool;

/**
 * Abstract mojo to execute a {@link KeyToolRequestWithKeyStoreAndAliasParameters} request.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.2
 */
public abstract class AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<R extends KeyToolRequestWithKeyStoreAndAliasParameters>
    extends AbstractKeyToolRequestWithKeyStoreParametersMojo<R>
{
    /**
     * Password through protected mechanism.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${passwordProtected}"
     * @since 1.2
     */
    private boolean passwordProtected;

    /**
     * Alias name of the entry to process.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${alias}"
     */
    private String alias;

    protected AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo( Class<R> requestType )
    {
        super( requestType );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected R prepareRequest()
    {
        R request = super.prepareRequest();

        request.setPasswordProtected( this.passwordProtected );
        request.setAlias( this.alias );
        return request;
    }
}
