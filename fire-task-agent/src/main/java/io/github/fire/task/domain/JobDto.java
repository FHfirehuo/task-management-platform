package io.github.fire.task.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author fire
 * 
 */
@Setter
@Getter
@ToString
public class JobDto implements Serializable {

	private static final long serialVersionUID = -2835252363664358884L;

	private int id;
	private String name;
	private String group;
	private int jdk;
	private String jarCode;
	private String jarName;
	private boolean selfStop;
	private Tactics tactics;
	private String cronExpression;
	private String args;
	private String channel;
	private String owner = "liuyi27";

}
