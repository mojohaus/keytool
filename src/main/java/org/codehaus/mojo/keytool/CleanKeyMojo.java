/*
 * Copyright 2001-2006 The Apache Software Foundation.
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

package org.codehaus.mojo.keytool;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * A Mojo that deletes a generated keystore file.
 * 
 * @author Sharmarke Aden (<a href="mailto:saden1@gmail.com">saden</a>)
 * 
 * @author $Author$
 * @version $Revision$
 * 
 * @goal clean
 * @phase clean
 */
public class CleanKeyMojo extends AbstractKeyToolMojo {

    /**
     * Remove the keystore file from the working directory. If it doesn't exist,
     * do nothing?
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        File keystoreFile = new File(this.keystore);
        if (keystoreFile.delete()) {
            getLog().info(
                    "Keystore file '" + this.keystore
                            + "' deleted successfully.");
        } else {
            getLog().warn(
                    "Keystore file '" + this.keystore + "' doesn't exist.");
        }

    }
}
