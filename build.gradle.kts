plugins {
    id("java")
    id("war")
}

group = "com.nastya"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
}

tasks.withType<War> {
    archiveFileName.set("${project.name}.war")
}


tasks.test {
    useJUnitPlatform()
}