package GymSystem;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

import org.postgresql.*;

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
	private int aID;
	
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
	 * @return true if the input manager/password combo exists
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public boolean validate() throws ClassNotFoundException, SQLException{
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
		Statement stmt = conn.createStatement();
		String sqlString = "SELECT e.eName, m.mpassword, e.eID "
				+ "FROM manager m JOIN employee e "
				+ "ON e.eID = m.eID;";
		ResultSet rs = stmt.executeQuery(sqlString);
		String thisUser;
		String thisPass;
		int thisID;
		while (rs.next()){
			thisUser = rs.getString("ename");
			thisPass = rs.getString("mpassword");
			String[] nameParts = thisUser.split(" ");
			thisID = rs.getInt("eID");
			if(nameParts[0].equals(this.aUser) && thisPass.equals(this.aPass)){
				this.aID = thisID;
				stmt.close();
				conn.close();
				return true;
			}
		}
		stmt.close();
		conn.close();
		return false;
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
        JButton viewMemberReportButton = new JButton("View Member Report");
		viewMemberReportButton.setToolTipText("View the total purchases of a given member");
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
    	viewMemberReportButton.addActionListener((ActionEvent e) -> {
            viewMemberReport();
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
        managePanel.add(viewMemberReportButton);
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
	 * TODO: Connect to DB and display results
	 * TODO: Maybe add way to have error if not a member?
	 */
	private void viewMemberReport(){
		JFrame viewMemberFrame = new JFrame();
		JPanel viewMemberPanel = new JPanel();
		viewMemberFrame.setTitle("View Member Report");
		viewMemberPanel.setLayout(new BoxLayout(viewMemberPanel, BoxLayout.Y_AXIS));
		JLabel memberIDLabel = new JLabel("Member ID: ");
		JTextField memberIDTextField = new JTextField();
		JButton viewMemberButton = new JButton("View Member Report");
		viewMemberButton.addActionListener((ActionEvent e) -> {
			if(memberIDTextField.getText().equals("")){
				JOptionPane.showMessageDialog(viewMemberFrame.getComponent(0), "Please enter a non-empty member ID.");
			}
			else{
				int memberID;

				try {
					memberID = Integer.parseInt(memberIDTextField.getText());

					JOptionPane.showMessageDialog(viewMemberFrame.getComponent(0), "TODO: The thing.");
					viewMemberFrame.dispatchEvent(new WindowEvent(viewMemberFrame, WindowEvent.WINDOW_CLOSING));
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(viewMemberFrame.getComponent(0), "Member ID must be numeric.");
				}
			}
			memberIDTextField.setText("");
		});
		viewMemberPanel.add(memberIDLabel);
		viewMemberPanel.add(memberIDTextField);
		viewMemberPanel.add(viewMemberButton);
		viewMemberFrame.add(viewMemberPanel);
		viewMemberFrame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent evt){
				viewMemberFrame.dispose();
			}
		});
		viewMemberFrame.pack();
		viewMemberFrame.setResizable(false);
		viewMemberFrame.setLocationRelativeTo(null);
		viewMemberFrame.setVisible(true);
	}
	
	/**
	 * ALTERNATIVE 5
	 * Changes the current user's password both locally and on the DB
	 * A user may only change his own password. this is enforced via the front-end
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
        	if(!(changeUserTextField.getText().equals(getUsername()))){
				JOptionPane.showMessageDialog(changeFrame.getComponent(0), "Wrong user");
			}
        	else if(!(String.copyValueOf(changePassField.getPassword()).equals(getPassword()))){
				JOptionPane.showMessageDialog(changeFrame.getComponent(0), "Wrong password");
			}
        	else if((String.copyValueOf(changeNewField.getPassword()).equals("")) && (String.copyValueOf(changeNewRepeatField.getPassword()).equals(""))){
        		JOptionPane.showMessageDialog(changeFrame.getComponent(0), "Please enter a non-empty password");
        	}
        	else if(!(String.copyValueOf(changeNewField.getPassword()).equals(String.copyValueOf(changeNewRepeatField.getPassword())))){
				JOptionPane.showMessageDialog(changeFrame.getComponent(0), "New passwords don't match");
			}
        	else{
        		String newPass = String.copyValueOf(changeNewField.getPassword());
        		int thisID = this.getID();
        		try {
					Class.forName("org.postgresql.Driver");
					Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
	        		Statement stmt = conn.createStatement();
	        		String sqlString = "UPDATE manager "
	        				+ "SET mpassword = '" + newPass + "' "
	        				+ "WHERE eID = " + thisID + ";";
	        		stmt.executeUpdate(sqlString);
	        		setPassword(newPass);
	        		JOptionPane.showMessageDialog(changeFrame.getComponent(0), "Password changed successfully");
	        		changeFrame.dispatchEvent(new WindowEvent(changeFrame, WindowEvent.WINDOW_CLOSING));
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(changeFrame.getComponent(0), "Error encountered. Aborting...");
					e1.printStackTrace();
	        		changeFrame.dispatchEvent(new WindowEvent(changeFrame, WindowEvent.WINDOW_CLOSING));
				}
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
	
	private String getUsername(){
		return this.aUser;
	}
	
	private String getPassword(){
		return this.aPass;
	}
	
	private int getID(){
		return this.aID;
	}
	
	private void setPassword(String newPass){
		this.aPass = newPass;
	}
}