package io.github.fire.task.netty;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理服务
 * 
 * @author fire
 *
 */
@Slf4j
@Service
public class AgentServer {

	@Value("${netty.port}")
	private int port;

	@Autowired
	private ServerCommunicationHandler requestService;

	private NioEventLoopGroup group;
	private NioEventLoopGroup group2;

	public void init() {

		new Thread(() -> start(), "JobServer").start();

	}

	public void close() {

		try {
			log.info("关闭 JobServer");
			group.shutdownGracefully().sync();
		} catch (InterruptedException e) {
			log.error("shutdown is error", e);
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * 方法描述: 启动服务端
	 * 
	 * @author 作者: 刘燚
	 * @date 时间: 2018年6月22日 上午11:07:27
	 * @throws 异常:
	 */
	public void start() {

		ClientCommunicationHandler servertHandler = new ClientCommunicationHandler(requestService);
		group = new NioEventLoopGroup();
		group2 = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group, group2).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							log.info("job客户端连接");
							ch.pipeline()
//							.addLast(new FixedLengthFrameDecoder(1<<5))
									.addLast(new StringDecoder()).addLast(new StringEncoder())
									// servertHandler 被标注为@Shareable，所以我们可以总是使用同样的实例
									.addLast(servertHandler);

							addJobClient(ch);
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind().sync();

			log.info("Job Server Started And Listen On {}", f.channel().localAddress());

			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			log.error("JobServer start is error", e);
			Thread.currentThread().interrupt();
		} finally {
			try {
				group.shutdownGracefully().sync();
				group2.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				log.error("JobServer shutdown is error", e);
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * 方法描述: 添加缓存
	 * 
	 * @author 作者: 刘燚
	 * 
	 * @param ch 要缓存并记录的SocketChannel
	 * 
	 * @date 时间: 2018年6月22日 下午2:36:10
	 * 
	 * @throws 异常:
	 */
	private void addJobClient(SocketChannel ch) {
		TaskClientMap.addClient(ch.id().asShortText(), ch);
	}

}
