plugins {
    id("java")
}

group = "hfnl"
version = "0.0.1"

dependencies {
    implementation(project(":examplemod"))
}

tasks.register<Copy>("copyDependencies") {
    val runtimeClasspath = sourceSets["main"].runtimeClasspath.files
    from(runtimeClasspath) {
        include {
            it.name.endsWith(".jar") && (
                    !it.name.startsWith("hmcl") &&
                            !it.name.startsWith("examplemod"))
        }
    }
    into(layout.buildDirectory.dir("libraries/"))
}

tasks.create<JavaExec>("run") {
    dependsOn(tasks.jar)

    group = "application"

    mainClass.set("hfnl.launch.Main")

    classpath = sourceSets["main"].runtimeClasspath + files(tasks.jar.get().archiveFile)


    workingDir = rootProject.rootDir.resolve("run")
    if (!workingDir.exists()) {
        workingDir.mkdirs()
    }

    val hmclJar = configurations.runtimeClasspath.get().asFileTree.files.firstOrNull {
        it.name.startsWith("hmcl-dev-") && it.name.endsWith(".jar")
    }
    hmclJar?.let { systemProperty("fabric.gameJarPath", it.absolutePath) }
    systemProperties.set("hfnl.disable.warning", true)

    val jvmArgsList = mutableListOf<String>()
    if(!javaVersion.isJava8){
        jvmArgsList.addAll(
            listOf(
                "--add-opens", "java.base/java.lang=ALL-UNNAMED",
                "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
                "--add-opens", "java.base/jdk.internal.loader=ALL-UNNAMED",
                "--add-opens", "javafx.base/com.sun.javafx.binding=ALL-UNNAMED",
                "--add-opens", "javafx.base/com.sun.javafx.event=ALL-UNNAMED",
                "--add-opens", "javafx.base/com.sun.javafx.runtime=ALL-UNNAMED",
                "--add-opens", "javafx.graphics/javafx.css=ALL-UNNAMED",
                "--add-opens", "javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED",
                "--add-opens", "javafx.graphics/com.sun.prism=ALL-UNNAMED",
                "--add-opens", "javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED",
                "--add-opens", "javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED",
                "--add-opens", "javafx.controls/javafx.scene.control.skin=ALL-UNNAMED",
                "--add-opens", "jdk.attach/sun.tools.attach=ALL-UNNAMED"
            )
        )
    }

    jvmArgs(jvmArgsList)
}

java {

}
