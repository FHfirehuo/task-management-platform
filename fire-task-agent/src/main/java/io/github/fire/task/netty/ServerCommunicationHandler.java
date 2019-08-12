package io.github.fire.task.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 服务端通信处理器
 * 
 * @author fire
 * 
 * @serial 现在使用http回调,其实使用kafka等消息才是最佳
 */
@Service
public class ServerCommunicationHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerCommunicationHandler.class);

	public void running(String jobId, String clientId, String clientVersion) {
		LOGGER.info("任务{} use {} running and clientVersion is {}", jobId, clientId, clientVersion);
	}

	public void shutdown(String jobId, String clientId) {
		LOGGER.info("任务{} in {} shutdown", jobId, clientId);
	}

	public void errorMessage(String id) {
		// 调用(消息/邮件)提醒
	}

}
