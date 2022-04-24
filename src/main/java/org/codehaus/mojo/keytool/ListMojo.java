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
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.Locale;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * To list entries in a keystore.
 * <p/>
 * Implemented as a wrapper around the SDK {@code keytool -list} (jdk 1.5) command.
 * <p/>
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.2
 */
@Mojo( name = "list", requiresProject = true, threadSafe = true )
public class ListMojo
        extends AbstractKeyToolRequestWithKeyStoreParametersMojo
{
    /**
     * Output in RFC style.
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean rfc;

    /**
     * Default contructor.
     */
    public ListMojo()
    {
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            printKeyStore();
        }
        catch ( KeyStoreException kse )
        {
            throw new MojoExecutionException( "Error while printing keystore information.", kse );
        }

    }

    private void printKeyStore() throws KeyStoreException, MojoFailureException
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

        final Enumeration<String> aliases = keyStore.aliases();

        for ( String alias = aliases.nextElement(); aliases.hasMoreElements(); )
        {
            printAlias( keyStore, alias );
        }
    }

    private void printAlias( KeyStore keyStore, String alias ) throws KeyStoreException
    {
        final Certificate certificate = keyStore.getCertificate( alias );
        final String entry = String.format( Locale.ROOT, "%s:%s",
                alias, certificate.getType() );

        System.out.println( entry );
    }
}
