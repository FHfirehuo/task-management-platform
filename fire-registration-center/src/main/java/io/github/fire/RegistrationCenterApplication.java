package io.github.fire;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RegistrationCenterApplication {

	public static void main(String[] args) {
        new SpringApplicationBuilder(RegistrationCenterApplication.class).run(args);
    }
}
