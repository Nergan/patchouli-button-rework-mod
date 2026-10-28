import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom") version "1.9.2"
    kotlin("jvm") version "2.1.20"
}

fun prop(name: String): String = project.property(name) as String

version = prop("mod_version")
group = prop("mod_group_id")

base {
    archivesName.set("patchoulibutton-fabric-${prop("minecraft_version")}")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

kotlin {
    jvmToolchain(21)
    sourceSets.named("main") {
        kotlin.srcDir(rootProject.file("src/main/kotlin"))
        kotlin.exclude("**/PatchouliButtonMod.kt")
        kotlin.exclude("**/config/**")
        kotlin.exclude("**/event/ModSetup.kt")
        kotlin.exclude("**/event/NeoForgeHooks.kt")
        kotlin.exclude("**/client/ClientModEvents.kt")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

sourceSets.named("main") {
    java.srcDir(rootProject.file("src/main/java"))
    resources.srcDir(rootProject.file("src/main/resources"))
}

repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
    maven {
        name = "Shedaniel"
        url = uri("https://maven.shedaniel.me/")
    }
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com/releases/")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${prop("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${prop("fabric_loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("fabric_api_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${prop("fabric_kotlin_version")}")
    modImplementation("maven.modrinth:patchouli:${prop("patchouli_fabric_version")}")
    // Patchouli кладёт Fiber внутрь своего jar. Loom в dev этот вложенный jar не поднимает.
    modRuntimeOnly("me.zeroeightsix:fiber:0.23.0-2")
    modImplementation("me.shedaniel.cloth:cloth-config-fabric:${prop("cloth_config_version")}")
    modCompileOnly("com.terraformersmc:modmenu:${prop("modmenu_version")}")
}

tasks.processResources {
    val version = project.version.toString()
    inputs.property("version", version)
    filesMatching("fabric.mod.json") {
        filter { line -> line.replace("\${version}", version) }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

// refmap пишет процессор аннотаций Java, Kotlin-миксинов он не видит.
// Без этих строк боевой Fabric ищет имя Mojmap, и клиент падает при загрузке класса книги.
val patchKotlinRefmap by tasks.registering {
    dependsOn(tasks.named("compileJava"))
    doLast {
        val refmap = layout.buildDirectory.file("classes/java/main/${base.archivesName.get()}-refmap.json").get().asFile
        val slurper = groovy.json.JsonSlurper()
        @Suppress("UNCHECKED_CAST")
        val root = slurper.parse(refmap) as MutableMap<String, Any?>
        val extra = mapOf(
            "com/patchoulibutton/mod/mixin/GuiBookLandingMixin" to mapOf(
                "init" to "Lvazkii/patchouli/client/book/gui/GuiBookLanding;method_25426()V",
                "drawForegroundElements" to "Lvazkii/patchouli/client/book/gui/GuiBookLanding;drawForegroundElements(Lnet/minecraft/class_332;IIF)V",
                "Lnet/minecraft/client/resources/language/I18n;get(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;" to
                    "Lnet/minecraft/class_1074;method_4662(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
            ),
        )
        fun putAll(target: MutableMap<String, Any?>) {
            for ((mixin, members) in extra) {
                @Suppress("UNCHECKED_CAST")
                val current = target[mixin] as? MutableMap<String, Any?> ?: mutableMapOf()
                current.putAll(members)
                target[mixin] = current
            }
        }
        @Suppress("UNCHECKED_CAST")
        val mappings = root["mappings"] as MutableMap<String, Any?>
        putAll(mappings)
        @Suppress("UNCHECKED_CAST")
        val data = root["data"] as MutableMap<String, Any?>
        @Suppress("UNCHECKED_CAST")
        val named = data["named:intermediary"] as MutableMap<String, Any?>
        putAll(named)
        refmap.writeText(groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(root)), Charsets.UTF_8)
    }
}

tasks.named("classes") {
    dependsOn(patchKotlinRefmap)
}
