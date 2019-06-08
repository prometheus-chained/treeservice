plugins {
    java
    id("org.springframework.boot") version "2.1.5.RELEASE"
    id("org.liquibase.gradle") version "2.0.1"
}

apply(plugin = "io.spring.dependency-management")

group = "com.tradeshift"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-jersey")
    implementation("org.springframework.boot:spring-boot-starter-web")

    compile("com.h2database", "h2", "1.4.199")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
