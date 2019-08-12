package io.github.fire.task.domain;

import java.lang.reflect.Field;

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
public class PatchJobDto extends JobDto {

	private static final long serialVersionUID = 5134283204658086502L;

	private boolean alreadyStop;
	private boolean forceStop;
	private boolean execute;

	public boolean available() {
		int available = 0;

		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.getGenericType().getTypeName().equals("boolean") && (boolean) field.get(this)) {
					available++;
				}
			}

			return available == 1;
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return false;
	}
}
