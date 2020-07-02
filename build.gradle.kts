plugins {
    kotlin("jvm") version "1.3.72"
    application
    id("com.github.johnrengelman.shadow").apply {
        version("6.0.0")
    }
}

group = "me.sagiri.char"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
//    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.register("runClient") {
    doLast{
        application(Action<JavaApplication>{
            application {
                mainClassName = "me.sagiri.char.client.MainKt"
            }
        })
    }
}

tasks.register("runServer") {
    doLast{
        application {
            mainClassName = "me.sagiri.char.server.MainKt"
        }
    }
}

tasks.register("shadJarClient") {
    doLast{

    }
}

application {
    mainClassName = "me.sagiri.char.client.MainKt"
}