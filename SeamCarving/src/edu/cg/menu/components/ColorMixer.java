package edu.cg.menu.components;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import edu.cg.RGBWeights;

@SuppressWarnings("serial")
public class ColorMixer extends JPanel {
	private JFormattedTextField red;
	private JFormattedTextField green;
	private JFormattedTextField blue;
	
	public ColorMixer() {
		super();
		
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel weightsLabel = new JLabel("RGB weights     ");
		add(weightsLabel);
		
		red = addAndGetTextField("      Red:");
		green = addAndGetTextField("   Green:");
		blue = addAndGetTextField("   Blue:");
	}
	
	private JFormattedTextField addAndGetTextField(String label) {
		JLabel jLabel = new JLabel(label);
		add(jLabel);
		JFormattedTextField tf = new JFormattedTextField(1);
		tf.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		tf.setColumns(5);
		add(tf);
		return tf;
	}

	public RGBWeights getRGBWeights() {
		if(!checkColorsValues()) {
			throw new IllegalArgumentException("The RGB weights values must be Integers between 0 and 100," + 
					System.lineSeparator() + "and their amount must be positive.");
		}
		
		int red = (Integer)this.red.getValue();
		int green = (Integer)this.green.getValue();
		int blue = (Integer)this.blue.getValue();
		
		return new RGBWeights(red, green, blue);
	}

	private boolean checkColorsValues() {
		try {
			int red = (Integer)this.red.getValue();
			int green = (Integer)this.green.getValue();
			int blue = (Integer)this.blue.getValue();
			
			return red >= 0 & red <= 100 &
					green >= 0 & green <= 100 &
					blue >= 0 & blue <= 100 &
					red + green + blue > 0;
			
		} catch(Exception e) {
			return false;
		}
	}
}
