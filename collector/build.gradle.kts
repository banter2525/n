plugins {
    id("com.navercorp.pinpoint.java11-library")
}

dependencies {
    implementation(project(":pinpoint-commons"))
    implementation(project(":pinpoint-commons-buffer"))
    implementation(project(":pinpoint-commons-profiler"))
    implementation(project(":pinpoint-commons-server"))
    implementation(project(":pinpoint-commons-server-cluster"))
    implementation(project(":pinpoint-commons-hbase"))
    implementation(project(":pinpoint-rpc"))
    implementation(project(":pinpoint-thrift"))
    implementation(project(":pinpoint-grpc"))
    implementation(platform(project(":pinpoint-plugins")))
    implementation(project(":pinpoint-profiler"))
    implementation(project(":pinpoint-plugins-loader"))
    implementation(project(":pinpoint-annotations"))

    implementation(libs.zookeeper)
    implementation(libs.guava)
    implementation(libs.netty3)
    implementation(libs.commons.lang3)
    implementation(libs.commons.collections4)
    implementation(libs.libthrift) {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
        exclude(group = "org.apache.httpcomponents", module = "httpcore")
        exclude(group = "org.slf4j", module = "slf4j-api")
        exclude(group = "javax.annotation", module = "javax.annotation-api")
    }
    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.databind)
    implementation(libs.slf4j.api)
    implementation(libs.log4j.slf4j.impl)
    implementation(libs.spring.core) {
        exclude(group = "commons-logging", module = "commons-logging")
    }
    implementation(libs.spring.context)
    implementation(libs.spring.orm)
    implementation(libs.spring.web)
    implementation(libs.spring.webmvc)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.log4j2)
    implementation(libs.metrics.core)
    implementation(libs.metrics.jvm)
    implementation(libs.metrics.servlets) {
        exclude(group = "com.papertrail", module = "profiler")
    }
    runtimeOnly(libs.commons.lang)
    runtimeOnly(libs.log4j.jcl) {
        exclude(group = "commons-logging", module = "commons-logging")
    }
    runtimeOnly(libs.log4j.core)
    testImplementation(libs.spring.test)
    testImplementation(libs.spring.boot.test)
    testImplementation(libs.awaitility)
    testImplementation(project(":pinpoint-rpc"))
    compileOnly(libs.javax.servlet.api)

    implementation(libs.hbase.shaded.client) {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
        exclude(group = "commons-logging", module = "commons-logging")
    }
    implementation(libs.hbasewd) {
        exclude(group = "log4j", module = "log4j")
    }
    implementation(libs.curator.client) {
        exclude(group = "org.apache.zookeeper", module = "zookeeper")
        exclude(group = "org.apache.curator", module = "curator-test")
    }
    implementation(libs.curator.framework) {
        exclude(group = "org.apache.zookeeper", module = "zookeeper")
        exclude(group = "org.apache.curator", module = "curator-test")
        exclude(group = "org.apache.curator", module = "curator-client")
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
}

description = "pinpoint-collector"