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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Abstract class that contains fields/methods common to KeyTool Mojo classes.
 *
 * @author Sharmarke Aden (<a href="mailto:saden1@gmail.com">saden</a>)
 * @author $Author$
 * @version $Revision$
 */
public abstract class AbstractKeyToolMojo
    extends AbstractMojo
{

    /**
     * Set to {@code true} to disable the plugin.
     *
     * @since 1.1
     */
    @Parameter( defaultValue = "false" )
    private boolean skip;

    /**
     * Enable verbose mode (in mojo and in keytool command).
     * <p/>
     * See <a href="http://docs.oracle.com/javase/1.5.0/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     */
    @Parameter( defaultValue = "false" )
    private boolean verbose;

    /**
     * @return value of the {@link #skip} flag
     */
    public final boolean isSkip()
    {
        return skip;
    }

    /**
     * @param skip the skip flag value to set.
     */
    public final void setSkip( boolean skip )
    {
        this.skip = skip;
    }

    /**
     * @return value of the {@link #verbose} flag
     */
    public final boolean isVerbose()
    {
        return verbose;
    }

    /**
     * @param verbose the verbose flag value to set.
     */
    public final void setVerbose( boolean verbose )
    {
        this.verbose = verbose;
    }

    /**
     * Gets a message for a given key from the resource bundle backing the implementation.
     *
     * @param key  The key of the message to return (cano not be null).
     * @param args Arguments to format the message with or {@code null}.
     * @return The message with key {@code key} from the resource bundle backing the implementation.
     */
    private String getMessage( final String key, final Object[] args )
    {
        if ( key == null )
        {
            throw new NullPointerException( "key" );
        }

        return new MessageFormat( ResourceBundle.getBundle( "keytool" ).getString( key ) ).format( args );
    }

    /**
     * Gets a message for a given key from the resource bundle backing the implementation.
     *
     * @param key The key of the message to return.
     * @return The message with key {@code key} from the resource bundle backing the implementation.
     */
    protected String getMessage( final String key )
    {
        return getMessage( key, new Object[]{ } );
    }

    /**
     * Gets a message for a given key and the given parameter from the resource bundle backing the implementation.
     *
     * @param key The key of the message to return.
     * @param arg argument of the sentence to translate
     * @return The message with key {@code key} from the resource bundle backing the implementation.
     */
    protected String getMessage( final String key, final Object arg )
    {
        return getMessage( key, new Object[]{ arg } );
    }

    /**
     * Gets a message for a given key and the given parameters from the resource bundle backing the implementation.
     *
     * @param key  The key of the message to return.
     * @param arg1 first argument of the sentence to translate
     * @param arg2 second argument of the sentence to translate
     * @return The message with key {@code key} from the resource bundle backing the implementation.
     */
    protected String getMessage( final String key, final Object arg1, final Object arg2 )
    {
        return getMessage( key, new Object[]{ arg1, arg2 } );
    }
}
