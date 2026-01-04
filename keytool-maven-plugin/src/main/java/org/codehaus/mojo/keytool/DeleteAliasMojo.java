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

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Delete an entry alias from a keystore using Java KeyStore API.
 *
 * @author tchemit
 * @since 2.0
 */
@Mojo(name = "deleteAlias", requiresProject = true, threadSafe = true)
public class DeleteAliasMojo extends KeyStoreWithAliasMojo {

    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            getLog().info(getMessage("disabled"));
            return;
        }

        // Validate parameters
        if (alias == null || alias.isEmpty()) {
            throw new MojoExecutionException("Alias is required");
        }

        File keystoreFile = getKeystoreFile();

        if (!keystoreFile.exists()) {
            throw new MojoExecutionException("Keystore does not exist: " + keystoreFile);
        }

        KeyStoreService service = new KeyStoreService(getLog());
        service.deleteAlias(keystoreFile, storetype, getKeystorePassword(), alias);
    }
}
