package io.github.fire.task;

import java.util.PriorityQueue;
import java.util.concurrent.CountDownLatch;

import io.github.fire.task.client.TaskClient;
import io.github.fire.task.event.ShutDownEvent;
import io.github.fire.task.listener.ListenerManager;
import io.github.fire.task.listener.ShutDownListener;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author fire
 *
 */
@Slf4j
public abstract class AbstractTask implements Task{


	private boolean enableNetty = false;

	private boolean selfStop = false;

	private static TaskClient client;


	static {
		client = TaskClient.getInstance();
	}

	/**
	 * 方法描述: 和任务管理平台的通信在此实现
	 * 
	 * @author 作者: 刘燚
	 */
	public final void start(String[] args) {
		
		try {
			int id = 0;
			int port = 9091;
			// 如果任务需要循环执行时,请在命令行中添加 once 指令
			for (String arg : args) {
				
				if (arg.startsWith("--taskId=")) {
					String[] command = arg.split("=");
					id = Integer.valueOf(command[1]);
				}

				if (arg.startsWith("--agentPort=")) {
					String[] command = arg.split("=");
					port = Integer.valueOf(command[1]);
				}

				if (arg.startsWith("--enableNettyClient=")) {
					String[] command = arg.split("=");
					enableNetty = Boolean.valueOf(command[1]);
				}

				if (arg.startsWith("--operationMode")) {
					String[] command = arg.split("=");
					if (("cycle").equals(command[1])) {
						selfStop = true;
						client.setKeepOn(true);
					}
					
					if (("quartz").equals(command[1])) {
						selfStop = true;
						client.setKeepOn(false);
					}
					
					//手动任务
					if (("manual").equals(command[1])) {
						selfStop = true;
						client.setKeepOn(false);
					}
					
					//常驻任务
					if (("permanent").equals(command[1])) {
						selfStop = false;
						client.setKeepOn(false);
					}
				}

			}

			if (enableNetty) {
				CountDownLatch latch = new CountDownLatch(1);
				client.init(id, port, latch);
				latch.await();
			}

			do {
				run(args);
			} while (selfStop && client.isKeepOn());

		} catch (Exception e) {
			log.error("运行出错", e);
		} finally {
			
			if (ListenerManager.getListeners() != null) {
				ShutDownEvent event = new ShutDownEvent(this);
				for (ShutDownListener listener : ListenerManager.getListeners()) {
					listener.shutDown(event);
				}
			}

			if (enableNetty) {
				client.close();
			}
		}
	}


	@Override
	public final void addListener(ShutDownListener shoutDownListener) {

		if (ListenerManager.getListeners() == null) {
			ListenerManager.setListeners(new PriorityQueue<>());
		}
		ListenerManager.getListeners().add(shoutDownListener);
	}

	/**
	 * 任务的业务逻辑在此处执行。请在此处开始你的代码
	 * 
	 * @author 作者: 刘燚
	 */
	public abstract void run(String[] args);

}
