val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest.ktlint:ktlint-cli:1.2.1") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))


val ktlintCheck by tasks.registering(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"

    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")

    args("src/**/*.kt")
}

tasks.register<JavaExec>("ktlintFormat") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style and format"

    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")

    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
    args("-F", "src/**/*.kt")
}
