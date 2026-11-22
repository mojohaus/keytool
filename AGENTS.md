# Agent guide for keytool

This file is for automated agents and human contributors working on this repository. It records
what the code and the POMs do not make obvious.

## Layout

This is a single-module Maven plugin, `keytool-maven-plugin`, with one mojo per `keytool` command
(`GenerateKeyPairMojo`, `ImportCertificateMojo`, `ListMojo`, and so on). All of them extend
`AbstractKeyToolMojo`. The work is done by the `@Named` components in `services`, which Sisu finds
through the index that `sisu-maven-plugin` generates; the mojos only collect parameters.

- Messages are in `src/main/resources/keytool*.properties` (default, `de`, `en`, `fr`) and are
  read through `ResourceBundle.getBundle("keytool")` in `AbstractKeyToolMojo`.
- The site is built from `src/site/markdown`.

## Build gates

- `spotless-maven-plugin` from mojo-parent runs during `process-sources`. Run `mvn spotless:apply`
  before committing. It formats Java with palantir-java-format, sorts the POM, and also reformats
  Markdown, including this file. `.git-blame-ignore-revs` lists the reformatting commit.
- The plugin compiles against `mavenVersion` and declares a literal Maven 3.9.0 as its
  `<prerequisites>`. Raising `mavenVersion` must not raise the prerequisite.
- Fields of the mojo classes are plugin parameters; renaming one changes the plugin's
  configuration API.
- Bouncy Castle (`bcprov-jdk18on`, version in `bouncycastle.version`) is a compile dependency,
  used only by `CertificateGenerationService`.

## Unit tests

- JUnit 5 only, under `src/test/java`. The service tests are plain JUnit tests that build
  keystores and certificates with `@TempDir`; `CleanKeyMojoTest` writes into `java.io.tmpdir`.

## Integration tests

- They live in `src/it`, one directory per test. The `run-its` profile is active by default
  (unless `maven.test.skip` is `true`), so `mvn verify` runs them; the invoker `install` goal puts
  the plugin into `target/local-repo` first. Run a subset with:

  ```shell
  mvn -B verify -Dinvoker.test=import-certificate-reply,import-certificate-skip-if-alias-exists
  ```

  There are no `setup-*` projects. `MKEYTOOL-10` and `single-ext-test` use `postbuild.groovy`
  instead of `verify.groovy`.

- They run in parallel, one per CPU core (`invoker.parallelThreads` 1C), and every forked Maven
  gets `JAVA_TOOL_OPTIONS=-XX:TieredStopAtLevel=1 -XX:+UseSerialGC`. A test must not depend on
  another test's output or on running alone.

- Write the hook script as `assert` statements. Invoker fails a script whose last expression is
  anything other than `true` or `null`, so a trailing `list.each { }` fails the test; use a `for`
  loop.

## Continuous integration

- `.github/workflows/maven.yml` calls the shared `maven-verify.yml` workflow in `mojohaus/.github`.
- Branches pushed to this repository build under the `push` event.

## Dependencies

- Dependabot updates are monthly and grouped. `maven-plugin-api` stays below 3.10, because the
  plugin is built for the Maven 3.9 line.

## Documentation updates

A change that users or contributors can notice ships with the documentation it affects, in the same pull
request. A refactor with no visible effect needs none.

- A new goal, parameter or default: the Javadoc of the mojo field, which becomes the goal page, and the goal
  list in `src/site/markdown/index.md`. Every `@Parameter` has a description, and one that is accepted but
  ignored says so.
- New or changed usage: an example in `src/site/markdown/usage.md`, and `examples.md` or `faq.md` where they
  repeat it. Only the first plugin declaration of `usage.md` carries a version, written as
  `<version><!-- latest release --></version>`; the others leave it out, so no example goes stale.
- The goal parameters have no command-line `property`; a page that shows `-Dname=value` must go through a
  property of the POM.
- A page added, removed or renamed: the menu in `src/site/site.xml`. Never rename a published page; its URL is
  linked from elsewhere.
- The Maven or Java requirement, or how to build and release: `README.md`, and the Requirements section of
  `src/site/markdown/index.md`.
- A new convention, build gate or test fixture: this file.

Check the result with `mvn site` and open `target/site/index.html`.

## Pull requests

- Squash, merge and rebase merges are all enabled. Release Drafter, through the shared
  release-drafter workflow, builds the release notes from pull request titles
  and labels, so the title should describe the change for a user.

