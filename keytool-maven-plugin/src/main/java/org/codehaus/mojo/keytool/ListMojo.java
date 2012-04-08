package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolListRequest;

/**
 * To list entries in a keystore.
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -list} (jdk 1.5) command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal list
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class ListMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolListRequest>
{
    /**
     * Output in RFC style.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${rfc}"
     * @since 1.2
     */
    private boolean rfc;

    public ListMojo()
    {
        super( KeyToolListRequest.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KeyToolListRequest prepareRequest()
    {
        KeyToolListRequest request = super.prepareRequest();

        request.setRfc( this.rfc );
        return request;
    }

}
