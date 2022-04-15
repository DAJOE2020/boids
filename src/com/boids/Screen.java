package com.boids;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;

public class Screen extends JPanel {

	private BufferedImage next_frame;  //image containing next frame
	private Graphics2D frame_write;	   //graphics class for writing to the next frame
	private JFrame frame;              //frame containing this component
	public int w, h;                   //size of screen

	//  constructor without offset
	public Screen(int w_in, int h_in, String title) {

		//  set values
		w = w_in;
		h = h_in;
		next_frame = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		frame_write = next_frame.createGraphics();
		frame = new JFrame(title);

		//  offsets to middle of screen
		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		int ox = (screen_size.width-w)/2;
		int oy = (screen_size.height-h)/2;

		//  generate new frame with arguments
		frame.setLocation(ox, oy);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
	}

	//  constructor with offset
	public Screen(int w_in, int h_in, int ox, int oy, String title) {
		w = w_in;
		h = h_in;
		next_frame = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		frame_write = next_frame.createGraphics();

		//  generate new frame with arguments
		frame = new JFrame(title);
		frame.setLocation(ox, oy);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(w,h);
	}
	
	public void fillRect(int x, int y, int rw, int rh, Color c) {
		frame_write.setColor(c);
		frame_write.fillRect(x,y,rw,rh);
	}

	public void clearToColor(Color c) {
		frame_write.setColor(c);
		frame_write.fillRect(0,0,w,h);
	}

	public Boolean setIcon(String dir) {
		//  tries to grab image from
		//  file and set to icon
		try {
			Image icon = ImageIO.read(new File(dir));
			frame.setIconImage(icon);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void flip() {repaint();}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D)g).drawImage(next_frame,null,0,0);
	}

	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints, Color c) {
		frame_write.setColor(c);
		frame_write.fillPolygon(xPoints,yPoints,nPoints);
	}

	public void drawArc(int x, int y, int cw, int ch, int sa, int aa, Color c) {
		frame_write.setColor(c);
		frame_write.drawArc(x,y,cw,ch,sa,aa);
	}

	public void fillArc(int x, int y, int cw, int ch, int sa, int aa, Color c) {
		frame_write.setColor(c);
		frame_write.fillArc(x,y,cw,ch,sa,aa);
	}

	public void drawLine(int x1, int y1, int x2, int y2, Color c) {
		frame_write.setColor(c);
		frame_write.drawLine(x1,y1,x2,y2);
	}
}
