package io.github.fire.task.listener;

import java.util.Queue;

/**
 * 
 * @author fire
 *
 */
public class ListenerManager {

	private ListenerManager() {
		
	}
	
	private static Queue<ShutDownListener> listeners;

	public static  Queue<ShutDownListener> getListeners() {
		return listeners;
	}
	
	public static void setListeners(Queue<ShutDownListener> listeners) {
		ListenerManager.listeners = listeners;
	}
	
}
