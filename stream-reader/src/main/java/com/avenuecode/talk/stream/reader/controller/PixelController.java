package com.avenuecode.talk.stream.reader.controller;

import java.util.Iterator;

import com.avenuecode.talk.stream.reader.controller.model.Pixel;
import com.avenuecode.talk.stream.reader.controller.view.PixelView;
import com.avenuecode.talk.stream.reader.controller.view.PixelViewListener;
import com.avenuecode.talk.stream.reader.service.PixelService;

public class PixelController implements Controller, PixelViewListener {

	private PixelService pixelService;
	private PixelView pixelView;
	
	public void initialize() {
		pixelService = new PixelService();
		pixelView = new PixelView();
		pixelView.setListener(this);
		pixelView.initialize();
	}

	public void onStartCapture() {
		Iterator<Pixel> pixelIterator = pixelService.getPixels();
		pixelView.startFrame();
		while(pixelIterator.hasNext()) {
			Pixel pixel = pixelIterator.next();
			pixelView.update(pixel);
		}
		pixelView.finishFrame();
	}

	public void onStopCapture() {
		
	}

}
