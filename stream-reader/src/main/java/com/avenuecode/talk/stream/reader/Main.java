package com.avenuecode.talk.stream.reader;

import javax.swing.SwingUtilities;

import com.avenuecode.talk.stream.reader.controller.PixelController;

public class Main {

	public static void main(String [] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PixelController controller = new PixelController();
				controller.initialize();
			}
		});
	}
	
}
