plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "Yggdrasil"
include("api")
include("bedwars")
include("bedwars:example")
findProject(":bedwars:example")?.name = "example"
include("util-InvUI")
include("limbo")
