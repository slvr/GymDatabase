import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GymAccess {

	public String aUser;
	private String aPass;
	
	/**
	 * Create a new GymAccess object. This constructor does not validate if the user/pass combo is valid, we trust that
	 * the main funciton will appropriately make use of the boolean validate method
	 * @param user the name of the manager accessing the database
	 * @param pass the password corresponding to this manager
	 */
	public GymAccess(String user, String pass){
		this.aUser = user;
		this.aPass = pass;
	}
	
	/**
	 * Ensure that the created GymAccess object is valid in the database
	 * TODO: connect to database & properly implement, current code is dummy code
	 * @return true if the input manager/password combo exists
	 */
	public boolean validate(){
		if(this.aUser.equals("Alec") && this.aPass.equals("test")){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Main window to access the functionalities
	 * TODO: Decide what all the actions are going to be, rename the methods appropriately
	 */
	public void manage(){
		JFrame manageFrame = new JFrame();
        JPanel managePanel = new JPanel();
        manageFrame.setTitle("Management System");
        Container c = manageFrame.getContentPane();
        Dimension d = new Dimension(200, 200);
        c.setPreferredSize(d);
        JButton oneButton = new JButton("Option 1");
        oneButton.setToolTipText("Seek option 1");
        JButton twoButton = new JButton("Option 2");
        twoButton.setToolTipText("Seek option 2");
        JButton threeButton = new JButton("Option 3");
        threeButton.setToolTipText("Seek Option 3");
        JButton fourButton = new JButton("Option 4");
        fourButton.setToolTipText("Seek option 4");
        JButton changePassButton = new JButton("Change Password");
        changePassButton.setToolTipText("Change your password");
        JButton exitButton = new JButton("Quit");
        exitButton.setToolTipText("Exit the management system");
        oneButton.addActionListener((ActionEvent e) -> {
            actionOne();
        });
        twoButton.addActionListener((ActionEvent e) -> {
            actionTwo();
        });
        threeButton.addActionListener((ActionEvent e) -> {
            actionThree();
        });
        fourButton.addActionListener((ActionEvent e) -> {
            actionFour();
        });
        changePassButton.addActionListener((ActionEvent e) -> {
            changePass();
        });
        exitButton.addActionListener((ActionEvent e) -> {
            manageFrame.dispose();
            System.exit(0);
        });
        managePanel.add(oneButton);
        managePanel.add(twoButton);
        managePanel.add(threeButton);
        managePanel.add(fourButton);
        managePanel.add(changePassButton);
        managePanel.add(exitButton);
        GridLayout manageLayout = new GridLayout(6, 1);
        managePanel.setLayout(manageLayout);
        manageFrame.add(managePanel);
        manageFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                manageFrame.dispose();
            }
        });
        manageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        manageFrame.pack();
        manageFrame.setResizable(false);
        manageFrame.setLocationRelativeTo(null);
        manageFrame.setVisible(true);
	}
	
	/**
	 * ALTERNATIVE 1
	 * TODO: Create this function
	 */
	private void actionOne(){
		
	}
	
	/**
	 * ALTERNATIVE 2
	 * TODO: Create this function
	 */
	private void actionTwo(){
		
	}
	
	/**
	 * ALTERNATIVE 3
	 * TODO: Create this function
	 */
	private void actionThree(){
		
	}
	
	/**
	 * ALTERNATIVE 4
	 * TODO: Create this function
	 */
	private void actionFour(){
		
	}
	
	/**
	 * ALTERNATIVE 5
	 * Changes the current user's password
	 * TODO: Connect to DB, add (NON-NULL) password field to Manager
	 */
	private void changePass(){
		JFrame changeFrame = new JFrame();
		JPanel changePanel = new JPanel();
		changeFrame.setTitle("Change Your Password");
		changePanel.setLayout(new BoxLayout(changePanel, BoxLayout.Y_AXIS));
		JLabel changeUserLabel = new JLabel("Name: ");
		JTextField changeUserTextField = new JTextField();
		JLabel changePassLabel = new JLabel("Current Password: ");
		JPasswordField changePassField = new JPasswordField();
		JLabel changeNewLabel = new JLabel("New Password: ");
		JPasswordField changeNewField = new JPasswordField();
		JLabel changeNewRepeatLabel = new JLabel("Repeat New Password: ");
		JPasswordField changeNewRepeatField = new JPasswordField();
		JButton changeButton = new JButton("Change Password");
        changeButton.addActionListener((ActionEvent e) -> {
        	if(!(changeUserTextField.getText().equals(this.aUser))){
				JOptionPane.showMessageDialog(changeFrame.getComponent(0), "Wrong user");
			}
        	else if(!(String.copyValueOf(changePassField.getPassword()).equals(this.aPass))){
				JOptionPane.showMessageDialog(changeFrame.getComponent(0), "Wrong password");
			}
        	else if((String.copyValueOf(changeNewField.getPassword()).equals("")) && (String.copyValueOf(changeNewRepeatField.getPassword()).equals(""))){
        		JOptionPane.showMessageDialog(changeFrame.getComponent(0), "Please enter a non-empty password");
        	}
        	else if(!(String.copyValueOf(changeNewField.getPassword()).equals(String.copyValueOf(changeNewRepeatField.getPassword())))){
				JOptionPane.showMessageDialog(changeFrame.getComponent(0), "New passwords don't match");
			}
        	else{
        		JOptionPane.showMessageDialog(changeFrame.getComponent(0), "[Will create function which changes password]");
        		changeFrame.dispatchEvent(new WindowEvent(changeFrame, WindowEvent.WINDOW_CLOSING));
        	}
        	changeUserTextField.setText("");
			changePassField.setText("");
			changeNewField.setText("");
			changeNewRepeatField.setText("");
        });
        changePanel.add(changeUserLabel);
        changePanel.add(changeUserTextField);
        changePanel.add(changePassLabel);
        changePanel.add(changePassField);
        changePanel.add(changeNewLabel);
        changePanel.add(changeNewField);
        changePanel.add(changeNewRepeatLabel);
        changePanel.add(changeNewRepeatField);
		changePanel.add(changeButton);
		changeFrame.add(changePanel);
		changeFrame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent evt){
				changeFrame.dispose();
			}
		});
		changeFrame.pack();
		changeFrame.setResizable(false);
		changeFrame.setLocationRelativeTo(null);
		changeFrame.setVisible(true);
	}
}