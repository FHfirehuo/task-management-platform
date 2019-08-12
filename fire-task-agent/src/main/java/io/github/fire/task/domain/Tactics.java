package io.github.fire.task.domain;

/**
 * 执行策略
 * 
 * @author fire
 * 
 */
public enum Tactics {
	ONCE(0), CYCLE(1);

	private int value;

	private Tactics(int v) {
		this.value = v;
	}

	public int getValue() {
		return value;
	}
}
