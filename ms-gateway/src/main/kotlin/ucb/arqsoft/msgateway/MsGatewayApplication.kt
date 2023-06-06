package ucb.arqsoft.msgateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@Configuration
class MsGatewayApplication

fun main(args: Array<String>) {
	runApplication<MsGatewayApplication>(*args)
}
