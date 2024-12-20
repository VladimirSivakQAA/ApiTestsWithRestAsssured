plugins {
    id("java")
    id("java-library")
    id("io.freefair.lombok") version "6.3.0"
    id("io.qameta.allure") version "2.9.6"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.junit.platform:junit-platform-launcher:1.11.3")
    implementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.3")
    implementation("org.assertj:assertj-core:3.26.0")
    implementation("io.qameta.allure:allure-junit5:2.29.1")
    implementation("io.qameta.allure:allure-assertj:2.29.1")
    runtimeOnly("org.aspectj:aspectjweaver:1.9.22")
    implementation("com.google.guava:guava:32.1.2-jre")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("io.rest-assured:rest-assured:4.4.0")
    implementation("io.qameta.allure:allure-rest-assured:2.29.1")
    implementation("net.bytebuddy:byte-buddy:1.14.6")
    implementation("org.aeonbits.owner:owner:1.0.12")

}

tasks.register<Test>("runAllTests") {
    useJUnitPlatform()
    systemProperties["file.encoding"] = "UTF-8"
    systemProperties["encoding"] = "UTF-8"
    systemProperty("allure.results.directory", "$rootDir/allure-results")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    systemProperty("allure.results.directory", "$rootDir/allure-results")
    systemProperties["file.encoding"] = "UTF-8"
    systemProperties["encoding"] = "UTF-8"
}

tasks.register<Test>("runSmokeTests") {
    useJUnitPlatform(){
        includeTags("smoke")
    }
    systemProperty("allure.results.directory", "$rootDir/allure-results")
    systemProperties["file.encoding"] = "UTF-8"
    systemProperties["encoding"] = "UTF-8"
}