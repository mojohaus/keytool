Creating a keystore file.
If validity is out, use the following cli to regenerate a keystore, certificate-request and certificate

rm src/test/resources/simple-keystore
rm src/test/resources/simple-certificate-request
rm src/test/resources/simple-certificate

keytool -genkeypair -alias foo_alias -keystore src/test/resources/simple-keystore -keypass key-passwd -validity 999 -storepass changeit -dname "CN=Me, OU=Unknown, O=Codehaus, L=Unknown, ST=Unknown, C=France"
keytool -certreq -alias foo_alias -keystore src/test/resources/simple-keystore -file src/test/resources/simple-certificate-request -keypass key-passwd -validity 999 -storepass changeit
keytool -export -alias foo_alias -keystore src/test/resources/simple-keystore -file src/test/resources/simple-certificate -storepass changeit


