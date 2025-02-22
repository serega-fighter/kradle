package net.bnb1.kradle.tasks

import net.bnb1.kradle.core.Feature
import net.bnb1.kradle.core.Properties
import net.bnb1.kradle.support.Tracer
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.util.GradleVersion
import java.nio.file.Paths
import java.util.zip.ZipFile
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure
import kotlin.streams.asSequence
import java.util.Properties as JavaProperties

open class KradleDumpTask : DefaultTask() {

    @Internal
    lateinit var properties: List<Properties>

    @Internal
    lateinit var features: List<Feature>

    @Internal
    lateinit var tracer: Tracer

    init {
        // Ensure that this task is always executed
        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun run() {
        val properties = JavaProperties()
        javaClass.getResource("/build.properties").openStream().use {
            properties.load(it)
        }
        val kradleVersion = properties["project.version"]
        dump(
            """
            kradle version: $kradleVersion"
            Gradle version: ${GradleVersion.current().version}
            """.trimIndent()
        )

        printEnabledFeatures()
        printTrace()
        printAppliedPlugins()
        printTasks()
        printProperties()
    }

    private fun printEnabledFeatures() {
        dump(
            """
            
            Enabled features:
            -----------------
            """.trimIndent()
        )

        features.asSequence()
            .filter { it.isEnabled }
            .sortedBy { it.name }
            .forEach { dump("- ${it.name}") }
    }

    private fun printTrace() {
        dump(
            """
            
            Trace:
            ------
            """.trimIndent()
        )
        val entries = tracer.entries
        entries.forEachIndexed { index, entry ->
            if (entry.level == 0) {
                if (index > 0) {
                    dump("")
                }
                dump(entry.message)
            } else {
                var prefix = ""

                repeat(entry.level - 1) {
                    prefix += "│  "
                }

                prefix += if (index == (entries.size - 1) || entries[index + 1].level < entry.level) {
                    "└─ "
                } else {
                    "├─ "
                }
                dump(prefix + entry.message)
            }
        }
    }

    private fun printTasks() {
        dump(
            """
           
            Tasks:
            ------
            """.trimIndent()
        )
        project.tasks.asSequence()
            .sortedBy { it.name }
            .forEach {
                val jar = getJar(it::class)
                dump("- ${it.name} ($jar, ${it::class.qualifiedName})")
            }
    }

    private fun printAppliedPlugins() {
        dump(
            """
            
            Applied plugins:
            ----------------
            """.trimIndent()
        )

        val plugins = project.buildscript.configurations.asSequence()
            .flatMap { it.resolvedConfiguration.lenientConfiguration.artifacts }
            .filter { it.type == "jar" }
            .flatMap {
                ZipFile(it.file).use { zip ->
                    zip.stream().asSequence()
                        .filter { entry -> entry.name.startsWith("META-INF/gradle-plugins/") }
                        .filter { entry -> entry.name.endsWith(".properties") }
                        .map { entry -> entry.name.replace(Regex("^META-INF/gradle-plugins/"), "") }
                        .map { name -> name.replace(Regex("\\.properties$"), "") }
                        .map { id -> Pair(id, it.file.name) }
                        .toSet()
                }
            }
            .sortedBy { it.first }
            .toSet()

        val internal = setOf("application", "checkstyle", "jacoco", "java", "java-library", "maven-publish", "pmd")
        internal.forEach {
            if (project.pluginManager.hasPlugin(it)) {
                val plugin = project.plugins.getPlugin(it)
                var jar = getJar(plugin::class)
                dump("- $it ($jar, ${plugin::class.qualifiedName})")
            }
        }

        plugins.forEach {
            if (project.pluginManager.hasPlugin(it.first)) {
                val plugin = project.plugins.getPlugin(it.first)
                dump("- ${it.first} (${it.second}, ${plugin::class.qualifiedName})")
            }
        }
    }

    private fun printProperties() {
        dump(
            """
            
            Properties:
            ----------------
            """.trimIndent()
        )

        properties.asSequence()
            .sortedBy { it::class.qualifiedName }
            .forEach {
                dump("${it::class.qualifiedName} {")
                printProperties(it, 1)
                dump("}\n")
            }
    }

    private fun printProperties(target: Any, level: Int) {
        val prefix = " ".repeat(level * 2)
        target::class.memberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .forEach { member ->
                val key = "${prefix}${member.name}"
                val value = member.getter.call(target)
                if (value != null) {
                    if (member.returnType.jvmErasure.isSubclassOf(Properties::class)) {
                        dump("$key = {")
                        printProperties(value, level + 1)
                        dump("$prefix}")
                    } else {
                        dump("$key = $value")
                    }
                } else {
                    dump("$key = NOT SET")
                }
            }
    }

    private fun getJar(clazz: KClass<*>) =
        Paths.get(clazz.java.protectionDomain.codeSource.location.toURI()).fileName

    private fun dump(text: String) {
        project.logger.lifecycle(text)
    }
}
