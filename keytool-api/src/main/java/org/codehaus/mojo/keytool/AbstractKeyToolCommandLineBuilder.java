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

import org.apache.commons.lang3.SystemUtils;
import org.apache.maven.shared.utils.cli.Commandline;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;

/**
 * To build the command line for a given {@link org.codehaus.mojo.keytool.KeyToolRequest}.
 *
 * @author tchemit
 * @since 1.1
 */
public abstract class AbstractKeyToolCommandLineBuilder implements KeyToolCommandLineBuilder {

    /**
     * Builder logger.
     */
    private Logger logger;

    /**
     * Keytool executable location.
     */
    private String keyToolFile;

    /** {@inheritDoc} */
    public final void setLogger(Logger logger) {
        this.logger = logger;
    }

    /** {@inheritDoc} */
    public final void setKeyToolFile(String keyToolFile) {
        this.keyToolFile = keyToolFile;
    }

    /**
     * {@inheritDoc}
     */
    public final void checkRequiredState() {
        if (logger == null) {
            throw new IllegalStateException("A logger instance is required.");
        }

        if (keyToolFile == null) {
            throw new IllegalStateException("A keyTool file is required.");
        }
    }

    /** {@inheritDoc} */
    public final void checkSupportedRequest(KeyToolRequest request) throws UnsupportedKeyToolRequestException {
        Class<? extends KeyToolRequest> requestType = request.getClass();
        if (!supportRequestType(requestType)) {
            throw new UnsupportedKeyToolRequestException(
                    "Request " + requestType.getName() + " is not supported by your version of keytool (java "
                            + SystemUtils.JAVA_VERSION
                            + ")");
        }
    }

    /**
     * Get the builder logger.
     *
     * @return the builder logger
     */
    protected final Logger getLogger() {
        return logger;
    }

    /**
     * Get the builder keytool program location.
     *
     * @return the builder keytool program location
     */
    protected final String getKeyToolFile() {
        return keyToolFile;
    }

    /**
     * Fill the commandline client with keytool command, optional verbose option and common options from the given
     * request.
     *
     * @param cli            the commandline client to prepare
     * @param keytoolcommand keytool command option to pass to keytool programm
     * @param request        the keytool request to consume
     * @see KeyToolRequestWithKeyStoreParameters
     * @see KeyToolRequestWithKeyStoreAndAliasParameters
     */
    protected final void addKeytoolCommandAndDefaultoptions(
            Commandline cli, String keytoolcommand, KeyToolRequest request) {
        addArg(cli, keytoolcommand);
        addArgIfTrue(cli, "-v", request.isVerbose());
        if (request instanceof KeyToolRequestWithKeyStoreParameters) {
            buildWithKeyStoreParameters((KeyToolRequestWithKeyStoreParameters) request, cli);
        }
        if (request instanceof KeyToolRequestWithKeyStoreAndAliasParameters) {
            buildWithKeyStoreAndAliasParameters((KeyToolRequestWithKeyStoreAndAliasParameters) request, cli);
        }
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool import request
     * @param cli     the commandline client to prepare
     */
    protected void buildWithKeyStoreParameters(KeyToolRequestWithKeyStoreParameters request, Commandline cli) {
        addArgIfNotEmpty(cli, "-keystore", request.getKeystore());
        addArgIfNotEmpty(cli, "-storepass", request.getStorepass());
        addArgIfNotEmpty(cli, "-storetype", request.getStoretype());
        addArgIfNotEmpty(cli, "-providername", request.getProvidername());
        addArgIfNotEmpty(cli, "-providerclass", request.getProviderclass());
        addArgIfNotEmpty(cli, "-providerarg", request.getProviderarg());
        addArgIfNotEmpty(cli, "-providerpath", request.getProviderpath());
    }

    /**
     * Fill the commandline client for the given {@code request}.
     *
     * @param request the keytool import request
     * @param cli     the commandline client to prepare
     */
    protected void buildWithKeyStoreAndAliasParameters(
            KeyToolRequestWithKeyStoreAndAliasParameters request, Commandline cli) {
        addArgIfNotEmpty(cli, "-protected", request.isPasswordProtected() ? Boolean.TRUE.toString() : "");
        addArgIfNotEmpty(cli, "-alias", request.getAlias());
    }

    /**
     * Convenience method to add an argument to the <code>command line</code> if the the value is not null or empty.
     *
     * @param cli   command line to fill
     * @param key   the argument name.
     * @param value the argument value to be added.
     */
    protected final void addArgIfNotEmpty(Commandline cli, String key, String value) {
        if (!StringUtils.isEmpty(value)) {
            addArg(cli, key);
            addArg(cli, value);
        }
    }

    /**
     * Convenience method to add repeated arguments to the <code>command line</code> for each value that is not null or empty.
     *
     * @param cli    command line to fill
     * @param key    the argument name.
     * @param values the argument values to be added.
     * @since 1.6
     */
    protected final void addArgsIfNotEmpty(Commandline cli, String key, List<String> values) {
        if (values != null) {
            for (String value : values) {
                addArgIfNotEmpty(cli, key, value);
            }
        }
    }

    /**
     * Convenience method to add an argument to the <code>command line</code> if the the value is not null or empty.
     *
     * @param cli   command line to fill
     * @param key   the argument name.
     * @param value the argument value to be added.
     */
    protected final void addArgIfNotEmpty(Commandline cli, String key, File value) {
        if (value != null) {
            addArg(cli, key);
            addArg(cli, value);
        }
    }

    /**
     * Convenience method to add an argument to the <code>command line</code> if the the value is true.
     *
     * @param cli   command line to fill
     * @param key   the argument name.
     * @param value the argument value to be test.
     */
    protected final void addArgIfTrue(Commandline cli, String key, boolean value) {
        if (value) {
            addArg(cli, key);
        }
    }

    /**
     * Convinience method to add an argument to the {@code command line}.
     *
     * @param cli   command line to fill
     * @param value the argument value to be added
     */
    protected final void addArg(Commandline cli, String value) {
        cli.createArg().setValue(value);
    }

    /**
     * Convinience method to add a file argument to the {@code command line}.
     *
     * @param cli   command line to fill
     * @param value the file argument value to be added
     */
    protected final void addArg(Commandline cli, File value) {
        cli.createArg().setFile(value);
    }
}
