package com.avenuecode.talk.stream.service;

import com.avenuecode.talk.stream.model.Pixel;

public interface IteratorListener {
	void onStart() throws Exception;
	void onPixelRead(Pixel pixel) throws Exception;
	void onFinish(Exception error);
}
