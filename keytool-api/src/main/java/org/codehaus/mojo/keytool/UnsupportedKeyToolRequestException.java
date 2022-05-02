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

import org.apache.maven.shared.utils.cli.javatool.JavaToolException;

/**
 * Signals that a request can not be consumed by the underlined keytool implementation.
 *
 * @author tchemit
 * @since 1.3
 */
public class UnsupportedKeyToolRequestException
    extends JavaToolException
{
    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for UnsupportedKeyToolRequestException.</p>
     *
     * @param message a {@link java.lang.String} object
     * @param cause a {@link java.lang.Throwable} object
     */
    public UnsupportedKeyToolRequestException( String message, Throwable cause )
    {
        super( message, cause );
    }

    /**
     * <p>Constructor for UnsupportedKeyToolRequestException.</p>
     *
     * @param message a {@link java.lang.String} object
     */
    public UnsupportedKeyToolRequestException( String message )
    {
        super( message );
    }
}
