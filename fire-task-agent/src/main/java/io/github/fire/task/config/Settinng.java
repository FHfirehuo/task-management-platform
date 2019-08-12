package io.github.fire.task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author fire
 * 
 */
@Component
@ConfigurationProperties(prefix = "server.port")
public class Settinng {

	private int job = 9061;

	public int getJobPort() {
		return job;
	}

	public void setJob(int job) {
		this.job = job;
	}

}
