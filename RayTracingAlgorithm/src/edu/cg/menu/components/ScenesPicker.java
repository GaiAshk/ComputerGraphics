package edu.cg.menu.components;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.google.gson.Gson;

import edu.cg.menu.GsonMaker;
import edu.cg.menu.MenuWindow;
import edu.cg.scene.Scene;

@SuppressWarnings("serial")
public class ScenesPicker extends JPanel {
	
	private MenuWindow menuWindow;
	private JTextField txtFilename;
	
	public ScenesPicker(MenuWindow menuWindow) {
		super();
		
		this.menuWindow = menuWindow;
		
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		txtFilename = new JTextField();
		txtFilename.addActionListener(e -> open(txtFilename.getText()));
		
		add(txtFilename);
		txtFilename.setColumns(40);
		
		JButton btnBrowse = new JButton("Browse scene...");
		btnBrowse.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser("scenes");
			int ret = fileChooser.showOpenDialog(menuWindow);
			if (ret == JFileChooser.APPROVE_OPTION)
				open(fileChooser.getSelectedFile().getPath());
		});
		
		add(btnBrowse);
	}
	
	public void open(String filename) {
		txtFilename.setText(filename);
		try {
			Gson gson = GsonMaker.getInstance();	
			String scene1Json = new String(Files.readAllBytes(Paths.get(filename)));
			Scene scene = gson.fromJson(scene1Json, Scene.class);
			menuWindow.setScene(scene);
		} catch(Exception e) {
			String msg = "Can't open scene!";
			menuWindow.log(msg);
			JOptionPane.showMessageDialog(menuWindow, msg, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
