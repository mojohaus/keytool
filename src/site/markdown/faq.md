# Frequently Asked Questions

## General Questions

### What keystore format should I use?

**Recommendation**: Use **PKCS12** format for new keystores.

- **PKCS12** is the industry standard and portable across different tools and platforms
- **JKS** is Java-specific and considered legacy (though still supported)

```xml
<configuration>
  <storetype>PKCS12</storetype>
  <keystore>${project.build.directory}/keystore.p12</keystore>
</configuration>
```

---

### How do I handle passwords securely?

**Never** hardcode passwords in your `pom.xml` or commit them to version control. Use one of these approaches:

#### 1. Maven Settings (Recommended)

Store passwords in `~/.m2/settings.xml`:

```xml
<settings>
  <profiles>
    <profile>
      <id>secure</id>
      <properties>
        <keystore.password>your-password</keystore.password>
        <key.password>your-key-password</key.password>
      </properties>
    </profile>
  </profiles>
</settings>
```

Activate with: `mvn clean install -P secure`

#### 2. Environment Variables

```xml
<configuration>
  <storepass>${env.KEYSTORE_PASSWORD}</storepass>
  <keypass>${env.KEY_PASSWORD}</keypass>
</configuration>
```

Set before build:

```bash
export KEYSTORE_PASSWORD=secret
export KEY_PASSWORD=secret
mvn clean install
```

#### 3. Command Line Properties

```bash
mvn clean install -Dkeystore.password=secret -Dkey.password=secret
```

#### 4. Maven Password Encryption

