package edu.cg.menu;

/*
 * This class displays an image in a new window and allows to draw a mask based on the image.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class MaskPainterWindow extends JFrame {
	private MenuWindow menuWindow;
	private BufferedImage img;
	private int imgHeight;
	private int imgWidth;
	private boolean[][] imageMask;
	private int brushSize = MEDIUM_BRUSH_SIZE;
	private static final int SMALL_BRUSH_SIZE = 4;
	private static final int MEDIUM_BRUSH_SIZE = 8;
	private static final int LARGE_BRUSH_SIZE = 16;
	private PainterPanel maskPainter = null;

	/**
	 * Create the window.
	 */
	public MaskPainterWindow(BufferedImage img, String title, MenuWindow menuWindow) {
		super();

		this.img = img;
		imgHeight = img.getHeight();
		imgWidth = img.getWidth();
		this.menuWindow = menuWindow;

		setTitle(title);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.white);
		JLabel brushLabel = new JLabel("Brush Size :  ");
		northPanel.add(brushLabel);

		JButton btnSmallBrush = new JButton("Small");
		btnSmallBrush.addActionListener(e -> {
			brushSize = SMALL_BRUSH_SIZE;
		});
		northPanel.add(btnSmallBrush);
		JButton btnMediumBrush = new JButton("Medium");
		btnMediumBrush.addActionListener(e -> {
			brushSize = MEDIUM_BRUSH_SIZE;
		});
		northPanel.add(btnMediumBrush);
		JButton btnLargeBrush = new JButton("Large");
		btnLargeBrush.addActionListener(e -> {
			brushSize = LARGE_BRUSH_SIZE;
		});
		northPanel.add(btnLargeBrush);

		JButton btnClear = new JButton("Clear Mask");
		btnClear.addActionListener(e -> {
			if (maskPainter != null) {
				maskPainter.clearMask();
			}
		});
		northPanel.add(btnClear);
		contentPane.add(northPanel, BorderLayout.NORTH);

		maskPainter = new PainterPanel();
		contentPane.add(maskPainter, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel();
		JButton btnRemoveObject = new JButton("Remove Object");
		btnRemoveObject.addActionListener(e -> {
			try {
			menuWindow.removeObjectFromImage(imageMask);
			setVisible(false);
			dispose();
			}
			catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}});
		southPanel.add(btnRemoveObject);
		
		JButton btnSetMask = new JButton("Set Mask");
		btnSetMask.addActionListener(e -> menuWindow.setImageMask(imageMask));
		southPanel.add(btnSetMask);
		
		contentPane.add(southPanel, BorderLayout.SOUTH);

		pack();
		setResizable(false);
		maskPainter.clearMask();
	}

	private class PainterPanel extends JPanel implements MouseListener, MouseMotionListener {
		private int prevX;
		private int prevY;
		private boolean dragging;

		public PainterPanel() {
			setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
			dragging = false;
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, null);
		}

		private int clipHighHeight(int x) {
			return Math.min(x, imgHeight - 1);
		}

		private int clipHighWidth(int x) {
			return Math.min(x, imgWidth - 1);
		}

		private int clipLow(int x) {
			return Math.max(x, 0);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int currentX = e.getX();
			int currentY = e.getY();
			int a_x = clipHighWidth(prevX + brushSize);
			int b_x = clipHighWidth(currentX + brushSize);
			int c_x = clipLow(currentX - brushSize);
			int d_x = clipLow(prevX - brushSize);
			int[] pX = { a_x, b_x, c_x, d_x };
			int a_y = clipHighHeight(prevY + brushSize);
			int b_y = clipHighHeight(currentY + brushSize);
			int c_y = clipLow(currentY - brushSize);
			int d_y = clipLow(prevY - brushSize);
			int[] pY = { a_y, b_y, c_y, d_y };
			Polygon poly = new Polygon(pX, pY, 4);
			Graphics g = getGraphics();
			g.setColor(new Color(255, 0, 0, 50));
			g.fillPolygon(poly);
			for (int x = Math.min(d_x, c_x); x <= Math.max(b_x, a_x); x++) {
				for (int y = Math.min(d_y, c_y); y <= Math.max(b_y, a_y); y++) {
					if (poly.contains(x, y)) {
						imageMask[y][x] = true;
					}
				}
			}
			prevX = currentX;
			prevY = currentY;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (dragging) {
				return;
			}
			dragging = true;
			prevX = e.getX();
			prevY = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			dragging = false;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		// Clears the current mask and repaints the input image.
		public void clearMask() {
			imageMask = new boolean[img.getHeight()][img.getWidth()];
			Graphics g = getGraphics();
			if (g != null) {
				paintComponent(g);
			}
		}
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (menuWindow != null) {
			menuWindow.log("Image: " + getTitle() + " has been " + (b ? "presented." : "vanished."));
		}
	}

}
