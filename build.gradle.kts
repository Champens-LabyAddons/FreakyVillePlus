plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "dk.fvtrademarket"
version = providers.environmentVariable("VERSION").getOrElse("0.0.1")

labyMod {
    defaultPackageName = "dk.fvtrademarket.fvplus" //change this to your main package name (used by all modules)

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    // When the property is set to true, you can log in with a Minecraft account
                    devLogin = true
                }
            }
        }
    }

    addonInfo {
        namespace = "fvplus"
        displayName = "FreakyVille+"
        author = "Champen_V1ldtand"
        description = "A collection of QOL changes for FreakyVille"
        minecraftVersion = "*"
        version = rootProject.version.toString()

        addon("labyswaypoints", true)
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}
