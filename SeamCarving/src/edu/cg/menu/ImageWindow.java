package edu.cg.menu;

/*
 * This class displays an image in a new window and allows to save it as a PNG file.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ImageWindow extends JFrame {
	private MenuWindow menuWindow;
	private BufferedImage img;

	/**
	 * Create the window.
	 */
	public ImageWindow(BufferedImage img, String title, MenuWindow menuWindow) {
		super();
		
		this.img = img;
		this.menuWindow = menuWindow;
		
		setTitle(title);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JButton btnSaveAs = new JButton("Save as...");
		btnSaveAs.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			int ret = fileChooser.showSaveDialog(ImageWindow.this);
			if (ret == JFileChooser.APPROVE_OPTION)
				save(fileChooser.getSelectedFile());
		});
		contentPane.add(btnSaveAs, BorderLayout.NORTH);
		
		JPanel panelImage = new ImagePanel();
		contentPane.add(panelImage, BorderLayout.CENTER);
		
		JButton btnSetWorkingImg = new JButton("Set as working image");
		btnSetWorkingImg.addActionListener(e -> menuWindow.setWorkingImage(img, title));
		contentPane.add(btnSetWorkingImg, BorderLayout.SOUTH);
		
		pack();
	}
	
	private class ImagePanel extends JPanel {
		public ImagePanel() {
			setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			g.drawImage(img, 0, 0, null);
		}
	}
	
	private void save(File file) {
		try {
			ImageIO.write(img, "png", file);
			menuWindow.log("File: " + file.getName() + ".png has been saved.");
		} catch (IOException e) {
			menuWindow.log("Failed to save image: " + getTitle()); 
			JOptionPane.showMessageDialog(this, "Can't save file!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		menuWindow.log("Image: " + getTitle() + " has been " + (b ? "presented." : "vanished."));
	}

}
