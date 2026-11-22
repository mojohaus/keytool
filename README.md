# MojoHaus Keytool Maven Plugin

[![The Apache License, Version 2.0](https://img.shields.io/github/license/mojohaus/keytool.svg?label=License)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/org.codehaus.mojo/keytool-maven-plugin.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/org.codehaus.mojo/keytool-maven-plugin)
[![Build Status](https://github.com/mojohaus/keytool/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/mojohaus/keytool/actions/workflows/maven.yml?query=branch%3Amaster)

The Keytool Maven Plugin manages keystores, key pairs, certificates and certificate requests from a Maven
build. Its goals use the Java KeyStore API and Bouncy Castle directly, so they do not start the `keytool`
program of the JDK.

It requires Maven 3.9.0 or later and Java 8 or later.

```xml
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>keytool-maven-plugin</artifactId>
  <version><!-- latest release --></version>
  <executions>
    <execution>
      <goals>
        <goal>generateKeyPair</goal>
      </goals>
      <phase>generate-resources</phase>
      <configuration>
        <keystore>${project.build.directory}/keystore.p12</keystore>
        <storetype>PKCS12</storetype>
        <storepass>changeit</storepass>
        <alias>myapp</alias>
        <dname>CN=localhost, O=Development</dname>
      </configuration>
    </execution>
  </executions>
</plugin>
```

The [plugin site](https://www.mojohaus.org/keytool/) lists every goal, with a
[usage guide](https://www.mojohaus.org/keytool/usage.html) and an [FAQ](https://www.mojohaus.org/keytool/faq.html).

## Contributing

Conventions for contributors and agents are in [AGENTS.md](AGENTS.md).

If you find a problem, [create an issue](https://github.com/mojohaus/keytool/issues) and describe what is going
wrong or what you expect to happen. A log file or a full working example helps. Describe one problem per issue.

Before you open a pull request, create an issue that describes the problem or the feature. A pull request
fixes one issue, and its description references it so that it closes on merge:

```text
Fixes #Issue
```

The maintainers choose how to merge, and the pull request title becomes the release note, so make it describe
the change for a user.

## Releasing

- Make sure `gpg-agent` is running.
- Run `mvn -B release:prepare release:perform`.

To publish the site:

```shell
cd target/checkout
mvn verify site site:stage scm-publish:publish-scm
```

