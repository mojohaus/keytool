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
 * To delete an entry alias from a keystore using Java KeyStore API.
 * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">keytool documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "deleteAlias", threadSafe = true)
public class DeleteAliasMojo extends AbstractKeyToolMojo {

    private static final Logger log = LoggerFactory.getLogger(DeleteAliasMojo.class);

    @Inject
    private CertificateManagementService service;

    /**
     * Location of the keystore file. The entry is deleted in place.
     */
    @Parameter(defaultValue = "${project.build.directory}/keystore", required = true)
    private File keystore;

    /**
     * Keystore type, for example <code>JKS</code> or <code>PKCS12</code>. Defaults to the default keystore type of the running JVM (<code>java.security.KeyStore.getDefaultType()</code>).
     */
    @Parameter
    private String storetype;

    /**
     * Password of the keystore.
     */
    @Parameter
    private String storepass;

    /**
     * Alias of the entry to delete. If the keystore has no such alias, a warning is logged and nothing changes.
     */
    @Parameter(required = true)
    private String alias;

    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            log.info(getMessage("disabled"));
            return;
        }

        try {
            if (alias == null || alias.isEmpty()) {
                throw new MojoExecutionException("Alias is required");
            }

            char[] password = storepass != null ? storepass.toCharArray() : null;

            service.deleteAlias(keystore, storetype, password, alias);
            service.deleteAlias(keystore, storetype, password, alias);

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to delete alias: " + e.getMessage(), e);
        }
    }
}
