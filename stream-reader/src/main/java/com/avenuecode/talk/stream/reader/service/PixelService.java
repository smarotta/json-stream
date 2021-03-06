package com.avenuecode.talk.stream.reader.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.avenuecode.talk.stream.reader.model.Pixel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PixelService {

	private static final String PIXEL_SERVICE = "http://localhost:8080/"; 
	
	public List<Pixel> getPixels(int offset, int count) throws IOException {
		
		//URL url = new URL(PIXEL_SERVICE + "/pixel/ba0abd8d-0b60-4150-bca8-36e75dc7b8a0/offset/" + offset + "/count/" + count);
		URL url = new URL(PIXEL_SERVICE + "/pixel/b1274d1d-2143-4324-aa72-587f23b19104/offset/" + offset + "/count/" + count);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		InputStream in = connection.getInputStream();
		ObjectMapper om = new ObjectMapper();
		return om.readValue(in, new TypeReference<List<Pixel>>() {});
	}
	
	public PixelServiceIterator getPixels() throws IOException {
		//URL url = new URL(PIXEL_SERVICE + "/pixel/ba0abd8d-0b60-4150-bca8-36e75dc7b8a0");
		URL url = new URL(PIXEL_SERVICE + "/pixel/b1274d1d-2143-4324-aa72-587f23b19104");
		//URL url = new URL(PIXEL_SERVICE + "/pixel/ocean.jpg/file");
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
