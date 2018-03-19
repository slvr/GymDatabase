import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Main {

	public static void main(String args[]){
		JFrame accessFrame = new JFrame();
		JPanel accessPanel = new JPanel();
		accessFrame.setTitle("Welcome to the GymBros System");
		accessPanel.setLayout(new BoxLayout(accessPanel, BoxLayout.Y_AXIS));
		JLabel accessUserLabel = new JLabel("Name: ");
		JTextField accessUsername = new JTextField();
		JLabel accessPassLabel = new JLabel("Password: ");
		JPasswordField accessPass = new JPasswordField();
		accessPanel.add(accessUserLabel);
		accessPanel.add(accessUsername);
		accessPanel.add(accessPassLabel);
		accessPanel.add(accessPass);
		JButton accessButton = new JButton("Log in");
		accessButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		accessButton.addActionListener((ActionEvent e) -> {
			String inputUser = accessUsername.getText();
			String inputPass = String.copyValueOf(accessPass.getPassword());
			GymAccess g = new GymAccess(inputUser, inputPass);
			boolean isValid = g.validate();
			if(isValid){
				accessFrame.setVisible(false);
				g.manage();
			}
			else{
				JOptionPane.showMessageDialog(accessFrame.getComponent(0), "Access Denied");
				accessUsername.setText("");
				accessPass.setText("");
			}
		});
		accessPanel.add(accessButton);
		accessFrame.add(accessPanel);
		accessFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		accessFrame.pack();
		accessFrame.setResizable(true);
		accessFrame.setLocationRelativeTo(null);
		accessFrame.setVisible(true);
	}
}
