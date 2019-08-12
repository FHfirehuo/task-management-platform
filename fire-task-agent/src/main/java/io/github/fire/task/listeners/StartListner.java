package io.github.fire.task.listeners;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * 
 * @author fire
 * 
 */
public class StartListner implements ApplicationListener<ApplicationStartedEvent > {
	
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {

//		ConfigurableApplicationContext app = event.getApplicationContext();
		
	}



}
