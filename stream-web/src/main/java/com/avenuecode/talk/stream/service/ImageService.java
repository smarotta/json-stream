package com.avenuecode.talk.stream.service;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.avenuecode.talk.stream.model.Pixel;

@Component
public class ImageService {
	
	public Iterator<Pixel> getPixel(String file) throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
		return new ImageServiceFileIterator(in);
		/*
		try {
			BufferedImage image = ImageIO.read(in);
			
			int width = image.getWidth();
			int height = image.getHeight();
			
			List<Pixel> pixels = new ArrayList<Pixel>(width*height);
	
			for (int x = 0; x < height; x++) {
				for (int y = 0; y < width; y++) {
					int argb = image.getRGB(y, x);
					Pixel pixel = new Pixel(x, y, argb);
					System.out.println("SERVER SIDE: " + Integer.toHexString(argb) + " " + x + ":" + y);
					pixels.add(pixel);
				}
			}
			
			return pixels;
		} finally {
			in.close();
		}
		*/
	}	
}
