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
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.utils.cli.Commandline;
import org.codehaus.mojo.keytool.api.*;
import org.codehaus.mojo.keytool.api.requests.KeyToolGenerateCertificateRequestRequest;
import org.codehaus.plexus.util.StringUtils;

/**
 * To generate certificate request.
 * Implemented as a wrapper around the SDK {@code keytool -certreq} command.
 * See <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/windows/keytool.html">keystore documentation</a>.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "generateCertificateRequest", requiresProject = true, threadSafe = true)
public class GenerateCertificateRequestMojo
        extends AbstractKeyToolRequestWithKeyStoreAndAliasParametersMojo<KeyToolGenerateCertificateRequestRequest> {
    /**
     * Key password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String keypass;

    /**
     * Output file name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private File file;

    /**
     * Signature algorithm name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String sigalg;

    /**
     * X.509 extension.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.6
     *
     * @deprecated Use {@link #exts instead}.
     */
    @Deprecated
    @Parameter
    private String ext;

    /**
     * X.509 extension.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.6
     */
    @Parameter
    private List<String> exts;

    /**
     * Distinguished name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String dname;

    /**
     * Default contructor.
     */
    public GenerateCertificateRequestMojo() {
        super(KeyToolGenerateCertificateRequestRequest.class);
    }

    /** {@inheritDoc} */
    @Override
    public void execute() throws MojoExecutionException {
        createParentDirIfNecessary(this.file.getPath());
        super.execute();
    }

    /** {@inheritDoc} */
    @Override
    protected KeyToolGenerateCertificateRequestRequest createKeytoolRequest() {
        KeyToolGenerateCertificateRequestRequest request = super.createKeytoolRequest();

        request.setSigalg(this.sigalg);
        if (this.exts != null && !this.exts.isEmpty()) {
            request.setExts(exts);
        } else {
            request.setExt(ext);
        }
        request.setDname(this.dname);
        request.setFile(this.file);
        request.setKeypass(this.keypass);
        return request;
    }

    /** {@inheritDoc} */
    @Override
    protected String getCommandlineInfo(Commandline commandLine) {
        String commandLineInfo = super.getCommandlineInfo(commandLine);

        commandLineInfo = StringUtils.replace(commandLineInfo, this.keypass, "'*****'");

        return commandLineInfo;
    }
}
