package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Configurable
import net.bnb1.kradle.features.AllProperties

class KotlinDsl(properties: AllProperties) {

    val kotlinxCoroutinesVersion = properties.kotlin.kotlinxCoroutinesVersion
    fun useCoroutines(version: String = Catalog.Versions.kotlinCoroutines) = kotlinxCoroutinesVersion.set(version)

    val lint = Configurable(KotlinLintDsl(properties))
    val codeAnalysis = Configurable(KotlinCodeAnalysisDsl(properties))
    val test = Configurable(properties.kotlinTest)
}
