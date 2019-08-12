package io.github.fire.task.quartz;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import io.github.fire.task.util.CommandLineUtil;

/**
 * 
 * @author fire
 *
 */
public class QuartzTask extends QuartzJobBean {

	/**
	 * 首先 Job 类需要一个无参构造方法，另外，在 Job 中存储状态属性是没有意义的，因为每次执行完成后，对象将会被删除。
	 */
	private QuartzTask() {

	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();

		String javaHome = dataMap.getString("javaHome");
		String javaSetting = dataMap.getString("javaSetting");

		String filePath = dataMap.getString("filePath");
		String code = dataMap.getString("code");
		String jar = dataMap.getString("jar");
		String args = dataMap.getString("args");
		int port = dataMap.getInt("port");
		int id = dataMap.getInt("id");
		String owner = dataMap.getString("owner");
		String defaultArgs = dataMap.getString("defaultArgs");

		CommandLineUtil.startUpJavaProcess(javaHome, javaSetting, filePath, code, jar, args, defaultArgs, port, id, "quartz", owner);
	}

}
