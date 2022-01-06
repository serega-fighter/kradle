# Kradle

Swiss army knife for Kotlin/JVM development.

`kradle` is a Gradle plugin, which sets up your Kotlin/JVM (or Java) project in no time.

With a few lines of configuration, you will be able to:

- [Bootstrap new projects](#feature-bootstrap)
- [Check for dependency updates](#feature-dependency-updates)
- [Run vulnerability scans](#feature-vulnerability-scan)
- [Run static code analysis](#feature-code-analysis)
- [Add automatic restart on code change](#feature-development-mode)
- [Add support for integration and functional testing](#feature-test)
- [Run JMH benchmarks](#feature-benchmark)
- [Create Uber-Jars](#feature-packaging)
- [Create Docker images](#feature-docker)
- [Generate documentation](#feature-documentation)

Most of the functionality is provided by other well-known plugins. `kradle` just takes care of the setup and provides a unified configuration DSL.

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
    - [`generateDocumentation`](#task-generate-documentation)
    - [`package`](#task-package)
    - [`uberJar`](#task-uber-jar)
    - [`buildImage`](#task-build-image)
    - [`install`](#task-install)
    - [`generateGitignore`](#task-generate-git-ignore)
    - [`generateBuildProperties`](#task-generate-build-properties)
    - [`generateCheckstyleConfig`](#task-generate-checkstyle-config)
    - [`generateDetektConfig`](#task-generate-detekt-config)

- [Features](#features)
    - [Bootstrapping](#feature-bootstrap)
    - [Git integration](#feature-git)
    - [Project properties](#feature-project-properties)
    - [Build properties](#feature-build-properties)
    - [Kotlin development](#feature-kotlin)
    - [Java development](#feature-java)
    - [Application development](#feature-application)
    - [Library development](#feature-library)
    - [Dependency updates](#feature-dependency-updates)
    - [Vulnerability scans](#feature-vulnerability-scan)
    - [Linting](#feature-lint)
    - [Code analysis](#feature-code-analysis)
    - [Development mode](#feature-development-mode)
    - [Test improvements](#feature-test)
    - [Benchmarks](#feature-benchmark)
    - [Packaging](#feature-packaging)
    - [Docker](#feature-docker)
    - [Documentation](#feature-documentation)
- [Presets](#presets)
    - [Kotlin/JVM application](#preset-kotlin-jvm-application)
    - [Kotlin/JVM library](#preset-kotlin-jvm-library)
    - [Java application](#preset-java-application)
    - [Java library](#preset-java-library)
    - [Configuration reference](#configuration-reference)
- [How to report bugs](#bugs)
- [License](#license)

<a id="whats-new"></a>
## What's new?

See [CHANGELOG](CHANGELOG.md).

<a id="very-quick-start"></a>
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

<a id="quick-start"></a>
## Quick Start

Add the `kradle` plugin to your build script: `net.bitsandbobs.kradle`.

_build.gradle.kts_

```kotlin
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.0"
    id("net.bitsandbobs.kradle") version "2.1.0"
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

The example above uses the Kotlin/JVM application [preset](#presets).

Check the [configuration reference](#configuration-reference) to see all available options.

For Java projects apply the Java plugin instead of the Kotlin plugin.

<a id="tasks"></a>
## Tasks

Which tasks are available, depends on the [features](#features) enabled.

| Task | Description | Alias for | Plugins used |
|---|---|---|---|
| <a id="task-bootstrap"></a>[bootstrap](#feature-bootstrap) | Bootstraps app/lib project | - | - |
| <a id="task-show-dependency-updates"></a>[showDependencyUpdates](#feature-dependency-updates) | Displays dependency updates | - | [Gradle Versions Plugin](https://plugins.gradle.org/plugin/com.github.ben-manes.versions) |
| <a id="task-lint-"></a>[lint](#feature-linti) | Runs [ktlint](https://ktlint.github.io/) (Kotlin) and [checkstyle](https://checkstyle.sourceforge.io/) (Java) | - | [ktlint Plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint), [Checkstyle Plugin](https://docs.gradle.org/current/userguide/checkstyle_plugin.html) |
| <a id="task-analyze-code"></a>[analyzeCode](#feature-code-analysis) | Runs [detekt](https://detekt.github.io/detekt/) (Kotlin), [PMD](https://pmd.github.io/) (Java) and [SpotBugs](https://spotbugs.github.io/) (Java) code analysis | - | [detekt Plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt), [PMD Plugin](https://docs.gradle.org/current/userguide/pmd_plugin.html), [SpotBugs Plugin](https://plugins.gradle.org/plugin/com.github.spotbugs) |
| <a id="task-analyze-dependencies"></a>[analyzeDependencies](#feature-vulnerability-scan) | Analyzes dependencies for vulnerabilities | - | [OWASP Dependency Check Plugin](https://plugins.gradle.org/plugin/org.owasp.dependencycheck) |
| <a id="task-dev"></a>[dev](#feature-development-mode) | Runs the application and stops it when sources change (use with `-t`, applications only) | - | - |
| <a id="task-run-benchmarks"></a>[runBenchmarks](#feature-benchmark) | Runs [JMH](https://github.com/openjdk/jmh) benchmarks | benchmark | [kotlinx.benchmark Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlinx.benchmark) |
| <a id="task-integration-test"></a>[integrationTest](#feature-test) | Runs integration tests | - | - |
| <a id="task-functional-test"></a>[functionalTest](#feature-test) | Runs functional tests | - | - |
| <a id="task-generate-documentation"></a>[generateDocumentation](#feature-documentation) | Generates [Dokka](https://kotlin.github.io/dokka/) HTML documentation | - | [Dokka Plugin](https://plugins.gradle.org/plugin/org.jetbrains.dokka) |
| <a id="task-package"></a>[package](#feature-packaging) | Creates JAR | jar | [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html) |
| <a id="task-uber-jar"></a>[uberJar](#feature-packaging) | Creates Uber-JAR (applications only) | - | [Gradle Shadow Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow) |
| <a id="task-build-image"></a>[buildImage](#feature-docker) | Builds Docker image (applications only) | - | [Jib Plugin](https://plugins.gradle.org/plugin/com.google.cloud.tools.jib) |
| <a id="task-install"></a>[install](#feature-library) | Installs JAR to local Maven repository (libraries only) |  publishToMavenLocal | [Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html) |
| <a id="task-generate-git-ignore"></a>[generateGitignore](#feature-git) | Generates _.gitignore_ | - | - |
| <a id="task-generate-build-properties"></a>[generateBuildProperties](#feature-build-properties) | Generates _build.properties_ | - | - |
| <a id="task-generate-detekt-config"></a>[generateDetektConfig](#feature-code-analysis) | Generates _detekt-config.yml_ | - | - |
| <a id="task-generate-checkstyle-config"></a>[generateCheckstyleConfig](#feature-code-analysis) | Generates _checkstyle.xml_ | - | - |
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

If the feature has options, it takes a configuration code block.

```kotlin
kradle {
    jvm {
        benchmark {
            jmhVersion("1.34")
        }
    }
}
```

This configures and enables the feature. Alternatively you can use `benchmark.enable { ... }`.

It is also possible to deactivate features. This can be useful if you are using [presets](#presets).

```kotlin
benchmark.disable()
benchmark(false)
```

Options shown in this section of the documentation represent the defaults.

<a id="feature-set-general"></a>
### General

```kotlin
kradle {
    general {
        ...
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

If [Git integration](#feature-git) is enabled, the Git commit id is also added.

The task is executed after `processResources`.

```properties
project.name=...
project.group=...
project.version=...
build.timestamp=...
git.commit-id=...
```

<a id="feature-set-jvm"></a>
### JVM features

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

Plugins used: [kotlinx.serialization Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.serialization)
, [All-open Compiler Plugin](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.allopen),
 [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)

#### Options

```kotlin
kradle {
    jvm {
        kotlin {
            // useCoroutines("1.6.0")
            lint {
                ktlint {
                    version("0.43.2")
                    rules {
                        // disable(...)
                    }
                }
            }
            codeAnalysis {
                detekt {
                    version("1.19.0")
                    configFile("detekt-config.yml")
                }
            }
            test {
                // useKotest("5.0.3")
                // useMockk("1.12.2")
            }
        }
    }
}
```

- `useCoroutines`: Adds Kotlin coroutines dependency
- `ktlint.version`: [ktlint](https://ktlint.github.io/) version used (only if [linting](#feature-lint) enabled)
- `ktlint.disable`: Disable [ktlint](https://ktlint.github.io/) rule. Can be called multiple times
- `detekt.version`: [detekt](https://detekt.github.io/detekt/) version used (only if [static code analysis](#feature-code-analysis) is enabled)
- `detekt.configFile`: [detekt](https://detekt.github.io/detekt/) configuration file used
- `useKoTest`: Adds [kotest](https://kotest.io/) test dependencies (only if [test improvements](#feature-test) are enabled)
- `useMockk`: Adds [mockk](https://mockk.io/) test dependency (only if [test improvements](#feature-test) are enabled)

<a id="feature-java"></a>
### Java development

```kotlin
kradle {
    jvm {
        java.enable()
    }
}
```

Plugins used: [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)

#### Options

```kotlin
kradle {
    jvm {
        java {
            previewFeatures(false)
            lint {
                checkstyle {
                    version("9.2.1")
                    configFile("checkstyle.xml")
                }
            }
            codeAnalysis {
                pmd {
                    version("6.41.0")
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
                spotBugs {
                    version("4.5.2")
                    // useFbContrib(7.4.7)
                    // useFindSecBugs(1.11.0)
                }
            }
        }
    }
}
```

- `previewFeatures`: Enable preview features
- `checkstyle.version`: [checkstyle](https://checkstyle.sourceforge.io/) version used (only if [linting](#feature-lint) is enabled)
- `checkstyle.configFile`: [checkstyle](https://checkstyle.sourceforge.io/) configuration file used
- `pmd.version`: [PMD](https://pmd.github.io/) version used (only if [code analysis](#feature-code-analysis) is enabled)
- `pmd.ruleSets.*`: Enable/disable [PMD](https://pmd.github.io/) rule sets
- `spotBugs.version`: [SpotBugs](https://spotbugs.github.io/) version used (only if [code analysis](#code-analysis) is enabled)
- `spotBugs.useFbContrib`: Use [fb-contrib](http://fb-contrib.sourceforge.net/) plugin
- `spotBugs.useFbContrib`: Use [Find Security Bugs](https://find-sec-bugs.github.io/) plugin

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
            // mainClass("...")
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

<a id="feature-dependency-updates"></a>
### Dependency updates

```kotlin
kradle {
    jvm {
        dependencyUpdates.enable()
    }
}
```

Adds the task `showDependencyUpdates`, which shows all available dependency updates. It only considers stable versions; no alpha, beta, release candidate or milestone builds.

Plugins used: [Gradle Versions Plugin](https://plugins.gradle.org/plugin/com.github.ben-manes.versions)

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

Adds the task `lint`, which runs:

- [ktlint](https://ktlint.github.io/) on Kotlin source files. It uses all standard and experimental rules per default.

- [checkstyle](https://checkstyle.sourceforge.io/) on Java source files. Looks for the configuration file _checkstyle.xml_ in the project root. If not found, `kradle` generates one.

`lint` is executed when running `check`.

Plugins used: [ktlint Plugin](https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint)

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

Adds the task `analyzeCode`, which runs:

- [detekt](https://detekt.github.io/detekt/) static code analysis for Kotlin

- [PMD](https://pmd.github.io/) code analysis for Java. Enabled rule sets: `errorprone`, `multithreading`, `performance` and `security`

- [SpotBugs](https://spotbugs.github.io/) code analysis for Java

Adds the tasks `generateDetektConfig`, which generates a configuration file with sane defaults.

`analyzeCode` is executed when running `check`.

Plugins used: [detekt Plugin](https://plugins.gradle.org/plugin/io.gitlab.arturbosch.detekt), [PMD Plugin](https://docs.gradle.org/current/userguide/pmd_plugin.html), [SpotBugs Plugin](https://plugins.gradle.org/plugin/com.github.spotbugs)

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

When launching the application with `dev`, the environment variable `DEV_MODE=true` is set.

To speed up application start, the JVM flag `-XX:TieredStopAtLevel=1` is used.

Requires [application development](#application-development).

<a id="feature-test"></a>
### Test improvements

```kotlin
kradle {
    test {
        test.enable()
    }
}
```

Test file names can end with `Test`, `Tests`, `Spec` or `IT`.

Plugins used: [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)
, [JaCocCo Plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html),
[Gradle Test Logger Plugin](https://plugins.gradle.org/plugin/com.adarshr.test-logger)

#### Options

```kotlin
kradle {
    jvm {
        test {
            prettyPrint(false)
            integrationTests(false)
            functionalTests(false)
            // customTests("<NAME>")
            // withJunitJupiter("5.8.2")
            // withJacoco(0.8.7")
        }
    }
}
```

- `prettyPrint`: Prettifies test output with [Gradle Test Logger Plugin](https://plugins.gradle.org/plugin/com.adarshr.test-logger)
- `integrationTests`: Adds task `integrationTest`, which runs tests under _src/integrationTest_. The task is executed when running `check`.
- `functionalTests`: Adds task `functionalTest`, which runs tests under _src/functionalTest_. The task is executed when running `check`.
- `customTests`: Adds task `<NAME>Test`, which runs tests under _src/&lt;NAME&gt;_. The task is executed when running `check`. Can be called multiple times.
- `withJunitJupiter`: Sets up [JUnit Jupiter](https://junit.org/junit5/) for running tests
- `withJacoco`: Generates [JaCoCo](https://www.jacoco.org/jacoco/) code coverage reports after tests. They can be found under _build/reports/jacoco/_.

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
            jmhVersion("1.34")
        }
    }
}
```

- `jmhVersion`: [JMH](https://github.com/openjdk/jmh) version used

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
            startupScript(false)
            // withJvmKill(1.16.0")
            // ports(...)
            // jvmOpts(...)
        }
    }
}
```

- `baseImage`: The base image used
- `ports`: List of exposed ports
- `jvmOpts`: Options passed to the JVM
- `withJvmKill`: Adds [jvmkill](https://github.com/airlift/jvmkill) to the image. [jvmkill](https://github.com/airlift/jvmkill) terminates the JVM if it is unable to allocate memory.
- `startupScript`: Uses a script as entrypoint for the container. You can provide your own script at _src/main/extra/app.sh_. Otherwise `kradle` will create one.

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

Package and module documentation can be placed in files _package.md_ or _module.md_ in the project or any source directory.

Plugins used: [Dokka Plugin](https://plugins.gradle.org/plugin/org.jetbrains.dokka)

<a id="presets"></a>
## Presets

Presets preconfigure `kradle` for specific use cases.

Configuration can be overriden. Example: You create a new library, but don't want _build.properties_ to be generated:

```kotlin
kradle {
    kotlinJvmLibrary {
        jvm {
            buildProperties.disable()
        }
    }
}
```

The configuration must be placed inside the preset block. Following will **NOT** work:

```kotlin
kradle {
    kotlinJvmLibrary {
    }
    jvm {
        buildProperties.disable()
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
            mainClass("...")
        }

        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()
        developmentMode.enable()

        test {
            prettyPrint(true)
            integrationTests(true)
            functionalTests(true)
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
        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()

        test {
            prettyPrint(true)
            integrationTests(true)
            functionalTests(true)
            withJunitJupiter()
            withJacoco()
        }

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
        java {
            codeAnalysis {
                spotBugs {
                    useFbContrib()
                    useFindSecBugs()
                }
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
            integrationTests(true)
            functionalTests(true)
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
            codeAnalysis {
                spotBugs {
                    useFbContrib()
                    useFindSecBugs()
                }
            }
        }
        library.enable()
        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint.enable()
        codeAnalysis.enable()

        test {
            prettyPrint(true)
            integrationTests(true)
            functionalTests(true)
            withJunitJupiter()
            withJacoco()
        }

        benchmark.enable()
        packaging.enable()
        documentation.enable()
    }
}
```

<a id="configuration-reference"></a>
## Configuration reference

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
                ktlint {
                    version("0.43.2")
                    rules {
                        disable("...")
                    }
                }
            }
            codeAnalysis {
                detekt {
                    version("1.19.0")
                    configFile("detekt-config.yml")
                }
            }
            test {
                useKotest(/* "5.0.3" */)
                useMockk(/* "1.12.2" */)
            }
        }
        java {
            previewFeatures(true)
            lint {
                checkstyle {
                    version("9.2.1")
                    configFile("checkstyle.xml")
                }
            }
            codeAnalysis {
                pmd {
                    version("6.41.0")
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
                spotBugs {
                    version("4.5.2")
                    useFbContrib(/* 7.4.7 */)
                    useFindSecBugs(/* 1.11.0 */)
                }
            }
        }
        application {
            mainClass("...")
        }
        library.enable() // Conflicts with application

        dependencyUpdates.enable()
        vulnerabilityScan.enable()
        lint {
            ignoreFailures(false)
        }
        codeAnalysis {
            ignoreFailures(false)
        }
        developmentMode.enable()

        test {
            prettyPrint(true)
            integrationTests(true)
            functionalTests(true)
            customTests(...)
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
            startupScript(true)
            ports(...)
            jvmOpts(...)
        }

        documentation.enable()
    }
}
```

<a id="bugs"></a>
## How to report bugs

Please open a new [issue](https://github.com/mrkuz/kradle/issues) and if possible provide the output of `gradle kradleDump`.

<a id="license"></a>
## License

This project is licensed under the terms of the [MIT license](LICENSE).
