plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.munchies-backend"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("io.github.cdimascio:java-dotenv:5.2.2")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	runtimeOnly("org.postgresql:postgresql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("software.amazon.awssdk:s3:2.20.32")
	implementation("software.amazon.awssdk:auth:2.20.32")  // Provides `AwsCredentials`
	implementation("software.amazon.awssdk:core:2.20.32")
	implementation("software.amazon.awssdk:regions:2.20.32")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
