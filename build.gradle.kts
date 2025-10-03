plugins {
    `java-library`
    java
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "hfnl"
version = "0.1.0"

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://maven.fabricmc.net/") }
}

dependencies {
    annotationProcessor(libs.jetbrains.annotations)

    //LOGGING
    implementation(libs.log4j.api)
    implementation(libs.log4j.core)
    implementation(libs.log4j.slf4j.impl)
    implementation(libs.slf4j.api)

    //ASM
    implementation(libs.asm)
    implementation(libs.asm.tree)
    implementation(libs.asm.commons)
    implementation(libs.asm.util)
    implementation(libs.asm.analysis)

    //FABRIC
    implementation(libs.fabric.loader)
    implementation(libs.sponge.mixin)
    implementation(libs.mixin.extras)

    //HMCL
    compileOnly(libs.kala.compress.zip)
    compileOnly(libs.kala.compress.tar)
    compileOnly(libs.simple.png.javafx)
    compileOnly(libs.gson)
    compileOnly(libs.toml)
    compileOnly(libs.xz)
    compileOnly(libs.fx.gson)
    compileOnly(libs.constant.pool.scanner)
    compileOnly(libs.opennbt)
    compileOnly(libs.nanohttpd)
    compileOnly(libs.jsoup)
    compileOnly(libs.chardet)
    compileOnly(libs.jna)
    compileOnly(libs.pci.ids)
    compileOnly(libs.twelvemonkeys.imageio.webp)
    compileOnly(libs.java.info)

    implementation("org.glavo.hmcl:hmcl-dev:3.6.18.297")
}
javafx {
    configuration = "compileOnly"
    version = "21"
    modules("javafx.controls")
}
java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.register<Copy>("copyDependencies") {
    val runtimeClasspath = sourceSets["main"].runtimeClasspath.files
    from(runtimeClasspath) {
        include {
            it.name.endsWith(".jar") && (
                    !it.name.startsWith("hmcl"))
        }
    }
    into(layout.buildDirectory.dir("libraries/"))
}

tasks.register<JavaExec>("run") {
    dependsOn(tasks.jar)

    group = "application"

    mainClass.set("hfnl.launch.Main")

    classpath = sourceSets["main"].runtimeClasspath + files(tasks.jar.get().archiveFile)

    workingDir = rootProject.rootDir.resolve("run")
    if (!workingDir.exists()) {
        workingDir.mkdirs()
    }

    val jvmArgsList = mutableListOf<String>()
    jvmArgsList.addAll(
        listOf(
            "-Dmixin.debug=true",
            "--add-opens", "java.base/java.lang=ALL-UNNAMED",
            "--add-opens", "java.base/java.net=ALL-UNNAMED",
            "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
            "--add-opens", "java.base/java.lang=ALL-UNNAMED",
            "--add-opens", "java.base/jdk.internal.loader=ALL-UNNAMED",
            "--add-opens", "jdk.attach/sun.tools.attach=ALL-UNNAMED"
        )
    )


    try {
        Class.forName("javafx.stage.Stage")
        jvmArgsList.addAll(
            listOf(
                "--add-opens", "javafx.base/com.sun.javafx.binding=ALL-UNNAMED",
                "--add-opens", "javafx.base/com.sun.javafx.event=ALL-UNNAMED",
                "--add-opens", "javafx.base/com.sun.javafx.runtime=ALL-UNNAMED",
                "--add-opens", "javafx.graphics/javafx.css=ALL-UNNAMED",
                "--add-opens", "javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED",
                "--add-opens", "javafx.graphics/com.sun.prism=ALL-UNNAMED",
                "--add-opens", "javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED",
                "--add-opens", "javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED",
                "--add-opens", "javafx.controls/javafx.scene.control.skin=ALL-UNNAMED",
            )
        )
    } catch (_: Throwable) {
    }

    jvmArgs(jvmArgsList)
}