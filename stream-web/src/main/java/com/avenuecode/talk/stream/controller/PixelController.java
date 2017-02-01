package com.avenuecode.talk.stream.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avenuecode.talk.stream.model.Pixel;
import com.avenuecode.talk.stream.service.ImageService;
import com.avenuecode.talk.stream.service.MultipartJsonService;

@RequestMapping("/pixel")
@RestController
public class PixelController {
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private MultipartJsonService multipartWriterService;
	
	@RequestMapping(value="multipart", produces="text/json")
	public void getPixels(HttpServletResponse response) throws IOException {
		Iterator<Pixel> iterator = imageService.getPixel("waterfall.jpg");
		
		String boundary = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
		response.setHeader("Content-Type", "multipart/mixed; boundary=" + boundary + "");
		
		multipartWriterService.<Pixel>streamJsonArray(response.getOutputStream(), iterator, boundary);
	}

}
