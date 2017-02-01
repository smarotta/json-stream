package com.avenuecode.talk.stream.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	@RequestMapping(value="/offset/{offset}/count/{count}", produces="application/json")
	public @ResponseBody List<Pixel> getPaginatedPixels(@PathVariable("offset") int offset, @PathVariable("count") int count) throws IOException {
		List<Pixel> pixels = new ArrayList<Pixel>(count);
		Iterator<Pixel> iterator = imageService.getPixel("waterfall.jpg");
		for(int x=0; x < offset; x++){
			if (iterator.hasNext()) {
				iterator.next();
			}
		}
		
		for(int x=0; x < count; x++){
			if (iterator.hasNext()) {
				pixels.add(iterator.next());
			}
		}
		
		return pixels;
	}
	
	@RequestMapping(value="multipart", produces="application/json")
	public void getMultipartPixels(HttpServletResponse response) throws IOException {
		Iterator<Pixel> iterator = imageService.getPixel("waterfall.jpg");
		
		String boundary = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
		response.setHeader("Content-Type", "multipart/mixed; boundary=" + boundary + "");
		
		multipartWriterService.<Pixel>streamJsonArray(response.getOutputStream(), iterator, boundary);
	}

}
