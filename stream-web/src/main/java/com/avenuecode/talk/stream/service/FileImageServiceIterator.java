package com.avenuecode.talk.stream.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.avenuecode.talk.stream.model.Pixel;

public class FileImageServiceIterator implements Iterator<Pixel>{

	private BufferedImage image;
	private InputStream in;
	private int currentPixel;
	private int currentX;
	private int currentY;
	private int width;
	private int height;
	private int totalPixels;
	
	public FileImageServiceIterator(InputStream in) {
		this.in = in;
	}
	
	@Override
	public boolean hasNext() {
		try {
			if (image == null) {
				image = ImageIO.read(in);
				width = image.getWidth();
				height = image.getHeight();
				totalPixels = width * height;
			}
		} catch (IOException e) {
			
		}
		
		boolean hasNext = currentX < width && currentY < height;
		if (!hasNext) {
			try {
				in.close();
			} catch (IOException e) { }
		}
		return hasNext;
	}

	@Override
	public Pixel next() {
		try {
			if (image == null) {
				image = ImageIO.read(in);
				width = image.getWidth();
				height = image.getHeight();
				totalPixels = width * height;
			}
		} catch (IOException e) { }
		
		int argb = image.getRGB(currentX, currentY);
		Pixel pixel = new Pixel(currentX, currentY, argb);
		if (currentX == width - 1) {
			currentX = 0;
			currentY++;
		}
		
		currentX++;
		currentPixel++;
		return pixel;
	}
}
