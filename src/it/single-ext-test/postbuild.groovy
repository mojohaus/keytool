import java.io.File

File keystore = new File(basedir, "target/keystore.jks")
assert keystore.exists() : "Keystore file was not created"

println "Keystore file exists: " + keystore.absolutePath
return true
