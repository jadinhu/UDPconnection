/**
 * WindowDisplay.cs
 * Created by: Jadson Almeida [jadson.sistemas@gmail.com]
 * Created on: 09/04/19 (dd/mm/yy)
 */
package window;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import udp.ConnectionHandler;
import udp.ShareData;

/**
 * JFrame class with label and Text Area to send/receive data.
 * 
 * @author Jadson
 */
public class WindowDisplay extends JFrame {

	private static final long serialVersionUID = 1L;
	JLabel label;
	JTextField textField;
	ShareData sendData;

	public WindowDisplay(String title, ShareData sendData, ConnectionHandler closer) {
		this.sendData = sendData;
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new FlowLayout());
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(300, 30));
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				SendText();
			}
		});
		addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
            	closer.Close();
                e.getWindow().dispose();
            }
        });
		add(textField);
		pack();
		setVisible(true);
	}

	public void AddLineToLabel(String text) {
		if (label == null) {
			label = new JLabel("<html>" + text + "</html>");
			add(label);
		} else {
			label.setText(label.getText().replace("</html>", "<br>" + text + "</html>"));
		}
		pack();
	}
	
	public void SendText() {
		sendData.Send(textField.getText());
		textField.setText("");
	}

}
