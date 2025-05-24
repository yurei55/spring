package com.springmvc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/bookimage")
public class ImageDownloadController {
	@Value("${uploadPath}")
	private String uploadPath;

	public ImageDownloadController() {
	}

	@GetMapping("/{fileName}")
	@ResponseBody
	public void download(@PathVariable("fileName") String filename, HttpServletResponse response) throws IOException {
		File downloadFile = new File(this.uploadPath, fileName);
		if (downloadFile.exists() == false) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		String mimeType = Files.probeContentType(downloadFile.toPath());
		response.setContentType(mimeType);
		byte[] buffer = new byte[1024];
		ServletOutputStream out = response.getOutputStream();
		try (InputStream in = new FileInputStream(downloadFile)) {
			for (int size; (size = in.read(buffer)) != -1;) {
				out.write(buffer, 0, size);
			}
		}
		response.flushBuffer();
	}

}
