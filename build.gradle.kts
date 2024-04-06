import com.vanniktech.maven.publish.SonatypeHost

val ktorVersion: String by project

plugins {
    kotlin("jvm") version "1.9.22"
    id("com.vanniktech.maven.publish") version "0.28.0"
}

group = "io.github.cotrin8672"
version = "1.2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
}

kotlin {
    jvmToolchain(17)
}

afterEvaluate {
    mavenPublishing {
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = false)
        signAllPublications()

        coordinates(
            group.toString(),
            name,
            version.toString()
        )

        pom {
            name.set("Ktor Line Webhook Signature Validation Plugin")
            url.set("https://github.com/cotrin8672/ktor-line-webhook-plugin")
            description.set("Ktor plugin for validating signature with line messaging api webhook")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("repo")
                }
            }
            developers {
                developer {
                    id.set("cotrin8672")
                    name.set("Cotrin")
                    email.set("gummy8672@gmail.com")
                }
            }
            scm {
                url.set("https://github.com/cotrin8672/ktor-line-webhook-plugin")
            }
        }
    }
}
