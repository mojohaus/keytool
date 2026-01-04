package org.codehaus.mojo.keytool.api.requests;

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

import java.io.File;

/**
 * Request Fixtures.
 *
 * @author tchemit
 * @since 1.3
 */
public class KeyToolRequestFixtures {
    /**
     * <p>createKeyToolChangeAliasRequest.</p>
     *
     * @param keyStore a {@link java.io.File} object
     * @return a {@link KeyToolChangeAliasRequest} object
     */
    public KeyToolChangeAliasRequest createKeyToolChangeAliasRequest(File keyStore) {
        KeyToolChangeAliasRequest request = new KeyToolChangeAliasRequest();
        request.setAlias("foo_alias");
        request.setDestalias("new_alias");
        request.setStoretype("jks");
        request.setStorepass("changeit");
        request.setKeystore(keyStore.getAbsolutePath());
        request.setKeypass("key-passwd");
        request.setVerbose(true);
        return request;
    }

    /**
     * <p>createKeyToolPrintCRLFileRequest.</p>
     *
     * @param file a {@link java.io.File} object
     * @return a {@link KeyToolPrintCRLFileRequest} object
     */
    public KeyToolPrintCRLFileRequest createKeyToolPrintCRLFileRequest(File file) {
        KeyToolPrintCRLFileRequest request = new KeyToolPrintCRLFileRequest();

        request.setFile(file);
        request.setVerbose(true);
        return request;
    }

    /**
     * <p>createKeyToolPrintCertificateRequestRequest.</p>
     *
     * @param file a {@link java.io.File} object
     * @return a {@link KeyToolPrintCertificateRequestRequest} object
     */
    public KeyToolPrintCertificateRequestRequest createKeyToolPrintCertificateRequestRequest(File file) {
        KeyToolPrintCertificateRequestRequest request = new KeyToolPrintCertificateRequestRequest();

        request.setFile(file);
        request.setVerbose(true);
        return request;
    }

    /**
     * <p>createKeyToolPrintCertificateRequest.</p>
     *
     * @param file a {@link java.io.File} object
     * @return a {@link KeyToolPrintCertificateRequest} object
     */
    public KeyToolPrintCertificateRequest createKeyToolPrintCertificateRequest(File file) {
        KeyToolPrintCertificateRequest request = new KeyToolPrintCertificateRequest();

        request.setFile(file);

        request.setVerbose(true);
        request.setRfc(true);
        return request;
    }

    /**
     * <p>createKeyToolListRequest.</p>
     *
     * @param keyStore a {@link java.io.File} object
     * @return a {@link KeyToolListRequest} object
     */
    public KeyToolListRequest createKeyToolListRequest(File keyStore) {
        KeyToolListRequest request = new KeyToolListRequest();
        request.setAlias("foo_alias");

        request.setStoretype("jks");
        request.setStorepass("changeit");
        request.setKeystore(keyStore.getAbsolutePath());
        request.setVerbose(true);
        request.setRfc(false);
        return request;
    }

    /**
     * <p>createKeyToolImportKeystoreRequest.</p>
     *
     * @param srcKeyStore a {@link java.io.File} object
     * @param destKeyStore a {@link java.io.File} object
     * @return a {@link KeyToolImportKeystoreRequest} object
     */
    public KeyToolImportKeystoreRequest createKeyToolImportKeystoreRequest(File srcKeyStore, File destKeyStore) {
        KeyToolImportKeystoreRequest request = new KeyToolImportKeystoreRequest();

        request.setSrcalias("foo_alias");
        request.setDestalias("new_alias");

        request.setSrcstoretype("jks");
        request.setDeststoretype("jks");

        request.setSrcstorepass("changeit");
        request.setDeststorepass("changeit");

        request.setSrckeystore(srcKeyStore.getAbsolutePath());
        request.setDestkeystore(destKeyStore.getAbsolutePath());

        request.setSrckeypass("key-passwd");
        request.setDestkeypass("key-passwd");
        request.setVerbose(true);
        request.setNoprompt(true);
        return request;
    }

    /**
     * <p>createKeyToolImportCertificateRequest.</p>
     *
     * @param keyStore a {@link java.io.File} object
     * @param file a {@link java.io.File} object
     * @return a {@link KeyToolImportCertificateRequest} object
     */
    public KeyToolImportCertificateRequest createKeyToolImportCertificateRequest(File keyStore, File file) {
        KeyToolImportCertificateRequest request = new KeyToolImportCertificateRequest();
        request.setAlias("foo_alias2");
        request.setStoretype("jks");
        request.setStorepass("changeit");
        request.setKeystore(keyStore.getAbsolutePath());
        request.setNoprompt(true);
        request.setVerbose(true);
        request.setTrustcacerts(true);
        request.setFile(file.getAbsolutePath());
        request.setKeypass("new-passwd");
        return request;
    }

    /**
     * <p>createKeyToolGenerateSecretKeyRequest.</p>
     *
     * @param keyStore a {@link java.io.File} object
     * @return a {@link KeyToolGenerateSecretKeyRequest} object
     */
    public KeyToolGenerateSecretKeyRequest createKeyToolGenerateSecretKeyRequest(File keyStore) {
        KeyToolGenerateSecretKeyRequest request = new KeyToolGenerateSecretKeyRequest();
        request.setAlias("new_foo_alias");
        request.setStoretype("jks");
        request.setStorepass("changeit");
        request.setKeystore(keyStore.getAbsolutePath());
        request.setKeypass("key-passwd");
        request.setKeyalg("DES");
        request.setKeysize("56");
        request.setVerbose(true);
        return request;
    }

