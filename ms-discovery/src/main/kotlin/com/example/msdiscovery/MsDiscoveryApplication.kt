package com.example.msdiscovery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@EnableEurekaServer
@Configuration
class MsDiscoveryApplication

fun main(args: Array<String>) {
	runApplication<MsDiscoveryApplication>(*args)
}
