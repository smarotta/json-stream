package com.avenuecode.talk.stream.service;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.avenuecode.talk.stream.model.Pixel;

@Component
public class ImageService {
	
	public Collection<Pixel> getPixel(String file) throws IOException {
		
		File imageFile = new File("C:\\Users\\Public\\Pictures\\Sample Pictures", file);
		
		BufferedImage image = ImageIO.read(imageFile);
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		List<Pixel> pixels = new ArrayList<Pixel>(width*height);

		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				int argb = image.getRGB(y, x);
				Pixel pixel = new Pixel(x, y, argb);
				pixels.add(pixel);
			}
		}
		
		return pixels;
	}

}
