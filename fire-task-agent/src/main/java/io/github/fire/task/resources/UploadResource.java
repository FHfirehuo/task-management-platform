package io.github.fire.task.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @author fire
 *
 */
@Slf4j
@Component
@Path("file")
public class UploadResource {

	@Value("${file.path}")
	private String filePath;

	@POST
	@Path("upload")
	@Consumes({ MediaType.MULTIPART_FORM_DATA }) // 指定接受类型
	@Produces(MediaType.APPLICATION_JSON) // 返回类型
	public void upload(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition fileMetaData) {

		long code = new Random().nextLong();
		String savePath = filePath + code;

		File filepath = new File(savePath);
		if (!filepath.exists()) {
			filepath.mkdirs();
		}

		if (!filepath.isDirectory()) {
			log.error("上传文件的地址不是目录");
		}

		String original = fileMetaData.getFileName();
		log.info("上传文件的原始名称: {}", original);
		savePath = savePath + File.separator + original;
		try {
			FileOutputStream fos = new FileOutputStream(savePath);
			IOUtils.copy(fileInputStream, fos);
		} catch (IllegalStateException e) {
			log.error("上传文件出错了", e);
		} catch (IOException e) {
			log.error("上传文件出错", e);
		}
	}
}
