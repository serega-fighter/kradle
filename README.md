# Kradle

[![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/net.bitsandbobs.kradle)](https://plugins.gradle.org/plugin/net.bitsandbobs.kradle)

Swiss army knife for Kotlin/JVM (and also Java) development.

`kradle` is a Gradle plugin, which sets up your Kotlin/JVM (or Java) project in no time.

With a few lines of configuration, you will be able to:

- [Bootstrap new projects](#feature-bootstrap)
- [Check for dependency updates](#feature-dependencies)
- [Run vulnerability scans](#feature-vulnerability-scan)
- [Run static code analysis](#feature-code-analysis)
- [Add automatic restart on code change](#feature-development-mode)
- [Add support for integration and functional testing](#feature-test)
- [Run test coverage analysis](#feature-code-coverage)
- [Run JMH benchmarks](#feature-benchmark)
- [Create Uber-Jars](#feature-packaging)
- [Create Docker images](#feature-docker)
- [Generate documentation](#feature-documentation)

Most of the functionality is provided by other well-known plugins. `kradle` takes care of the setup and provides a unified configuration DSL.

## Table of contents

- [What's new?](#whats-new)
- [(Very) Quick Start](#very-quick-start)
- [Quick Start](#quick-start)
- [Tasks](#tasks)
    - [`bootstrap`](#task-bootstrap)
    - [`showDependencyUpdates`](#task-show-dependency-updates)
    - [`analyzeDependencies`](#task-analyze-dependencies)
    - [`lint`](#task-lint)
    - [`analyzeCode`](#task-analyze-code)
    - [`dev`](#task-dev)
    - [`runBenchmarks`](#task-run-benchmarks)
    - [`integrationTest`](#task-integration-test)
    - [`functionalTest`](#task-functional-test)
    - [`runTests`](#task-run-tests)
    - [`analyzeTestCoverage`](#task-analyze-test-coverage)
    - [`generateDocumentation`](#task-generate-documentation)
    - [`package`](#task-package)
    - [`uberJar`](#task-uber-jar)
    - [`buildImage`](#task-build-image)
    - [`install`](#task-install)
    - [`generateGitignore`](#task-generate-git-ignore)
    - [`generateBuildProperties`](#task-generate-build-properties)
    - [`generateCheckstyleConfig`](#task-generate-checkstyle-config)
    - [`generateDetektConfig`](#task-generate-detekt-config)
    - [`generateLombokConfig`](#task-generate-lombok-config)

- [Features](#features)
    - [Bootstrapping](#feature-bootstrap)
    - [Git integration](#feature-git)
    - [Project properties](#feature-project-properties)
    - [Build properties](#feature-build-properties)
    - [Kotlin development](#feature-kotlin)
    - [Java development](#feature-java)
    - [Application development](#feature-application)
    - [Library development](#feature-library)
    - [Dependency management](#feature-dependencies)
    - [Vulnerability scans](#feature-vulnerability-scan)
    - [Linting](#feature-lint)
    - [Code analysis](#feature-code-analysis)
    - [Development mode](#feature-development-mode)
    - [Test improvements](#feature-test)
    - [Code coverage](#feature-code-coverage)
    - [Benchmarks](#feature-benchmark)
    - [Packaging](#feature-packaging)
    - [Docker](#feature-docker)
    - [Documentation](#feature-documentation)
- [Presets](#presets)
    - [Kotlin/JVM application](#preset-kotlin-jvm-application)
    - [Kotlin/JVM library](#preset-kotlin-jvm-library)
    - [Java application](#preset-java-application)
    - [Java library](#preset-java-library)
- [Configuration DSL reference](#configuration-reference)
- [How to report bugs](#bugs)
- [Versioning](#versioning)
- [License](#license)

<a id="whats-new"></a>
## What's new?

See [CHANGELOG](CHANGELOG.md).

<a id="very-quick-start"></a>
## (Very) Quick Start

Kotlin:

```shell
mkdir demo && cd demo
curl -O https://raw.githubusercontent.com/mrkuz/kradle/stable/examples/kotlin/app/settings.gradle.kts
curl -O https://raw.githubusercontent.com/mrkuz/kradle/stable/examples/kotlin/app/build.gradle.kts
gradle bootstrap
```

Java:

```shell
mkdir demo && cd demo
curl -O https://raw.githubusercontent.com/mrkuz/kradle/stable/examples/java/app/settings.gradle.kts
curl -O https://raw.githubusercontent.com/mrkuz/kradle/stable/examples/java/app/build.gradle.kts
gradle bootstrap
```

Run application:

`./gradlew run`

Package application and run JAR:

`./gradlew uberJar && java -jar build/libs/demo-1.0.0-uber.jar`

Build Docker image and run container:

`./gradlew buildImage && docker run --rm demo`

<a id="quick-start"></a>
## Quick Start

Add the `kradle` plugin to your build script: `net.bitsandbobs.kradle`.

_build.gradle.kts_

```kotlin
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    id("net.bitsandbobs.kradle") version "2.2.0"
}

group = "com.example"
version = "1.0.0"

kradle {
    kotlinJvmApplication {
        jvm {
            application {
                mainClass("com.example.demo.AppKt")
            }
        }
    }
}
```

Make sure you apply the Kotlin plugin before `kradle`. For applications, you have to provide the `mainClass`.

If you are starting from scratch, you can run `gradle boostrap` to initialize Git, add Gradle wrapper and create essential directories
and files.

The example above uses the Kotlin/JVM application [preset](#presets).

Check the [configuration reference](#configuration-reference) to see all available options.

For Java projects apply the Java plugin instead of the Kotlin plugin.

<a id="tasks"></a>
## Tasks

Which tasks are available, depends on the [features](#features) enabled.

| Task | Description | Alias for | Plugins used |
|---|---|---|---|
| <a id="task-bootstrap"></a>[bootstrap](#feature-bootstrap) | Bootstraps app/lib project | - | - |
| <a id="task-show-dependency-updates"></a>[showDependencyUpdates](#feature-dependencies) | Displays dependency updates | - | [Gradle Versions Plugin](https://plugins.gradle.org/plugin/com.github.ben-manes.versions) |
| <a id="task-lint-"></a>[lint](#feature-linti) | Runs [ktlint](https://ktlint.github.io/) (Kotlin) and [checkstyle](https://checkstyle.sourceforge.io/) (Java) | - | [ktlint Plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint), [Checkstyle Plugin](https://docs.gradle.org/current/userguide/checkstyle_plugin.html) |
| <a id="task-analyze-code"></a>[analyzeCode](#feature-code-analysis) | Runs [detekt](https://detekt.github.io/detekt/) (Kotlin), [PMD](https://pmd.github.io/) (Java) and [SpotBugs](https://spotbugs.github.io/) (Java) code analysis | - | [detekt Plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt), [PMD Plugin](https://docs.gradle.org/current/userguide/pmd_plugin.html), [SpotBugs Plugin](https://plugins.gradle.org/plugin/com.github.spotbugs) |
| <a id="task-analyze-dependencies"></a>[analyzeDependencies](#feature-vulnerability-scan) | Analyzes dependencies for vulnerabilities | - | [OWASP Dependency Check Plugin](https://plugins.gradle.org/plugin/org.owasp.dependencycheck) |
| <a id="task-dev"></a>[dev](#feature-development-mode) | Runs the application and stops it when sources change (use with `-t`, applications only) | - | - |
| <a id="task-run-benchmarks"></a>[runBenchmarks](#feature-benchmark) | Runs [JMH](https://github.com/openjdk/jmh) benchmarks | benchmark | [kotlinx.benchmark Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlinx.benchmark) |
| <a id="task-integration-test"></a>[integrationTest](#feature-test) | Runs integration tests | - | - |
| <a id="task-functional-test"></a>[functionalTest](#feature-test) | Runs functional tests | - | - |
| <a id="task-run-tests"></a>[runTests](#feature-test) | Runs all tests | - | - |
| <a id="task-analyze-test-coverage"></a>[analyzeTestCoverage](#feature-code-coverage) | Runs test coverage analysis | - | [Kover](https://github.com/Kotlin/kotlinx-kover), [JaCocCo Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html) |
| <a id="task-generate-documentation"></a>[generateDocumentation](#feature-documentation) | Generates [Dokka](https://kotlin.github.io/dokka/) HTML documentation | - | [Dokka Plugin](https://plugins.gradle.org/plugin/org.jetbrains.dokka) |
| <a id="task-package"></a>[package](#feature-packaging) | Creates JAR | jar | [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html) |
| <a id="task-uber-jar"></a>[uberJar](#feature-packaging) | Creates Uber-JAR (applications only) | - | [Gradle Shadow Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow) |
| <a id="task-build-image"></a>[buildImage](#feature-docker) | Builds Docker image (applications only) | - | [Jib Plugin](https://plugins.gradle.org/plugin/com.google.cloud.tools.jib) |
| <a id="task-install"></a>[install](#feature-library) | Installs JAR to local Maven repository (libraries only) |  publishToMavenLocal | [Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html) |
| <a id="task-generate-git-ignore"></a>[generateGitignore](#feature-git) | Generates _.gitignore_ | - | - |
| <a id="task-generate-build-properties"></a>[generateBuildProperties](#feature-build-properties) | Generates _build.properties_ | - | - |
| <a id="task-generate-detekt-config"></a>[generateDetektConfig](#feature-code-analysis) | Generates _detekt-config.yml_ | - | - |
| <a id="task-generate-checkstyle-config"></a>[generateCheckstyleConfig](#feature-code-analysis) | Generates _checkstyle.xml_ | - | - |
| <a id="task-generate-lombok-config"></a>[generateLombokConfig](#feature-java) | Generates _lombok.config_ | - | - |
| kradleDump | Dumps kradle diagnostic information | - | - |

<a id="features"></a>
## Features

`kradle` groups its functionality into features. They must be enabled explicitly. For example to get support for benchmarks:

```kotlin
kradle {
    jvm {
        benchmark.enable()
    }
}
```

You can use one of the following statements, whatever fits your taste.

```kotlin
benchmark.enable()
benchmark(true)
benchmark()
```

If the feature has options, `enable` takes a configuration code block as argument.

```kotlin
kradle {
    jvm {
        benchmark.enable {
            jmh {
                version("1.34")
            }
        }
    }
}
```

This configures and enables the feature. You can omit `enable` and use `benchmark { … }`.
To configure the feature without enabling it, use `configureOnly`.

It is also possible to disable features. This can be useful if you are using [presets](#presets) and want to get rid of inherited features.

```kotlin
benchmark.disable()
benchmark(false)
```

Options shown in this section of the documentation represent the defaults.

If the name of the option starts with `use`, it adds dependencies to your project (e.g. `useKotest`).

Features can have sub-features. For example, `junitJupiter` is a sub-feature of `test`.

```kotlin
kradle {
    jvm {
        test {
            junitJupiter.enable {
                version("5.8.2")
            }
        }
    }
}
```

In contrast to normal features, some of them are enabled per default.

<a id="feature-set-general"></a>
### General

```kotlin
kradle {
    general {
        …
    }
}
```

Groups general features.

<a id="feature-bootstrap"></a>
### Bootstrapping

```kotlin
kradle {
    general {
        bootstrap.enable()
    }
}
```

Adds the task `bootstrap`, which

- Initializes Git
- Adds Gradle wrapper
- Creates essentials directories and files

<a id="feature-git"></a>
### Git integration

```kotlin
kradle {
    general {
        git.enable()
    }
}
```

Adds the task `generateGitignore`, which generates _.gitignore_ with sane defaults.

`gitCommit` is added to the project properties.

<a id="feature-project-properties"></a>
### Project properties

```kotlin
kradle {
    general {
        projectProperties.enable()
    }
}
```

Looks for a file called _project.properties_ in the project directory. If found, the entries are added to the
project properties.

<a id="feature-build-properties"></a>
### Build properties

```kotlin
kradle {
    general {
        buildProperties.enable()
    }
}
```

Adds the task `generateBuildProperties`, which generates a file _build.properties_ containing the project name, group, version and the
build timestamp.

If [Git integration](#feature-git) is enabled, the Git commit id is added.

The task is executed after `processResources`.

```properties
project.name=…
project.group=…
project.version=…
build.timestamp=…
git.commit-id=…
```

<a id="feature-set-jvm"></a>
### JVM features

```kotlin
kradle {
    jvm {
        …
    }
}
```

Groups JVM related features.

#### Options

```kotlin
kradle {
    jvm {
        targetJvm("17")
    }
}
```

- `targetJvm`: Sets target release (`"--release"`)

<a id="feature-kotlin"></a>
### Kotlin development

```kotlin
kradle {
    jvm {
        kotlin.enable()
    }
}
```

Adds Kotlin Standard Library, Kotlin reflection library, and kotlin.test library dependencies.

Enables [Opt-ins](https://kotlinlang.org/docs/opt-in-requirements.html).

[JSR-305](https://jcp.org/en/jsr/detail?id=305) nullability mismatches are reported as error (`"-Xjsr305=strict"`).

Plugins used: [kotlinx.serialization Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.serialization),
[All-open Compiler Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.allopen),
[Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html),
[detekt Plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt),
[ktlint Plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint)

#### Sub-features

- [detekt](https://detekt.github.io/detekt/) static code analysis
  - Enabled per default
  - Requires feature [code analysis](#feature-code-analysis)
  - Adds the tasks `generateDetektConfig`, which generates a configuration file with sane defaults

- [ktlint](https://ktlint.github.io/)
  - Enabled per default
  - Requires feature [linting](#feature-lint)
  - Uses all standard and experimental rules per default

#### Options

```kotlin
kradle {
    jvm {
        kotlin {
            // useCoroutines("1.6.0")
            lint {
                ktlint.enable {
                    version("0.43.2")
                    rules {
                        // disable("…")
                    }
                }
            }
            codeAnalysis {
                detekt.enable {
                    version("1.19.0")
                    configFile("detekt-config.yml")
                }
            }
            test {
                // useKotest("5.1.0")
                // useMockk("1.12.2")
            }
        }
    }
}
```

- `useCoroutines`: Adds Kotlin coroutines dependency
- `test.useKoTest`: Adds [kotest](https://kotest.io/) test dependencies (only if [test improvements](#feature-test) are enabled)
- `test.useMockk`: Adds [mockk](https://mockk.io/) test dependency (only if [test improvements](#feature-test) are enabled)
- `ktlint.version`: ktlint version used
- `ktlint.rules.disable`: Disables ktlint rule. Can be called multiple times
- `detekt.version`: detekt version used
- `detekt.configFile`: detekt configuration file used

<a id="feature-java"></a>
### Java development

```kotlin
kradle {
    jvm {
        java.enable()
    }
}
```

Plugins used: [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html),
[PMD Plugin](https://docs.gradle.org/current/userguide/pmd_plugin.html),
[SpotBugs Plugin](https://plugins.gradle.org/plugin/com.github.spotbugs),
[Checkstyle Plugin](https://docs.gradle.org/current/userguide/checkstyle_plugin.html)

#### Sub-features

- [PMD](https://pmd.github.io/)
  - Enabled per default
  - Active rule sets: `errorprone`, `multithreading`, `performance` and `security`
- [SpotBugs](https://spotbugs.github.io/)
  - Requires feature [code analysis](#feature-code-analysis)
  - Enabled per default
- [checkstyle](https://checkstyle.sourceforge.io/)
  - Enabled per default
  - Requires feature [linting](#feature-lint)
  - Looks for the configuration file _checkstyle.xml_ in the project root. If not found, `kradle` generates one

#### Options

```kotlin
kradle {
    jvm {
        java {
            previewFeatures(false)
            // withLombok("1.18.22")
            lint {
                checkstyle.enable {
                    version("9.3")
                    configFile("checkstyle.xml")
                }
            }
            codeAnalysis {
                pmd.enable {
                    version("6.42.0")
                    ruleSets {
                        bestPractices(false)
                        codeStyle(false)
                        design(false)
                        documentation(false)
                        errorProne(true)
                        multithreading(true)
                        performance(true)
                        security(true)
                    }
                }
                spotBugs.enable {
                    version("4.5.3")
                    // useFbContrib(7.4.7)
                    // useFindSecBugs(1.11.0)
                }
            }
        }
    }
}
```
- `previewFeatures`: Enables preview features
- `withLombok`: Enables [Project Lombok](https://projectlombok.org/). Adds the task `generateLombokConfig`, which generates _lombok.config_ with sane defaults
- `checkstyle.version`: checkstyle version used
- `checkstyle.configFile`: checkstyle configuration file used
- `pmd.version`: PMD version used
- `pmd.ruleSets.*`: Enables/disables PMD rule sets
- `spotBugs.version`: SpotBugs version used
- `spotBugs.useFbContrib`: Enables [fb-contrib](http://fb-contrib.sourceforge.net/) plugin
- `spotBugs.useFbContrib`: Enables [Find Security Bugs](https://find-sec-bugs.github.io/) plugin

<a id="feature-application"></a>
### Application development

```kotlin
kradle {
    jvm {
        application.enable()
    }
}
```

Conflicts with [library development](#feature-library).

Plugins used: [Application Plugin](https://docs.gradle.org/current/userguide/application_plugin.html)

#### Options

```kotlin
kradle {
    jvm {
        application {
            // mainClass("…")
        }
    }
}
```

- `mainClass`: Sets the main class (required)

<a id="feature-library"></a>
### Library development

```kotlin
kradle {
    jvm {
        library.enable()
    }
}
```

Adds the task `install`, which installs the library to your local Maven repository.

Conflicts with [application development](#feature-application).

Plugins used: [Java Library Plugin](https://docs.gradle.org/current/userguide/java_library_plugin.html), [Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html)

<a id="feature-dependencies"></a>
### Dependency management

```kotlin
kradle {
    jvm {
        dependencies.enable()
    }
}
```

Adds the task `showDependencyUpdates`, which shows all available dependency updates. It only considers stable versions; no alpha, beta, release candidate or milestone builds.

Plugins used: [Gradle Versions Plugin](https://plugins.gradle.org/plugin/com.github.ben-manes.versions)

#### Options

```kotlin
kradle {
    jvm {
        dependencies {
            useCaffeine("3.0.5")
            useGuava("31.0.1-jre")
            useLog4j("2.17.1")
        }
    }
}
```

- `useCaffeine`: Adds [Caffeine](https://github.com/ben-manes/caffeine) caching library
- `useGuava`: Adds [Guava](https://github.com/google/guava) Google core libraries
- `useLog4j`: Adds [log4j](https://logging.apache.org/log4j/2.x/) logging

<a id="feature-vulnerability-scan"></a>
### Vulnerability scans

```kotlin
kradle {
    jvm {
        vulnerabilityScan.enable()
    }
}
```

Adds the task `analyzeDependencies`, which scans all dependencies on the runtime and compile classpath for vulnerabilities.

Plugins used: [OWASP Dependency Check Plugin](https://plugins.gradle.org/plugin/org.owasp.dependencycheck)

<a id="feature-lint"></a>
### Linting

```kotlin
kradle {
    jvm {
        lint.enable()
    }
}
```

Adds the task `lint`, which runs enabled linters.

`lint` is executed when running `check`.

#### Options

```kotlin
kradle {
    jvm {
        lint {
            ignoreFailures(false)
        }
    }
}
```

- `ignoreFailures`: Build is successful, even if there are linting errors

<a id="feature-code-analysis"></a>
### Code analysis

```kotlin
kradle {
    jvm {
        codeAnalysis.enable()
    }
}
```

Adds the task `analyzeCode`, which runs enabled code analysis tools:

`analyzeCode` is executed when running `check`.

#### Options

```kotlin
kradle {
    jvm {
        codeAnalysis {
            ignoreFailures(false)
        }
    }
}
```

- `ignoreFailures`: Build is successful, even if there are code analysis errors

<a id="feature-development-mode"></a>
### Development mode

```kotlin
kradle {
    jvm {
        developmentMode.enable()
    }
}
```

Adds the task `dev`, which watches the directories _src/main/kotlin_, _src/main/java_ and _src/main/resource_. If changes are detected, the
application is stopped. Should be used with continuous build flag `-t` to archive automatic rebuilds and restarts.

When launching the application with `dev`, the environment variable `KRADLE_DEV_MODE=true` is set.

To speed up application start, the JVM flag `-XX:TieredStopAtLevel=1` is used.

Requires [application development](#application-development).

<a id="feature-test"></a>
### Test improvements

```kotlin
kradle {
    jvm {
        test.enable()
    }
}
```

Test file names can end with `Test`, `Tests`, `Spec` or `IT`.

When running tests, the environment variables `KRADLE_PROJECT_DIR` and `KRADLE_PROJECT_ROOT_DIR` are set.

Adds the task `runTests`, which runs all tests (unit, integration, functional, custom).

Plugins used: [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)
, [Gradle Test Logger Plugin](https://plugins.gradle.org/plugin/com.adarshr.test-logger)

#### Sub-features

- [JUnit Jupiter](https://junit.org/junit5/) testing framework
  - Enabled per defaults

#### Options

```kotlin
kradle {
    jvm {
        test {
            junitJupiter.enable {
                version("5.8.2")
            }
            prettyPrint(false)
            showStandardStreams(false)
            withIntegrationTests(false)
            withFunctionalTests(false)
            /*
            withCustomTests("<NAME>")
            useArchUnit("0.22.0")
            useTestcontainers("1.16.3")
            */
        }
    }
}
```

- `junitJupiter.version`: JUnit Jupiter version used
- `prettyPrint`: Prettifies test output with [Gradle Test Logger Plugin](https://plugins.gradle.org/plugin/com.adarshr.test-logger)
- `showStandardStreams`: Shows stdout and stderr in test output
- `withIntegrationTests`: Adds task `integrationTest`, which runs tests under _src/integrationTest_. The task is executed when running `check`
- `withFunctionalTests`: Adds task `functionalTest`, which runs tests under _src/functionalTest_. The task is executed when running `check`
- `withCustomTests`: Adds task `<NAME>Test`, which runs tests under _src/&lt;NAME&gt;_. The task is executed when running `check`. Can be called multiple times
- `useArchUnit`: Adds [ArchUnit](https://www.archunit.org/) test dependencies
- `useTestcontainers`: Adds [Testcontainers](https://www.testcontainers.org/) test dependencies

<a id="feature-code-coverage"></a>
### Code coverage

```kotlin
kradle {
    jvm {
        codeCoverage.enable()
    }
}
```

Adds the task `analyzeTestCoverage`, which runs enabled test coverage tools.

`analyzeTestCoverage` is executed when running `check`.

Plugins used: [Kover](https://github.com/Kotlin/kotlinx-kover),
[JaCocCo Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html)

#### Sub-features

- [Kover](https://github.com/Kotlin/kotlinx-kover)
  - Enabled per default
  - Generates HTML report in _build/reports/kover/_
- [JaCocCo](https://www.jacoco.org/)
  - Generates HTML report in _build/reports/jacoco/_

#### Options

```kotlin
kradle {
    jvm {
        codeCoverage {
            kover {
                excludes("…")
            }
            /*
            jacoco.enable {
                version("0.8.7")
                excludes("…")
            }
            */
        }
    }
}
```

- `kover.excludes`: List of test tasks to exclude from analysis
- `jacoco.version`: JaCoCo version used
- `jacoco.excludes`: List of test tasks to exclude from analysis

<a id="feature-benchmark"></a>
### Benchmarks

```kotlin
kradle {
    jvm {
        benchmark.enable()
    }
}
```

Adds the task `runBenchmarks`, which runs [JMH](https://github.com/openjdk/jmh) benchmarks found under _src/benchmark/kotlin_.

Plugins used: [kotlinx.benchmark Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlinx.benchmark), [All-open Compiler Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.allopen)

#### Options

```kotlin
kradle {
    jvm {
        benchmark {
            jmh {
                version("1.34")
            }
        }
    }
}
```

- `jmh.version`: [JMH](https://github.com/openjdk/jmh) version used

<a id="feature-packaging"></a>
### Packaging

```kotlin
kradle {
    jvm {
        packaging.enable()
    }
}
```

Adds the task `uberJar`, which creates an Uber-Jar. This is a JAR containing all dependencies.

Adds the task `package`, which is an alias for `jar`.

Adds `Main-Class` the manifest, so the JAR is runnable (application only).

Plugins used: [Gradle Shadow Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)

#### Options

```kotlin
kradle {
    jvm {
        packaging {
            uberJar {
                minimize(false)
            }
        }
    }
}
```

- `minimize`: Minimizes Uber-Jar, only required classes are added

<a id="feature-docker"></a>
### Docker

```kotlin
kradle {
    jvm {
        docker.enable()
    }
}
```

Adds the task `buildImage`, which creates a Docker image using [Jib](https://github.com/GoogleContainerTools/jib).

Files in _src/main/extra/_ will be copied to the image directory _/app/extra/_.

Plugins used: [Jib Plugin](https://plugins.gradle.org/plugin/com.google.cloud.tools.jib)

Requires [application development](#feature-application).

#### Options

```kotlin
kradle {
    jvm {
        docker {
            baseImage("bellsoft/liberica-openjdk-alpine:17")
            // ports(…)
            // jvmOpts("…")
            // withJvmKill(1.16.0")
            withStartupScript(false)
        }
    }
}
```

- `baseImage`: Base image used
- `ports`: List of exposed ports
- `jvmOpts`: Options passed to the JVM
- `withJvmKill`: Adds [jvmkill](https://github.com/airlift/jvmkill) to the image, which terminates the JVM if it is unable to allocate memory
- `withStartupScript`: Uses a script as entrypoint for the container. Either you provide your own script at _src/main/extra/app.sh_ or `kradle` will create one

<a id="feature-documentation"></a>
### Documentation

```kotlin
kradle {
    jvm {
        documentation.enable()
    }
}
```

Adds the task `generateDocumentation`, which uses [Dokka](https://kotlin.github.io/dokka/) to generate a HTML documentation based on
KDoc comments. The documentation can be found under _build/docs_.

Package and module documentation can be placed in _package.md_ or _module.md_ in the project or any source directory.

Plugins used: [Dokka Plugin](https://plugins.gradle.org/plugin/org.jetbrains.dokka)

<a id="presets"></a>
## Presets

Presets preconfigure `kradle` for specific use cases. The options set by the preset can be overridden.

Example:

```kotlin
kradle {
    kotlinJvmLibrary {
        jvm {
            buildProperties.disable()
        }
    }
}
```

<a id="preset-kotlin-jvm-application"></a>
### Kotlin/JVM application

```kotlin
kradle {
    kotlinJvmApplication {
        jvm {
            application {
                mainClass("…")
            }
        }
    }
}
```

Same as:

```kotlin
kradle {
    general {
        bootstrap.enable()
        git.enable()
        projectProperties.enable()
        buildProperties.enable()
    }

    jvm {
        kotlin {
            useCoroutines()
            lint {
                ktlint {
                    rules {
                        disable("no-wildcard-imports")
                    }
                }
            }
            test {
                useKotest()
                useMockk()
            }
        }
        application {
            mainClass("…")
        }

        dependencies.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()
        developmentMode.enable()

        test {
            prettyPrint(true)
            withIntegrationTests()
            withFunctionalTests()
        }
        codeCoverage.enable()
        benchmark.enable()
        packaging.enable()
        docker {
            withJvmKill()
        }
        documentation.enable()
    }
}
```

<a id="preset-kotlin-jvm-library"></a>
### Kotlin/JVM library

```kotlin
kradle {
    kotlinJvmLibrary.activate()
}
```

Same as:

```kotlin
kradle {
    general {
        bootstrap.enable()
        git.enable()
        projectProperties.enable()
        buildProperties.enable()
    }

    jvm {
        kotlin {
            useCoroutines()
            lint {
                ktlint {
                    rules {
                        disable("no-wildcard-imports")
                    }
                }
            }
            test {
                useKotest()
                useMockk()
            }
        }
        library.enable()
        dependencies.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()

        test {
            prettyPrint(true)
            withIntegrationTests()
            withFunctionalTests()
        }
        codeCoverage.enable()
        benchmark.enable()
        packaging.enable()
        documentation.enable()
    }
}
```

<a id="preset-java-application"></a>
### Java application

```kotlin
kradle {
    javaApplication {
        jvm {
            application {
                mainClass("…")
            }
        }
    }
}
```

Same as:

```kotlin
kradle {
    general {
        bootstrap.enable()
        git.enable()
        projectProperties.enable()
        buildProperties.enable()
    }

    jvm {
        java {
            withLombok()
            codeAnalysis {
                spotBugs {
                    useFbContrib()
                    useFindSecBugs()
                }
            }
        }
        application {
            mainClass("…")
        }
        dependencies.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()
        developmentMode.enable()

        test {
            prettyPrint(true)
            withIntegrationTests()
            withFunctionalTests()
        }
        codeCoverage.enable()
        benchmark.enable()
        packaging.enable()
        docker {
            withJvmKill()
        }
        documentation.enable()
    }
}
```

<a id="preset-java-library"></a>
### Java library

```kotlin
kradle {
    javaLibrary.activate()
}
```

Same as:

```kotlin
kradle {
    general {
        bootstrap.enable()
        git.enable()
        projectProperties.enable()
        buildProperties.enable()
    }

    jvm {
        java {
            withLombok()
            codeAnalysis {
                spotBugs {
                    useFbContrib()
                    useFindSecBugs()
                }
            }
        }
        library.enable()
        dependencies.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()

        test {
            prettyPrint(true)
            withIntegrationTests()
            withFunctionalTests()
        }
        codeCoverage.enable()
        benchmark.enable()
        packaging.enable()
        documentation.enable()
    }
}
```

<a id="configuration-reference"></a>
## Configuration DSL reference

This example shows all features enabled with their default configuration.

```kotlin
kradle {

    general {
        bootstrap.enable()
        git.enable()
        projectProperties.enable()
        buildProperties.enable()
    }

    jvm {
        targetJvm("17")
        kotlin {
            // useCoroutines("1.6.0")
            lint {
                ktlint.enable {
                    version("0.43.2")
                    rules {
                        // disable("…")
                    }
                }
            }
            codeAnalysis {
                detekt.enable {
                    version("1.19.0")
                    configFile("detekt-config.yml")
                }
            }
            test {
                // useKotest("5.1.0")
                // useMockk("1.12.2")
            }
        }
        java {
            previewFeatures(false)
            // withLombok("1.18.22")
            lint {
                checkstyle.enable {
                    version("9.3")
                    configFile("checkstyle.xml")
                }
            }
            codeAnalysis {
                pmd.enable {
                    version("6.42.0")
                    ruleSets {
                        bestPractices(false)
                        codeStyle(false)
                        design(false)
                        documentation(false)
                        errorProne(true)
                        multithreading(true)
                        performance(true)
                        security(true)
                    }
                }
                spotBugs.enable {
                    version("4.5.3")
                    useFbContrib("7.4.7")
                    useFindSecBugs("1.11.0")
                }
            }
        }
        application {
            // mainClass("…")
        }
        library.enable() // Conflicts with application

        dependencies {
            // useCaffeine("3.0.5")
            // useGuava("31.0.1-jre")
            // useLog4j("2.17.1")
        }

        vulnerabilityScan.enable()
        lint {
            ignoreFailures(false)
        }
        codeAnalysis {
            ignoreFailures(false)
        }
        developmentMode.enable()

        test {
            junitJupiter.enable {
                version("5.8.2")
            }
            prettyPrint(false)
            showStandardStreams(false)
            withIntegrationTests(false)
            withFunctionalTests(false)
            // withCustomTests("…")
            // useArchUnit("0.22.0")
            // useTestcontainers("1.16.3")
        }

        codeCoverage {
            kover.enable {
                // excludes("…")
            }
            jacoco.configureOnly {
                version("0.8.7")
                // excludes("…")
            }
        }

        benchmark {
            jmh {
                version("1.34")
            }
        }

        packaging {
            uberJar {
                minimize(false)
            }
        }

        docker {
            baseImage("bellsoft/liberica-openjdk-alpine:17")
            // jvmOpts("…")
            // ports(…)
            // withJvmKill("1.16.0")
            withStartupScript(false)
        }

        documentation.enable()
    }
}
```

<a id="bugs"></a>
## How to report bugs

Please open a new [issue](https://github.com/mrkuz/kradle/issues) and if possible provide the output of `gradle kradleDump`.

<a id="versioning"></a>
## Versioning (since 2.0.0)

`kradle` uses a `MAJOR.MINOR.PATCH` pattern.

### Patch release

- Contains only bug fixes.
- Configuration DSL will not change.
- Dependencies are only updated when required to fix an issue.

### Minor release

- Introduces new features.
- Contains dependency upgrades.
- Configuration DSL can change, but in a backwards compatible manner.
- Default values for options can change.
- Functionality can change.
- Make sure to check the [CHANGELOG](CHANGELOG.md) before updating.

### Major release

- Configuration DSL can change and violate backwards compatibility (__Breaking change__ in the [CHANGELOG](CHANGELOG.md)).
- Otherwise same as minor release.

<a id="license"></a>
## License

This project is licensed under the terms of the [MIT license](LICENSE).
