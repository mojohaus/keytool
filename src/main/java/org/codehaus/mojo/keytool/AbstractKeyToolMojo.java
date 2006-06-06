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

import org.apache.maven.plugin.AbstractMojo;

/**
 * Abstract class that contains fields/methods common to KeyTool Mojo classes.
 * 
 * @author Sharmarke Aden (<a href="mailto:saden1@gmail.com">saden</a>)
 * 
 * @author $Author$
 * @version $Revision$
 */
public abstract class AbstractKeyToolMojo extends AbstractMojo {

    /**
     * Where to execute the keytool command.
     * 
     * @parameter expression="${workingdir}" default-value="${basedir}"
     * @required
     */
    protected File workingDirectory;

    /**
     * See <a
     * href="http://java.sun.com/j2se/1.4.2/docs/tooldocs/windows/keytool.html#Commands">options</a>.
     * 
     * @parameter expression="${keystore}"
     */
    protected String keystore;

    /**
     * @return Returns the keystore.
     */
    public String getKeystore() {
        return this.keystore;
    }

    /**
     * @param keystore
     *            The keystore to set.
     */
    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    /**
     * @return Returns the workingDirectory.
     */
    public File getWorkingDir() {
        return this.workingDirectory;
    }

    /**
     * @param workingDirectory
     *            The workingDirectory to set.
     */
    public void setWorkingDir(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

}
