# Keytool Maven Plugin

This plugin provides the capability to manipulate keys and keystores using the Java KeyStore API directly, without requiring external keytool command execution. It may provide better performance, error handling, and Maven integration compared to shell-based approaches.

## Goals Overview

### Key Management

- **[keytool:generateKeyPair](generateKeyPair-mojo.html)** - Generates a key pair and self-signed certificate
  - Supports RSA, DSA, and EC algorithms
  - Configurable key size and validity period
  - X.509v3 certificate generation
- **[keytool:generateSecretKey](generateSecretKey-mojo.html)** - Generates a secret key
  - Supports AES, DES, DESede algorithms
  - Configurable key size
- **[keytool:changeKeyPassword](changeKeyPassword-mojo.html)** - Changes the key password of an entry

### Certificate Management

- **[keytool:generateCertificate](generateCertificate-mojo.html)** - Generates a certificate from a certificate request
  - Creates X.509 certificates
  - Supports custom validity periods and extensions
- **[keytool:generateCertificateRequest](generateCertificateRequest-mojo.html)** - Generates a certificate request (CSR)
  - PKCS#10 format
  - PEM encoding support
- **[keytool:importCertificate](importCertificate-mojo.html)** - Imports a certificate from a file into a keystore
  - Supports DER and PEM formats
  - Optional trust validation
- **[keytool:exportCertificate](exportCertificate-mojo.html)** - Exports a certificate from a keystore to a file
  - DER or PEM format output

### Keystore Management

- **[keytool:list](list-mojo.html)** - Lists entries in a keystore
  - Shows aliases, creation dates, and entry types
- **[keytool:importKeystore](importKeystore-mojo.html)** - Imports all entries from one keystore to another
  - Preserves entry types and attributes
- **[keytool:changeAlias](changeAlias-mojo.html)** - Changes the alias of an entry
- **[keytool:changeStorePassword](changeStorePassword-mojo.html)** - Changes the keystore password
- **[keytool:deleteAlias](deleteAlias-mojo.html)** - Deletes an entry from a keystore
- **[keytool:clean](clean-mojo.html)** - Deletes a generated keystore file

### Information & Diagnostics

- **[keytool:printCertificate](printCertificate-mojo.html)** - Prints the content of a certificate
  - Shows subject, issuer, validity, and fingerprints
- **[keytool:printCertificateRequest](printCertificateRequest-mojo.html)** - Prints the content of a certificate request
- **[keytool:printCRLFile](printCRLFile-mojo.html)** - Prints the content of a CRL file

## Requirements

- **Maven**: 3.6.3 or higher
- **JDK**: 8 or higher (Java 17+ recommended)

## Usage

General instructions on how to use the Keytool Plugin can be found on the [usage page](usage.html).

For specific examples and use cases, see the comprehensive [usage guide](usage.html).

## Version History

### 2.0.0 (Current)

- Complete rewrite using Java KeyStore API instead of command-line execution
- Migrated to JSR-330 dependency injection with Eclipse Sisu
- Removed deprecated command-line API
- Updated to Bouncy Castle 1.80 for certificate generation
- All Mojos are now thread-safe
- Comprehensive unit and integration test coverage
- Updated documentation to Java 17

### Previous Versions

Earlier versions used command-line execution of the keytool utility.
