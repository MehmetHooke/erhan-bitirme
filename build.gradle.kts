// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google() // Google Maven repository
        mavenCentral() // Central Maven repository
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.0") // Android Gradle Plugin
        classpath("com.google.gms:google-services:4.4.2") // Google Services Plugin
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

allprojects {
    repositories {
        google() // Google Maven repository
        mavenCentral() // Central Maven repository
    }
}
