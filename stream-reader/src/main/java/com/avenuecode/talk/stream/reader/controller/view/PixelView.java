package com.avenuecode.talk.stream.reader.controller.view;

import java.awt.Dimension;
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

public class PixelView implements View<Pixel, PixelViewListener> {

	private PixelViewListener listener;
	private Iterator<Pixel> model;
	
	public void setListener(PixelViewListener listener) {
		this.listener = listener;
	}
	
	public void initialize() {		
		// Create views swing UI components 
		JTextField searchTermTextField = new JTextField(26);
		JButton filterButton = new JButton("Filter");
		JTable table = new JTable();

		// Set the view layout
		JPanel ctrlPane = new JPanel();
		ctrlPane.add(searchTermTextField);
		ctrlPane.add(filterButton);

		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new Dimension(700, 182));
		tableScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Market Movers",
				TitledBorder.CENTER, TitledBorder.TOP));

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ctrlPane, tableScrollPane);
		splitPane.setDividerLocation(35);
		splitPane.setEnabled(false);

		// Display it all in a scrolling window and make the window appear
		JFrame frame = new JFrame("Swing MVC Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(splitPane);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void startFrame() {
		
	}
	
	public void update(Pixel pixel) {
		
	}
	
	public void finishFrame() {
		
	}
}
