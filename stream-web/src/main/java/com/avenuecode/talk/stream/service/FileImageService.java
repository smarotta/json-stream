package com.avenuecode.talk.stream.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.avenuecode.talk.stream.model.Pixel;
import com.avenuecode.talk.stream.service.dao.ImageDAOJdbcTemplate;

@Component
public class FileImageService {
	
	public Iterator<Pixel> getPixelIterator(String file) throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
		return new FileImageServiceIterator(in);
	}
	
	public List<Pixel> getPixelCollection(String file) throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
		try {
			BufferedImage image = ImageIO.read(in);
			
			int width = image.getWidth();
			int height = image.getHeight();
			
			List<Pixel> pixels = new ArrayList<Pixel>(width*height);
	
			for (int x = 0; x < height; x++) {
				for (int y = 0; y < width; y++) {
					int argb = image.getRGB(y, x);
					pixels.add(new Pixel(x, y, argb));
				}
			}
			return pixels;
		} finally {
			in.close();
		}
	}	
}
