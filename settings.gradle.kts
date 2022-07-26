pluginManagement {
    
    val dockerPluginId: String by settings
    val dockerPluginVersion: String by settings
    val quarkusPluginId: String by settings
    val quarkusPluginVersion: String by settings
    
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    
    plugins {
        id(dockerPluginId) version dockerPluginVersion
        id(quarkusPluginId) version quarkusPluginVersion
    }
}

rootProject.name = "jdbi-quarkus-native"