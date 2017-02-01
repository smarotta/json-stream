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

public class ImageServiceFileIterator implements Iterator<Pixel>{

	private BufferedImage image;
	private InputStream in;
	private int currentPixel;
	private int currentX;
	private int currentY;
	private int width;
	private int height;
	private int totalPixels;
	
	public ImageServiceFileIterator(InputStream in) {
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
		
		if (currentPixel % 1000 == 0) {
			Runtime runtime = Runtime.getRuntime();
			NumberFormat format = NumberFormat.getInstance();
			StringBuilder sb = new StringBuilder();
			long maxMemory = runtime.maxMemory();
			long allocatedMemory = runtime.totalMemory();
			long freeMemory = runtime.freeMemory();
			sb.append("Current progress ").append(currentPixel).append("/").append(totalPixels).append("\r\n");
			sb.append("free memory: ").append(format.format(freeMemory / 1024)).append("\r\n");
			sb.append("allocated memory: ").append(format.format(allocatedMemory / 1024)).append("\r\n");
			sb.append("max memory: ").append(format.format(maxMemory / 1024)).append("\r\n");
			sb.append("total free memory: ").append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024)).append("\r\n");
			sb.append("======================");
			
			System.out.println(sb);
		}
		//System.out.println("SERVER SIDE: " + Integer.toHexString(pixel.getARGB()) + " " + pixel.getX() + ":" + pixel.getY());
		return pixel;
	}
}
