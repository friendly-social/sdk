plugins {
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.ktlint) apply false
}

tasks {
    val printVersion by registering {
        group = "CI"

        doFirst {
            println(libs.versions.friendly.get())
        }
    }
}
