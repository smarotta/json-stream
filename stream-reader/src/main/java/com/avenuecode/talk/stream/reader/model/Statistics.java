package com.avenuecode.talk.stream.reader.model;

public class Statistics {

	private long captureStartTimestamp;
	private long captureEndTimestamp;
	private long bytesRead;
	
	public long getCaptureStartTimestamp() {
		return captureStartTimestamp;
	}

	public void setCaptureStartTimestamp(long captureStartTimestamp) {
		this.captureStartTimestamp = captureStartTimestamp;
	}

	public long getCaptureEndTimestamp() {
		return captureEndTimestamp;
	}

	public void setCaptureEndTimestamp(long captureEndTimestamp) {
		this.captureEndTimestamp = captureEndTimestamp;
	}

	public long getBytesRead() {
		return bytesRead;
	}
	
	public void setBytesRead(long bytesRead) {
		this.bytesRead = bytesRead;
	}
	
	public double getMegaBytesPerSecond() {
		if (bytesRead > 0l) {
			return (bytesRead / (double)(captureEndTimestamp - captureStartTimestamp)) * 0.001d;
		} else {
			return 0.0d;
		}
	}
}
