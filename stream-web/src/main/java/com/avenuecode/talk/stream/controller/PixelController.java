package com.avenuecode.talk.stream.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avenuecode.talk.stream.model.Pixel;
import com.avenuecode.talk.stream.service.ImageService;
import com.avenuecode.talk.stream.service.MultipartJsonService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RequestMapping("/pixel")
@RestController
public class PixelController {
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private MultipartJsonService multipartWriterService;
	
	@RequestMapping(produces="text/html")
	public String getPixelsUI() throws IOException {
		return "<a href=\"/pixel/all\">pixel.json</a><br>" +
				"<a href=\"/pixel/multipart\">pixel.json</a><br>";
	}

	@RequestMapping("all")
	public Collection<Pixel> getPixels() throws IOException {
		return imageService.getPixel("waterfall.jpg");
	}


	@RequestMapping(value="multipart", produces="text/json")
	public void getPixels(HttpServletResponse response) throws IOException {
		Iterator<Pixel> iterator = imageService.getPixel("waterfall.jpg").iterator();
		
		String boundary = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
		response.setHeader("Content-Type", "multipart/mixed; boundary=" + boundary + "");
		
		multipartWriterService.<Pixel>streamJsonArray(response.getOutputStream(), iterator, boundary);
	}

}
