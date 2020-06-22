package edu.cg.menu.components;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.google.gson.Gson;

import edu.cg.Scenes;
import edu.cg.menu.MenuWindow;
import edu.cg.scene.Scene;
import edu.cg.menu.GsonMaker;

@SuppressWarnings("serial")
public class ScenesCreator extends JPanel {
	private JFormattedTextField sceneNumTextField;
	
	public ScenesCreator(MenuWindow menuWindow, Consumer<String> sceneSelector) {
		super();
		
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		add(new JLabel("Scenes Creator:     "));
		
		sceneNumTextField = addAndGetTextField("      scene");
		
		
		add(new JLabel("            "));
		
		String btnName = "Create scene";
		JButton btnCreate = new JButton(btnName);
		btnCreate.addActionListener(e -> {
			try {
				if(!checkSceneNum())
					throw new IllegalArgumentException("The scnene number must be a positive integer.");

				int sceneNum = (Integer)sceneNumTextField.getValue();
				String sceneName = "scene" + sceneNum;

				Class<Scenes> scenes = Scenes.class;
				try {
					Method sceneMakerMethod = scenes.getMethod(sceneName);

					Gson gson = GsonMaker.getInstance();
					Object scene = sceneMakerMethod.invoke(null);
					String jsonStr = gson.toJson(scene, Scene.class);
					String sceneFileName = "scenes/" + sceneName + ".json";
					FileWriter writer = new FileWriter(sceneFileName);
					writer.write(jsonStr);
					writer.flush();
					writer.close();

					sceneSelector.accept(sceneFileName);

				} catch (NoSuchMethodException | SecurityException e1) {
					throw new RuntimeException("The function: Scenes." + sceneName + "() doesn't exist");
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
					throw new RuntimeException("Unknon error while inviking:  Scenes." + sceneName + "()");
				} catch (IOException e1) {
					throw new RuntimeException("Couldn't create file: " + sceneName + ".json");
				}
			} catch (Exception ex) {
				String msg = "Error in " + btnName + "!" + System.lineSeparator() + ex.getMessage();
				menuWindow.log(msg);
				JOptionPane.showMessageDialog(menuWindow, msg, "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		add(btnCreate);
	}
	
	private JFormattedTextField addAndGetTextField(String label) {
		JLabel jLabel = new JLabel(label);
		add(jLabel);
		JFormattedTextField tf = new JFormattedTextField(1);
		tf.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		tf.setColumns(3);
		add(tf);
		return tf;
	}
	
	private boolean checkSceneNum() {
		try {
			return (Integer)sceneNumTextField.getValue() > 0;
		} catch(Exception e) {
			return false;
		}
	}
}
