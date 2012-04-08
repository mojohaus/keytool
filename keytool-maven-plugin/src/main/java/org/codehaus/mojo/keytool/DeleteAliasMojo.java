package org.codehaus.mojo.keytool;

import org.codehaus.mojo.keytool.requests.KeyToolDeleteRequest;

/**
 * To delete an entry alias from a keystore.
 * <p/>
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -delete} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @goal deleteAlias
 * @phase package
 * @requiresProject
 * @since 1.2
 */
public class DeleteAliasMojo
    extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolDeleteRequest>
{

    public DeleteAliasMojo()
    {
        super( KeyToolDeleteRequest.class );
    }

}
