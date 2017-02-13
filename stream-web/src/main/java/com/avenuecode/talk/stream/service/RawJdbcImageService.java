package com.avenuecode.talk.stream.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.avenuecode.talk.stream.model.Pixel;
import com.avenuecode.talk.stream.service.dao.ImageDAOJdbcTemplate;
import com.avenuecode.talk.stream.service.dao.ImageDAORawJdbc;

@Component
public class RawJdbcImageService {
		
	@Autowired
	private FileImageService fileImageService; 
	
	@Autowired
	private ImageDAORawJdbc imageDAO;
	
	public void readPixels(IteratorListener listener, String imageId) throws IOException {
		imageDAO.getPixels(listener, imageId);
	}
	
	public void readPixels(IteratorListener listener, String imageId, int offset, int count) {
		imageDAO.getPixels(listener, imageId, offset, count);
	}
}
