package net.bnb1.kradle.features.jvm

import net.bnb1.kradle.core.Blueprint
import net.bnb1.kradle.createTask
import org.gradle.api.Project
import org.gradle.api.Task

class CodeAnalysisBlueprint(project: Project) : Blueprint(project) {

    override fun doCreateTasks() {
        project.createTask<Task>(CodeAnalysisFeature.MAIN_TASK, "Runs code analysis")
        project.tasks.getByName("check").dependsOn(CodeAnalysisFeature.MAIN_TASK)
    }
}
