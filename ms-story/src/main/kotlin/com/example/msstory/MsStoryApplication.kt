package com.example.msstory


import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@Configuration
class MsStoryApplication{
	@Bean
	fun runner(cf: ConnectionFactory): ApplicationRunner {
		return ApplicationRunner{
			var open = false
			while(!open){
				try{
					cf.createConnection().close()
					open = true
				}catch (e: Exception){
					println("Waiting for RabbitMQ...")
					Thread.sleep(5000)
				}
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<MsStoryApplication>(*args)
}
