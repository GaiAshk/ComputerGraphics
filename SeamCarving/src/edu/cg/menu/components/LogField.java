package edu.cg.menu.components;

import java.awt.BorderLayout;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import edu.cg.Logger;

@SuppressWarnings("serial")
public class LogField extends JPanel implements Logger {
	private JTextArea txtLog;
	
	
	public LogField() {
		super();
		
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));
		
		JLabel lblLog = new JLabel(" Log:   ");
		add(lblLog, BorderLayout.WEST);
		
		txtLog = new JTextArea(); 
		txtLog.setEditable(false);
		txtLog.setLineWrap(true);
		JScrollPane scrollLog = new JScrollPane(txtLog);
		add(scrollLog);
	}

	@Override
	public synchronized void log(String s) {
		if(s == null)
			s = "null";
		
		Calendar cal = Calendar.getInstance();
		String hh = convertTime(cal.get(Calendar.HOUR_OF_DAY));
		String mm = convertTime(cal.get(Calendar.MINUTE));
		String ss = convertTime(cal.get(Calendar.SECOND));
		String time = "[" + hh + ":" + mm + ":" + ss + "] ~ ";
		String msg = time + s + System.lineSeparator();
		txtLog.append(msg);
	}
	
	private static String convertTime(int t) {
		return (t < 10 ? "0" : "") + t;
	}

}
