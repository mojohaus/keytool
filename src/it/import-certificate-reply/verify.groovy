import java.io.*
import java.security.*
import java.security.cert.*

// Verify the server keystore exists and contains the correct entries
def keystoreFile = new File(basedir, "target/truststore-server.jks")
assert keystoreFile.exists() : "Server keystore should exist"

// Load the keystore
def ks = KeyStore.getInstance("JKS")
def password = "changeit".toCharArray()
ks.load(new FileInputStream(keystoreFile), password)

// Verify the 'localhost' alias is a key entry (has private key)
assert ks.isKeyEntry("localhost") : "localhost should be a key entry"

// Verify the private key can be retrieved
def privateKey = ks.getKey("localhost", password)
assert privateKey != null : "Private key should exist for localhost"

// Verify the certificate chain exists
def chain = ks.getCertificateChain("localhost")
assert chain != null : "Certificate chain should exist"
assert chain.length > 0 : "Certificate chain should not be empty"

// Verify the CA cert is in the keystore as a trusted cert
assert ks.containsAlias("cxfca") : "CA certificate should be imported"
assert ks.isCertificateEntry("cxfca") : "cxfca should be a certificate entry"

println "SUCCESS: Certificate reply was successfully imported for key pair"
println "  - localhost is a key entry: " + ks.isKeyEntry("localhost")
println "  - Private key exists: " + (privateKey != null)
println "  - Certificate chain length: " + chain.length
println "  - CA certificate exists: " + ks.containsAlias("cxfca")

return true

