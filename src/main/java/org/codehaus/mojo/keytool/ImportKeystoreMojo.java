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
import org.codehaus.mojo.keytool.services.KeyStoreManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To import all entries of a keystore to another keystore.
 * Uses Java KeyStore API directly.
 * <strong>Note</strong> This operation was not implemented by the keytool before jdk 1.6.
 * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">keytool documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "importKeystore", threadSafe = true)
public class ImportKeystoreMojo extends AbstractKeyToolMojo {

    private static final Logger log = LoggerFactory.getLogger(ImportKeystoreMojo.class);

    @Inject
    private KeyStoreManagementService service;

    /**
     * Source keystore name.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srckeystore;

    /**
     * Destination keystore name.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destkeystore;

    /**
     * Source keystore type.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srcstoretype;

    /**
     * Destination keystore type.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String deststoretype;

    /**
     * Source keystore password.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srcstorepass;

    /**
     * Destination keystore password.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String deststorepass;

    /**
     * Source keystore password protected.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean srcprotected;

    /**
     * Source keystore provider name.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srcprovidername;

    /**
     * Destination keystore provider name.
     * See <a hresf="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destprovidername;

    /**
     * Source alias.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srcalias;

    /**
     * Destination alias.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destalias;

    /**
     * Source key password.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String srckeypass;

    /**
     * Destination key password.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String destkeypass;

    /**
     * Do not prompt.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean noprompt;

    /**
     * Provider class name.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providerclass;

    /**
     * Provider argument.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providerarg;

    /**
     * Provider classpath.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providerpath;

    /**
     * If value is {@code true}, then will do nothing if keystore already exists.
     *
     * @since 1.3
     */
    @Parameter
    private boolean skipIfExist;

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            log.info("Skipping execution");
            return;
        }

        if (skipIfExist) {
            // check if keystore already exist
            File destinationKeystoreFile = new File(destkeystore);
            boolean keystoreFileExists = destinationKeystoreFile.exists();

            if (keystoreFileExists) {
                log.info("Skip execution, keystore already exists at {}", destinationKeystoreFile);
                return;
            }
        }

        try {
            // Using injected service
            service.importKeystore(
                    new File(srckeystore),
                    srcstoretype,
                    srcstorepass != null ? srcstorepass.toCharArray() : null,
                    new File(destkeystore),
                    deststoretype,
                    deststorepass != null ? deststorepass.toCharArray() : null,
                    srcalias,
                    destalias,
                    srckeypass != null ? srckeypass.toCharArray() : null,
                    destkeypass != null ? destkeypass.toCharArray() : null);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to import keystore", e);
        }
    }
}
