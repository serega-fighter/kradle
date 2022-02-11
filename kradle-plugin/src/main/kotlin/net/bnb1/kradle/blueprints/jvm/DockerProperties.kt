package net.bnb1.kradle.blueprints.jvm

import net.bnb1.kradle.core.Properties

class DockerProperties(var baseImage: String) : Properties {

    val ports = mutableSetOf<Int>()
    var withJvmKill: String? = null
    var withStartupScript = false
    var jvmOpts: String? = null
}
