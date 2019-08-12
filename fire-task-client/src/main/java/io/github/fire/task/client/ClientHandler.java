package io.github.fire.task.client;

import java.lang.management.ManagementFactory;

import io.github.fire.task.event.ShutDownEvent;
import io.github.fire.task.listener.ListenerManager;
import io.github.fire.task.listener.ShutDownListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author fire
 *
 */
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

	private int jobId;
	private String saltKey;
	private String clientVersion = "0.0.1";

	public ClientHandler(int id, String saltKey) {
		jobId = id;
		this.saltKey = saltKey;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("--- Server is active ---");

		String name = ManagementFactory.getRuntimeMXBean().getName();
		String pid = name.split("@")[0];

		String auth = saltKey + ",RUNNING" + "," + jobId + "," + clientVersion + "," + pid;
		ctx.writeAndFlush(auth);
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {

		log.info("--- Server is inactive ---");
		TaskClient client = TaskClient.getInstance();
		if (client.isKeepOn()) {
			log.info("1 min 之后尝试重新连接服务器...");
			Thread.sleep(60 * 1000L);
			client.connect();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

		// 重连操作可以写在这里,通过对 Throwable 的子类型做出反应来进行重连操作
		log.error("连接出现异常", cause);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			// 不管是读事件空闲还是写事件空闲都向服务器发送心跳包
			IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
			if (idleStateEvent.state() == IdleState.WRITER_IDLE) {

			}
		}

		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {

		try {
			if (msg instanceof String) {
				String message = (String) msg;
				log.info("接收到消息 {}", message);
				String[] reslut = message.split(",");
				if (saltKey.equals(reslut[0]) && "SHUTDOWN".equals(reslut[1])) {
					log.info("触发SHUTDOWN");
					TaskClient.getInstance().setKeepOn(false);

					if (ListenerManager.getListeners() != null) {
						ShutDownEvent event = new ShutDownEvent(this);
						for (ShutDownListener listener : ListenerManager.getListeners()) {
							listener.shutDown(event);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("处理消息出现异常", e);
		}
	}
}
