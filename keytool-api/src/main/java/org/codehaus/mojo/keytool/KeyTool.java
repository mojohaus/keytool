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

/**
 * Provides a facade to invoke KeyTool tool.
 *
 * @author tchemit <chemit@codelutin.com>
 * @version $Id$
 * @since 1.1
 */
public interface KeyTool
{

    /**
     * Plexus role name.
     */
    String ROLE = KeyTool.class.getName();

    /**
     * Executes KeyTool tool using the parameters specified by the given invocation request.
     *
     * @param request The invocation request to execute, must not be <code>null</code>.
     * @return The result of the KeyTool invocation, never <code>null</code>.
     * @throws KeyToolException if something fails while init the command
     */
    KeyToolResult execute( KeyToolRequest request )
        throws KeyToolException;
}
