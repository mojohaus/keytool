import java.io.File
import java.security.KeyStore

// Check that keystore was created
File keystoreFile = new File(basedir, "target/test-keystore.jks")
assert keystoreFile.exists() : "Keystore file should exist at: ${keystoreFile.absolutePath}"

// Load and verify keystore
KeyStore ks = KeyStore.getInstance("JKS")
keystoreFile.withInputStream { fis ->
    ks.load(fis, "testpass".toCharArray())
}

// Check that certificate was imported
assert ks.containsAlias("imported-cert") : "Keystore should contain 'imported-cert' alias"
assert ks.getCertificate("imported-cert") != null : "Certificate should not be null"

// Check build log for informational message instead of warning
File buildLog = new File(basedir, "build.log")
assert buildLog.exists() : "Build log should exist"
String logContent = buildLog.text
assert logContent.contains("Certificate was added to keystore") : "Log should contain success message"
assert logContent.contains("[INFO]") : "Message should be logged as INFO"

println "Integration test passed successfully!"
return true
