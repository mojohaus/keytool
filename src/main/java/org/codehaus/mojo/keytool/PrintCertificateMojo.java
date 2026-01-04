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
import org.codehaus.mojo.keytool.services.PrintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To print the content of a certificate.
 * Uses Java Certificate API directly.
 * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">keystore documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "printCertificate", threadSafe = true)
public class PrintCertificateMojo extends AbstractKeyToolMojo {

    private static final Logger log = LoggerFactory.getLogger(PrintCertificateMojo.class);

    /**
     * Output in RFC style.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private boolean rfc;

    /**
     * Input file name.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private File file;

    /**
     * SSL server host and port.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String sslserver;

    /**
     * Signed jar file.
     * See <a href="https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private File jarfile;

    @Inject
    private PrintService printService;

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            log.info("Skipping execution");
            return;
        }

        printService.printCertificate(file, jarfile, sslserver, rfc);
    }
}
