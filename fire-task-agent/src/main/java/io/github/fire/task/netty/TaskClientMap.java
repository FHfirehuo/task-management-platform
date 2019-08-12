package io.github.fire.task.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

/**
 * 客户端链接集合
 * 
 * @author fire
 * 
 */
public class TaskClientMap {
	
	private TaskClientMap() {
	}

	private static Map<String, Channel> jobClient = null;

	/**
	 * 
	 * @return jobClient 包含所有job client的 ConcurrentHashMap
	 */
	public static Map<String, Channel> getClients() {
		return jobClient;
	}

	/**
	 * 
	 * @param host ip地址
	 * @return ip下的job Channel
	 */
	public static Channel getClientByChannelId(String channelId) {
		if (jobClient == null || jobClient.isEmpty()) {
			return null;
		}
		return jobClient.get(channelId);
	}

	/**
	 * 
	 * @param host ip地址
	 * @param channel ip下的job Channel
	 */
	public static void addClient(String channelId, Channel channel) {
		if (jobClient == null) {
			jobClient = new ConcurrentHashMap<>();
		}
		jobClient.put(channelId, channel);
	}

	/**
	 * 
	 * @param host ip地址
	 * @return 删除的数量
	 */
	public static int removeClientByChannelId(String channelId) {
		if (jobClient.containsKey(channelId)) {
			jobClient.remove(channelId);
			return 1;
		} else {
			return 0;
		}
	}

	public static int size() {
		if (jobClient == null || jobClient.isEmpty()) {
			return 0;
		}
		return jobClient.size();
	}

	public static boolean exist(String channelId) {
		if (jobClient == null || jobClient.isEmpty()) {
			return false;
		}
		return jobClient.containsKey(channelId);
	}
}
