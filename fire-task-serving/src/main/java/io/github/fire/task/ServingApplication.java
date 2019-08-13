package io.github.fire.task;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ServingApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ServingApplication.class)
		.run(args);
	}
}
