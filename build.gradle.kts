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
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.fabricmc.net/") }
    }
    dependencies {
        implementation("net.fabricmc:fabric-loader:0.16.10")
        implementation("net.fabricmc:sponge-mixin:0.15.4+mixin.0.8.7")
        implementation("net.fabricmc:access-widener:2.1.0")
        implementation("net.fabricmc:tiny-mappings-parser:0.2.2.14")
        implementation("org.ow2.asm:asm:9.7.1")
        implementation("org.slf4j:slf4j-api:2.0.9")
        implementation("io.github.llamalad7:mixinextras-common:0.4.1")
        implementation("org.ow2.asm:asm-tree:9.7.1")
        implementation("org.ow2.asm:asm-commons:9.7.1")
        implementation("org.ow2.asm:asm-util:9.7.1")
        implementation("org.ow2.asm:asm-analysis:9.7.1")
        implementation("com.google.code.gson:gson:2.11.0")
        implementation("org.glavo.hmcl:hmcl-dev:3.6+")
    }
    javafx {
        configuration = "compileOnly"
        version = "17"
        modules("javafx.controls", "javafx.media")
    }

}
