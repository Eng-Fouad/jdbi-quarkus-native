plugins {
    java
    id("io.quarkus")
    id("com.bmuschko.docker-remote-api")
}

group = "io.fouad"
version = "0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

// Project properties can be accessed via delegation
val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val jdbiPlatformGroupId: String by project
val jdbiPlatformArtifactId: String by project
val jdbiPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation(enforcedPlatform("${jdbiPlatformGroupId}:${jdbiPlatformArtifactId}:${jdbiPlatformVersion}"))
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("io.quarkus:quarkus-agroal")
    implementation("io.quarkus:quarkus-arc")
    implementation("org.jdbi:jdbi3-sqlobject")
    implementation("org.jdbi:jdbi3-postgres")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.compilerArgs = listOf("-parameters", "-Xdoclint:none", "-Xlint:all", "-Xlint:-exports", "-Xlint:-serial", "-Xlint:-try",
                                      "-Xlint:-requires-transitive-automatic", "-Xlint:-requires-automatic", "-Xlint:-missing-explicit-ctor")
    }

    compileTestJava {
        options.encoding = "UTF-8"
        options.compilerArgs = listOf("-parameters", "-Xdoclint:none", "-Xlint:all", "-Xlint:-exports", "-Xlint:-serial", "-Xlint:-try",
                                      "-Xlint:-requires-transitive-automatic", "-Xlint:-requires-automatic", "-Xlint:-missing-explicit-ctor")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    test {
        useJUnitPlatform()
    }
    
    quarkusDev {
        workingDirectory.set(rootProject.projectDir) // there is a bug that prevents setting working directory correctly. It will be fixed in next release 2.12: https://github.com/quarkusio/quarkus/pull/26729
    }
}

tasks.create<Copy>("prepareDockerFiles") {
    from(".")
    include("src/", "gradle/", "build.gradle.kts", "settings.gradle.kts", "gradle.properties", "gradlew")
    into("build/docker")
}

tasks.create("buildRegularNativeImage", com.bmuschko.gradle.docker.tasks.image.DockerBuildImage::class) {
    dependsOn("prepareDockerFiles")
    group = "io.fouad"
    dockerFile.set(file("build/docker/src/main/docker/Dockerfile"))
    buildArgs.set(mapOf("GRADLE_BUILD_ARGS" to "-Dquarkus.package.type=native -Dquarkus.native.additional-build-args=-H:ReflectionConfigurationFiles=reflection-config.json,-H:DynamicProxyConfigurationFiles=proxy-config.json"))
    images.add("io.fouad/jdbi-quarkus-native:${project.version}")
    platform.set("linux/amd64")
    remove.set(true) // remove intermediate containers after a successful build
}

tasks.create("buildCompressedNativeImage", com.bmuschko.gradle.docker.tasks.image.DockerBuildImage::class) {
    dependsOn("prepareDockerFiles")
    group = "io.fouad"
    dockerFile.set(file("build/docker/src/main/docker/Dockerfile"))
    buildArgs.set(mapOf("GRADLE_BUILD_ARGS" to "-Dquarkus.package.type=native -Dquarkus.native.additional-build-args=-H:ReflectionConfigurationFiles=reflection-config.json,-H:DynamicProxyConfigurationFiles=proxy-config.json -Dquarkus.native.compression.level=10 -Dquarkus.native.compression.additional-args=--ultra-brute,-v"))
    images.add("io.fouad/jdbi-quarkus-native:${project.version}")
    platform.set("linux/amd64")
    remove.set(true) // remove intermediate containers after a successful build
}

tasks.create("saveNativeImageAsFile", com.bmuschko.gradle.docker.tasks.image.DockerSaveImage::class) {
    group = "io.fouad"
    image.set("io.fouad/jdbi-quarkus-native:${project.version}")
    destFile.set(file("build/images/jdbi-quarkus-native-${project.version}.docker.image.tar"))
    useCompression.set(true)
}