Use [Maven's password encryption feature](https://maven.apache.org/guides/mini/guide-encryption.html):

```bash
mvn --encrypt-master-password
mvn --encrypt-password
```

---

### Can I generate keystores automatically during the build?

Yes! The plugin is designed to integrate seamlessly with Maven's build lifecycle:

```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>keytool-maven-plugin</artifactId>
  <version>2.0.0</version>
  <executions>
    <execution>
      <id>auto-generate-keystore</id>
      <phase>generate-resources</phase>
      <goals>
        <goal>generateKeyPair</goal>
      </goals>
      <configuration>
        <keystore>${project.build.directory}/auto-generated.jks</keystore>
        <alias>auto-key</alias>
        <keyalg>RSA</keyalg>
        <keysize>2048</keysize>
        <validity>365</validity>
        <dname>CN=${project.artifactId}, O=${project.organization.name}</dname>
        <storepass>${keystore.password}</storepass>
        <keypass>${key.password}</keypass>
      </configuration>
    </execution>
  </executions>
</plugin>
```

For development environments, you can skip keystore generation when not needed:

```bash
mvn clean install -Dkeytool.skip=true
```

---

### How do I migrate from command-line keytool to this plugin?

The plugin uses the Java KeyStore API directly, providing the same functionality as command-line keytool with better Maven integration:

|   Command-line keytool    |          Maven Plugin Goal           |
|---------------------------|--------------------------------------|
| `keytool -genkeypair`     | `keytool:generateKeyPair`            |
| `keytool -importcert`     | `keytool:importCertificate`          |
| `keytool -exportcert`     | `keytool:exportCertificate`          |
| `keytool -list`           | `keytool:list`                       |
| `keytool -certreq`        | `keytool:generateCertificateRequest` |
| `keytool -gencert`        | `keytool:generateCertificate`        |
| `keytool -delete`         | `keytool:deleteAlias`                |
| `keytool -changealias`    | `keytool:changeAlias`                |
| `keytool -storepasswd`    | `keytool:changeStorePassword`        |
| `keytool -keypasswd`      | `keytool:changeKeyPassword`          |
| `keytool -importkeystore` | `keytool:importKeystore`             |

Simply replace command-line invocations with the corresponding Maven goal and configuration.

---

### What algorithms are supported?

#### Key Algorithms

- **RSA** - Most common, recommended 2048+ bits
- **DSA** - Legacy, not recommended for new applications
- **EC** (Elliptic Curve) - Modern, efficient (256, 384, 521 bits)

#### Signature Algorithms

- **SHA256WithRSA** - Recommended for RSA keys
- **SHA256withECDSA** - Recommended for EC keys
- **SHA256WithDSA** - For DSA keys
- **SHA384WithRSA**, **SHA512WithRSA** - Higher security RSA variants

#### Secret Key Algorithms

- **AES** - Recommended (128, 192, 256 bits)
- **DES** - Legacy, not recommended
- **DESede** (3DES) - Legacy, use AES instead

---

### Can I use this plugin in a multi-module project?

Yes! You can configure the plugin in the parent POM and inherit in modules, or configure module-specific keystores:

**Parent POM:**

```xml
<build>
  <pluginManagement>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>keytool-maven-plugin</artifactId>
        <version>2.0.0</version>
        <configuration>
          <!-- Common configuration -->
          <keyalg>RSA</keyalg>
          <keysize>2048</keysize>
          <validity>365</validity>
          <storepass>${keystore.password}</storepass>
        </configuration>
      </plugin>
    </plugins>
  </pluginManagement>
</build>
```

**Module POM:**

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>keytool-maven-plugin</artifactId>
      <executions>
        <execution>
          <goals>
            <goal>generateKeyPair</goal>
          </goals>
          <configuration>
            <!-- Module-specific configuration -->
            <keystore>${project.build.directory}/${project.artifactId}.jks</keystore>
            <alias>${project.artifactId}</alias>
            <dname>CN=${project.artifactId}, O=${project.parent.organization.name}</dname>
          </configuration>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

---

### How do I troubleshoot "Keystore was tampered with, or password was incorrect"?

This error typically occurs when:

1. **Wrong password** - Double-check your password configuration
2. **Corrupted keystore** - The keystore file may be corrupted
3. **Wrong keystore type** - Specify the correct `storetype` (JKS, PKCS12, etc.)

**Solutions:**

```xml
<!-- Explicitly specify keystore type -->
<configuration>
  <keystore>${project.build.directory}/myapp.jks</keystore>
  <storetype>JKS</storetype>  <!-- or PKCS12 -->
  <storepass>${correct.password}</storepass>
</configuration>
```

For PKCS12 files:

```xml
<configuration>
  <keystore>${project.build.directory}/myapp.p12</keystore>
  <storetype>PKCS12</storetype>
  <storepass>${correct.password}</storepass>
</configuration>
```

---

### Can I use this with continuous integration systems?

Absolutely! The plugin works great with CI/CD pipelines:

**GitHub Actions Example:**

```yaml
name: Build with Keystore

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      
      - name: Set up JDK
        uses: actions/setup-java@v2
        with:
          java-version: '17'
          
      - name: Build with Maven
        run: mvn clean install
        env:
          KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
          KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
```

**Jenkins Example:**

```groovy
pipeline {
    agent any
    
    environment {
        KEYSTORE_PASSWORD = credentials('keystore-password')
        KEY_PASSWORD = credentials('key-password')
    }
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
    }
}
```

---

### What's the difference between version 1.x and 2.0?

**Version 2.0** represents a complete architectural rewrite:

|         Feature          |      Version 1.x       |          Version 2.0           |
|--------------------------|------------------------|--------------------------------|
| **Implementation**       | Command-line execution | Java KeyStore API              |
| **Performance**          | Process overhead       | Direct API calls               |
| **Thread Safety**        | No                     | Yes (parallel builds)          |
| **Dependency Injection** | Plexus                 | JSR-330 + Sisu                 |
| **Test Coverage**        | Limited                | Comprehensive (38 unit + 4 IT) |
| **Bouncy Castle**        | Old version            | 1.80 (latest)                  |
| **Documentation**        | Java 1.5 refs          | Java 17 refs                   |
| **Deprecated Code**      | Command-line API       | Removed                        |

**Migration is straightforward** - the plugin configuration remains compatible, but you get better performance and reliability.

---

### Where can I get help?

- **Documentation**: See the [usage guide](usage.html) for comprehensive examples
- **Issues**: Report bugs or request features on [GitHub Issues](https://github.com/mojohaus/keytool/issues)
- **Source Code**: [GitHub Repository](https://github.com/mojohaus/keytool)
- **Maven Central**: [org.codehaus.mojo:keytool-maven-plugin](https://search.maven.org/artifact/org.codehaus.mojo/keytool-maven-plugin)

