package com.example.msconfigserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

@SpringBootApplication
@EnableConfigServer
class MsConfigServerApplication

fun main(args: Array<String>) {
	runApplication<MsConfigServerApplication>(*args)
}