    /**
     * <p>createKeyToolGenerateKeyPairRequest.</p>
     *
     * @param keyStore a {@link java.io.File} object
     * @return a {@link KeyToolGenerateKeyPairRequest} object
     */
    public KeyToolGenerateKeyPairRequest createKeyToolGenerateKeyPairRequest(File keyStore) {
        KeyToolGenerateKeyPairRequest request = new KeyToolGenerateKeyPairRequest();
        request.setAlias("dest_foo_alias");
        request.setStoretype("jks");
        request.setStorepass("changeit");
        request.setKeystore(keyStore.getAbsolutePath());
        request.setKeypass("key-passwd");
        request.setSigalg("SHA1withDSA");
        request.setDname("CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France");
        request.setVerbose(true);
        request.setValidity("100");
        request.setStartdate("2011/11/11");
        request.setKeyalg("DSA");
        request.setKeysize("1024");
        request.setExt("");
        return request;
    }

    /**
     * <p>createKeyToolGenerateCertificateRequestRequest.</p>
     *
     * @param keyStore a {@link java.io.File} object
     * @param outputFile a {@link java.io.File} object
     * @return a {@link KeyToolGenerateCertificateRequestRequest} object
     */
    public KeyToolGenerateCertificateRequestRequest createKeyToolGenerateCertificateRequestRequest(
            File keyStore, File outputFile) {
        KeyToolGenerateCertificateRequestRequest request = new KeyToolGenerateCertificateRequestRequest();
        request.setAlias("foo_alias");
        request.setStoretype("jks");
        request.setStorepass("changeit");
        request.setKeystore(keyStore.getAbsolutePath());
        request.setKeypass("key-passwd");
        request.setFile(outputFile);
        request.setSigalg("SHA1withDSA");
        request.setDname("CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France");
        request.setVerbose(true);
        return request;
    }

    /**
     * <p>createKeyToolGenerateCertificateRequest.</p>
     *
     * @param keyStore a {@link java.io.File} object
     * @param inFile a {@link java.io.File} object
     * @param outputFile a {@link java.io.File} object
     * @return a {@link KeyToolGenerateCertificateRequest} object
     */
    public KeyToolGenerateCertificateRequest createKeyToolGenerateCertificateRequest(
            File keyStore, File inFile, File outputFile) {
        KeyToolGenerateCertificateRequest request = new KeyToolGenerateCertificateRequest();
        request.setAlias("foo_alias");
        request.setStoretype("jks");
        request.setStorepass("changeit");
        request.setKeystore(keyStore.getAbsolutePath());
        request.setKeypass("key-passwd");
        request.setRfc(true);

        request.setInfile(inFile);
        request.setOutfile(outputFile);
        request.setSigalg("SHA1withDSA");
        request.setDname("CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France");
        request.setStartdate("2011/11/11");
        request.setExt("");
        request.setValidity("100");
        request.setVerbose(true);
        return request;
    }

    /**
     * <p>createKeyToolExportCertificateRequest.</p>
     *
     * @param keyStore a {@link java.io.File} object
     * @param outputFile a {@link java.io.File} object
     * @return a {@link KeyToolExportCertificateRequest} object
     */
    public KeyToolExportCertificateRequest createKeyToolExportCertificateRequest(File keyStore, File outputFile) {
        KeyToolExportCertificateRequest request = new KeyToolExportCertificateRequest();
        request.setAlias("foo_alias");
        request.setStoretype("jks");
        request.setStorepass("changeit");
        request.setKeystore(keyStore.getAbsolutePath());
        request.setRfc(true);
        request.setFile(outputFile.getAbsolutePath());
        request.setVerbose(true);
        return request;
    }

    /**
     * <p>createKeyToolDeleteRequest.</p>
     *
     * @param keyStore a {@link java.io.File} object
     * @return a {@link KeyToolDeleteRequest} object
     */
    public KeyToolDeleteRequest createKeyToolDeleteRequest(File keyStore) {
        KeyToolDeleteRequest request = new KeyToolDeleteRequest();
        request.setAlias("foo_alias");
        request.setStoretype("jks");
        request.setStorepass("changeit");
        request.setKeystore(keyStore.getAbsolutePath());
        request.setVerbose(true);
        return request;
    }

    /**
     * <p>createKeyToolChangeStorePasswordRequest.</p>
     *
     * @param keyStore a {@link java.io.File} object
     * @return a {@link KeyToolChangeStorePasswordRequest} object
     */
    public KeyToolChangeStorePasswordRequest createKeyToolChangeStorePasswordRequest(File keyStore) {
        KeyToolChangeStorePasswordRequest request = new KeyToolChangeStorePasswordRequest();
        request.setStoretype("jks");
        request.setStorepass("changeit");
        request.setKeystore(keyStore.getAbsolutePath());
        request.setNewPassword("new-changeit");
        request.setVerbose(true);
        return request;
    }

    /**
     * <p>createKeyToolChangeKeyPasswordRequest.</p>
     *
     * @param keyStore a {@link java.io.File} object
     * @return a {@link KeyToolChangeKeyPasswordRequest} object
     */
    public KeyToolChangeKeyPasswordRequest createKeyToolChangeKeyPasswordRequest(File keyStore) {
        KeyToolChangeKeyPasswordRequest request = new KeyToolChangeKeyPasswordRequest();
        request.setAlias("foo_alias");
        request.setStoretype("jks");
        request.setStorepass("changeit");
        request.setKeystore(keyStore.getAbsolutePath());
        request.setKeypass("key-passwd");
        request.setNewPassword("new-key-passwd");
        request.setVerbose(true);
        return request;
    }
}
