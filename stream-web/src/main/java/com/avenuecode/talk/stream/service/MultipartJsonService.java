package com.avenuecode.talk.stream.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class MultipartJsonService {
	
	private static final int MULTIPART_SIZE = 2;
	
	private void flushJsonGenerator(JsonGenerator jsonGenerator, OutputStream outputStream, ByteArrayOutputStream bout) throws IOException {
		jsonGenerator.flush();
		bout.writeTo(outputStream);
		bout.reset();
		outputStream.flush();
	}
	
	public <T> void streamJsonArray(OutputStream outputStream, Iterator<T> iterator, String boundary) throws IOException {
		byte [] boundaryStart = ("--" + boundary + "\r\n\r\n").getBytes();
		byte [] boundaryLimitData = ("\r\n\r\n--" + boundary + "\r\n\r\n").getBytes();
		byte [] boundaryEnd = ("\r\n--" + boundary + "--").getBytes();

		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		JsonFactory jsonFactory = new JsonFactory();
		ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		JsonGenerator jsonGenerator = jsonFactory.createGenerator(bout);
		jsonGenerator.setCodec(objectMapper);
		
		
		for (long x=0l; iterator.hasNext(); x++) {
			if (x == 0l) {
				outputStream.write(boundaryStart);
				jsonGenerator.writeStartArray();
			} else if (x % MULTIPART_SIZE == 0) {
				jsonGenerator.writeEndArray();
				flushJsonGenerator(jsonGenerator, outputStream, bout);
				outputStream.write(boundaryLimitData);
				jsonGenerator.writeStartArray();
			}
			
			jsonGenerator.writeObject(iterator.next());
			
			flushJsonGenerator(jsonGenerator, outputStream, bout);

			/*
			if (x > 10) {
				break;
			}
			*/
		}
		jsonGenerator.writeEndArray();
		flushJsonGenerator(jsonGenerator, outputStream, bout);
		outputStream.write(boundaryEnd);
		outputStream.flush();
	}



	
	
}
