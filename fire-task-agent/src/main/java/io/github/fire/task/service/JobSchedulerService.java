package io.github.fire.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.fire.task.domain.Job;
import io.github.fire.task.domain.JobDto;
import io.github.fire.task.quartz.QuartzTask;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author fire
 *
 */
@Slf4j
@Service
public class JobSchedulerService {

	@Autowired
	private Scheduler scheduler;

	public void add(String javaHome, String javaSetting, String filePath, int port, String defaultArgs, JobDto job) {

		TriggerKey triggerKey = TriggerKey.triggerKey(job.getName(), job.getGroup());

		try {
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (null != trigger) {
				// 已经有了此任务
				return;
			}
		} catch (SchedulerException e) {
			log.error("添加任务时判断任务是否存在出现错误", e);
		}

		JobDetail jobDetail = JobBuilder.newJob(QuartzTask.class).withIdentity(job.getName(), job.getGroup())

				.usingJobData("javaHome", javaHome).usingJobData("javaSetting", javaSetting)
				.usingJobData("filePath", filePath).usingJobData("code", job.getJarCode())
				.usingJobData("jar", job.getJarName()).usingJobData("args", job.getArgs()).usingJobData("port", port)
				.usingJobData("id", job.getId()).usingJobData("defaultArgs", defaultArgs)
				.usingJobData("owner", job.getOwner())
				.build();
		// 表达式调度构建器(即任务执行的时间)
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getName(), job.getGroup())
				.withSchedule(scheduleBuilder).build();

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			log.error("添加任务出现错误", e);
		}
	}

	/**
	 * 删除一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public boolean deleteJob(JobDto job) throws SchedulerException {
		JobKey jobKey = JobKey.jobKey(job.getName(), job.getGroup());
		return scheduler.deleteJob(jobKey);
	}

	/**
	 * 获取所有计划中的任务列表
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<Job> getAllJob() throws SchedulerException {

		List<Job> jobList = new ArrayList<>();

		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				Job job = new Job();
				job.setName(jobKey.getName());
				job.setGroup(jobKey.getGroup());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					job.setCronExpression(cronExpression);
				}

				JobDataMap dataMap = scheduler.getJobDetail(jobKey).getJobDataMap();
				job.setJavaHome(dataMap.getString("javaHome"));
				job.setJarCode(dataMap.getString("code"));
				job.setJarName(dataMap.getString("jar"));
				job.setArgs(dataMap.getString("args"));
				job.setId(dataMap.getInt("id"));
				jobList.add(job);
			}
		}
		return jobList;
	}


	// TODO Auto-generated method stub
	public boolean have(JobDto job) throws SchedulerException {
		List<Job> allJob = this.getAllJob();
		for (Job job2 : allJob) {
			if (Objects.equals(job.getGroup(), job2.getGroup()) && Objects.equals(job.getName(), job2.getName())) {
				return true;
			}
		}
		return false;
	}

}
