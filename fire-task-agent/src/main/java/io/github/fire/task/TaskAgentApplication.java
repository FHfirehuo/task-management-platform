package io.github.fire.task;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import io.github.fire.task.listeners.ClosedListener;
import io.github.fire.task.listeners.StartListner;

/**
 * 
 * @author fire
 *
 */
@SpringBootApplication
public class TaskAgentApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder()
		.sources(TaskAgentApplication.class)
		.bannerMode(Banner.Mode.OFF)
		.listeners(new StartListner(), new ClosedListener())
		.run(args);
	}
}
