plugins {
    id("com.navercorp.pinpoint.java11-library")
}

dependencies {
    implementation(project(":pinpoint-commons"))
    implementation(project(":pinpoint-commons-profiler"))
    implementation(project(":pinpoint-commons-hbase"))
    implementation(project(":pinpoint-plugins-loader"))
    implementation(project(":pinpoint-annotations"))
    implementation(project(":pinpoint-commons-buffer"))
    implementation(libs.commons.collections4)
    implementation(libs.spring.core) {
        exclude(group = "commons-logging", module = "commons-logging")
    }
    implementation(libs.spring.context)
    implementation(libs.spring.boot)
    implementation(libs.commons.lang3)
    implementation(libs.libthrift)
    implementation(libs.log4j.api)
    runtimeOnly(libs.slf4j.api)
    runtimeOnly(libs.log4j.slf4j.impl)
    runtimeOnly(libs.log4j.core)
    runtimeOnly(libs.log4j.jcl) {
        exclude(group = "commons-logging", module = "commons-logging")
    }
    testImplementation(libs.awaitility)
    testImplementation(libs.spring.test)
    compileOnly(project(":pinpoint-thrift"))
    testCompileOnly(project(":pinpoint-thrift"))
    compileOnly(project(":pinpoint-grpc"))
    testCompileOnly(project(":pinpoint-grpc"))

    implementation(libs.hbase.shaded.client) {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
        exclude(group = "commons-logging", module = "commons-logging")
    }
    implementation(libs.hbasewd) {
        exclude(group = "log4j", module = "log4j")
    }

    implementation(libs.grpc.core) {
        exclude(group = "io.opencensus", module = "opencensus-api")
        exclude(group = "io.opencensus", module = "opencensus-contrib-grpc-metrics")
        exclude(group = "com.google.code.findbugs", module = "jsr305")
    }
    implementation(libs.grpc.netty) {
        exclude(group = "io.netty", module = "netty-codec-http2")
        exclude(group = "io.netty", module = "netty-handler-proxy")
    }
    implementation(libs.netty.handler)
    implementation(libs.netty.transport.native.epoll)
    implementation(libs.netty.codec.http2)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.protobuf)

    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.databind)

    implementation(libs.jackson.annotations)
    implementation(libs.commons.math3)
}

description = "pinpoint-commons-server"