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
import org.codehaus.mojo.keytool.services.CertificateManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To import a certificate into a keystore using Java KeyStore API.
 * <p>
 * This Mojo has been refactored to use Java's KeyStore API directly instead of
 * executing external keytool command, providing better performance and error handling.
 * <p>
 * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">keytool documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "importCertificate", threadSafe = true)
public class ImportCertificateMojo extends AbstractKeyToolMojo {

    private static final Logger log = LoggerFactory.getLogger(ImportCertificateMojo.class);

    @Inject
    private CertificateManagementService service;

    /**
     * Key password.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keypass;

    /**
     * Input file name.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String file;

    /**
     * Do not prompt.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean noprompt;

    /**
     * Trust certificates from cacerts.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean trustcacerts;

    /**
     * If value is {@code true}, then will do nothing if keystore already exists.
     *
     * @since 1.3
     */
    @Parameter
    private boolean skipIfExist;

    /**
     * If value is {@code true}, skip the import silently if the alias already exists in the keystore.
     * This is useful when running multiple executions without clean, allowing idempotent builds.
     *
     * @since 1.8
     */
    @Parameter(defaultValue = "false")
    private boolean skipIfAliasExists;

    /**
     * Keystore file location.
     */
    @Parameter(defaultValue = "${project.build.directory}/keystore", required = true)
    private File keystore;

    /**
     * Keystore type (e.g., "JKS", "PKCS12").
     */
    @Parameter
    private String storetype;

    /**
     * Keystore password.
     */
    @Parameter
    private String storepass;

    /**
     * Key alias.
     */
    @Parameter(required = true)
    private String alias;

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {

        if (isSkip()) {
            log.info(getMessage("disabled"));
            return;
        }

        if (skipIfExist && keystore.exists()) {
            log.info("Skip execution, keystore already exists at {}", keystore);
            return;
        }
        try {
            if (file == null || file.isEmpty()) {
                throw new MojoExecutionException("Certificate file is required");
            }

            if (alias == null || alias.isEmpty()) {
                throw new MojoExecutionException("Alias is required");
            }

            File certFile = new File(file);
            if (!certFile.exists()) {
                throw new MojoExecutionException("Certificate file does not exist: " + certFile);
            }

            char[] password = (storepass != null) ? storepass.toCharArray() : null;
            char[] keyPassword = (keypass != null) ? keypass.toCharArray() : null;

            service.importCertificate(keystore, storetype, password, alias, certFile, skipIfAliasExists, keyPassword);

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to import certificate: " + e.getMessage(), e);
        }
    }
}
