package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.Catalog
import net.bnb1.kradle.dsl.Properties

class DockerProperties : Properties() {

    val baseImage = value("bellsoft/liberica-openjdk-alpine:${Catalog.Versions.jvm}")
    val ports = valueSet<Int>()
    val withJvmKill = optional(Catalog.Versions.jvmKill)

    val withAppSh = flag()
    val withStartupScript = withAppSh
    val startupScript = withAppSh

    val javaOpts = optional<String>()
    val jvmOpts = javaOpts
}
