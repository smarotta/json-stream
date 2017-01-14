package com.avenuecode.talk.stream.reader.service;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.stream.EntityState;
import org.apache.james.mime4j.stream.MimeTokenStream;

import com.avenuecode.talk.stream.reader.controller.model.Pixel;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PixelService {

	private static final String PIXEL_SERVICE = "http://localhost:8080/"; 
	
	private void readddd(MimeTokenStream stream) {
		Reader reader;
		System.out.println("=======================");
		try {
			reader = stream.getReader();
			int r = 0;
			while(r > -1) {
				r = reader.read();
				System.out.print((char)r);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("=======================");
	}
	
	private List<Pixel> read(MimeTokenStream stream) {
		List<Pixel> pixels = new ArrayList<Pixel>();
		JsonFactory jsonFactory = new JsonFactory();
		JsonParser jp = null;
		try {
			jp = jsonFactory.createParser(stream.getReader());
			jp.setCodec(new ObjectMapper());
			while(jp.currentToken() != JsonToken.NOT_AVAILABLE) {
				while(jp.nextToken() != JsonToken.START_OBJECT);
				if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
					Pixel pixel = jp.readValueAs(Pixel.class);
					pixels.add(pixel);
				}
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				jp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return pixels;
	}
	
	public Iterator<Pixel> getPixels() {
		String uri = PIXEL_SERVICE + "/pixel/multipart";
		
		HttpClient client = new DefaultHttpClient(); 
		
		HttpGet request = new HttpGet(uri);
		
		try {
			HttpResponse response = client.execute(request);
			
			try {
				MimeTokenStream stream = new MimeTokenStream();
				stream.parse(response.getEntity().getContent());
				
				for (EntityState state = stream.getState(); state != EntityState.T_END_OF_STREAM; state = stream.next()) {
					switch (state) {
					
						case T_FIELD:
							System.out.println(stream.getField().getName() + ":" + stream.getField().getBody());
							break;
					
						case T_BODY:
							readddd(stream);
							System.out.println(stream.getBodyDescriptor().getBoundary());
							break;
							
						default:
							System.out.println(state);
							break;
					}
				}
			} catch (MimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		
		} finally {
			
		}
		
		
		return null;
	}
	
	public static void main(String[] args) {
		PixelService ps = new PixelService();
		ps.getPixels();
	}
	
}
