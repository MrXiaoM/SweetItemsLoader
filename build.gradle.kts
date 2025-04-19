plugins {
    java
    `maven-publish`
    id ("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "top.mrxiaom.sweet.itemsadder"
version = "1.0.2"
val targetJavaVersion = 8
val shadowGroup = "top.mrxiaom.sweet.itemsadder.libs"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://s01.oss.sonatype.org/content/groups/public/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
    // compileOnly("org.spigotmc:spigot:1.20") // NMS

    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.3-beta-14")

    implementation("org.jetbrains:annotations:24.0.0")
    implementation("com.github.technicallycoded:FoliaLib:0.4.4")
    implementation("top.mrxiaom:PluginBase:1.3.8")
}
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}
tasks {
    shadowJar {
        archiveClassifier.set("")
        mapOf(
            "org.intellij.lang.annotations" to "annotations.intellij",
            "org.jetbrains.annotations" to "annotations.jetbrains",
            "com.tcoded.folialib" to "folialib",
            "top.mrxiaom.pluginbase" to "base",
        ).forEach { (original, target) ->
            relocate(original, "$shadowGroup.$target")
        }
        listOf(
            "top/mrxiaom/pluginbase/func/AbstractGui*",
            "top/mrxiaom/pluginbase/func/gui/*",
            "top/mrxiaom/pluginbase/utils/PAPI*",
            "top/mrxiaom/pluginbase/utils/IA*",
            "top/mrxiaom/pluginbase/utils/ItemStackUtil*",
            "top/mrxiaom/pluginbase/func/GuiManager*",
            "top/mrxiaom/pluginbase/gui/*",
            "top/mrxiaom/pluginbase/func/LanguageManager*",
            "top/mrxiaom/pluginbase/func/language/*",
            "top/mrxiaom/pluginbase/utils/Adventure*",
        ).forEach(this::exclude)
    }
    build {
        dependsOn(shadowJar)
    }
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from(sourceSets.main.get().resources.srcDirs) {
            expand(mapOf("version" to version))
            include("plugin.yml")
        }
    }
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components.getByName("java"))
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()
        }
    }
}
