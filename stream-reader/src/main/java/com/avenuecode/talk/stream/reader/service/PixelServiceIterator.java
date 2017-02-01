package com.avenuecode.talk.stream.reader.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.MultipartStream;

import com.avenuecode.talk.stream.reader.controller.model.Pixel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PixelServiceIterator implements Iterator<Pixel> {

	private String boundary;
	private ByteArrayOutputStream bout;
	private MultipartStream multipartStream;
	private InputStream in;
	private Boolean nextPart;
	
	private Iterator<Pixel> currentPixelBucket;
	
	public PixelServiceIterator(InputStream in, String boundary) {
		this.in = in;
		this.boundary = boundary;
		this.nextPart = null;
		this.currentPixelBucket = new ArrayList<Pixel>().iterator();
	}
	
	private void initializeMultipartStream() throws IOException {
		bout = new ByteArrayOutputStream();
		multipartStream = new MultipartStream(in, boundary.getBytes(), 1024, null);
		nextPart = multipartStream.skipPreamble();
	}
	
	private void shutdownMultipartStreamSilently() {
		try {
			in.close();
		} catch (IOException e) { }
	}
	
	private List<Pixel> parseBodyPart(ByteArrayOutputStream bout) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.readValue(bout.toByteArray(), new TypeReference<List<Pixel>>() {});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return new ArrayList<Pixel>();
	}
	
	private void readNextPixelBucket() {
		try {
			if (multipartStream == null) {
				initializeMultipartStream();
			}
			if(nextPart) {
				multipartStream.readBodyData(bout);
				Iterable<Pixel> iterable = parseBodyPart(bout);
				bout.reset();
				currentPixelBucket = iterable.iterator();
				nextPart = multipartStream.readBoundary();
			}
		} catch (IOException e) {
			throw new PixelServiceRuntimeException(e);
		} finally {
			if (!nextPart) {
				shutdownMultipartStreamSilently();
			}
		}
	}
	
	public boolean hasNext() {
		if (!currentPixelBucket.hasNext()) {
			readNextPixelBucket();
		}
		return currentPixelBucket.hasNext();
	}
	
	public Pixel next() {
		if (!currentPixelBucket.hasNext()) {
			readNextPixelBucket();
		}
		
		if (currentPixelBucket.hasNext()) {
			return currentPixelBucket.next();
		} else {
			return null;
		}
	}

}
