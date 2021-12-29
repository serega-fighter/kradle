package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.create
import net.bnb1.kradle.featureRegistry
import net.bnb1.kradle.features.Blueprint
import net.bnb1.kradle.propertiesRegistry
import net.bnb1.kradle.sourceSets
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.gradle.kotlin.dsl.named

private val GROUP_PATTERN = Regex("^[a-z]+(\\.[a-z0-9]+)+$")

class DevelopmentModeBlueprint(project: Project) : Blueprint(project) {

    override fun checkPreconditions() {
        if (!project.featureRegistry.get<ApplicationFeature>().isEnabled) {
            throw GradleException("'developmentMode' requires 'application' feature")
        }
    }

    override fun createTasks() {
        val mainSourceSet = project.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
        val agentResource = javaClass.getResource("/agent.jar")
        project.create<JavaExec>(
            "dev",
            "Runs the application and stops it when sources change (use with -t)"
        ) {
            val agentFile = project.rootDir.resolve(".gradle/kradle/agent.jar")
            doFirst {
                agentFile.parentFile.mkdirs()
                agentFile.writeBytes(agentResource.readBytes())
            }
            // Allows the application to figure out we are running in development mode
            environment("DEV_MODE", "true")
            // Tell agent about the project root
            environment("PROJECT_ROOT", project.rootDir)
            // Speed up start when developing
            jvmArgs = listOf("-XX:TieredStopAtLevel=1")
            classpath = mainSourceSet.runtimeClasspath
            jvmArgs = jvmArgs + listOf("-javaagent:${agentFile.absolutePath}")
        }
    }

    override fun configure() {
        val properties = project.propertiesRegistry.get<ApplicationProperties>()
        val mainClass = properties.mainClass
        project.tasks.named<JavaExec>("dev").configure { this.mainClass.set(mainClass.get()) }
    }
}
