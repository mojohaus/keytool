package org.codehaus.mojo.keytool;

/*
 * Copyright 2005-2013 The Codehaus
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * To delete an entry alias from a keystore.
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -delete} command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.2
 */
@Mojo( name = "deleteAlias",
       requiresProject = true,
       threadSafe = true )
public class DeleteAliasMojo
        extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo
{

    /**
     * Default contructor.
     */
    public DeleteAliasMojo()
    {
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        // TODO: implement
        throw new UnsupportedOperationException(
                "not yet implemented: [org.codehaus.mojo.keytool.DeleteAliasMojo::execute]." );
    }

    private void deleteAlias() throws KeyStoreException, MojoFailureException
    {
        final KeyStore keyStore = KeyStore.getInstance( getStoretype() );

        try ( InputStream ksIn = Files.newInputStream( getKeystoreFile().toPath() ) )
        {
            keyStore.load( ksIn, getStorepass().toCharArray() );
        }
        catch ( IOException ioException )
        {
            throw new MojoFailureException( "Unable to open file [" + keyStore + "].", ioException );
        }
        catch ( CertificateException | NoSuchAlgorithmException keystoreLoadException )
        {
            throw new MojoFailureException( "Unable to load keystore [" + getKeystoreFile().getAbsolutePath() + "].",
                    keystoreLoadException );
        }

        try
        {
            keyStore.deleteEntry( getAlias() );
        }
        catch ( KeyStoreException kse )
        {
            throw new MojoFailureException( "Unable to delete alias [" + getAlias() + "] from Keystore.", kse );
        }
    }
}
