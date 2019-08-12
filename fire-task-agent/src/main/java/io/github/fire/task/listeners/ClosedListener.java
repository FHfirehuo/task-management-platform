package io.github.fire.task.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * 
 * @author fire
 * 
 */
public class ClosedListener implements ApplicationListener<ContextClosedEvent > {

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		
//		ApplicationContext app = event.getApplicationContext();
		
	}

}
