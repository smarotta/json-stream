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
	
	public <T> void streamJsonArray(OutputStream outputStream, Iterator<T> iterator, String boundary) throws IOException {
		byte [] boundaryStart = ("--" + boundary + "\r\nContent-type: text/json; charset=us-ascii \r\n\r\n").getBytes();
		byte [] boundaryLimitData = ("\r\n\r\n--" + boundary + "\r\nContent-type: text/json; charset=us-ascii \r\n\r\n").getBytes();
		byte [] boundaryEnd = ("\r\n--" + boundary + "--").getBytes();

		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		JsonFactory jsonFactory = new JsonFactory();
		ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		JsonGenerator jsonGenerator = jsonFactory.createGenerator(bout);
		jsonGenerator.setCodec(objectMapper);
		
		jsonGenerator.writeStartArray();
		for (long x=0l; iterator.hasNext(); x++) {
			if (x == 0l) {
				outputStream.write(boundaryStart);
			} else {
				outputStream.write(boundaryLimitData);
			}
			
			jsonGenerator.writeObject(iterator.next());
			bout.writeTo(outputStream);
			bout.reset();
			
			outputStream.flush();
			
			if (x > 10) {
				break;
			}
		}
		jsonGenerator.writeEndArray();
		bout.writeTo(outputStream);
		bout.reset();
		outputStream.write(boundaryEnd);
		outputStream.flush();
	}
	
}
