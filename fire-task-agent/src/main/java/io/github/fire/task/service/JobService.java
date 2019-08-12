package io.github.fire.task.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.github.fire.task.domain.Job;
import io.github.fire.task.domain.JobDto;
import io.github.fire.task.domain.PatchJobDto;
import io.github.fire.task.domain.Tactics;
import io.github.fire.task.netty.TaskClientMap;
import io.github.fire.task.util.CommandLineUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author fire
 *
 */
@Slf4j
@Service
public class JobService {

	@Value("${java.7}")
	private String java7;

	@Value("${java.8}")
	private String java8;

	@Value("${java.setting}")
	private String javaSetting;

	@Value("${file.path}")
	private String filePath;

	@Value("${netty.port}")
	private int port;

	@Value("${job.args}")
	private String defaultArgs;

	@Value("${job.storage}")
	private String storage;

	@Autowired
	private JobSchedulerService jobSchedulerService;

	public void addAndStart(JobDto job) {

		if (job.isSelfStop() && job.getTactics() == Tactics.ONCE) {
			this.addAndstartQuartzJob(job);
		}

		if (job.isSelfStop() && job.getTactics() == Tactics.CYCLE) {
			this.addCycleJob(job);
		}

		if (!job.isSelfStop()) {
			this.addNonStopJob(job);
		}

	}

	private void addNonStopJob(JobDto job) {

		log.info("启动NonStop任务");
//>/dev/null 2>&1 & echo $! > pid.txt
		String javaHome = this.getJavaHome(job.getJdk());

		CommandLineUtil.startUpJavaProcess(javaHome, javaSetting, filePath, job.getJarCode(), job.getJarName(),
				job.getArgs(), defaultArgs, port, job.getId(), "permanent", job.getOwner());
	}

	private void addCycleJob(JobDto job) {

		log.info("启动Cycle任务");
		// >/dev/null 2>&1 & echo $! > pid.txt
		String javaHome = this.getJavaHome(job.getJdk());

		CommandLineUtil.startUpJavaProcess(javaHome, javaSetting, filePath, job.getJarCode(), job.getJarName(),
				job.getArgs(), defaultArgs, port, job.getId(), "cycle", job.getOwner());
	}

	/**
	 * 
	 * 添加并启动使用 Quartz管理的定时任务
	 * 
	 */
	private void addAndstartQuartzJob(JobDto job) {

		log.debug("start up quartz task");

		String javaHome = this.getJavaHome(job.getJdk());

		jobSchedulerService.add(javaHome, javaSetting, filePath, port, defaultArgs, job);
	}

	private String getJavaHome(int jdk) {
		String javaHome;
		switch (jdk) {
		case 7:
			javaHome = java7;
			break;
		case 8:
			javaHome = java8;
			break;
		default:
			javaHome = java8;
			break;
		}
		return javaHome;
	}

	public void storage() {

		File storageDir = new File(storage);
		if (!storageDir.exists()) {
			storageDir.mkdirs();
		}

		File jobsdata = new File(storage + File.separator + "job.data");

		try (FileOutputStream os = new FileOutputStream(jobsdata);
				ObjectOutputStream oos = new ObjectOutputStream(os)) {
			List<Job> jobs = jobSchedulerService.getAllJob();

			for (Job job : jobs) {
				oos.writeObject(job);
			}
		} catch (IOException e) {
			
			log.error("Save all quzrtz tasks to local failure", e);
		} catch (SchedulerException e) {
			log.error("Get the task failed in quzrtz", e);
		}

	}

	public void init() {

		File jobsdata = new File(storage + File.separator + "job.data");
		if (!jobsdata.exists()) {
			return;
		}

		try (FileInputStream is = new FileInputStream(jobsdata); ObjectInputStream ois = new ObjectInputStream(is)) {

			while (is.available() > 0) {
				Job job = (Job) ois.readObject();
				jobSchedulerService.add(job.getJavaHome(), javaSetting, filePath, port, defaultArgs, job);
			}

		} catch (IOException e) {
			log.error("Failed to read file ...", e);
		} catch (ClassNotFoundException e) {
			log.error("Conversion failed after reading task", e);
		} finally {
			Path path = jobsdata.toPath();
			try {
				Files.delete(path);
			} catch (IOException e) {
				log.error("Failed to delete all files", e);
			}
		}

	}

	public boolean stop(JobDto job) {

		try {
			if (job.isSelfStop() && job.getTactics() == Tactics.ONCE) {
				log.debug("delete quzrtz task");

				if (!jobSchedulerService.have(job)) {
					return true;
				}
				return jobSchedulerService.deleteJob(job);
			}

			log.info("停止非 quzrtz 任务");
			String channelId = job.getChannel();
			if (TaskClientMap.exist(channelId)) {

				String auth = "SALT_KEY" + ",SHOUTDOWN";
				TaskClientMap.getClientByChannelId(channelId).writeAndFlush(auth);
				log.info("停止非 quzrtz 任务 SHOUTDOWN");
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					log.info("停止任务时休眠出错", e);
				}

				auth = "SALT_KEY" + ",SHUTDOWN";
				TaskClientMap.getClientByChannelId(channelId).writeAndFlush(auth);
				log.info("停止非 quzrtz 任务 SHUTDOWN");
				log.info("给{}发送SHUTDOWN消息", channelId);
				return true;
			} else {
				log.debug("停止任务{} 时没找到channelId", channelId);
			}

		} catch (Exception e) {
			log.error("停止任务失败", e);
		}

		return false;
	}

	public boolean operating(PatchJobDto dto) {

		if (dto.isAlreadyStop()) {
			return this.alreadyStop(dto);
		}

		if (dto.isForceStop()) {
			return this.forceStop(dto);
		}

		if (dto.isExecute()) {
			return this.execute(dto);
		}

		return false;
	}

	private boolean alreadyStop(PatchJobDto job) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean execute(PatchJobDto job) {
		if (job.getTactics() != Tactics.ONCE) {
			return false;
		}

		// >/dev/null 2>&1
//		 + " --spring.pid.file=" + filePath + job.getJarCode() + File.separator+ "app.txt >/dev/null 2>&1"

		String javaHome = this.getJavaHome(job.getJdk());
		CommandLineUtil.startUpJavaProcess(javaHome, javaSetting, filePath, job.getJarCode(), job.getJarName(),
				job.getArgs(), defaultArgs, port, job.getId(), "manual", job.getOwner());
		return true;
	}

	private boolean forceStop(PatchJobDto job) {
		File pidFile = new File(filePath + job.getJarCode() + File.separator + "pid.txt");
		if (!pidFile.exists()) {
			return false;
		}

		String num = null;
		try (FileReader fr = new FileReader(pidFile); BufferedReader bwo = new BufferedReader(fr)) {
			num = bwo.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (StringUtils.isEmpty(num)) {
			return false;
		}
		String cmd = "kill -9 " + num;
		log.info(cmd);
		CommandLineUtil.exec(cmd);

		return true;
	}

}
