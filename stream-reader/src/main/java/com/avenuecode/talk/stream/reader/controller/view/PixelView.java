package com.avenuecode.talk.stream.reader.controller.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.avenuecode.talk.stream.reader.controller.model.Pixel;

public class PixelView extends Canvas implements View<Pixel, PixelViewListener>, ActionListener {

	private static final long serialVersionUID = 3977607004204039955L;
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	
	private PixelViewListener listener;
	private BufferedImage backBuffer;
	
	public void setListener(PixelViewListener listener) {
		this.listener = listener;
	}
	
	public void initialize() {		
		// Create views swing UI components 
		JButton startMultipartButton = new JButton("Start Multipart");
		startMultipartButton.setActionCommand("start_multipart");
		startMultipartButton.addActionListener(this);

		JButton startPagingButton = new JButton("Start Paginating");
		startPagingButton.setActionCommand("start_paginating");
		startPagingButton.addActionListener(this);

		// Set the view layout
		JPanel ctrlPane = new JPanel();
		ctrlPane.add(startMultipartButton);
		ctrlPane.add(startPagingButton);

		JPanel panelView = new JPanel();
		panelView.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		panelView.setLayout(null);
		
		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,WIDTH,HEIGHT);
		panelView.add(this);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ctrlPane, panelView);
		splitPane.setDividerLocation(35);
		splitPane.setEnabled(false);

		JFrame container = new JFrame("Multipart Viewer");
		
		// Display it all in a scrolling window and make the window appear
		//JFrame frame = new JFrame("Swing MVC Demo");
		container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.add(splitPane);
		container.pack();
		container.setLocationRelativeTo(null);
		container.setResizable(false);
		container.setVisible(true);
		
		//createBufferStrategy(1);
		//bufferStrategy = getBufferStrategy();
		
		backBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D backBufferGraphics = backBuffer.createGraphics();
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.fillRect(0, 0, WIDTH, HEIGHT);
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		g.drawImage(backBuffer, 0, 0, null);
	}
	
	public void startFrame() {
		
	}

	public void update(Pixel pixel) {
		Graphics2D g = backBuffer.createGraphics();
		g.setColor(new Color(pixel.getARGB(), true));
		g.drawLine(pixel.getX(), pixel.getY(), pixel.getX(), pixel.getY());
		repaint();
	}
	
	public void finishFrame() {
		
	}

	public void actionPerformed(ActionEvent e) {
		if ("start_multipart".equals(e.getActionCommand())) {
			Graphics2D backBufferGraphics = backBuffer.createGraphics();
			backBufferGraphics.setColor(Color.GREEN);
			backBufferGraphics.fillRect(0, 0, WIDTH, HEIGHT);
			repaint();
			
			new Thread(new Runnable(){
				public void run() {
					listener.onStartCapture(true);
				}
				
			}).start();
		}
		
		if ("start_paginating".equals(e.getActionCommand())) {
			Graphics2D backBufferGraphics = backBuffer.createGraphics();
			backBufferGraphics.setColor(Color.MAGENTA);
			backBufferGraphics.fillRect(0, 0, WIDTH, HEIGHT);
			repaint();
			
			new Thread(new Runnable(){
				public void run() {
					listener.onStartCapture(false);
				}
				
			}).start();
		}
	}
}
