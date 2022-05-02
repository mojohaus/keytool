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

import org.apache.maven.shared.utils.cli.javatool.JavaToolResult;

import java.io.File;

/**
 * Test the {@link KeyToolGenerateSecretKeyRequest}.
 *
 * @author tchemit
 * @since 1.1
 */
public class KeyToolGenerateSecretKeyRequestIT
    extends AbstractKeyToolGenerateSecretKeyRequestIT
{

    @Override
    protected void requestResult( JavaToolResult keyToolResult, File keyStore )
    {
        assertKeyToolResult( keyToolResult,
                             new String[]{ "-genseckey", "-v", "-keystore", keyStore.getAbsolutePath(), "-storepass",
                                 "changeit", "-storetype", "jks", "-alias", "new_foo_alias", "-keypass", "key-passwd",
                                 "-keyalg", "DES", "-keysize", "56" }, 1 );
        //FIXME tchemit 2011-11-06 Can not generate in this keystore a non private key, make this works
    }

}
