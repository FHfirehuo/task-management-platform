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
@Getter
@Setter
@ToString
public class Job extends JobDto implements Serializable {

	private static final long serialVersionUID = -5224846366434511322L;

	private String javaHome;

}
