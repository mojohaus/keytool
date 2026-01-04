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
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.keytool.services.CertificateGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To generate a key pair into a keystore.
 * Uses Bouncy Castle for certificate generation.
 *
 * @author tchemit
 * @since 1.0
 */
@Mojo(name = "generateKeyPair", threadSafe = true)
public class GenerateKeyPairMojo extends AbstractKeyToolMojo {

    private static final Logger log = LoggerFactory.getLogger(GenerateKeyPairMojo.class);

    @Parameter
    private File keystore;

    @Parameter
    private String storetype;

    @Parameter
    private String storepass;

    @Parameter
    private String alias;

    @Parameter(defaultValue = "RSA")
    private String keyalg;

    @Parameter(defaultValue = "2048")
    private String keysize;

    @Parameter
    private String keypass;

    @Parameter
    private String sigalg;

    @Parameter
    private String dname;

    @Parameter(defaultValue = "90")
    private String validity;

    @Parameter
    private List<String> exts;

    @Inject
    private CertificateGenerationService certGenService;

    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            log.info("Skipping execution");
            return;
        }

        try {
            int keySizeInt = Integer.parseInt(keysize != null ? keysize : "2048");
            int validityInt = Integer.parseInt(validity != null ? validity : "90");

            List<String> extensions = exts != null && !exts.isEmpty() ? exts : null;

            certGenService.generateKeyPair(
                    keystore,
                    storetype,
                    storepass != null ? storepass.toCharArray() : null,
                    alias,
                    keyalg != null ? keyalg : "RSA",
                    keySizeInt,
                    sigalg,
                    dname,
                    validityInt,
                    keypass != null ? keypass.toCharArray() : null,
                    extensions);

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate key pair", e);
        }
    }
}
