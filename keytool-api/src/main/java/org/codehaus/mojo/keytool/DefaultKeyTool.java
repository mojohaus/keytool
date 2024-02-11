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

import org.apache.maven.shared.utils.cli.Commandline;
import org.apache.maven.shared.utils.cli.StreamConsumer;
import org.apache.maven.shared.utils.cli.javatool.AbstractJavaTool;
import org.apache.maven.shared.utils.cli.javatool.JavaToolException;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

/**
 * Default implementation of component {@link org.codehaus.mojo.keytool.KeyTool}.
 *
 * @author tchemit§
 * @since 1.1
 */
@Component(role = KeyTool.class, hint = "default")
public class DefaultKeyTool extends AbstractJavaTool<KeyToolRequest> implements KeyTool {
    /**
     * Command line builder.
     */
    @Requirement
    protected KeyToolCommandLineBuilder builder;

    /**
     * <p>Constructor for DefaultKeyTool.</p>
     */
    public DefaultKeyTool() {
        super("keytool");
    }

    /** {@inheritDoc} */
    @Override
    protected Commandline createCommandLine(KeyToolRequest request, String javaToolFile) throws JavaToolException {
        builder.setLogger(getLogger());
        builder.setKeyToolFile(javaToolFile);
        Commandline cli;
        try {
            cli = builder.build(request);
        } catch (CommandLineConfigurationException | UnsupportedKeyToolRequestException e) {
            throw new JavaToolException("Error configuring command-line. Reason: " + e.getMessage(), e);
        }
        if (request.isVerbose()) {
            getLogger().info(cli.toString());
        } else {
            getLogger().debug(cli.toString());
        }
        return cli;
    }

    /** {@inheritDoc} */
    @Override
    protected StreamConsumer createSystemOutStreamConsumer(KeyToolRequest request) {
        StreamConsumer systemOut = request.getSystemOutStreamConsumer();

        if (systemOut == null) {

            final boolean verbose = request.isVerbose();

            systemOut = new StreamConsumer() {

                /**
                 * {@inheritDoc}
                 */
                public void consumeLine(final String line) {
                    if (verbose) {
                        getLogger().info(line);
                    } else {
                        getLogger().debug(line);
                    }
                }
            };
        }
        return systemOut;
    }
}
