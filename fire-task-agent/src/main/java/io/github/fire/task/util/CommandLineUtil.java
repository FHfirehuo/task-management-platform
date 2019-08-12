package io.github.fire.task.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author fire
 *
 */
@Slf4j
public class CommandLineUtil {

	public static void exec(final String cmd, final String owner) {
		try {

			log.debug("execute: {}", cmd);

			/**
			 * redirectErrorStream
			 * 子进程生成的任何错误输出将与标准输出合并，以便可以使用Process.getInputStream（）方法读取这两者
			 * 
			 */
			final Process process = Runtime.getRuntime().exec(cmd);

			new Thread(() -> {
				if (process != null) {
					InputStream is = process.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
					String line;
					try {
						while ((line = br.readLine()) != null) {
							log.debug(line);
						}
					} catch (Exception e) {
						log.error("read process getInputStream error:", e);
					}
				}
				
			}).start();

			new Thread() {
				public void run() {
					if (process != null) {

						InputStream es = process.getErrorStream();
						InputStreamReader isr = new InputStreamReader(es);
						BufferedReader br = new BufferedReader(isr);
						String line;
						try {
							while ((line = br.readLine()) != null) {
								// 给相关人员发报警信息
								log.debug(line);
							}
						} catch (IOException e) {
							log.error("read process getErrorStream error", e);
						}
					}
					
				}
			}.start();
			
			log.debug("execute {} end", cmd);
			
		} catch (Exception e) {
			log.error("execute {} error", cmd, e);
		}
	}

	/**
	 * 
	 * @param operationMode 运行模式[quartz:定时任务; manual:手动执行;cycle:循环任务;permanent:常驻任务]
	 */
	// " --spring.pid.file=" + jarDirectory + "pid.txt " +
	public static void startUpJavaProcess(String javaHome, String javaSetting, String filePath, String jarCode,
			String jarName, String args, String defaultArgs, int port, int id, String operationMode, String owner) {
		String jarDirectory = filePath + jarCode + File.separator;
		String cmd = javaHome + " " + javaSetting + " -jar " + jarDirectory + jarName + " " + args + " " + defaultArgs
				+ " --agentPort=" + port + " --taskId=" + id + " --enableNettyClient=true --operationMode="
				+ operationMode;
		exec(cmd, owner);
	}

	public static void exec(String cmd) {
		exec(cmd, null);
	}
}
