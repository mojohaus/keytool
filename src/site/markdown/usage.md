# Usage Guide

This guide provides comprehensive examples of how to use the Keytool Maven Plugin in your projects.

## Table of Contents

- [Basic Configuration](#basic-configuration)
- [Generating Key Pairs](#generating-key-pairs)
- [Managing Certificates](#managing-certificates)
- [Working with Certificate Requests (CSR)](#working-with-certificate-requests-csr)
- [Keystore Operations](#keystore-operations)
- [Advanced Examples](#advanced-examples)
- [Best Practices](#best-practices)

## Basic Configuration

Add the plugin to your `pom.xml`:

```xml
<project>
  <build>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>keytool-maven-plugin</artifactId>
        <version>2.0.0</version>
      </plugin>
    </plugins>
  </build>
</project>
```

## Generating Key Pairs

### Generate RSA Key Pair with Self-Signed Certificate

```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>keytool-maven-plugin</artifactId>
  <version>2.0.0</version>
  <executions>
    <execution>
      <id>generate-keypair</id>
      <phase>generate-resources</phase>
      <goals>
        <goal>generateKeyPair</goal>
      </goals>
      <configuration>
        <keystore>${project.build.directory}/myapp.jks</keystore>
        <storepass>changeit</storepass>
        <alias>myapp</alias>
        <keyalg>RSA</keyalg>
        <keysize>2048</keysize>
        <sigalg>SHA256WithRSA</sigalg>
        <validity>365</validity>
        <dname>CN=My Application, OU=Development, O=My Company, L=City, ST=State, C=US</dname>
        <keypass>changeit</keypass>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Generate EC (Elliptic Curve) Key Pair

```xml
<execution>
  <id>generate-ec-keypair</id>
  <goals>
    <goal>generateKeyPair</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/ec-keystore.jks</keystore>
    <storepass>changeit</storepass>
    <alias>ec-key</alias>
    <keyalg>EC</keyalg>
    <keysize>256</keysize>
    <sigalg>SHA256withECDSA</sigalg>
    <validity>730</validity>
    <dname>CN=EC Certificate, O=My Company</dname>
    <keypass>changeit</keypass>
  </configuration>
</execution>
```

### Generate Multiple Key Pairs

```xml
<executions>
  <execution>
    <id>generate-signing-key</id>
    <phase>generate-resources</phase>
    <goals>
      <goal>generateKeyPair</goal>
    </goals>
    <configuration>
      <keystore>${project.build.directory}/signing.jks</keystore>
      <alias>signing</alias>
      <keyalg>RSA</keyalg>
      <keysize>4096</keysize>
      <validity>1825</validity>
      <dname>CN=Code Signing, O=My Company</dname>
      <!-- Additional configuration -->
    </configuration>
  </execution>
  
  <execution>
    <id>generate-encryption-key</id>
    <phase>generate-resources</phase>
    <goals>
      <goal>generateKeyPair</goal>
    </goals>
    <configuration>
      <keystore>${project.build.directory}/encryption.jks</keystore>
      <alias>encryption</alias>
      <keyalg>RSA</keyalg>
      <keysize>2048</keysize>
      <validity>1095</validity>
      <dname>CN=Encryption, O=My Company</dname>
      <!-- Additional configuration -->
    </configuration>
  </execution>
</executions>
```

## Managing Certificates

### Import Certificate from File

```xml
<execution>
  <id>import-ca-cert</id>
  <goals>
    <goal>importCertificate</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/truststore.jks</keystore>
    <storepass>changeit</storepass>
    <alias>ca-cert</alias>
    <file>${project.basedir}/certs/ca.crt</file>
    <noprompt>true</noprompt>
    <trustcacerts>true</trustcacerts>
  </configuration>
</execution>
```

### Export Certificate to File

Export in DER format:

```xml
<execution>
  <id>export-cert-der</id>
  <goals>
    <goal>exportCertificate</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/myapp.jks</keystore>
    <storepass>changeit</storepass>
    <alias>myapp</alias>
    <file>${project.build.directory}/myapp.crt</file>
    <rfc>false</rfc>
  </configuration>
</execution>
```

Export in PEM format:

```xml
<execution>
  <id>export-cert-pem</id>
  <goals>
    <goal>exportCertificate</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/myapp.jks</keystore>
    <storepass>changeit</storepass>
    <alias>myapp</alias>
    <file>${project.build.directory}/myapp.pem</file>
    <rfc>true</rfc>
  </configuration>
</execution>
```

### Print Certificate Information

```xml
<execution>
  <id>print-cert</id>
  <goals>
    <goal>printCertificate</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/myapp.jks</keystore>
    <storepass>changeit</storepass>
    <alias>myapp</alias>
    <verbose>true</verbose>
  </configuration>
</execution>
```

## Working with Certificate Requests (CSR)

### Generate Certificate Signing Request

```xml
<execution>
  <id>generate-csr</id>
  <goals>
    <goal>generateCertificateRequest</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/myapp.jks</keystore>
    <storepass>changeit</storepass>
    <alias>myapp</alias>
    <keypass>changeit</keypass>
    <file>${project.build.directory}/myapp.csr</file>
    <sigalg>SHA256WithRSA</sigalg>
    <dname>CN=myapp.example.com, OU=IT, O=Example Inc, L=New York, ST=NY, C=US</dname>
  </configuration>
</execution>
```

### Print CSR Information

```xml
<execution>
  <id>print-csr</id>
  <goals>
    <goal>printCertificateRequest</goal>
  </goals>
  <configuration>
    <file>${project.build.directory}/myapp.csr</file>
  </configuration>
</execution>
```

### Generate Certificate from CSR

```xml
<execution>
  <id>sign-csr</id>
  <goals>
    <goal>generateCertificate</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/ca.jks</keystore>
    <storepass>changeit</storepass>
    <alias>ca</alias>
    <keypass>changeit</keypass>
    <infile>${project.build.directory}/myapp.csr</infile>
    <outfile>${project.build.directory}/myapp-signed.crt</outfile>
    <validity>365</validity>
    <sigalg>SHA256WithRSA</sigalg>
    <rfc>true</rfc>
  </configuration>
</execution>
```

## Keystore Operations

### List Keystore Entries

```xml
<execution>
  <id>list-entries</id>
  <goals>
    <goal>list</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/myapp.jks</keystore>
    <storepass>changeit</storepass>
    <verbose>true</verbose>
  </configuration>
</execution>
```

### Import Entire Keystore

```xml
<execution>
  <id>import-keystore</id>
  <goals>
    <goal>importKeystore</goal>
  </goals>
  <configuration>
    <srckeystore>${project.basedir}/source.jks</srckeystore>
    <srcstorepass>source-password</srcstorepass>
    <destkeystore>${project.build.directory}/dest.jks</destkeystore>
    <deststorepass>dest-password</deststorepass>
  </configuration>
</execution>
```

### Convert Keystore Format (JKS to PKCS12)

```xml
<execution>
  <id>convert-to-pkcs12</id>
  <goals>
    <goal>importKeystore</goal>
  </goals>
  <configuration>
    <srckeystore>${project.build.directory}/myapp.jks</srckeystore>
    <srcstoretype>JKS</srcstoretype>
    <srcstorepass>changeit</srcstorepass>
    <destkeystore>${project.build.directory}/myapp.p12</destkeystore>
    <deststoretype>PKCS12</deststoretype>
    <deststorepass>changeit</deststorepass>
  </configuration>
</execution>
```

### Change Alias

```xml
<execution>
  <id>change-alias</id>
  <goals>
    <goal>changeAlias</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/myapp.jks</keystore>
    <storepass>changeit</storepass>
    <alias>oldname</alias>
    <destalias>newname</destalias>
  </configuration>
</execution>
```

### Change Passwords

Change key password:

```xml
<execution>
  <id>change-key-password</id>
  <goals>
    <goal>changeKeyPassword</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/myapp.jks</keystore>
    <storepass>changeit</storepass>
    <alias>myapp</alias>
    <keypass>oldpassword</keypass>
    <newPassword>newpassword</newPassword>
  </configuration>
</execution>
```

Change store password:

```xml
<execution>
  <id>change-store-password</id>
  <goals>
    <goal>changeStorePassword</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/myapp.jks</keystore>
    <storepass>oldpassword</storepass>
    <newPassword>newpassword</newPassword>
  </configuration>
</execution>
```

### Delete Entry

```xml
<execution>
  <id>delete-entry</id>
  <goals>
    <goal>deleteAlias</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/myapp.jks</keystore>
    <storepass>changeit</storepass>
    <alias>obsolete-entry</alias>
  </configuration>
</execution>
```

### Clean Keystore File

```xml
<execution>
  <id>clean-keystore</id>
  <phase>clean</phase>
  <goals>
    <goal>clean</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/myapp.jks</keystore>
  </configuration>
</execution>
```

## Advanced Examples

### Generate Secret Key for Encryption

```xml
<execution>
  <id>generate-aes-key</id>
  <goals>
    <goal>generateSecretKey</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/secrets.jks</keystore>
    <storepass>changeit</storepass>
    <alias>aes-key</alias>
    <keyalg>AES</keyalg>
    <keysize>256</keysize>
    <keypass>changeit</keypass>
  </configuration>
</execution>
```

### Complete Certificate Lifecycle Workflow

This example shows a complete workflow for generating a key pair, creating a CSR, and managing the resulting certificate:

```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>keytool-maven-plugin</artifactId>
  <version>2.0.0</version>
  <executions>
    <!-- 1. Generate key pair -->
    <execution>
      <id>step1-generate-keypair</id>
      <phase>generate-resources</phase>
      <goals>
        <goal>generateKeyPair</goal>
      </goals>
      <configuration>
        <keystore>${project.build.directory}/server.jks</keystore>
        <storepass>changeit</storepass>
        <alias>server</alias>
        <keyalg>RSA</keyalg>
        <keysize>2048</keysize>
        <validity>365</validity>
        <dname>CN=localhost, OU=Development, O=My Company</dname>
        <keypass>changeit</keypass>
      </configuration>
    </execution>
    
    <!-- 2. Generate CSR for CA signing -->
    <execution>
      <id>step2-generate-csr</id>
      <phase>generate-resources</phase>
      <goals>
        <goal>generateCertificateRequest</goal>
      </goals>
      <configuration>
        <keystore>${project.build.directory}/server.jks</keystore>
        <storepass>changeit</storepass>
        <alias>server</alias>
        <keypass>changeit</keypass>
        <file>${project.build.directory}/server.csr</file>
        <dname>CN=server.example.com, OU=IT, O=Example Inc, L=City, ST=State, C=US</dname>
      </configuration>
    </execution>
    
    <!-- 3. Print CSR for verification -->
    <execution>
      <id>step3-verify-csr</id>
      <phase>process-resources</phase>
      <goals>
        <goal>printCertificateRequest</goal>
      </goals>
      <configuration>
        <file>${project.build.directory}/server.csr</file>
      </configuration>
    </execution>
    
    <!-- 4. Export certificate for distribution -->
    <execution>
      <id>step4-export-cert</id>
      <phase>package</phase>
      <goals>
        <goal>exportCertificate</goal>
      </goals>
      <configuration>
        <keystore>${project.build.directory}/server.jks</keystore>
        <storepass>changeit</storepass>
        <alias>server</alias>
        <file>${project.build.directory}/server.pem</file>
        <rfc>true</rfc>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Multi-Environment Keystores

Use Maven profiles to manage different keystores for different environments:

```xml
<profiles>
  <profile>
    <id>development</id>
    <activation>
      <activeByDefault>true</activeByDefault>
    </activation>
    <build>
      <plugins>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>keytool-maven-plugin</artifactId>
          <version>2.0.0</version>
          <executions>
            <execution>
              <id>dev-keystore</id>
              <goals>
                <goal>generateKeyPair</goal>
              </goals>
              <configuration>
                <keystore>${project.build.directory}/dev-keystore.jks</keystore>
                <dname>CN=dev.example.com, O=Development</dname>
                <validity>90</validity>
                <!-- Dev-specific configuration -->
              </configuration>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </build>
  </profile>
  
  <profile>
    <id>production</id>
    <build>
      <plugins>
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>keytool-maven-plugin</artifactId>
          <version>2.0.0</version>
          <executions>
            <execution>
              <id>prod-keystore</id>
              <goals>
                <goal>importCertificate</goal>
              </goals>
              <configuration>
                <keystore>${project.build.directory}/prod-keystore.jks</keystore>
                <file>${project.basedir}/config/prod-cert.crt</file>
                <!-- Production-specific configuration -->
              </configuration>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </build>
  </profile>
</profiles>
```

## Best Practices

### Security Considerations

1. **Never hardcode passwords in pom.xml** - Use Maven properties or environment variables:

```xml
<configuration>
  <storepass>${keystore.password}</storepass>
  <keypass>${key.password}</keypass>
</configuration>
```

Then run with:

```bash
mvn clean install -Dkeystore.password=secret -Dkey.password=secret
```

2. **Use settings.xml for sensitive values**:

```xml
<!-- In ~/.m2/settings.xml -->
<settings>
  <profiles>
    <profile>
      <id>secure</id>
      <properties>
        <keystore.password>your-secure-password</keystore.password>
        <key.password>your-key-password</key.password>
      </properties>
    </profile>
  </profiles>
</settings>
```

3. **Use PKCS12 format for new keystores** - It's the industry standard:

```xml
<configuration>
  <storetype>PKCS12</storetype>
</configuration>
```

### Performance Tips

1. **Bind to appropriate phases** - Generate keys early, clean late:
   - `generate-resources` for key generation
   - `clean` for keystore cleanup
2. **Skip goals when not needed**:

```bash
mvn package -Dkeytool.skip=true
```

3. **Use thread-safe execution** - All goals support parallel builds:

```bash
mvn clean install -T 4
```

### Algorithm Recommendations

|         Use Case          | Algorithm |  Key Size  |       Signature Algorithm       |
|---------------------------|-----------|------------|---------------------------------|
| **Code Signing**          | RSA       | 4096       | SHA256WithRSA                   |
| **TLS/SSL Server**        | RSA or EC | 2048 / 256 | SHA256WithRSA / SHA256withECDSA |
| **Client Authentication** | RSA or EC | 2048 / 256 | SHA256WithRSA / SHA256withECDSA |
| **Data Encryption**       | RSA       | 2048+      | SHA256WithRSA                   |
| **Symmetric Keys**        | AES       | 256        | N/A                             |

### Troubleshooting

**Problem**: "Keystore file does not exist"

```xml
<!-- Solution: Ensure parent directory exists or create keystore first -->
<execution>
  <id>create-keystore</id>
  <phase>generate-resources</phase>
  <goals>
    <goal>generateKeyPair</goal>
  </goals>
</execution>
```

**Problem**: "Invalid keysize for EC"

```xml
<!-- Solution: Use appropriate keysize for EC (256, 384, 521) -->
<configuration>
  <keyalg>EC</keyalg>
  <keysize>256</keysize>  <!-- Not 2048! -->
</configuration>
```

**Problem**: "Alias already exists"

```xml
<!-- Solution: Delete the alias first or use a different alias -->
<execution>
  <id>delete-old-alias</id>
  <phase>generate-resources</phase>
  <goals>
    <goal>deleteAlias</goal>
  </goals>
  <configuration>
    <alias>myapp</alias>
  </configuration>
</execution>
```

## Command Line Usage

Execute goals directly from command line:

```bash
# Generate a key pair
mvn keytool:generateKeyPair -Dkeystore=my.jks -Dalias=mykey -Ddname="CN=Test"

# List keystore entries
mvn keytool:list -Dkeystore=my.jks -Dstorepass=changeit

# Export certificate
mvn keytool:exportCertificate -Dkeystore=my.jks -Dalias=mykey -Dfile=cert.pem

# Import certificate
mvn keytool:importCertificate -Dkeystore=trust.jks -Dalias=ca -Dfile=ca.crt
```

## Additional Resources

- [Plugin Goals Documentation](plugin-info.html) - Complete list of all available goals
- [FAQ](faq.html) - Frequently asked questions
- [Java Keytool Documentation](https://docs.oracle.com/en/java/javase/17/docs/specs/man/keytool.html) - Official Java keytool reference
- [Maven Jarsigner Plugin](https://maven.apache.org/plugins/maven-jarsigner-plugin/) - For JAR signing operations

