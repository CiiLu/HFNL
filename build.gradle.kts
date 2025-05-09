plugins {
    java
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "hfnl"
version = "0.0.1"
subprojects {
    apply(plugin = "java")
    apply(plugin = "org.openjfx.javafxplugin")

    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://maven.aliyun.com/repository/jcenter") }
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.fabricmc.net/") }
    }
    dependencies {
        implementation("net.fabricmc:fabric-loader:0.16.14")
        implementation("net.fabricmc:sponge-mixin:0.15.4+mixin.0.8.7")

        implementation("org.slf4j:slf4j-api:2.0.9")
        implementation("org.apache.logging.log4j:log4j-api:2.8.1")
        implementation("org.apache.logging.log4j:log4j-core:2.8.1")

        implementation("io.github.llamalad7:mixinextras-common:0.4.1")

        implementation("org.ow2.asm:asm:9.8")
        implementation("org.ow2.asm:asm-tree:9.8")
        implementation("org.ow2.asm:asm-commons:9.8")
        implementation("org.ow2.asm:asm-util:9.8")
        implementation("org.ow2.asm:asm-analysis:9.8")

        implementation("com.google.code.gson:gson:2.11.0")
        implementation("org.glavo.hmcl:hmcl-dev:3.6+")
    }
    javafx {
        configuration = "compileOnly"
        version = "17"
        modules("javafx.controls")
    }
    java {
        targetCompatibility = JavaVersion.VERSION_11
        sourceCompatibility = JavaVersion.VERSION_11
    }
}
