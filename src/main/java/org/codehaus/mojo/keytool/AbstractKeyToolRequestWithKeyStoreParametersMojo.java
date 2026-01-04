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
import java.net.MalformedURLException;

import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.utils.cli.Commandline;
import org.codehaus.mojo.keytool.api.*;
import org.codehaus.plexus.util.StringUtils;

/**
 * Abstract mojo to execute a {@link KeyToolRequestWithKeyStoreParameters} request.
 *
 * @param <R> generic type of request used by the mojo
 * @author tchemit
 * @since 1.2
 */
public abstract class AbstractKeyToolRequestWithKeyStoreParametersMojo<R extends KeyToolRequestWithKeyStoreParameters>
        extends AbstractKeyToolRequestMojo<R> {

    /**
     * Keystore location.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    @Parameter
    private String keystore;

    /**
     * Keystore type.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    @Parameter
    private String storetype;

    /**
     * Keystore password.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    @Parameter(alias = "storepass")
    private String storepass;

    /**
     * Provider name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providername;

    /**
     * Provider class name.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providerclass;

    /**
     * Provider argument.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providerarg;

    /**
     * Provider classpath.
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     *
     * @since 1.2
     */
    @Parameter
    private String providerpath;

    /**
     * Constructor of abstract mojo.
     *
     * @param requestType type of keytool request used by the mojo
     */
    public AbstractKeyToolRequestWithKeyStoreParametersMojo(Class<R> requestType) {
        super(requestType);
    }

    /** {@inheritDoc} */
    @Override
    protected R createKeytoolRequest() {
        R request = super.createKeytoolRequest();

        if (StringUtils.isNotEmpty(keystore)) {

            File file = getFile(keystore);

            // make sure the parent directory of the keystore exists

            boolean mkdirs = file.getParentFile().mkdirs();
            getLog().debug("mdkirs: " + mkdirs + " " + file.getParentFile());

            // force to not use this parameter
            request.setKeystore(file.getAbsolutePath());
        }

        request.setProviderarg(providerarg);
        request.setProviderclass(providerclass);
        request.setProvidername(providername);
        request.setProviderpath(providerpath);
        request.setStorepass(storepass);
        request.setStoretype(storetype);
        return request;
    }

    /** {@inheritDoc} */
    @Override
    protected String getCommandlineInfo(final Commandline commandLine) {
        String commandLineInfo = super.getCommandlineInfo(commandLine);

        commandLineInfo = StringUtils.replace(commandLineInfo, this.storepass, "'*****'");

        return commandLineInfo;
    }

    /**
     * Create the parent directory of the given file location.
     *
     * @param file file location to check
     */
    protected final void createParentDirIfNecessary(final String file) {
        if (file != null) {
            final File fileDir = new File(file).getParentFile();

            if (fileDir != null) {
                // not a relative path
                boolean mkdirs = fileDir.mkdirs();
                getLog().debug("mdkirs: " + mkdirs + " " + fileDir);
            }
        }
    }

    /**
     * <p>getFile.</p>
     *
     * @param path a {@link java.lang.String} object
     * @return a {@link java.io.File} object
     */
    protected File getFile(String path) {

        try {
            return new File(new File(path).toURL().getFile());
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Could not obtain directory " + path);
        }
    }

    /**
     * <p>getKeystoreFile.</p>
     *
     * @return a {@link java.io.File} object
     */
    protected File getKeystoreFile() {
        return getFile(keystore);
    }
}
