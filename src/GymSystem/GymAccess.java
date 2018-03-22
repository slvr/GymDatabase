package GymSystem;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.ArrayList;

import org.postgresql.*;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
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
	 */
	public void manage(){
		JFrame manageFrame = new JFrame();
        JPanel managePanel = new JPanel();
        manageFrame.setTitle("Management System");
        Container c = manageFrame.getContentPane();
        Dimension d = new Dimension(200, 200);
        c.setPreferredSize(d);
        JButton buyButton = new JButton("Accept a Purchase");
        buyButton.setToolTipText("A member is making a purchase [all sales are final]");
        JButton roomButton = new JButton("View Room Occupations");
        roomButton.setToolTipText("Get a list of times when a certain room cannot be accessed");
        JButton signupButton = new JButton("Register for a class");
        signupButton.setToolTipText("Register a member for a class");
        JButton viewMemberReportButton = new JButton("View Member Report");
		viewMemberReportButton.setToolTipText("View the total purchases of a given member");
        JButton changePassButton = new JButton("Change Password");
        changePassButton.setToolTipText("Change your password");
        JButton exitButton = new JButton("Quit");
        exitButton.setToolTipText("Exit the management system");
        buyButton.addActionListener((ActionEvent e) -> {
            allowPurchase();
        });
        roomButton.addActionListener((ActionEvent e) -> {
            viewAvailabilities();
        });
        signupButton.addActionListener((ActionEvent e) -> {
            signUpForClass();
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
        managePanel.add(buyButton);
        managePanel.add(roomButton);
        managePanel.add(signupButton);
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
	 * Enter a member name and allow them to purchase a certain item.
	 */
	private void allowPurchase(){
		JFrame purchaseFrame = new JFrame();
		JPanel purchasePanel = new JPanel();
		purchaseFrame.setTitle("Allow a purchase");
		purchasePanel.setLayout(new BoxLayout(purchasePanel, BoxLayout.Y_AXIS));
		
		JLabel memberLabel = new JLabel("Name:");
		JTextField memberText = new JTextField();
		ButtonGroup group = new ButtonGroup();
		JRadioButton signupChoice = new JRadioButton("Sign up for a class");
		signupChoice.setActionCommand("0");
		group.add(signupChoice);
		JRadioButton buyChoice = new JRadioButton("Buy equipment");
		buyChoice.setActionCommand("1");
		group.add(buyChoice);
		JRadioButton rentChoice = new JRadioButton("Rent a facility");
		rentChoice.setActionCommand("2");
		group.add(rentChoice);
		JButton purchaseButton = new JButton("Purchase [All sales final]");
		purchaseButton.addActionListener((ActionEvent e) -> {
			String member = memberText.getText();
			String selected = group.getSelection().getActionCommand();
			if(member.equals("")){
				JOptionPane.showMessageDialog(purchaseFrame.getComponent(0), "Please enter a name");
				memberText.setText("");
			}
			try{
				if(selected.equals("0")){ //Sign up
					Class.forName("org.postgresql.Driver");
					Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
					Statement stmt = conn.createStatement();
					String sqlString = "SELECT cid, ctime, cname "
							+ "FROM class;";
					ResultSet rs = stmt.executeQuery(sqlString);
					ArrayList<Integer> classes = new ArrayList<Integer>();
					ArrayList<String> times = new ArrayList<String>();
					ArrayList<String> names = new ArrayList<String>();
					while(rs.next()){
						classes.add(rs.getInt("cid"));
						times.add(rs.getString("ctime"));
						names.add(rs.getString("cname"));
					}
					JFrame signupFrame = new JFrame();
					JPanel signupPanel = new JPanel();
					ButtonGroup group2 = new ButtonGroup();
					for(int i = 0; i < classes.size(); i++){
						JRadioButton thisChoice = new JRadioButton(names.get(i) + " at " + times.get(i));
						thisChoice.setActionCommand("" + classes.get(i) + "");
						group2.add(thisChoice);
						signupPanel.add(thisChoice);
					}
					JButton signupButton = new JButton("Select");
					signupButton.addActionListener((ActionEvent e1) -> {
						String selectedClass = group2.getSelection().getActionCommand();
						int cid = Integer.parseInt(selectedClass);
						int result = signup(member, cid);
						if(result == -1){ //member does not exist
							JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Member does not exist.");
			        		signupFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
						}
						else if(result == -2){ //caught exception
							JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Error encountered. Aborting...");
		        			signupFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
						}
						else if(result == -3){ //already signed up for this class
							JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Member already signed up for this class!");
						}
						else{ //good. Member now in the signs_up_for table, must be added to the purchase table
							//TODO
						}
					});
					signupFrame.getRootPane().setDefaultButton(purchaseButton);
					signupPanel.add(signupButton);
					signupFrame.add(signupPanel);
					signupFrame.addWindowListener(new WindowAdapter(){
						@Override
						public void windowClosing(WindowEvent evt){
							signupFrame.dispose();
						}
					});
					signupFrame.pack();
					signupFrame.setResizable(false);
					signupFrame.setLocationRelativeTo(null);
					signupFrame.setVisible(true);
					stmt.close();
					conn.close();
				}
				else if(selected.equals("1")){ //Buy
					//Get available eqtypes and number available for each
					Class.forName("org.postgresql.Driver");
					Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
					Statement stmt = conn.createStatement();
					String sqlString = "";
					ResultSet rs = stmt.executeQuery(sqlString);
					//Select how many of each item you wish to buy
					
					int result = buy(member, eqtypes);
					
					stmt.close();
					conn.close();
				}
				else if(selected.equals("2")){ //Rent
					Class.forName("org.postgresql.Driver");
					Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
					Statement stmt = conn.createStatement();
					String sqlString = "SELECT * "
							+ "FROM facility";
					ResultSet rs = stmt.executeQuery(sqlString);
					
					rent(member, roomnum);
					
					stmt.close();
					conn.close();
				}
				else{
					JOptionPane.showMessageDialog(purchaseFrame.getComponent(0), "Please select a purchase type");
					memberText.setText("");
				}
			}
			catch(Exception e1){
				JOptionPane.showMessageDialog(purchaseFrame.getComponent(0), "Error encountered. Aborting...");
				e1.printStackTrace();
				purchaseFrame.dispatchEvent(new WindowEvent(purchaseFrame, WindowEvent.WINDOW_CLOSING));
			}
		});
		purchasePanel.add(signupChoice);
		purchasePanel.add(buyChoice);
		purchasePanel.add(rentChoice);
		purchasePanel.add(memberLabel);
		purchasePanel.add(memberText);
		purchaseFrame.getRootPane().setDefaultButton(purchaseButton);
		purchasePanel.add(purchaseButton);
		purchaseFrame.add(purchasePanel);
		purchaseFrame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent evt){
				purchaseFrame.dispose();
			}
		});
		purchaseFrame.pack();
		purchaseFrame.setResizable(false);
		purchaseFrame.setLocationRelativeTo(null);
		purchaseFrame.setVisible(true);
	}
	
	private int buy(String member, String[] eqtypes){
		
	}
	
	private int rent(String member, int roomnum){
		
	}
	
	/**
	 * ALTERNATIVE 2
	 * View when a facility cannot be accessed, due to a class being given during certain times.
	 */
	private void viewAvailabilities(){
		JFrame availFrame = new JFrame();
		JPanel availPanel = new JPanel();
		availFrame.setTitle("View Availabilities");
		availPanel.setLayout(new BoxLayout(availPanel, BoxLayout.Y_AXIS));
		
		//TODO: Select needed information
		
		JButton availButton = new JButton("View Availabilities");
		availButton.addActionListener((ActionEvent e) -> {
			 // TODO: Create this function
		});

		availFrame.getRootPane().setDefaultButton(availButton);
		availPanel.add(availButton);
		availFrame.add(availPanel);
		availFrame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent evt){
				availFrame.dispose();
			}
		});
		availFrame.pack();
		availFrame.setResizable(false);
		availFrame.setLocationRelativeTo(null);
		availFrame.setVisible(true);
	}
	
	/**
	 * ALTERNATIVE 3
	 * Sign up a member for a certain class. Once signed up, show them a list of all their registered classes and times
	 */
	private void signUpForClass(){
		JFrame signupFrame = new JFrame();
		JPanel signupPanel = new JPanel();
		signupFrame.setTitle("Sign up");
		signupPanel.setLayout(new BoxLayout(signupPanel, BoxLayout.Y_AXIS));
		JLabel memberName = new JLabel("Name:");
		JTextField memberTextField = new JTextField();
		JLabel className = new JLabel("Class:");
		JTextField classTextField = new JTextField();
		JButton signupButton = new JButton("Allow Sign-up");
		signupButton.addActionListener((ActionEvent e) -> {
			String member = memberTextField.getText();
			String classN = classTextField.getText();
			if(member.equals("") || classN.equals("")){
				JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Please complete all fields");
			}
			else{
				try{
					Class.forName("org.postgresql.Driver");
					Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
					Statement stmt = conn.createStatement();
					String sqlString = "SELECT cid, ctime, froomnum "
							+ "FROM class "
							+ "WHERE cname = '" + classN + "';";
					ResultSet rs = stmt.executeQuery(sqlString);
					ArrayList<Integer> classes = new ArrayList<Integer>();
					ArrayList<String> times = new ArrayList<String>();
					ArrayList<Integer> roomnums = new ArrayList<Integer>();
					while(rs.next()){
						classes.add(rs.getInt("cid"));
						times.add(rs.getString("ctime"));
						roomnums.add(rs.getInt("froomnum"));
					}
					if(classes.size() == 0){
						JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Class does not exist.");
	        			signupFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
	        			
	        			stmt.close();
		        		conn.close();
		        		return;
					}
					else if(classes.size() > 1){
						signupFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
						JFrame selectFrame = new JFrame();
						JPanel selectPanel = new JPanel();
						selectFrame.setTitle("Sign up");
						selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.Y_AXIS));
						ButtonGroup group = new ButtonGroup();
						for(int i = 0; i < classes.size(); i++){
							JRadioButton thisChoice = new JRadioButton(times.get(i) + " in " + roomnums.get(i));
							thisChoice.setActionCommand(times.get(i));
							group.add(thisChoice);
							selectPanel.add(thisChoice);
						}
						JButton selectButton = new JButton("Select Section");
						selectButton.addActionListener((ActionEvent e1) ->{
							String selected = group.getSelection().getActionCommand();
							int i = 0;
							while(i < times.size()){
								if(times.get(i).equals(selected)){
									break;
								}
								i++;
							}
							int cid = classes.get(i);
							int execute = signup(member, cid);
							if(execute == -1){
								JOptionPane.showMessageDialog(selectFrame.getComponent(0), "Member does not exist.");
								selectFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
			        			signupFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
							}
							if(execute == -2){
								JOptionPane.showMessageDialog(selectFrame.getComponent(0), "Error encountered. Aborting...");
								selectFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
			        			signupFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
							}
							if(execute == -3){
								JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Member already signed up for this class!");
							}
							showRegisteredClasses(member);
							selectFrame.dispose();
						});
						selectFrame.getRootPane().setDefaultButton(selectButton);
						selectPanel.add(selectButton);
						selectFrame.add(selectPanel);
						selectFrame.addWindowListener(new WindowAdapter(){
							@Override
							public void windowClosing(WindowEvent evt){
								selectFrame.dispose();
								signupFrame.dispose();
							}
						});
						selectFrame.pack();
						selectFrame.setResizable(false);
						selectFrame.setLocationRelativeTo(null);
						selectFrame.setVisible(true);
					}
					else{
						int cid = classes.get(0);
						int execute = signup(member, cid);
						if(execute == -1){
							JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Member does not exist.");
		        			signupFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
						}
						if(execute == -2){
							JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Error encountered. Aborting...");
		        			signupFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
						}
						if(execute == -3){
							JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Member already signed up for this class!");
						}
						showRegisteredClasses(member);
					}
					stmt.close();
					conn.close();
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Error encountered. Aborting...");
					e1.printStackTrace();
					signupFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
				}
			}
			memberTextField.setText("");
			classTextField.setText("");
			
		});
		signupFrame.getRootPane().setDefaultButton(signupButton);
		signupPanel.add(memberName);
		signupPanel.add(memberTextField);
		signupPanel.add(className);
		signupPanel.add(classTextField);
		signupPanel.add(signupButton);
		signupFrame.add(signupPanel);
		signupFrame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent evt){
				signupFrame.dispose();
			}
		});
		signupFrame.pack();
		signupFrame.setResizable(false);
		signupFrame.setLocationRelativeTo(null);
		signupFrame.setVisible(true);
	}
	
	//Helper function to signUpForClass
	private int signup(String member, int cid){
		try{
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
    		Statement stmt = conn.createStatement();
    		String sqlString = "SELECT mid FROM member WHERE mname = '" + member + "';";
    		ResultSet rs = stmt.executeQuery(sqlString);
    		while (!rs.next()) { //If member does not exist     			
    			stmt.close();
        		conn.close();
        		return -1;
			}
    		int mid = rs.getInt("mid");
    		
    		sqlString = "SELECT mid, cid "
    				+ "FROM signs_up_for "
    				+ "WHERE mid = " + mid + " AND cid = " + cid;
    		rs = stmt.executeQuery(sqlString);
    		while (rs.next()) { //If member is already signed up    			
    			stmt.close();
        		conn.close();
        		return -3;
			}
    		sqlString = "INSERT INTO signs_up_for VALUES (" + mid + ", " + cid + ");";
    		stmt.executeUpdate(sqlString);
    		stmt.close();
    		conn.close();
    		return 0;
		}
		catch (Exception e){
			e.printStackTrace();
			return -2;
		}
	}
	
	private void showRegisteredClasses(String member){
		try{
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
			Statement stmt = conn.createStatement();
			String sqlString = "SELECT c.cname, c.ctime, c.froomnum "
					+ "FROM class c, member m, signs_up_for s "
					+ "WHERE c.cid = s.cid "
					+ "AND m.mid = s.mid "
					+ "AND m.mname = '" + member + "';";
			ResultSet rs = stmt.executeQuery(sqlString);
			
			JFrame dispFrame = new JFrame();
			JPanel dispPanel = new JPanel();
			dispFrame.setTitle("Their classes");
			dispPanel.setLayout(new BoxLayout(dispPanel, BoxLayout.Y_AXIS));
			while(rs.next()){
				JLabel thisLabel = new JLabel(rs.getString("cname") + " at " + rs.getString("ctime") + " in " + rs.getInt("froomnum"));
				dispPanel.add(thisLabel);
			}
			stmt.close();
			conn.close();
			JButton dispButton = new JButton("End");
			dispButton.addActionListener((ActionEvent e) -> {
				dispFrame.dispose();
			});
			dispFrame.getRootPane().setDefaultButton(dispButton);
			dispPanel.add(dispButton);
			dispFrame.add(dispPanel);
			dispFrame.addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosing(WindowEvent evt){
					dispFrame.dispose();
				}
			});
			dispFrame.pack();
			dispFrame.setResizable(false);
			dispFrame.setLocationRelativeTo(null);
			dispFrame.setVisible(true);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * ALTERNATIVE 4
	 * Write a member's name, return an overview of all their purchases
	 */
	private void viewMemberReport(){
		JFrame viewMemberFrame = new JFrame();
		JPanel viewMemberPanel = new JPanel();
		viewMemberFrame.setTitle("View Member Report");
		viewMemberPanel.setLayout(new BoxLayout(viewMemberPanel, BoxLayout.Y_AXIS));
		JLabel memberNameLabel = new JLabel("Member Name: ");
		JTextField memberNameTextField = new JTextField();
		JButton viewMemberButton = new JButton("View Member Report");
		viewMemberButton.addActionListener((ActionEvent e) -> {
			if(memberNameTextField.getText().equals("")){
				JOptionPane.showMessageDialog(viewMemberFrame.getComponent(0), "Please enter a name.");
			}
			else{
				try {
					Class.forName("org.postgresql.Driver");
					Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
	        		Statement stmt = conn.createStatement();
	        		String sqlString = "SELECT mid FROM member WHERE mname = '" + memberNameTextField.getText() + "';";
	        		ResultSet rs = stmt.executeQuery(sqlString);
	        		
	        		while (!rs.next()) { //If nothing in result set 
	        			JOptionPane.showMessageDialog(viewMemberFrame.getComponent(0), "Member does not exist.");
	        			viewMemberFrame.dispatchEvent(new WindowEvent(viewMemberFrame, WindowEvent.WINDOW_CLOSING));
	        			
	        			stmt.close();
		        		conn.close();
		        		return;
        			}
	        		     		
        			int memberID = rs.getInt("mid");
        			
        			try {
    	        		sqlString = "SELECT * FROM member_report(" + memberID + ");";
    	        		rs = stmt.executeQuery(sqlString);
    	        		
    	        		JFrame viewFrame = new JFrame();
    	                JPanel viewPanel = new JPanel();
    	                viewFrame.setTitle("Record");
    	                viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
    	                while(rs.next()){
    	                    String ptype = rs.getString("ptype");
    	                    float total = rs.getFloat("total");
    	                    
    	                    JLabel viewLabel = new JLabel();
    	                    viewLabel.setText(ptype + ": $" + String.format("%.2f", total));
    	                    viewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    	                    viewPanel.add(viewLabel);
    	                }
    	                JButton backButton = new JButton("Back");
    	                backButton.addActionListener((ActionEvent f) -> {
    	                    viewFrame.dispose();
    	                });
    	                viewPanel.add(backButton);
    	                viewFrame.add(viewPanel);
    	                viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	                viewFrame.pack();
    	                viewFrame.setLocationRelativeTo(null);
    	                viewFrame.setVisible(true);
        			} catch (Exception e1) {
    					JOptionPane.showMessageDialog(viewMemberFrame.getComponent(0), "Error encountered. Aborting...");
    					e1.printStackTrace();
    					viewMemberFrame.dispatchEvent(new WindowEvent(viewMemberFrame, WindowEvent.WINDOW_CLOSING));
    				}
	        		
	        		stmt.close();
	        		conn.close();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(viewMemberFrame.getComponent(0), "Error encountered. Aborting...");
					e1.printStackTrace();
					viewMemberFrame.dispatchEvent(new WindowEvent(viewMemberFrame, WindowEvent.WINDOW_CLOSING));
				}
			}
			memberNameTextField.setText("");
		});
		viewMemberFrame.getRootPane().setDefaultButton(viewMemberButton);
		viewMemberPanel.add(memberNameLabel);
		viewMemberPanel.add(memberNameTextField);
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
	        		stmt.close();
	        		conn.close();
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
        changeFrame.getRootPane().setDefaultButton(changeButton);
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