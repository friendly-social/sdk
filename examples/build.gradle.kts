plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktlint)
    application
}

application {
    mainClass = "friendly.sdk.examples.MainKt"
}

kotlin {
    jvmToolchain(8)

    compilerOptions {
        extraWarnings = true
        allWarningsAsErrors = true
        progressiveMode = true
        freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
    }
}

dependencies {
    implementation(projects.sdk)
    implementation(libs.slf4j.simple)
}
