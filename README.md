# Kradle

Swiss army knife for Kotlin/JVM development.

`kradle` is a plugin for Gradle, adding essential features for Kotlin/JVM development to your build.

With a few lines of configuration, you will be able to:

- [Bootstrap new projects](#bootstrapping)
- [Check for dependency updates](#dependency-updates)
- [Run vulnerability scans](#vulnerability-scans)
- [Run static code analysis](#code-analysis)
- [Add automatic restart on code change](#development-mode)
- [Add support for integration and functional testing](#test-improvements)
- [Run JMH benchmarks](#benchmarks)
- [Create Uber-Jars](#packaging)
- [Create Docker images](#docker)
- [Generate documentation](#documentation)

`kradle` takes care of applying the required plugins and configuring them.

## (Very) Quick Start

```shell
mkdir demo && cd demo
curl -O https://raw.githubusercontent.com/mrkuz/kradle/stable/examples/app/settings.gradle.kts
curl -O https://raw.githubusercontent.com/mrkuz/kradle/stable/examples/app/build.gradle.kts
gradle bootstrap
```

Run application:

`./gradlew run`

Package application and run JAR:

`./gradlew uberJar && java -jar build/libs/demo-1.0.0-uber.jar`

Build Docker image and run container:

`./gradlew buildImage && docker run --rm demo`


## Quick Start

Add the `kradle` plugin to your build script: `net.bitsandbobs.kradle`.

_build.gradle.kts_

```kotlin
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    id("net.bitsandbobs.kradle") version "2.0.0"
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

Make sure you apply the Kotlin plugin before `kradle`. For applications, you also have to provide the `mainClass`.

If you are starting from scratch, you can run `gradle boostrap` to initialize Git, add Gradle wrapper and create essential directories
and files.

With `kradle` applied, many new [tasks](#tasks) become available.

The example above uses the Kotlin/JVM application [preset](#presets).

## Tasks

| Task | Description | Alias for | Plugins used |
|---|---|---|---|
| [bootstrap](#bootstrapping) | Bootstraps app/lib project | - | - |
| [showDependencyUpdates](#dependency-updates) | Displays dependency updates | - | [Gradle Versions Plugin](https://plugins.gradle.org/plugin/com.github.ben-manes.versions) |
| [lint](#linting) | Runs [ktlint](https://ktlint.github.io/) | ktlintCheck | [ktlint Plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint) |
| [analyzeCode](#code-analysis) | Runs [detekt](https://detekt.github.io/detekt/) code analysis | - | [detekt Plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt) |
| [generateDetektConfig](#code-analysis) | Generates _detekt-config.yml_ | - | - |
| [analyzeDependencies](#vulnerability-scans) | Analyzes dependencies for vulnerabilities | - | [OWASP Dependency Check Plugin](https://plugins.gradle.org/plugin/org.owasp.dependencycheck) |
| [dev](#development-mode) | Runs the application and stops it when sources change (use with `-t`, applications only) | - | - |
| [runBenchmarks](#benchmarks) | Runs [JMH](https://github.com/openjdk/jmh) benchmarks | benchmark | [kotlinx.benchmark Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlinx.benchmark) |
| [integrationTest](#test-improvements) | Runs integration tests | - | - |
| [functionalTest](#test-improvements) | Runs functional tests | - | - |
| [generateDocumentation](#documentation) | Generates [Dokka](https://kotlin.github.io/dokka/) HTML documentation | - | [Dokka Plugin](https://plugins.gradle.org/plugin/org.jetbrains.dokka) |
| [package](#packaging) | Creates JAR | jar | [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html) |
| [uberJar](#packaging) | Creates Uber-JAR (applications only) | - | [Gradle Shadow Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow) |
| [buildImage](#docker) | Builds Docker image (applications only) | - | [Jib Plugin](https://plugins.gradle.org/plugin/com.google.cloud.tools.jib) |
| [install](#library-development) | Installs JAR to local Maven repository (libraries only) |  publishToMavenLocal | [Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html) |
| [generateGitignore](#git-integration) | Generates _.gitignore_ | - | - |
| [generateBuildProperties](#build-properties) | Generates _build.properties_ | - | - |

---

## Features

Features provided by `kradle` must be enabled explicitly. For example to enable benchmarks:

```kotlin
kradle {
    jvm {
        benchmarks.enable()
    }
}
```

If the feature has options, you can pass in a configuration code block. This configures and enables the feature.

```kotlin
kradle {
    jvm {
        benchmark {
            jmhVersion("1.34")
        }
    }
}
```

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

If [Git integration](#git-integration) is enabled, the Git commit id is also added.

The task is executed after `processResources`.

```properties
project.name=...
project.group=...
project.version=...
build.timestamp=...
git.commit-id=...
```

### JVM

```kotlin
kradle {
    jvm {
        ...
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

- `targetJvm`: Sets `sourceCompatibility` and `targetCompatibility`

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

Plugins used: [kotlinx.serialization Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.serialization)
, [All-open Compiler Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.allopen)

#### Options

```kotlin
kradle {
    jvm {
        kotlin {
            useCoroutines(/* "1.6.0" */)
            lint {
                ktlintVersion("0.43.2")
            }
            codeAnalysis {
                detektVersion("1.19.0")
                detektConfigFile("detekt-config.yml")
            }
            test {
                useKotest(/* "5.0.3" */)
                useMockk(/* "1.12.1" */)
            }
        }
    }
}
```

- `useCoroutines`: Adds Kotlin coroutines dependency
- `ktlintVersion`: [ktlint](https://ktlint.github.io/) version used for [linting](#linting) (if enabled)
- `detektVersion`: [detekt](https://detekt.github.io/detekt/) version used for [static code analysis](#code-analysis) (if enabled)
- `detectConfigFile`: [detekt](https://detekt.github.io/detekt/) configuration file used
- `useKoTest`: Adds [kotest](https://kotest.io/) test dependencies (if [test improvements](#test-improvements) are enabled)
- `useMockk`: Adds [mockk](https://mockk.io/) test dependency (if [test improvements](#test-improvements) are enabled)

### Application development

```kotlin
kradle {
    jvm {
        application.enable()
    }
}
```

Conflicts with [library development](#library-development).

Plugins used: [Application Plugin](https://docs.gradle.org/current/userguide/application_plugin.html)

#### Options

```kotlin
kradle {
    jvm {
        application {
            mainClass("...")
        }
    }
}
```

- `mainClass`: Sets the main class (required)

### Library development

```kotlin
kradle {
    jvm {
        library.enable()
    }
}
```

Adds the task `install`, which installs the library to your local Maven repository.

Conflicts with [application development](#application-development).

Plugins used: [Java Library Plugin](https://docs.gradle.org/current/userguide/java_library_plugin.html), [Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html)

### Dependency updates

```kotlin
kradle {
    jvm {
        dependencyUpdates.enable()
    }
}
```

Adds the task `showDependencyUpdates`, which shows all available dependency updates. It only considers stable versions; no release candidates or milestone builds.

Plugins used: [Gradle Versions Plugin](https://plugins.gradle.org/plugin/com.github.ben-manes.versions)

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

### Linting

```kotlin
kradle {
    jvm {
        lint.enable()
    }
}
```

Adds the task `lint`, which runs [ktlint](https://ktlint.github.io/) on the project. It uses the standard rule set (including experimental rules) with one exception: Wildcard imports are allowed.

`lint` is executed when running `check`.

Plugins used: [ktlint Plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint)

### Code analysis

```kotlin
kradle {
    jvm {
        codeAnalysis.enable()
    }
}
```

Adds the task `analyzeCode`, which runs [detekt](https://detekt.github.io/detekt/) static code analysis.

Adds the tasks `generateDetektConfig`, which generates a configuration file with sane defaults.

`analyzeCode` is executed when running `check`.

Plugins used: [detekt Plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt)

### Development mode

```kotlin
kradle {
    jvm {
        developmentMode.enable()
    }
}
```

Adds the task `dev`, which watches the directories _src/main/kotlin_ and _src/main/resource_. If changes are detected, the
application is stopped. Should be used with continuous build flag `-t` to archive automatic rebuilds and restarts.

When launching the application with `dev`, the environment variable `DEV_MODE=true` is set.

To speed up application start, the JVM flag `-XX:TieredStopAtLevel=1` is used.

Requires [application development](#application-development).

### Test improvements

```kotlin
kradle {
    test {
        test.enable()
    }
}
```

Test file names can end with `Test`, `Tests` or `IT`.

Plugins used: [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)
, [JaCocCo Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html),
[Gradle Test Logger Plugin](https://plugins.gradle.org/plugin/com.adarshr.test-logger)

#### Options

```kotlin
kradle {
    jvm {
        test {
            prettyPrint(true)
            withIntegrationTests(true)
            withFunctionalTests(true)
            withJunitJupiter(/* "5.8.2" */)
            withJacoco(/* "0.8.7" */)
        }
    }
}
```

- `prettyPrint`: Prettifies test output with [Gradle Test Logger Plugin](https://plugins.gradle.org/plugin/com.adarshr.test-logger)
- `withIntegrationTests`: Adds task `integrationTest`, which runs tests under _src/integrationTest_. The task is executed when running `check`.
- `withFunctionalTests`: Adds task `functionalTest`, which runs tests under _src/functionalTest_. The task is executed when running `check`.
- `withJunitJupiter`: Sets up [JUnit Jupiter](https://junit.org/junit5/) for running tests
- `withJacoco`: Generates [JaCoCo](https://www.jacoco.org/jacoco/) code coverage reports after tests. They can be found under _build/reports/jacoco/_.

### Benchmarks

```kotlin
kradle {
    jvm {
        benchmarks.enable()
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
            jmhVersion("1.34")
        }
    }
}
```

- `jmhVersion`: [JMH](https://github.com/openjdk/jmh) version used

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
                minimize(true)
            }
        }
    }
}
```

- `minimize`: Minimizes Uber-Jar, only required classes are added

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

Requires [application development](#application-development).

#### Options

```kotlin
kradle {
    jvm {
        docker {
            baseImage("bellsoft/liberica-openjdk-alpine:17")
            ports.add(8080)
            jvmOpts("-Xmx1G")
            withJvmKill(/* "1.16.0" */)
            withAppSh(true)
        }
    }
}
```

- `baseImage`: The base image used
- `ports`: List of exposed ports
- `jvmOpts`: Options passed to the JVM
- `withJvmKill`: Adds [jvmkill](https://github.com/airlift/jvmkill) to the image. [jvmkill](https://github.com/airlift/jvmkill) terminates the JVM if it is unable to allocate memory.
- `withAppSh`: Uses a script as entrypoint for the container. You can provide your own script in _src/main/extra/app.sh_. If you don't, `kradle` will create one for you.

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

Package and module documentation can be placed in files _package.md_ or _module.md_ in the project or any source directory.

Plugins used: [Dokka Plugin](https://plugins.gradle.org/plugin/org.jetbrains.dokka)

## Presets

Presets preconfigure `kradle` for specific use cases.

You can override the configuration. For example, you create a new library, but don't want _build.properties_ to be generated:

```kotlin
kradle {
    kotlinJvmLibrary {
        jvm {
            buildProperties.disable()
        }
    }
}
```

The overridden configuration must be placed inside the preset block. Following will **NOT** work:

```kotlin
kradle {
    kotlinJvmLibrary.activate()
    jvm {
        buildProperties.disable()
    }
}
```

### Kotlin/JVM application

```kotlin
kradle {
    kotlinJvmApplication {
        jvm {
            application {
                mainClass("...")
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
            test {
                useKotest()
                useMockk()
            }
        }
        application {
            mainClass("...")
        }

        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()
        developmentMode.enable()

        test {
            prettyPrint(true)
            withIntegrationTests(true)
            withFunctionalTests(true)
            withJunitJupiter()
            withJacoco()
        }

        benchmark.enable()
        packaging.enable()
        docker {
            withJvmKill()
        }
        documentation.enable()
    }
}
```

### Kotlin/JVM library

```kotlin
kradle {
    kotlinLibraryApplication.activate()
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
            test {
                useKotest()
                useMockk()
            }
        }
        library.enable()
        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()

        test {
            prettyPrint(true)
            withIntegrationTests(true)
            withFunctionalTests(true)
            withJunitJupiter()
            withJacoco()
        }

        benchmark.enable()
        packaging.enable()
        documentation.enable()
    }
}
```

## Appendix A: Configuration reference

This example configuration shows all available options.

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
            useCoroutines(/* "1.6.0" */)
            lint {
                ktlintVersion("0.43.2")
            }
            codeAnalysis {
                detektConfigFile("detekt-config.yml")
                detektVersion("1.19.0")
            }
            test {
                useKotest(/* "5.0.3" */)
                useMockk(/* "1.12.1" */)
            }
        }
        application {
            mainClass("...")
        }
        // library.enable() // Conflicts with application

        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()
        developmentMode.enable()

        test {
            prettyPrint(true)
            withIntegrationTests(true)
            withFunctionalTests(true)
            withJunitJupiter(/* "5.8.2" */)
            withJacoco(/* "0.8.7" */)
        }

        benchmark {
            jmhVersion("1.34")
        }

        packaging {
            uberJar {
                minimize(true)
            }
        }

        docker {
            baseImage("bellsoft/liberica-openjdk-alpine:17")
            withJvmKill(/* "1.16.0" */)
            withAppSh(true)
            ports.add(8080)
            jvmOpts("-Xmx1G")
        }

        documentation.enable()
    }
}
```

## Changelog

### Version 2.0.0 (2021-12-29)

- Fix `bootstrap` for multi-project builds
- Fix exclusion of alpha versions and RCs in `showDependencyUpdates`
- Set default target JVM to 17
- Update plugins and dependencies
- New plugin `net.bitsandbobs.kradle`, which combines the functionality of `kradle-app` and `kradle-lib`
- New configuration DSL (see [Appendix A](#appendix-a-configuration-reference))
- Deprecate `kradle-app` and `kradle-lib`, consider using `net.bitsandbobs.kradle` instead

### Version 1.2.0 (2021-09-23)

- The tasks `showDependencyUpdates`, `analyzeCode`, `analyzeDependencies`, `generateDocumentation`,
  `uberJar` and `buildImage` are no longer aliases. Instead, they are independent tasks.
- Support use of `@JvmName`
- Automatically add `kotlin-reflect` to project dependencies
- __Breaking change__: `run` no longer sets `DEV_MODE=true`
- JMH and detekt versions are now configurable
- Strict JSR-305 processing
- Fix package statement of main class generated by `bootstrap`
- __Breaking change__: The JAR created by `uberJar` is no longer minimized by default
- __Breaking change__: Content of _build.properties_ generated by `generateBuildProperties` changed

  Before

  ```properties
  version=...
  timestamp=...
  git.commit-id=...
  ```

  After

  ```properties
  project.name=...
  project.group=...
  project.version=...
  build.timestamp=...
  git.commit-id=...
  ```

### Version 1.1.0 (2021-09-09)

- New task `bootstrap`: Bootstraps new app/lib project
- New task `dev`: Runs the application and stops it when sources change (for automatic rebuilds and restarts)
- New task `generateGitignore`: Generates _.gitignore_
- Added source sets and tasks for integration and functional tests
- module.md and package.md for Dokka can also be placed in any source directory
- Syntactic sugar: Added methods for configuration

  Before

    ```kotlin
    kradle {
        targetJvm.set("16")
    }
    ```

  After

    ```kotlin
    kradle {
        targetJvm("16")
    }
    ```

- Added configuration for main class

  Before

    ```kotlin
    application {
        mainClass.set("com.example.demo.AppKt")
    }
    ```

  After

    ```kotlin
    kradle {
        mainClass("com.example.demo.App")
    }
    ```


- Moved JaCoCo version to `tests`

  Before

    ```kotlin
    kradle {
        jacocoVersion("0.8.7")
    }
    ```

  After

    ```kotlin
    kradle {
        tests {
            jacocoVersion("0.8.7")
        }
    }
    ```
