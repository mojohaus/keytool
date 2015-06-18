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
 * Test the {@link org.codehaus.mojo.keytool.requests.KeyToolChangeAliasRequest}.
 *
 * @author tchemit <chemit@codelutin.com>
 * @since 1.1
 */
public abstract class AbstractKeyToolChangeAliasRequestIT
    extends AbstractKeyToolRequestIT<KeyToolChangeAliasRequest>
{

    protected AbstractKeyToolChangeAliasRequestIT()
    {
    }

    protected AbstractKeyToolChangeAliasRequestIT( boolean supportedRequest )
    {
        super( supportedRequest );
    }

    @Override
    public final void testRequest()
        throws Exception
    {

        File keyStore = resourceFixtures.simpleKeyStore();

        KeyToolChangeAliasRequest request = requestFixtures.createKeyToolChangeAliasRequest( keyStore );

        JavaToolResult keyToolResult = consumeRequest( request );

        requestResult( keyToolResult, keyStore );
    }

    protected abstract void requestResult( JavaToolResult keyToolResult, File keyStore );
}
