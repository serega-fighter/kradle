package net.bnb1.kradle.features

import net.bnb1.kradle.featureRegistry
import org.gradle.api.GradleException
import org.gradle.api.Project
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A feature set is used to group [features][Feature].
 *
 * If the set is activated, the assigned and enabled features are also activated.
 */
open class FeatureSet(private val project: Project) {

    private val activated = AtomicBoolean(false)

    fun activate() {
        if (!tryActivate()) {
            throw GradleException("'${this::class.simpleName}' was already activated")
        }
    }

    fun tryActivate(): Boolean {
        if (!activated.compareAndSet(false, true)) {
            return false
        }
        project.featureRegistry.map.values.asSequence()
            .filter { it.isEnabled }
            .filter { it.isInactive }
            .filter { it.isParent(this::class) }
            .forEach { activateOrdered(it) }
        return true
    }

    private fun activateOrdered(feature: Feature) {
        feature.shouldActivateAfter().asSequence()
            .map { project.featureRegistry.map[it] as Feature }
            .filter { it.isEnabled }
            .filter { it.isInactive }
            .forEach { activateOrdered(it) }
        feature.activate()
    }
}
