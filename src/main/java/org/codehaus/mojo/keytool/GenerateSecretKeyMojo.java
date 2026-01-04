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

import javax.inject.Inject;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.keytool.services.SecretKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To generate a secret key into a keystore.
 * Uses Java KeyStore API directly.
 * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">keystore documentation</a>.
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "generateSecretKey", threadSafe = true)
public class GenerateSecretKeyMojo extends AbstractKeyToolMojo {

    /**
     * Keystore location.
     */
    @Parameter
    private File keystore;

    /**
     * Keystore type.
     */
    @Parameter
    private String storetype;

    /**
     * Keystore password.
     */
    @Parameter
    private String storepass;

    /**
     * Alias.
     */
    @Parameter
    private String alias;

    /**
     * Key algorithm name.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keyalg;

    /**
     * Key bit size.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keysize;

    /**
     * Key password.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keypass;

    private static final Logger log = LoggerFactory.getLogger(GenerateSecretKeyMojo.class);

    @Inject
    private SecretKeyService secretKeyService;

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            log.info("Skipping execution");
            return;
        }

        secretKeyService.generateSecretKey(keystore, storetype, storepass, alias, keyalg, keysize, keypass);
    }
}
