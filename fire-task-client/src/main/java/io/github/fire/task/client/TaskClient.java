package io.github.fire.task.client;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author fire
 *
 */
public class TaskClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskClient.class);

	private static final TaskClient clientInstance = new TaskClient();
	// 密钥,现在写死后期可有后台传递
	private static final String SALT_KEY = "SALT_KEY";

	private int id;
	private int port;
	private boolean keepOn = true;
	private static final String HOST = "localhost";

	private NioEventLoopGroup group;
	private Channel channel;
	private Bootstrap bootstrap;

	private Thread shutdownHook;

	private final Object startupShutdownMonitor = new Object();

	private TaskClient() {
	}

	public static synchronized TaskClient getInstance() {
		return clientInstance;
	}

	public int getPort() {
		return port;
	}

	public int getId() {
		return id;
	}

	public boolean isKeepOn() {
		return keepOn;
	}

	public void setKeepOn(boolean keepOn) {
		this.keepOn = keepOn;
	}

	public void init(int id, int port, CountDownLatch latch) {
		this.id = id;
		this.port = port;
		new Thread(() -> start(latch), "jarClient").start();
		registerShutdownHook();
	}

	public void close() {

		try {

			if (channel != null && channel.isActive()) {
				LOGGER.info("发送SHUTDOWN 消息");

				String auth = SALT_KEY + ",SHUTDOWN" + "," + this.id;
				channel.writeAndFlush(auth).addListener(ChannelFutureListener.CLOSE);
			}
			LOGGER.info("关闭 job client");
			group.shutdownGracefully().sync();
		} catch (InterruptedException e) {
			LOGGER.error(" job client shutdown is error", e);
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * 启动netty进行通信
	 * 
	 * @author 作者: 刘燚
	 */
	protected void start(CountDownLatch latch) {
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.remoteAddress(new InetSocketAddress(HOST, port)).handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {

						ch.pipeline().addLast(new StringDecoder()).addLast(new StringEncoder())
								.addLast(new IdleStateHandler(0, 10, 0)).addLast(new ClientHandler(id, SALT_KEY));

					}
				});

		// 调用连接操作
		connect(latch);
	}

	protected void connect(CountDownLatch latch) {
		try {
			// 连接操作是在ConnectionListener里完成的
			ChannelFuture future = bootstrap.connect().addListener(new ConnectionListener(latch)).sync();
			channel = future.channel();
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			LOGGER.error("job Client start is error", e);
			Thread.currentThread().interrupt();
		} finally {
			LOGGER.info("第一次 连接 finally");
		}

	}

	public void connect() {
		LOGGER.info("断线重连");
		try {
			ChannelFuture future = bootstrap.connect().addListener(new ConnectionListener()).sync();
			channel = future.channel();
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			LOGGER.error("job Client start is error", e);
			Thread.currentThread().interrupt();
		} finally {
			LOGGER.info("重连 finally");
		}

	}

	public void registerShutdownHook() {
		if (this.shutdownHook == null) {
			// No shutdown hook registered yet.
			this.shutdownHook = new Thread() {
				@Override
				public void run() {
					synchronized (startupShutdownMonitor) {
						close();
					}
				}
			};
			Runtime.getRuntime().addShutdownHook(this.shutdownHook);
		}
	}

}
