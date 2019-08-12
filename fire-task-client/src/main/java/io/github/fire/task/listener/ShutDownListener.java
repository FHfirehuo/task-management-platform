package io.github.fire.task.listener;

import java.util.EventListener;

import io.github.fire.task.event.ShutDownEvent;

/**
 * 
 * @author fire
 *
 */
public interface ShutDownListener extends EventListener {
	
	public void shutDown(ShutDownEvent event);
}
