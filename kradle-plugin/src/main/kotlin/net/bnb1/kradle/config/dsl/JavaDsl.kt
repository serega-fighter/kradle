package net.bnb1.kradle.config.dsl

import net.bnb1.kradle.config.AllBlueprints
import net.bnb1.kradle.config.AllProperties
import net.bnb1.kradle.dsl.Configurable

class JavaDsl(blueprints: AllBlueprints, properties: AllProperties) {

    val previewFeatures = properties.java.previewFeatures
    val withLombok = properties.java.withLombok
    val lint = Configurable(JavaLintDsl(blueprints, properties))
    val codeAnalysis = Configurable(JavaCodeAnalysisDsl(blueprints, properties))
}
