package com.avenuecode.talk.stream.reader.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.avenuecode.talk.stream.reader.controller.view.PixelView;
import com.avenuecode.talk.stream.reader.controller.view.PixelViewListener;
import com.avenuecode.talk.stream.reader.model.Pixel;
import com.avenuecode.talk.stream.reader.model.Statistics;
import com.avenuecode.talk.stream.reader.service.PixelService;
import com.avenuecode.talk.stream.reader.service.PixelServiceIterator;

public class PixelController implements Controller, PixelViewListener {

	private PixelService pixelService;
	private PixelView pixelView;
	
	public void initialize() {
		pixelService = new PixelService();
		pixelView = new PixelView();
		pixelView.setListener(this);
		pixelView.initialize();
	}

	private void startMultipartCapture() {
		try {
			Statistics statistics = new Statistics();
			statistics.setCaptureStartTimestamp(System.currentTimeMillis());
			
			PixelServiceIterator pixelIterator = pixelService.getPixels();
			pixelView.startFrame();
			while(pixelIterator.hasNext()) {
				Pixel pixel = pixelIterator.next();
				pixelView.update(pixel);
			}
			statistics.setCaptureEndTimestamp(System.currentTimeMillis());
			statistics.setBytesRead(pixelIterator.getTotalBytesRead());
			
			pixelView.finishFrame(statistics);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void startPaginatedCapture() {
		try {
			Statistics statistics = new Statistics();
			statistics.setCaptureStartTimestamp(System.currentTimeMillis());
			
			pixelView.startFrame();
			List<Pixel> pixels = new ArrayList<Pixel>();
			int pageSize = 500;
			int offset = 0;
			do {
				pixels = pixelService.getPixels(offset, pageSize);
				offset += pageSize;
				for(Pixel pixel:pixels) {
					pixelView.update(pixel);
				}
			} while(!pixels.isEmpty());
			
			statistics.setCaptureEndTimestamp(System.currentTimeMillis());
			pixelView.finishFrame(statistics);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onStartCapture(boolean useMultipart) {
		if (useMultipart) {
			startMultipartCapture();
		} else {
			startPaginatedCapture();
		}
	}

	public void onStopCapture() {
		
	}

}
