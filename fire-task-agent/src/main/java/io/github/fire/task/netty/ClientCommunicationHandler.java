package io.github.fire.task.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *  客户端通信处理器
 * 
 * @author fire
 * 
 * {@code @Sharable} 标示一个Channel-Handler 可以被多个Channel 安全地共享
 *  
 */
@Sharable
public class ClientCommunicationHandler extends ChannelInboundHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientCommunicationHandler.class);

	private ServerCommunicationHandler requestService;

	public ClientCommunicationHandler(ServerCommunicationHandler requestService) {
		this.requestService = requestService;
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		String id = ctx.channel().id().asShortText();
		LOGGER.info(" {} 断开链接", id);
		TaskClientMap.removeClientByChannelId(id);
		super.handlerRemoved(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		String id = ctx.channel().id().asShortText();
		LOGGER.info(" {} 异常断开链接--修改状态", id);
		// 调用监控
		requestService.errorMessage(id);
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		LOGGER.info("接收到消息 {}", msg);
		String id = ctx.channel().id().asShortText();

		if (msg instanceof String) {
			String message = (String) msg;
			if (!auth(message, id)) {
				ctx.close();
			}

		}
	}

	// 模拟api请求前的验证
	private boolean auth(String message, String clientId) {
		LOGGER.info("令牌验证 message {}", message);
		String[] msg = message.split(",");
		String saltKey = msg[0]; // 密钥
		if (!"SALT_KEY".equals(saltKey)) {
			return false;
		}

		switch (msg[1]) {
		case "RUNNING":
			String clientVersion = "";
			if (msg.length > 3) {
				clientVersion = msg[3];
			}
			requestService.running(msg[2], clientId, clientVersion);
			break;
		case "SHUTDOWN":
			requestService.shutdown(msg[2], clientId);
			break;
		case "SHOUTDOWN":
			requestService.shutdown(msg[2], clientId);
			break;
		default:
			break;
		}

		return true;
	}
}
