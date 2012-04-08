package org.codehaus.mojo.keytool;

/*
 * Copyright 2005-2012 The Codehaus
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

import java.io.File;

/**
 * A Mojo that deletes a generated keystore file.
 *
 * @author Sharmarke Aden (<a href="mailto:saden1@gmail.com">saden</a>)
 * @author $Author$
 * @version $Revision$
 * @goal clean
 * @phase clean
 */
public class CleanKeyMojo
    extends AbstractKeyToolMojo
{

    /**
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @parameter expression="${keystore}"
     */
    private String keystore;

    /**
     * @return Returns the keystore.
     */
    public final String getKeystore()
    {
        return this.keystore;
    }

    /**
     * @param keystore The keystore to set.
     */
    public final void setKeystore( String keystore )
    {
        this.keystore = keystore;
    }

    /**
     * {@inheritDoc}
     */
    public void execute()
    {
        if ( isSkip() )
        {
            getLog().info( getMessage( "disabled", null ) );
        }
        else
        {
            File keystoreFile = new File( this.getKeystore() );
            if ( keystoreFile.delete() )
            {
                getLog().info( "Keystore file '" + keystoreFile + "' deleted successfully." );
            }
            else
            {
                getLog().warn( "Keystore file '" + keystoreFile + "' doesn't exist." );
            }
        }
    }
}
