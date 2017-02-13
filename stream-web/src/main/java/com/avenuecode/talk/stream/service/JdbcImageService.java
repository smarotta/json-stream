package com.avenuecode.talk.stream.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.avenuecode.talk.stream.model.Pixel;
import com.avenuecode.talk.stream.service.dao.ImageDAOJdbcTemplate;

@Component
@Transactional
public class JdbcImageService {
	
	@Autowired
	private FileImageService fileImageService; 
	
	@Autowired
	private ImageDAOJdbcTemplate imageDAO;
	
	public Iterator<Pixel> getPixelIterator(String imageId) throws IOException {
		return imageDAO.getPixels(imageId);
	}
	
	public Iterator<Pixel> getPixels(String imageId, int offset, int count) {
		return imageDAO.getPixels(imageId, offset, count);
	}
	
	public void saveImage(String path, String name) throws IOException {
		List<Pixel> pixels = fileImageService.getPixelCollection(path);
		imageDAO.saveImage(pixels, path, name);
	}
}
