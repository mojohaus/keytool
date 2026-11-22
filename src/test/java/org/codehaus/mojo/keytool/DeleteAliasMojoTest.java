package org.codehaus.mojo.keytool;

/*
 * Copyright MojoHaus and Contributors
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

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.mojo.keytool.services.CertificateManagementService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks that {@link DeleteAliasMojo} asks the service to delete the alias exactly once.
 */
class DeleteAliasMojoTest {

    private static class RecordingService extends CertificateManagementService {

        final List<String> deletedAliases = new ArrayList<>();

        @Override
        public void deleteAlias(File keystoreFile, String keystoreType, char[] keystorePassword, String alias) {
            deletedAliases.add(alias);
        }
    }

    private static void set(DeleteAliasMojo mojo, String name, Object value) throws Exception {
        Field field = DeleteAliasMojo.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(mojo, value);
    }

    private static DeleteAliasMojo mojo(RecordingService service, String alias) throws Exception {
        DeleteAliasMojo mojo = new DeleteAliasMojo();
        set(mojo, "service", service);
        set(mojo, "keystore", new File("keystore"));
        set(mojo, "storepass", "secret");
        set(mojo, "alias", alias);
        return mojo;
    }

    @Test
    void deletesTheAliasOnce() throws Exception {
        RecordingService service = new RecordingService();

        mojo(service, "myalias").execute();

        assertEquals(Collections.singletonList("myalias"), service.deletedAliases);
    }

    @Test
    void skipDeletesNothing() throws Exception {
        RecordingService service = new RecordingService();
        DeleteAliasMojo mojo = mojo(service, "myalias");
        mojo.setSkip(true);

        mojo.execute();

        assertTrue(service.deletedAliases.isEmpty());
    }

    @Test
    void emptyAliasIsRejected() throws Exception {
        RecordingService service = new RecordingService();

        assertThrows(MojoExecutionException.class, () -> mojo(service, "").execute());

        assertTrue(service.deletedAliases.isEmpty());
    }
}
