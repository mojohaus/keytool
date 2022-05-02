package org.codehaus.mojo.keytool.requests;

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

import org.apache.maven.shared.utils.cli.javatool.AbstractJavaToolRequest;
import org.codehaus.mojo.keytool.KeyToolRequest;

import java.io.File;

/**
 * Specifies the commons parameters used to control a key tool invocation.
 *
 * @author tchemit
 * @since 1.1
 */
public abstract class AbstractKeyToolRequest
    extends AbstractJavaToolRequest
    implements KeyToolRequest
{

    /**
     * Verbose output.
     * See <a href="http://java.sun.com/javase/6/docs/technotes/tools/windows/keytool.html#Options">options</a>.
     */
    private boolean verbose;

    /**
     * List of additional arguments to append to the jarsigner command line.
     */
    private String[] arguments;

    /**
     * Location of the working directory.
     */
    private File workingDirectory;

    /**
     * {@inheritDoc}
     *
     * @return a boolean
     */
    public boolean isVerbose()
    {
        return verbose;
    }

    /**
     * {@inheritDoc}
     *
     * @return an array of {@link java.lang.String} objects
     */
    public String[] getArguments()
    {
        return arguments;
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@link java.io.File} object
     */
    public File getWorkingDirectory()
    {
        return workingDirectory;
    }

    /** {@inheritDoc} */
    public void setVerbose( boolean verbose )
    {
        this.verbose = verbose;
    }

    /**
     * {@inheritDoc}
     *
     * @param arguments an array of {@link java.lang.String} objects
     */
    public void setArguments( String[] arguments )
    {
        this.arguments = arguments;
    }

    /** {@inheritDoc} */
    public void setWorkingDirectory( File workingDirectory )
    {
        this.workingDirectory = workingDirectory;
    }
}
