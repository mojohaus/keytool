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
 * To change the key password of an entry of a keystore.
 * Uses Java KeyStore API directly.
 * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">keytool documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "changeKeyPassword", threadSafe = true)
public class ChangeKeyPasswordMojo extends AbstractKeyToolMojo {

    private static final Logger log = LoggerFactory.getLogger(ChangeKeyPasswordMojo.class);

    @Inject
    private KeyStoreManagementService service;

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
     * New password.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">keytool options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String newPassword;

    /**
     * Key password.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keypass;

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            log.info("Skipping execution");
            return;
        }

        try {
            // Using injected service
            service.changeKeyPassword(
                    keystore,
                    storetype,
                    storepass != null ? storepass.toCharArray() : null,
                    alias,
                    keypass != null ? keypass.toCharArray() : null,
                    newPassword != null ? newPassword.toCharArray() : null);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to change key password", e);
        }
    }
}
