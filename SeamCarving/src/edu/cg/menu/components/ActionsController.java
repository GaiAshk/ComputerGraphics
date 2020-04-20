package edu.cg.menu.components;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import edu.cg.Logger;
import edu.cg.menu.MenuWindow;

@SuppressWarnings("serial")
public class ActionsController extends JPanel {
	private List<JButton> buttons;
	private Logger logger;
	private Component mainComponent;
	
	public ActionsController(MenuWindow menuWindow) {
		super();
		
		buttons = new ArrayList<>();
		logger = menuWindow;
		mainComponent = menuWindow;
		
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel1 = new JPanel();
		panel1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(panel1);
		
		//sets buttons:
		panel1.add(addAndGetButton("Change hue - example", menuWindow::changeHue));
		panel1.add(addAndGetButton("Greyscale", menuWindow::greyscale));
		panel1.add(addAndGetButton("Mask Image", menuWindow::maskImage));
		panel1.add(addAndGetButton("Resize", menuWindow::resize));
		
		
		JPanel panel2 = new JPanel();
		panel2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		add(panel2);
		
		panel2.add(addAndGetButton("Show seams - vertical", menuWindow::showSeamsVertical));
		panel2.add(addAndGetButton("Show seams - horizontal", menuWindow::showSeamsHorizontal));
	}
	
	private JButton addAndGetButton(String btnName, Runnable action) {
		JButton btn = new JButton(btnName);
		btn.addActionListener(e -> {
			try {
				action.run();
			} catch (Exception ex) {
				String msg = "Error in " + btnName + "!" + System.lineSeparator() + ex.getMessage();
				logger.log(msg);
				JOptionPane.showMessageDialog(mainComponent, msg, "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		btn.setEnabled(false);
		buttons.add(btn);
		return btn;
	}

	public void activateButtons() {
		for(JButton btn: buttons)
			btn.setEnabled(true);
	}
}
