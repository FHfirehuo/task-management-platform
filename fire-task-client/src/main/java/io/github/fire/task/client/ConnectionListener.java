package io.github.fire.task.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * 
 * @author fire
 *
 */
class ConnectionListener implements ChannelFutureListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionListener.class);

	private CountDownLatch latch;
	
	public ConnectionListener() {
		
	}

	public ConnectionListener(CountDownLatch latch) {
		this.latch = latch;
	}

	public void operationComplete(ChannelFuture future) {

		if (!future.isSuccess()) {

			LOGGER.info("连接失败");
			
			TaskClient jobClient = TaskClient.getInstance();
			
			if (!jobClient.isKeepOn()) {
				LOGGER.info("自停单次任务。不重新连接服务端。");
			} else {

				LOGGER.info("一分钟后重新连接服务端。");

				future.channel().eventLoop().schedule(() -> {
					LOGGER.info("开始重连操作...");
					jobClient.connect();
				}, 1, TimeUnit.MINUTES);
			}
		} else {
			LOGGER.info("连接服务端成功...");
		}

		if (latch != null) {
			LOGGER.info("第一次连接解除锁");
			// 无轮成功与否都不阻止主程序的运行
			latch.countDown();
		}

	}

}
