package edu.cg.menu.components;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import edu.cg.menu.MenuWindow;

@SuppressWarnings("serial")
public class ImagePicker extends JPanel {
	
	private MenuWindow menuWindow;
	
	public ImagePicker(MenuWindow menuWindow) {
		super();
		
		this.menuWindow = menuWindow;
		
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JTextField txtFilename = new JTextField();
		txtFilename.addActionListener(e -> open(txtFilename.getText()));
		
		add(txtFilename);
		txtFilename.setColumns(40);
		
		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			int ret = fileChooser.showOpenDialog(menuWindow);
			if (ret == JFileChooser.APPROVE_OPTION) {
				String filename = fileChooser.getSelectedFile().getPath();
				txtFilename.setText(filename);
				open(filename);
			}
		});
		add(btnBrowse);
		
		JButton btnReload = new JButton("Reset to original");
		btnReload.addActionListener(e -> open(txtFilename.getText()));
		
		add(btnReload);
	}

	private void open(String filename) {
		try {
			File imgFile = new File(filename);
			BufferedImage img = ImageIO.read(imgFile);
			if(img == null)
				throw new NullPointerException();
			menuWindow.setWorkingImage(img, imgFile.getName());
			menuWindow.present();
		} catch(Exception e) {
			String msg = "Can't open file!";
			menuWindow.log(msg);
			JOptionPane.showMessageDialog(menuWindow, msg, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
