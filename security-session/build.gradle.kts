plugins {
    id("io.micronaut.build.internal.security-module")
}

dependencies {
    annotationProcessor(mn.micronaut.graal)
    api(mn.micronaut.http)
    api(mnSession.micronaut.session)
    api(project(":security"))
    api(mn.micronaut.http.server)
    implementation(libs.reactor.core)
    testImplementation(mn.micronaut.http.client)
    testImplementation(mn.micronaut.inject.groovy)
    testImplementation(mn.micronaut.http.server.netty)
    testImplementation(project(":test-suite-utils"))
    testImplementation(project(":test-suite-utils-security"))
}
