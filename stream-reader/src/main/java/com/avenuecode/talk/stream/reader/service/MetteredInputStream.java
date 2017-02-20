package com.avenuecode.talk.stream.reader.service;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MetteredInputStream extends FilterInputStream {

	private long totalBytesRead;
	
	protected MetteredInputStream(InputStream in) {
		super(in);
	}
	
	@Override
	public int read() throws IOException {
		totalBytesRead++;
		return super.read();
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int bytesRead = super.read(b, off, len);
		totalBytesRead += bytesRead;
		return bytesRead;
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		int bytesRead = super.read(b);
		totalBytesRead += bytesRead;
		return bytesRead;
	}

	public long getTotalBytesRead() {
		return totalBytesRead;
	}
}
