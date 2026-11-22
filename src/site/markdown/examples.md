# Examples

The Keytool Maven Plugin provides comprehensive examples for all common keystore operations. This page provides quick-start examples and links to detailed documentation.

## Quick Start Examples

### Generate a Development Keystore

The simplest way to get started - generate a self-signed certificate for development:

```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>keytool-maven-plugin</artifactId>
  <executions>
    <execution>
      <goals>
        <goal>generateKeyPair</goal>
      </goals>
      <configuration>
        <keystore>${project.build.directory}/keystore.jks</keystore>
        <alias>myapp</alias>
        <dname>CN=localhost, O=Development</dname>
        <storepass>changeit</storepass>
        <keypass>changeit</keypass>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Import a Trusted Certificate

Import a CA certificate into a truststore:

```xml
<execution>
  <goals>
    <goal>importCertificate</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/truststore.jks</keystore>
    <alias>ca-cert</alias>
    <file>ca-certificate.crt</file>
    <storepass>changeit</storepass>
  </configuration>
</execution>
```

### Export Certificate for Sharing

Export a certificate to a file in DER format:

```xml
<execution>
  <goals>
    <goal>exportCertificate</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/keystore.jks</keystore>
    <alias>myapp</alias>
    <file>${project.build.directory}/certificate.crt</file>
    <storepass>changeit</storepass>
  </configuration>
</execution>
```

### Generate Certificate Signing Request

Create a CSR for CA signing:

```xml
<execution>
  <goals>
    <goal>generateCertificateRequest</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/keystore.jks</keystore>
    <alias>myapp</alias>
    <file>${project.build.directory}/request.csr</file>
    <dname>CN=myapp.example.com, O=Example Inc</dname>
    <storepass>changeit</storepass>
    <keypass>changeit</keypass>
  </configuration>
</execution>
```

### List Keystore Contents

Display all entries in a keystore:

```xml
<execution>
  <goals>
    <goal>list</goal>
  </goals>
  <configuration>
    <keystore>${project.build.directory}/keystore.jks</keystore>
    <storepass>changeit</storepass>
  </configuration>
</execution>
```

## Comprehensive Examples

For detailed examples covering all use cases, see the complete [Usage Guide](usage.html), which includes:

### Key Generation

- RSA, DSA, and EC key pairs
- Multiple key pairs in one build
- Algorithm and key size selection
- Custom validity periods

### Certificate Management

- Import certificates (DER and PEM formats) and export them (DER format)
- Print certificate details
- Certificate chain handling
- Trusted certificate management

### Certificate Requests (CSR)

- Generate PKCS#10 certificate requests
- Custom subject DNs and extensions (`san`, `ian`, `bc`, `ku`)
- Print CSR information
- Sign CSRs to create certificates

### Keystore Operations

- List all entries
- Import entire keystores
- Convert between formats (JKS ↔ PKCS12)
- Change aliases and passwords
- Delete entries
- Clean generated keystores

### Advanced Workflows

- Complete certificate lifecycle
- Multi-environment configurations
- Secret key generation (AES, DES)
- CI/CD integration

### Security Best Practices

- Password management strategies
- Algorithm recommendations
- Keystore format selection
- Thread-safe parallel builds

## Command Line Examples

The goal parameters have no command-line properties, so the configuration comes from the POM. See
[Command Line Usage](usage.html#command-line-usage) for running a goal or a single execution with `mvn keytool:...`.

## Additional Resources

- **[Complete Usage Guide](usage.html)** - Comprehensive documentation with all examples
- **[Plugin Goals](plugin-info.html)** - Detailed parameter documentation for each goal
- **[FAQ](faq.html)** - Answers to common questions
- **[GitHub Repository](https://github.com/mojohaus/keytool)** - Source code and issue tracking

## Getting Help

If you have questions or need help:

1. Check the [Usage Guide](usage.html) for detailed examples
2. Review the [FAQ](faq.html) for common issues
3. Search existing [GitHub Issues](https://github.com/mojohaus/keytool/issues)
4. Create a new issue if you've found a bug or have a feature request

