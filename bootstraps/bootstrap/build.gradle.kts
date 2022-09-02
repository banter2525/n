plugins {
    id("com.navercorp.pinpoint.java8-library")
}

dependencies {
    api(project(":pinpoint-commons"))
    api(project(":pinpoint-annotations"))
    api(project(":pinpoint-bootstrap-core"))
    testImplementation(libs.log4j.api.jdk7)
    testImplementation(libs.log4j.slf4j.impl.jdk7)
    testImplementation(libs.log4j.core.jdk7)
    testImplementation(libs.commons.io)
}

description = "pinpoint-bootstrap"
