package org.codehaus.mojo.keytool;

/*
 * Copyright 2005-2011 The Codehaus
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

import junit.framework.TestCase;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;

/**
 * Unit Test class that test the removal of an existing and non existing
 * keystore file.
 *
 * @author Sharmarke Aden (<a href="mailto:saden1@gmail.com">saden</a>)
 * @author $Author$
 * @version $Revision$
 */
public class CleanKeyMojoTest
    extends TestCase
{

    private static final String EXISTING_TEST_KEYSTORE = ".keystore";

    private static final String NON_EXISTING_TEST_KEYSTORE = ".keystoreZXCVBNNM";

    private CleanKeyMojo mojo;

    protected void setUp()
        throws Exception
    {
        mojo = new CleanKeyMojo();

    }

    public void tearDown()
    {
        mojo = null;
    }

    /**
     * Remove the temp keystore file created in the setup.
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     * @throws IOException
     */
    public void testExistingRemoveKeystore()
        throws MojoExecutionException, MojoFailureException, IOException
    {
        String tempKeystore =
            System.getProperty( "java.io.tmpdir" ) + File.separator + CleanKeyMojoTest.EXISTING_TEST_KEYSTORE;
        File tempKeystoreFile = new File( tempKeystore );
        tempKeystoreFile.createNewFile();
        mojo.setKeystore( tempKeystore );
        mojo.execute();

    }

    /**
     * Remove the temp keystore file created in the setup. existent
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    public void testRemoveNonExistentKeystore()
        throws MojoExecutionException, MojoFailureException
    {
        mojo.setKeystore( CleanKeyMojoTest.NON_EXISTING_TEST_KEYSTORE );
        mojo.execute();
    }

}
