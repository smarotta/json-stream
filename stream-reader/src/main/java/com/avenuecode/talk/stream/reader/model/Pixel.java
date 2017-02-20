package com.avenuecode.talk.stream.reader.model;

public class Pixel {

	private int x;
	private int y;
	private int a;
	private int r;
	private int g;
	private int b;
	
	public Pixel() {
		
	}
	
	public Pixel(int x, int y, int argb) {
		this.x = x;
		this.y = y;
		setARGB(argb);
	}
		
	public int getARGB() {
		int argb = 0;
		argb += a << 24;
		argb += r << 16;
		argb += g << 8;
		argb += b;
		return argb;
	}
	
	public void setARGB(int argb) {
		a = (argb & 0xFF000000) >> 24;
		r = (argb & 0x00FF0000) >> 16;
		g = (argb & 0x0000FF00) >> 8;
		b = (argb & 0x000000FF) >> 0;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getA() {
		return a;
	}
	
	public void setA(int a) {
		this.a = a;
	}
	
	public int getR() {
		return r;
	}
	
	public void setR(int r) {
		this.r = r;
	}
	
	public int getG() {
		return g;
	}
	
	public void setG(int g) {
		this.g = g;
	}
	
	public int getB() {
		return b;
	}
	
	public void setB(int b) {
		this.b = b;
	}
	
}
