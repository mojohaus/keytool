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

import org.apache.maven.shared.utils.cli.javatool.JavaToolRequest;

import java.io.File;


/**
 * Specifies the common parameters used to control a KeyTool tool invocation.
 *
 * @author tchemit
 * @since 1.1
 */
public interface KeyToolRequest extends JavaToolRequest
{

    /**
     * Gets the value of the {@code verbose} field.
     *
     * @return the value of the {@code verbose} field.
     */
    boolean isVerbose();

    /**
     * Gets the value of the {@code maxMemory} field.
     *
     * @return the value of the {@code maxMemory} field.
     */
    String[] getArguments();

    /**
     * Gets the value of the {@code workingDirectory} field.
     *
     * @return the value of the {@code workingDirectory} field.
     */
    File getWorkingDirectory();

    /**
     * Sets the new given value to the field {@code verbose} of the request.
     *
     * @param verbose the new value of the field {@code verbose}.
     */
    void setVerbose( boolean verbose );

    /**
     * Sets the new given value to the field {@code arguments} of the request.
     *
     * @param arguments the new value of the field {@code arguments}.
     */
    void setArguments( String[] arguments );

    /**
     * Sets the new given value to the field {@code workingDirectory} of the request.
     *
     * @param workingDirectory the new value of the field {@code workingDirectory}.
     */
    void setWorkingDirectory( File workingDirectory );
}
