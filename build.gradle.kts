plugins {
    java
    id("org.springframework.boot") version "2.1.5.RELEASE"
}

apply(plugin = "io.spring.dependency-management")

group = "com.tradeshift"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
    implementation("org.springframework.boot:spring-boot-starter-jersey")
    implementation("org.springframework.boot:spring-boot-starter-web")

    compile("org.neo4j", "neo4j", "3.5.6")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
