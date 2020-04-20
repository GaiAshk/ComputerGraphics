package edu.cg.menu.components;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class ScaleSelector extends JPanel {
	private JFormattedTextField width;
	private JFormattedTextField height;

	private Checkbox nearestNeighbor;

	public ScaleSelector() {
		super();

		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		width = addAndGetTextField("   Width:", 640);
		height = addAndGetTextField("   Height:", 480);

		JLabel someSpaces = new JLabel("  ");
		add(someSpaces);

		CheckboxGroup group = new CheckboxGroup();
		nearestNeighbor = new Checkbox("Nearest neighbor  ", group, true);
		Checkbox seamCarving = new Checkbox("Seam carving", group, false);
		add(nearestNeighbor);
		add(seamCarving);
	}

	private JFormattedTextField addAndGetTextField(String label, int value) {
		JLabel jLabel = new JLabel(label);
		add(jLabel);
		JFormattedTextField tf = new JFormattedTextField(value);
		tf.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		tf.setColumns(5);
		add(tf);
		return tf;
	}

	public int width() {
		int ans = (Integer) width.getValue();

		if (ans <= 0)
			throw new IllegalArgumentException("Width argument must be positive");

		return ans;
	}

	public int height() {
		int ans = (Integer) height.getValue();

		if (ans <= 0)
			throw new IllegalArgumentException("Height argument must be positive");

		return ans;
	}

	public static enum ResizingOperation {
		NEAREST_NEIGHBOR("nearest neighbor"), SEAM_CARVING("seam carving");

		public final String title;

		ResizingOperation(String title) {
			this.title = title;
		}
	}

	public ResizingOperation resizingOperation() {
		if (nearestNeighbor.getState())
			return ResizingOperation.NEAREST_NEIGHBOR;
		else
			return ResizingOperation.SEAM_CARVING;
	}

	public void setWidth(int width) {
		this.width.setValue(width);
	}

	public void setHeight(int height) {
		this.height.setValue(height);
	}
}
