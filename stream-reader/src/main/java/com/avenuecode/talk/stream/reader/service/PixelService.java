package com.avenuecode.talk.stream.reader.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.avenuecode.talk.stream.reader.controller.model.Pixel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PixelService {

	private static final String PIXEL_SERVICE = "http://localhost:8080/"; 
	
	public List<Pixel> getPixels(int offset, int count) throws IOException {
		
		URL url = new URL(PIXEL_SERVICE + "/pixel/offset/" + offset + "/count/" + count);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		InputStream in = connection.getInputStream();
		ObjectMapper om = new ObjectMapper();
		return om.readValue(in, new TypeReference<List<Pixel>>() {});
	}
	
	public PixelServiceIterator getPixels() throws IOException {
		URL url = new URL(PIXEL_SERVICE + "/pixel/multipart");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		String boundary = readContentTypeBoundary(connection);
		
		InputStream in = connection.getInputStream();
		
		return new PixelServiceIterator(in, boundary);
	}

	private String readContentTypeBoundary(HttpURLConnection connection) {
		String contentTypeHeader = connection.getHeaderField("Content-Type");
		String [] contentTypeHeaderParts = contentTypeHeader.split(";");
		String boundary = null;
		for(String headerPart:contentTypeHeaderParts) {
			if (headerPart.contains("boundary=")) {
				String [] keyValue = headerPart.split("=");
				if (keyValue.length == 2) {
					boundary = keyValue[1];
				}
			}
		}
		return boundary;
	}
}
