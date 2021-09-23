package net.bnb1.kradle.blueprints

import net.bnb1.kradle.KradleExtension
import net.bnb1.kradle.PluginBlueprint
import net.bnb1.kradle.alias
import net.bnb1.kradle.annotations.Invasive
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension

@Invasive
object JavaBlueprint : PluginBlueprint<JavaPlugin> {

    override fun configureEager(project: Project) {
        project.alias("package", "Creates JAR", "jar")
    }

    override fun configure(project: Project, extension: KradleExtension) {
        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        val javaVersion = JavaVersion.toVersion(extension.targetJvm.get())
        javaExtension.sourceCompatibility = javaVersion
        javaExtension.targetCompatibility = javaVersion
    }
}
