package com.avenuecode.talk.stream.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import com.avenuecode.talk.stream.model.Pixel;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MultipartJsonServiceWriter implements IteratorListener {

	private static final int MULTIPART_SIZE = 2;
	
	private int currentPixelCount;
	
	private OutputStream outputStream;
	private String boundary;
	private byte [] boundaryStart;
	private byte [] boundaryLimitData;
	private byte [] boundaryEnd;
	private ByteArrayOutputStream bout;
	private JsonGenerator jsonGenerator;
	
	public MultipartJsonServiceWriter(OutputStream outputStream, String boundary) {
		this.outputStream = outputStream;
		this.boundary = boundary;
	}
	
	private void flushJsonGenerator() throws IOException {
		jsonGenerator.flush();
		bout.writeTo(outputStream);
		bout.reset();
		outputStream.flush();
	}	
	
	@Override
	public void onStart() throws Exception {
		boundaryStart = ("--" + boundary + "\r\n\r\n").getBytes();
		boundaryLimitData = ("\r\n\r\n--" + boundary + "\r\n\r\n").getBytes();
		boundaryEnd = ("\r\n--" + boundary + "--").getBytes();

		bout = new ByteArrayOutputStream();

		JsonFactory jsonFactory = new JsonFactory();
		ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		jsonGenerator = jsonFactory.createGenerator(bout);
		jsonGenerator.setCodec(objectMapper);
	}

	@Override
	public void onPixelRead(Pixel pixel) throws Exception {
		if (currentPixelCount == 0l) {
			outputStream.write(boundaryStart);
			jsonGenerator.writeStartArray();
		} else if (currentPixelCount % MULTIPART_SIZE == 0) {
			jsonGenerator.writeEndArray();
			flushJsonGenerator();
			outputStream.write(boundaryLimitData);
			jsonGenerator.writeStartArray();
		}
		
		jsonGenerator.writeObject(pixel);
		flushJsonGenerator();
		currentPixelCount++;
	}

	@Override
	public void onFinish(Exception error) {
		try {
			jsonGenerator.writeEndArray();
			flushJsonGenerator();
			outputStream.write(boundaryEnd);
			outputStream.flush();
		} catch (IOException e) {
			//we could do something here.. well, we COULD.
		}
	}

}
