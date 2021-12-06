

plugins {
  id("org.jetbrains.kotlin.jvm") version "1.5.31"

  application
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  implementation("com.google.guava:guava:30.1.1-jre")

  implementation("io.arrow-kt:arrow-core:1.0.1")

  implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.5.2")

  testImplementation("org.jetbrains.kotlin:kotlin-test")

  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

