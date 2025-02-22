# Kradle

## Changelog

### Version main-SNAPSHOT (2022-02-15)

#### Bugfixes
#### Additions
#### Changes

### Version 2.3.1 (2022-02-15)

#### Bugfixes

- Fix support for Gradle 7.4

### Version 2.3.0 (2022-02-14)

#### Bugfixes

- `kover`: Exclude `benchmark` sources from analysis

#### Additions

- New feature `scripts`
- New feature `buildProfiles`
- New feature `helm` ([Helm](https://helm.sh/))
- New alias `compile` for `classes`
- New alias `verify` for `check`
- New project property `gitBranchPrefix`
- `docker`: New task `pushImage`
- `docker`: New options `imageName`, `arguments` and `allowInsecureRegistries`
- `docker`: Add project property `imageName`
- `docker`: Add `jvmOptions` as alternative to `jvmOpts`

#### Changes

- `bootstrap`: Call `git add -u` when done
- `docker`: Use project name as default image name (instead of root project name)
- `package`: Use project name as default artifact name (instead of root project name)
- `detekt`: Config file is relative to project directory (instead of root project directory)
- `checkstyle`: Config file is relative to project directory (instead of root project directory)
- `generateLombokConfig`: Config file is generated in project directory (instead of root project directory)
- Look for _project.properties_ in project directory (instead of root project directory)

### Version 2.2.0 (2022-02-06)

#### Bugfixes

- `jvm`: Fix support for target JVM 8
- `benchmarks`: Fix pure Java projects with benchmarks enabled
- `docker`:  Fix `buildImage` in combination with `startupScript` or `withJvmKill` (first image created was missing extra files)
- `bootstrap`: Don't overwrite existing _App.kt_ or _App.java_ files

#### Additions

- New project properties: `mainClass` and `gitBranch`
- New feature `dependencies` (supersedes `dependencyUpdates`)
- New feature `codeCoverage` and task `analyzeTestCoverage`
- Add support for [Project Lombok](https://projectlombok.org/)
- Add support for [Kover](https://github.com/Kotlin/kotlinx-kover)
- `jacoco`: New option `excludes`
- `jacoco`: Add configuration to `codeCoverage`

    Before

    ```kotlin
    kradle {
        jvm {
            test {
                withJacoco("0.8.7")
            }
        }
    }
    ```

    After

    ```kotlin
    kradle {
        jvm {
            codeCoverage {
                jacoco {
                    version("0.8.7")
                }
            }
        }
    }
    ```
- `test`: New task `runTests`
- `test`: New option `showStandardStreams`
- `test`: Set environment variables `KRADLE_PROJECT_DIR` and `KRADLE_PROJECT_ROOT_DIR`
- `test`: New options `useArchUnit` and `useTestcontainers`
- `test`: Add `test.withCustomTests` as alternative to `test.customTests`
- `test`: Alternative configuration for JUnit Jupiter

    Before

    ```kotlin
    kradle {
        jvm {
            test {
                withJunitJupiter("5.8.2")
            }
        }
    }
    ```

    After

    ```kotlin
    kradle {
        jvm {
            test {
                // junitJupiter()
                // junitJupiter(true)
                // junitJupiter.enable()
                // junitJupiter.enable { … }
                junitJupiter {
                    version("5.8.2")
                }
            }
        }
    }
    ```

- `docker`: Add `test.withStartupScript` as alternative to `test.startupScript`
- `benchmark`: Alternative configuration for JMH version

    Before

    ```kotlin
    kradle {
        benchmark {
            jmhVersion("1.34")
        }
    }
    ```

    After

    ```kotlin
    kradle {
        benchmark {
            jmh {
                version("1.34")
            }
        }
    }
    ```

#### Changes

- Add possibility to disable linters and code analysis tools
- Use Kover as default for test coverage instead of JaCoCo
- Enable Lombok in all Java presets
- `jacoco`: Use one report task which handles all test source sets instead of one task per set
- `jacoco`: No longer create report after running tests
- `test`: Remove access to unit test classes from integration and functional tests
- `test`: Enable JUnit Jupiter per default
- `dev`: Rename environment variable DEV_MODE to KRADLE_DEV_MODE
- `docker`: Add [tini](https://github.com/krallin/tini) when using `startupScript` or `withJvmKill`

### Version 2.1.0 (2022-01-06)

#### Bugfixes

- `developmentMode`: Make sure `dev` works with Gradle toolchains

#### Additions

- New feature [Java development](README.md#feature-java) (linting, code analysis, bootstrapping, preview features)
- New task `kradleDump`
- `lint`, `codeAnalysis`: New option `ignoreFailures`
- `ktlint`: Rules are configurable
- `ktlint`, `detekt`: Alternative configuration

    Before

    ```kotlin
    kradle {
        jvm {
            kotlin {
                lint {
                    ktlintVersion("0.43.2")
                }
                codeAnalysis {
                    detektConfigFile("detekt-config.yml")
                    detektVersion("1.19.0")
                }
            }
        }
    }
    ```

    After

    ```kotlin
    kradle {
        jvm {
            kotlin {
                lint {
                    ktlint {
                        version("0.43.2")
                    }
                }
                codeAnalysis {
                    detekt {
                        version("1.19.0")
                        configFile("detekt-config.yml")
                    }
                }
            }
        }
    }
    ```
- `test`: Add support for [custom tests](README.md#feature-test)
- `test`: Alternative configuration

    Before

    ```kotlin
    kradle {
        jvm {
            test {
                withIntegrationTests(true)
                withFunctionalTests(true)
            }
        }
    }
    ```

    After

    ```kotlin
    kradle {
        jvm {
            test {
                integrationTests(true)
                functionalTests(true)
            }
        }
    }
    ```

- `docker`: Alternative configuration

    Before

      ```
      kradle {
          jvm {
            docker {
                withAppSh(true)
                ports.add(…)
            }
          }
      }
      ```

    After

      ```
      kradle {
          jvm {
            docker {
                startupScript(true)
                ports(…)
            }
          }
      }
      ```

#### Changes

- `ktlint`: Rule `no-wildcard-imports` is no longer disabled (it still is in presets)
- `showDependencyUpdates`: Exclude beta versions
- `test`: Recognize files ending with `Spec`

### Version 2.0.1 (2022-01-01)

#### Bugfixes

- `kradle` now works in combination with [Gradle toolchains](https://docs.gradle.org/current/userguide/toolchains.html)

### Version 2.0.0 (2021-12-29)

#### Bugfixes

- `bootstrap`: Fix for multi-project builds
- `showDependencyUpdates`: Fix regular expression for exclusion of alpha versions and RCs

#### Additions

- New plugin `net.bitsandbobs.kradle`, which combines the functionality of `kradle-app` and `kradle-lib`
- New configuration DSL (see [configuration reference](README.md#configuration-reference))

#### Changes

- `jvm`: Set default target JVM to 17
- Deprecate `kradle-app` and `kradle-lib`. They still work, but won't receive any new features. Consider using `net.bitsandbobs.kradle` instead.
- __Breaking change__: [`disable`](https://github.com/mrkuz/kradle/tree/v1.2.0#blueprints) for `kradle-app` and `kradle-lib` removed

### Version 1.2.0 (2021-09-23)

#### Bugfixes

- Fix package statement of main class generated by `bootstrap`

#### Additions

- Support use of `@JvmName`
- New option `jmhVersion`
- New option `detektVersion`

#### Changes

- The tasks `showDependencyUpdates`, `analyzeCode`, `analyzeDependencies`, `generateDocumentation`,  `uberJar` and `buildImage` are now independent tasks, not aliases
- Add `kotlin-reflect` to project dependencies
- Enable strict JSR-305 processing
- `DEV_MODE=true` is no longer set when launching the application with `run`
- `uberJar`: JAR is no longer minimized
- `generateBuildProperties`: Content of _build.properties_ changed

    Before

    ```properties
    version=…
    timestamp=…
    git.commit-id=…
    ```

    After

    ```properties
    project.name=…
    project.group=…
    project.version=…
    build.timestamp=…
    git.commit-id=…
    ```

### Version 1.1.0 (2021-09-09)

#### Additions

- New task `bootstrap`: Bootstraps new app/lib project
- New task `dev`: Runs the application and stops it when sources change (for automatic rebuilds and restarts)
- New task `generateGitignore`: Generates _.gitignore_
- Add source sets and tasks for integration and functional tests
- Syntactic sugar: Overload invoke operator for setting options

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

- Add option for main class inside `kradle` configuration

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

- Add `jacocoVersion` to `test`

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

#### Changes

- `generateDocumentation`: _module.md_ and _package.md_ can be placed inside source directories
