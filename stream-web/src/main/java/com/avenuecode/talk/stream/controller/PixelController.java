package com.avenuecode.talk.stream.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.avenuecode.talk.stream.model.Pixel;
import com.avenuecode.talk.stream.service.FileImageService;
import com.avenuecode.talk.stream.service.IteratorListener;
import com.avenuecode.talk.stream.service.JdbcImageService;
import com.avenuecode.talk.stream.service.MultipartJsonService;
import com.avenuecode.talk.stream.service.MultipartJsonServiceWriter;
import com.avenuecode.talk.stream.service.RawJdbcImageService;
import com.avenuecode.talk.stream.service.dao.ImageDAORawJdbc;


@RequestMapping("/pixel")
@RestController
public class PixelController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PixelController.class);
	
	@Autowired
	private FileImageService fileImageService;
	
	@Autowired
	private RawJdbcImageService jdbcImageService;
	
	@Autowired
	private JdbcImageService jdbcTemplateImageService;
	
	@Autowired
	private MultipartJsonService multipartWriterService;
	
	@RequestMapping(value="/{imageId}/offset/{offset}/count/{count}", produces="application/json")
	public @ResponseBody List<Pixel> getPaginatedPixels(@PathVariable("imageId") String imageId, @PathVariable("offset") int offset, @PathVariable("count") int count) throws IOException {
		final List<Pixel> pixels = new ArrayList<Pixel>(count);
		jdbcImageService.readPixels(new IteratorListener() {
			@Override
			public void onStart() throws Exception {
			}
			
			@Override
			public void onPixelRead(Pixel pixel) throws Exception {
				pixels.add(pixel);
			}
			
			@Override
			public void onFinish(Exception error) {
			}
		}, imageId, offset, count);
		
		return pixels;
	}
	
	@RequestMapping(value="/{imageId}", produces="application/json")
	public void getMultipartPixels(HttpServletResponse response, @PathVariable("imageId") String imageId) throws IOException {
		String boundary = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
		response.setHeader("Content-Type", "multipart/mixed; boundary=" + boundary + "");
		
		MultipartJsonServiceWriter multipartJsonWriter = new MultipartJsonServiceWriter(response.getOutputStream(), boundary);
		jdbcImageService.readPixels(multipartJsonWriter, imageId);
	}
	
	@RequestMapping(value="/{imageId}/file", produces="application/json")
	public void getMultipartPixelsFromFile(HttpServletResponse response, @PathVariable("imageId") String imageId) throws Exception {
		String boundary = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
		response.setHeader("Content-Type", "multipart/mixed; boundary=" + boundary + "");
		
		MultipartJsonServiceWriter multipartJsonWriter = new MultipartJsonServiceWriter(response.getOutputStream(), boundary);
		Iterator<Pixel> it = fileImageService.getPixelIterator(imageId);
		try {
			multipartJsonWriter.onStart();
			while(it.hasNext()) {
				multipartJsonWriter.onPixelRead(it.next());
			}
		}finally {
			multipartJsonWriter.onFinish(null);
		}
	}

	@RequestMapping(value="/createTest", method=RequestMethod.GET, produces="application/json")
	public void a() throws IOException {
		jdbcTemplateImageService.saveImage("ocean.jpg", "Ocean");
	}
	
}
