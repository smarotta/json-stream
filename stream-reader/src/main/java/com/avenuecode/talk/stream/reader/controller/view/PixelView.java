package com.avenuecode.talk.stream.reader.controller.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.avenuecode.talk.stream.reader.controller.Controller;
import com.avenuecode.talk.stream.reader.controller.model.Pixel;

public class PixelView extends Canvas implements View<Pixel, PixelViewListener>, ActionListener {

	private static final long serialVersionUID = 3977607004204039955L;
	
	private PixelViewListener listener;
	private BufferedImage backBuffer;
	//private BufferStrategy bufferStrategy; 
	
	public void setListener(PixelViewListener listener) {
		this.listener = listener;
	}
	
	public void initialize() {		
		// Create views swing UI components 
		JTextField searchTermTextField = new JTextField(26);
		JButton startButton = new JButton("Start");
		startButton.setActionCommand("start");
		startButton.addActionListener(this);

		// Set the view layout
		JPanel ctrlPane = new JPanel();
		ctrlPane.add(searchTermTextField);
		ctrlPane.add(startButton);

		JPanel panelView = new JPanel();
		panelView.setPreferredSize(new Dimension(800,600));
		panelView.setLayout(null);
		
		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,800,600);
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
		
		backBuffer = new BufferedImage(800, 600, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D backBufferGraphics = backBuffer.createGraphics();
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.fillRect(0, 0, 800, 600);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(backBuffer, 0, 0, null);
	}
	
	public void startFrame() {
		
	}
	
	public void update(Pixel pixel) {
		Graphics2D g = backBuffer.createGraphics();
		g.setColor(new Color(pixel.getARGB(), true));
		g.drawLine(pixel.getX(), pixel.getY(), pixel.getX() + 10, pixel.getY());
		repaint();
		/*
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	public void finishFrame() {
		
	}

	public void actionPerformed(ActionEvent e) {
		if ("start".equals(e.getActionCommand())) {
			new Thread(new Runnable(){
				public void run() {
					listener.onStartCapture();
				}
				
			}).start();
		}
	}
}
