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

    /**
     * Location of the keystore file. It is created if it does not exist; an entry with the same alias is replaced.
     */
    @Parameter
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
     * Alias under which the key pair is stored.
     */
    @Parameter
    private String alias;

    /**
     * Key algorithm, for example <code>RSA</code>, <code>DSA</code> or <code>EC</code>.
     */
    @Parameter(defaultValue = "RSA")
    private String keyalg;

    /**
     * Key size in bits, for example <code>2048</code> for RSA or <code>256</code> for EC.
     */
    @Parameter(defaultValue = "2048")
    private String keysize;

    /**
     * Password of the private key. Defaults to the keystore password.
     */
    @Parameter
    private String keypass;

    /**
     * Signature algorithm of the self-signed certificate. Defaults to <code>SHA256withECDSA</code> for <code>EC</code> keys, <code>SHA256WithDSA</code> for <code>DSA</code> keys and <code>SHA256WithRSA</code> otherwise.
     */
    @Parameter
    private String sigalg;

    /**
     * Distinguished name of the certificate subject and issuer, for example <code>CN=localhost, O=Example</code>.
     */
    @Parameter
    private String dname;

    /**
     * Validity of the certificate in days, counted from the time of the build.
     */
    @Parameter(defaultValue = "90")
    private String validity;

    /**
     * Certificate extensions, one <code>&lt;ext&gt;</code> element each, for example <code>san=dns:localhost,ip:127.0.0.1</code>, <code>bc=ca:true</code> or <code>ku=digitalSignature,keyCertSign</code>. The supported types are <code>san</code> (<code>subjectAlternativeName</code>), <code>ian</code> (<code>issuerAlternativeName</code>), <code>bc</code> (basic constraints) and <code>ku</code> (<code>keyUsage</code>); other types are ignored with a warning.
     */
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
