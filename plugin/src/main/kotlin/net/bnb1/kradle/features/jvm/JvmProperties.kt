package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.features.ConfigurableFeatureImpl
import net.bnb1.kradle.features.EmptyProperties
import net.bnb1.kradle.features.Properties
import net.bnb1.kradle.property
import org.gradle.api.Project

class JvmProperties(project: Project) : Properties(project) {

    val targetJvm = property(factory.property("17"))

    val kotlin = ConfigurableFeatureImpl(KotlinFeature(), KotlinProperties(project))
        .setParent(JvmFeatureSet::class)
        .addBlueprint(JavaBlueprint(project))
        .addBlueprint(KotlinBlueprint(project))
        .register(project)
        .asInterface()
    val application = ConfigurableFeatureImpl(ApplicationFeature(), ApplicationProperties(project))
        .setParent(JvmFeatureSet::class)
        .addBlueprint(ApplicationBlueprint(project))
        .register(project)
        .asInterface()
    val library = ConfigurableFeatureImpl(LibraryFeature(), EmptyProperties(project))
        .setParent(JvmFeatureSet::class)
        .addBlueprint(LibraryBlueprint(project))
        .register(project)
        .asInterface()
    val dependencyUpdates = ConfigurableFeatureImpl(DependencyUpdatesFeature(), EmptyProperties(project))
        .setParent(JvmFeatureSet::class)
        .addBlueprint(DependencyUpdatesBlueprint(project))
        .register(project)
        .asInterface()
    val vulnerabilityScan = ConfigurableFeatureImpl(VulnerabilityScanFeature(), EmptyProperties(project))
        .setParent(JvmFeatureSet::class)
        .addBlueprint(OwaspDependencyCheckBlueprint(project))
        .register(project)
        .asInterface()
    val lint = ConfigurableFeatureImpl(LintFeature(), EmptyProperties(project))
        .setParent(JvmFeatureSet::class)
        .addBlueprint(LintBlueprint(project))
        .register(project)
        .asInterface()
    val codeAnalysis = ConfigurableFeatureImpl(CodeAnalysisFeature(), EmptyProperties(project))
        .setParent(JvmFeatureSet::class)
        .addBlueprint(CodeAnalysisBlueprint(project))
        .register(project)
        .asInterface()
    val test = ConfigurableFeatureImpl(TestFeature(), TestProperties(project))
        .setParent(JvmFeatureSet::class)
        .addBlueprint(TestBlueprint(project))
        .register(project)
        .asInterface()
    val benchmark = ConfigurableFeatureImpl(BenchmarkFeature(), BenchmarkProperties(project))
        .setParent(JvmFeatureSet::class)
        .addBlueprint(BenchmarksBlueprint(project))
        .register(project)
        .asInterface()
    val `package` = ConfigurableFeatureImpl(PackageFeature(), PackageProperties(project))
        .setParent(JvmFeatureSet::class)
        .addBlueprint(PackageBlueprint(project))
        .register(project)
        .asInterface()
    val packaging = `package`
    val documentation = ConfigurableFeatureImpl(DocumentationFeature(), EmptyProperties(project))
        .setParent(JvmFeatureSet::class)
        .addBlueprint(DokkaBlueprint(project))
        .register(project)
        .asInterface()
    val docker = ConfigurableFeatureImpl(DockerFeature(), DockerProperties(project))
        .setParent(JvmFeatureSet::class)
        .addBlueprint(JibBlueprint(project))
        .register(project)
        .asInterface()
}
