package io.github.fire.task.config;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import io.github.fire.task.resources.UploadResource;

/**
 * 
 * @author fire
 * 
 */
@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		init();
	}

	private void init() {
//		register(WadlResource.class);

		register(UploadResource.class);
		register(MultiPartFeature.class);
		// 跨域过滤器注册
//		register(CorsFilter.class);

	}

}
