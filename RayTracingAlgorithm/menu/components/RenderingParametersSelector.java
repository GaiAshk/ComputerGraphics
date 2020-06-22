package edu.cg.menu.components;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import edu.cg.algebra.Ops;
import edu.cg.menu.MenuWindow;
import edu.cg.scene.Scene;

@SuppressWarnings("serial")
public class RenderingParametersSelector extends JPanel {
	private JFormattedTextField width;
	private JFormattedTextField height;
	private JFormattedTextField viewAngle;
	private JFormattedTextField recursionLevel;
	
	private Checkbox x1;
	private Checkbox x2;
	private Checkbox x3;
	
	private Checkbox reflection;
	private Checkbox refraction;
	
	public RenderingParametersSelector() {
		super();
		
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new GridLayout(0, 1, 0, 0));
	}
	
	public void initFields() {
		JPanel panel1 = new JPanel();
		panel1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(panel1);
		
		width = addAndGetTextField(" Width:", 400, panel1);
		height = addAndGetTextField(" Height:", 400, panel1);
		viewAngle = addAndGetTextField(" View Angle:", 90.0, panel1);
		
		JLabel antiAliasingLabel = new JLabel("   Anti aliasing:");
		panel1.add(antiAliasingLabel);
		
		
		CheckboxGroup group = new CheckboxGroup();
		Font myFont = new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 12);
		x1 = new Checkbox("x1 ", group, true);
		x1.setFont(myFont);
		x2 = new Checkbox("x2 ", group, false);
		x2.setFont(myFont);
		x3 = new Checkbox("x3", group, false);
		x3.setFont(myFont);
		panel1.add(x1);
		panel1.add(x2);
		panel1.add(x3);
		
		
		JPanel panel2 = new JPanel();
		panel2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(panel2);
		
		recursionLevel = addAndGetTextField(" Max recursion level:", 1, panel2);
		recursionLevel.setColumns(2);
		
		
		panel2.add(new JLabel("   "));
		
		reflection = new Checkbox("Render reflections  ");
		reflection.setFont(myFont);
		
		
		
		refraction = new Checkbox("Render refractions");
		refraction.setFont(myFont);
		
		panel2.add(reflection);
		
		panel2.add(refraction);
	}
	
	private JFormattedTextField addAndGetTextField(String label, int value, JPanel panel) {
		JLabel jLabel = new JLabel(label);
		panel.add(jLabel);
		JFormattedTextField tf = new JFormattedTextField(value);
		tf.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		tf.setColumns(3);
		panel.add(tf);
		return tf;
	}
	
	private JFormattedTextField addAndGetTextField(String label, double value, JPanel panel) {
		JLabel jLabel = new JLabel(label);
		panel.add(jLabel);
		JFormattedTextField tf = new JFormattedTextField(value);
		tf.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		tf.setColumns(3);
		panel.add(tf);
		return tf;
	}
	
	public int width() throws IllegalArgumentException{
		int ans = (Integer)width.getValue();
		
		if(ans <= 0)
			throw new IllegalArgumentException("Width argument must be positive");
		
		return ans;
	}

	public int height() throws IllegalArgumentException{
		int ans = (Integer)height.getValue();
		
		if(ans <= 0)
			throw new IllegalArgumentException("Height argument must be positive");
		
		return ans;
	}
	
	public double viewAngle() throws IllegalArgumentException{
		double ans = (Double)viewAngle.getValue();
		
		if(ans <= Ops.epsilon || ans >= 180.0)
			throw new IllegalArgumentException("View angle must be in the range (0.0, 180.0) exclusive.");
		
		return ans;
	}

	public void setWidth(int width) {
		this.width.setValue(width);
	}
	
	public void setHeight(int height) {
		this.height.setValue(height);
	}
	
	public void setViewPlainWidth(double width) {
		this.viewAngle.setValue(width);
	}
	
	public boolean readParameters(Scene scene, MenuWindow menuWindow) {
		try {
			int recursionLevel = (Integer)this.recursionLevel.getValue();
			if(recursionLevel < 1 | recursionLevel > 10)
				throw new RuntimeException();

			scene.initMaxRecursionLevel(recursionLevel);
		} catch (Exception ex) {
			String msg = "Recursion level must be an Integer between 1 to 10.";
			menuWindow.log(msg);
			JOptionPane.showMessageDialog(menuWindow, msg, "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		if(x1.getState())
			scene.initAntiAliasingFactor(1);
		else if(x2.getState())
			scene.initAntiAliasingFactor(2);
		else
			scene.initAntiAliasingFactor(3);

		scene.initRenderRefarctions(refraction.getState())
		.initRenderReflections(reflection.getState());
		
		return true;
	}
	
	public void writeParameters(Scene scene) {
		switch(scene.getFactor()) {
		case 1:
			x1.setState(true);
			break;
		case 2:
			x2.setState(true);
			break;
		default:
			x3.setState(true);
			break;
		}
		
		recursionLevel.setValue(scene.getMaxRecursionLevel());
		refraction.setState(scene.getRenderRefarctions());
		reflection.setState(scene.getRenderReflections());
	}
}
