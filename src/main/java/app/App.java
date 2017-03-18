package app;

import app.gui.AppFrame;

import javax.swing.JFrame;

public class App
{
	public static void main(String[] args)
	{
		AppFrame appFrame = new AppFrame();
		appFrame.setTitle("Huffman Compression and Decompression");
		appFrame.setSize(800, 190);
		appFrame.setResizable(true);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		appFrame.setVisible(true);
	}
}