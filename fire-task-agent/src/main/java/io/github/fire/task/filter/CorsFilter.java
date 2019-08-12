package io.github.fire.task.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.springframework.beans.factory.annotation.Value;

/**
 * 
 * @author fire
 * 
 */
//@Component
public class CorsFilter implements ContainerResponseFilter {

	@Value("${allowOrigin}")
	private String allowOrigin;

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {

		responseContext.getHeaders().add("Access-Control-Allow-Origin", allowOrigin);

		// 允许的Header值，不支持通配符
		responseContext.getHeaders().add("Access-Control-Allow-Headers",
				"Accept, Origin, XRequestedWith, Content-Type, LastModified");

		responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");

		// 即使只用其中几种，header和options是不能删除的，因为浏览器通过options请求来获取服务的跨域策略
		responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

		// CORS策略的缓存时间
		responseContext.getHeaders().add("Access-Control-Max-Age", "1209600");

	}

}