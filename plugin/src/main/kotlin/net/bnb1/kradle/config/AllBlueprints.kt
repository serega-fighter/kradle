package net.bnb1.kradle.config

import net.bnb1.kradle.features.general.BootstrapBlueprint
import net.bnb1.kradle.features.general.BuildPropertiesBlueprint
import net.bnb1.kradle.features.general.GitBlueprint
import net.bnb1.kradle.features.general.ProjectPropertiesBlueprint
import net.bnb1.kradle.features.jvm.AllOpenBlueprint
import net.bnb1.kradle.features.jvm.ApplicationBlueprint
import net.bnb1.kradle.features.jvm.BenchmarksBlueprint
import net.bnb1.kradle.features.jvm.CheckstyleBlueprint
import net.bnb1.kradle.features.jvm.CodeAnalysisBlueprint
import net.bnb1.kradle.features.jvm.DependencyUpdatesBlueprint
import net.bnb1.kradle.features.jvm.DetektBlueprint
import net.bnb1.kradle.features.jvm.DevelopmentModeBlueprint
import net.bnb1.kradle.features.jvm.DokkaBlueprint
import net.bnb1.kradle.features.jvm.JacocoBlueprint
import net.bnb1.kradle.features.jvm.JavaAppBootstrapBlueprint
import net.bnb1.kradle.features.jvm.JavaBlueprint
import net.bnb1.kradle.features.jvm.JavaLibBootstrapBlueprint
import net.bnb1.kradle.features.jvm.JibBlueprint
import net.bnb1.kradle.features.jvm.KotlinAppBootstrapBlueprint
import net.bnb1.kradle.features.jvm.KotlinBlueprint
import net.bnb1.kradle.features.jvm.KotlinLibBootstrapBlueprint
import net.bnb1.kradle.features.jvm.KotlinTestBlueprint
import net.bnb1.kradle.features.jvm.KtlintBlueprint
import net.bnb1.kradle.features.jvm.LibraryBlueprint
import net.bnb1.kradle.features.jvm.LintBlueprint
import net.bnb1.kradle.features.jvm.MavenPublishBlueprint
import net.bnb1.kradle.features.jvm.OwaspDependencyCheckBlueprint
import net.bnb1.kradle.features.jvm.PackageApplicationBlueprint
import net.bnb1.kradle.features.jvm.PackagingBlueprint
import net.bnb1.kradle.features.jvm.PmdBlueprint
import net.bnb1.kradle.features.jvm.ShadowBlueprint
import net.bnb1.kradle.features.jvm.SpotBugsBlueprint
import net.bnb1.kradle.features.jvm.TestBlueprint
import net.bnb1.kradle.inject
import net.bnb1.kradle.support.Registry
import org.gradle.api.Project

class AllBlueprints(registry: Registry, properties: AllProperties, project: Project) {

    // General
    val bootstrap = registry { BootstrapBlueprint(project) }
    val git = registry { GitBlueprint(project) }
    val projectProperties = registry { ProjectPropertiesBlueprint(project) }
    val buildProperties = registry { BuildPropertiesBlueprint(project) }

    // JVM
    val java = registry {
        JavaBlueprint(project).inject {
            javaProperties = properties.java
            jvmProperties = properties.jvm
        }
    }
    val kotlin = registry {
        KotlinBlueprint(project).inject {
            kotlinProperties = properties.kotlin
            jvmProperties = properties.jvm
        }
    }
    val allOpen = registry { AllOpenBlueprint(project) }
    val application = registry {
        ApplicationBlueprint(project).inject {
            applicationProperties = properties.application
            javaProperties = properties.java
        }
    }
    val library = registry { LibraryBlueprint(project) }
    val mavenPublish = registry { MavenPublishBlueprint(project) }
    val dependencyUpdates = registry { DependencyUpdatesBlueprint(project) }
    val owaspDependencyCheck = registry { OwaspDependencyCheckBlueprint(project) }
    val lint = registry { LintBlueprint(project) }
    val codeAnalysis = registry { CodeAnalysisBlueprint(project) }
    val developmentMode = registry {
        DevelopmentModeBlueprint(project).inject {
            applicationProperties = properties.application
            javaProperties = properties.java
        }
    }

    val test = registry {
        TestBlueprint(project).inject {
            testProperties = properties.test
            javaProperties = properties.java
        }
    }
    val jacoco = registry {
        JacocoBlueprint(project).inject {
            testProperties = properties.test
        }
    }
    val benchmarks = registry {
        BenchmarksBlueprint(project).inject {
            benchmarkProperties = properties.benchmark
            javaProperties = properties.java
        }
    }
    val packaging = registry { PackagingBlueprint(project) }
    val packageApplication = registry {
        PackageApplicationBlueprint(project).inject {
            applicationProperties = properties.application
        }
    }
    val shadow = registry {
        ShadowBlueprint(project).inject {
            uberJarProperties = properties.uberJar
        }
    }
    val dokka = registry { DokkaBlueprint(project) }
    val jib = registry {
        JibBlueprint(project).inject {
            dockerProperties = properties.docker
            applicationProperties = properties.application
        }
    }
    val javaAppBootstrap = registry {
        JavaAppBootstrapBlueprint(project).inject {
            applicationProperties = properties.application
        }
    }
    val javaLibBootstrap = registry { JavaLibBootstrapBlueprint(project) }
    val pmd = registry {
        PmdBlueprint(project).inject {
            pmdProperties = properties.pmd
            codeAnalysisProperties = properties.codeAnalysis
        }
    }
    val spotBugs = registry {
        SpotBugsBlueprint(project).inject {
            spotBugsProperties = properties.spotBugs
            codeAnalysisProperties = properties.codeAnalysis
        }
    }
    val checkstyle = registry {
        CheckstyleBlueprint(project).inject {
            checkstyleProperties = properties.checkstyle
            lintProperties = properties.lint
        }
    }
    val kotlinAppBootstrap = registry {
        KotlinAppBootstrapBlueprint(project).inject {
            applicationProperties = properties.application
        }
    }
    val kotlinLibBootstrap = registry { KotlinLibBootstrapBlueprint(project) }
    val detekt = registry {
        DetektBlueprint(project).inject {
            detektProperties = properties.detekt
            codeAnalysisProperties = properties.codeAnalysis
        }
    }
    val ktlint = registry {
        KtlintBlueprint(project).inject {
            ktlintProperties = properties.ktlint
            lintProperties = properties.lint
        }
    }
    val kotlinTest = registry {
        KotlinTestBlueprint(project).inject {
            kotlinTestProperties = properties.kotlinTest
            testProperties = properties.test
        }
    }
}
