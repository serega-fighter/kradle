package net.bnb1.kradle.features.jvm

import io.gitlab.arturbosch.detekt.Detekt
import net.bnb1.kradle.Catalog
import net.bnb1.kradle.createHelperTask
import net.bnb1.kradle.createTask
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.tasks.GenerateDetektConfigTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

private const val CONFIGURATION_NAME = "kradleDetekt"

class DetektBlueprint(project: Project) : Blueprint(project) {

    override fun createTasks() {
        val properties = project.propertiesRegistry.get<KotlinCodeAnalysisProperties>()
        val configFile = project.rootDir.resolve(properties.detektConfigFile.get())

        project.createTask<GenerateDetektConfigTask>("generateDetektConfig", "Generates detekt-config.yml") {
            outputFile.set(project.rootDir.resolve(properties.detektConfigFile.get()))
        }

        project.configurations.create(CONFIGURATION_NAME) {
            val dependencyProvider = project.provider {
                project.dependencies.create("${Catalog.Dependencies.Tools.detekt}:${properties.detektVersion.get()}")
            }
            dependencies.addLater(dependencyProvider)
        }

        val detektTask = project.createHelperTask<Task>("detekt", "Runs detekt")
        project.tasks.getByName(CodeAnalysisFeature.MAIN_TASK).dependsOn(detektTask)

        val kotlinExtension = project.extensions.getByType(KotlinProjectExtension::class.java)
        kotlinExtension.sourceSets.forEach { sourceSet ->
            val sourceFiles = sourceSet.kotlin.files
                .filter { it.extension.toLowerCase() == "kt" }
                .toSet()

            val taskName = "detekt" + sourceSet.name[0].toUpperCase() + sourceSet.name.substring(1)

            project.createHelperTask<Detekt>(taskName, "Runs detekt code analysis on '${sourceSet.name}'") {
                setSource(sourceFiles)
                detektClasspath.setFrom(project.configurations.getAt(CONFIGURATION_NAME))
                reports {
                    html {
                        required.set(true)
                        outputLocation.set(project.buildDir.resolve("reports/detekt/${sourceSet.name}.html"))
                    }
                    xml.required.set(false)
                    sarif.required.set(false)
                    txt.required.set(false)
                }
                if (configFile.exists()) {
                    buildUponDefaultConfig = false
                    config.setFrom(configFile)
                }
            }

            detektTask.dependsOn(taskName)
        }
    }
}
