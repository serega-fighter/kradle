package net.bnb1.kradle.features

class FeaturePlan(private val features: AllFeatures, private val blueprints: AllBlueprints) {

    fun initialize() {
        features.bootstrap += blueprints.bootstrap
        features.git += blueprints.git
        features.projectProperties += blueprints.projectProperties

        features.kotlin += setOf(
            blueprints.java,
            blueprints.kotlin,
            blueprints.allOpen,
            blueprints.kotlinAppBootstrap.also {
                it dependsOn features.bootstrap
                it dependsOn features.application
            },
            blueprints.kotlinLibBootstrap.also {
                it dependsOn features.bootstrap
                it dependsOn features.library
            },
            blueprints.buildProperties.also { it dependsOn features.buildProperties }
        )
        features.java += setOf(
            blueprints.java,
            blueprints.javaAppBootstrap.also {
                it dependsOn features.bootstrap
                it dependsOn features.application
            },
            blueprints.javaLibBootstrap.also {
                it dependsOn features.bootstrap
                it dependsOn features.library
            },
            blueprints.buildProperties
        )
        features.application.also { me ->
            me conflictsWith features.library
            me += blueprints.application
        }
        features.library.also { me ->
            me conflictsWith features.application
            me += setOf(
                blueprints.library,
                blueprints.mavenPublish
            )
        }
        features.dependencyUpdates += blueprints.dependencyUpdates
        features.vulnerabilityScan += blueprints.owaspDependencyCheck
        features.lint.also { me ->
            me activatesAfter features.test
            me activatesAfter features.benchmark
            me += setOf(
                blueprints.lint,
                blueprints.ktlint.also { it dependsOn features.kotlin },
                blueprints.checkstyle.also { it dependsOn features.java }
            )
        }
        features.codeAnalysis.also { me ->
            me activatesAfter features.test
            me activatesAfter features.benchmark
            me += setOf(
                blueprints.codeAnalysis,
                blueprints.detekt.also { it dependsOn features.kotlin },
                blueprints.pmd.also { it dependsOn features.java },
                blueprints.spotBugs.also { it dependsOn features.java }
            )
        }
        features.developmentMode.also { me ->
            me requires features.application
            me += blueprints.developmentMode
        }
        features.test += setOf(
            blueprints.test,
            blueprints.jacoco,
            blueprints.kotlinTest.also { it dependsOn features.kotlin },
        )
        features.benchmark += setOf(
            blueprints.allOpen.also { it dependsOn features.kotlin },
            blueprints.benchmarks
        )
        features.packaging.also { me ->
            me activatesAfter features.application
            me += setOf(
                blueprints.packaging,
                blueprints.packageApplication.also { it dependsOn features.application },
                blueprints.shadow.also { it dependsOn features.application }
            )
        }
        features.docker.also { me ->
            me requires features.application
            me += blueprints.jib
        }
        features.documentation += blueprints.dokka
    }
}
