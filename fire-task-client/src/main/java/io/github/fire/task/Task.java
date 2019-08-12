package io.github.fire.task;

import io.github.fire.task.listener.ShutDownListener;

/**
 * 
 * @author fire
 *
 */
public interface Task {
	
	void start(String[] args);
	
	void addListener(ShutDownListener shoutDownListener);
	
}
