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

import org.apache.commons.lang.SystemUtils;

import java.io.File;

/**
 * Util methods for keytool.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.1
 */
public class KeyToolUtil
{
    /**
     * Constructs the operating system specific File path of the JRE cacerts file.
     *
     * @return a File representing the path to the command.
     */
    public static File getJRECACerts()
    {

        File cacertsFile;

        String cacertsFilepath = "lib" + File.separator + "security" + File.separator + "cacerts";

        // For IBM's JDK 1.2
        if ( SystemUtils.IS_OS_AIX )
        {
            cacertsFile = new File( SystemUtils.getJavaHome() + "/", cacertsFilepath );
        }
        else if ( SystemUtils.IS_OS_MAC_OSX ) // what about IS_OS_MAC_OS ??
        {
            cacertsFile = new File( SystemUtils.getJavaHome() + "/", cacertsFilepath );
        }
        else
        {
            cacertsFile = new File( SystemUtils.getJavaHome() + "/", cacertsFilepath );
        }

        return cacertsFile;
    }

    /**
     * Prevent instanciation of util class.
     */
    private KeyToolUtil()
    {
    }
}
