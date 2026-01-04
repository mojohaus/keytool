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
 * To list entries in a keystore using Java KeyStore API.
 * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">keytool documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "list", threadSafe = true)
public class ListMojo extends AbstractKeyToolMojo {

    private static final Logger log = LoggerFactory.getLogger(ListMojo.class);

    @Inject
    private KeyStoreManagementService service;

    @Parameter(defaultValue = "${project.build.directory}/keystore", required = true)
    private File keystore;

    @Parameter
    private String storetype;

    @Parameter
    private String storepass;

    @Parameter
    private String alias;

    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            log.info(getMessage("disabled"));
            return;
        }

        try {
            char[] password = (storepass != null) ? storepass.toCharArray() : null;

            // Using injected service
            service.listAliases(keystore, storetype, password, alias);

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to list keystore: " + e.getMessage(), e);
        }
    }
}
