package io.github.fire.task.event;

import java.util.EventObject;

/**
 * 
 * @author fire
 *
 */
public class ShutDownEvent extends EventObject {

	private static final long serialVersionUID = -4784154099202571886L;

	public ShutDownEvent(Object source) {
		super(source);
	}
}
