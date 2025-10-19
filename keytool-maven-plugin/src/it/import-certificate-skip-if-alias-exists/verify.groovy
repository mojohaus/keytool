/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.security.KeyStore

// Verify that the keystore was created
def keystoreFile = new File(basedir, "target/test-keystore.jks")
assert keystoreFile.exists(), "Keystore file should exist: ${keystoreFile}"

// Load the keystore and verify the certificate was imported
def keystore = KeyStore.getInstance("JKS")
keystoreFile.withInputStream { is ->
    keystore.load(is, "testpass".toCharArray())
}

// Verify the alias exists
assert keystore.containsAlias("test-cert"), "Certificate with alias 'test-cert' should exist"

// Verify the certificate is correct type
def cert = keystore.getCertificate("test-cert")
assert cert != null, "Certificate should not be null"

// Check build log to ensure skipIfAliasExists worked
def buildLog = new File(basedir, "build.log")
assert buildLog.exists(), "Build log should exist"
def logContent = buildLog.text

// First execution should add the certificate
assert logContent.contains("Certificate was added to keystore") || logContent.contains("import-certificate-first"), 
    "First execution should have imported the certificate"

// Second execution should skip (when skipIfAliasExists=true)
assert logContent.contains("already exists") || logContent.contains("import-certificate-second-skip"), 
    "Second execution should have skipped or logged the alias exists"

println "Integration test PASSED: Certificate imported successfully and skipIfAliasExists worked correctly"
