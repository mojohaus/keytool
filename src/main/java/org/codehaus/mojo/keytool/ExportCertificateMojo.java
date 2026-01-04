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
 * To export a certificate from a keystore using Java KeyStore API.
 * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">keytool documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "exportCertificate", threadSafe = true)
public class ExportCertificateMojo extends AbstractKeyToolMojo {

    private static final Logger log = LoggerFactory.getLogger(ExportCertificateMojo.class);

    @Inject
    private CertificateManagementService service;

    @Parameter(defaultValue = "${project.build.directory}/keystore", required = true)
    private File keystore;

    @Parameter
    private String storetype;

    @Parameter
    private String storepass;

    @Parameter(required = true)
    private String alias;

    /**
     * Output file name.
     *
     * @since 1.2
     */
    @Parameter(required = true)
    private String file;

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            log.info(getMessage("disabled"));
            return;
        }

        createParentDirIfNecessary(file);

        try {
            if (alias == null || alias.isEmpty()) {
                throw new MojoExecutionException("Alias is required");
            }

            if (file == null || file.isEmpty()) {
                throw new MojoExecutionException("Output file is required");
            }

            char[] password = (storepass != null) ? storepass.toCharArray() : null;
            File outputFile = new File(file);

            service.exportCertificate(keystore, storetype, password, alias, outputFile);

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to export certificate: " + e.getMessage(), e);
        }
    }
}
