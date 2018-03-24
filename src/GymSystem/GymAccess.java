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

//import org.postgresql.*;

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
        JButton regButton = new JButton("Register a new member");
        regButton.setToolTipText("A new member has joined the complex!");
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
            viewBookings();
        });
        signupButton.addActionListener((ActionEvent e) ->{
        	signUpForClass();
        });
        regButton.addActionListener((ActionEvent e) -> {
            signUpMember();
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
        managePanel.add(regButton);
        managePanel.add(viewMemberReportButton);
        managePanel.add(changePassButton);
        managePanel.add(exitButton);
        GridLayout manageLayout = new GridLayout(7, 1);
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
	    // build purchase window
		JFrame purchaseFrame = new JFrame();
		JPanel purchasePanel = new JPanel();
		purchaseFrame.setTitle("Allow a purchase");
		purchasePanel.setLayout(new BoxLayout(purchasePanel, BoxLayout.Y_AXIS));

		// name prompt
		JLabel memberLabel = new JLabel("Name:");
		JTextField memberText = new JTextField();

		// purchase option radio buttons
		ButtonGroup purchGroup = new ButtonGroup();
		JRadioButton classChoice = new JRadioButton("Pay for a class");
		classChoice.setActionCommand("0");
		purchGroup.add(classChoice);
		JRadioButton buyChoice = new JRadioButton("Buy equipment");
		buyChoice.setActionCommand("1");
		purchGroup.add(buyChoice);
		//JRadioButton rentChoice = new JRadioButton("Rent a facility");
		//rentChoice.setActionCommand("2");
		//purchGroup.add(rentChoice);

		JButton purchaseButton = new JButton("Purchase [All sales final]");
		purchaseButton.addActionListener((ActionEvent e) -> {
			String member = memberText.getText();
			// test selected
            String selected;
            try {
                selected = purchGroup.getSelection().getActionCommand();
                if (member.equals("")) throw new NullPointerException();
            } catch (NullPointerException npe) {
                selected = "-1";
            }

			/*if(member.equals("")){
				JOptionPane.showMessageDialog(purchaseFrame.getComponent(0), "Please enter a name");
				memberText.setText("");
			}*/
			try{
                // connect to database
                Class.forName("org.postgresql.Driver");
                Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
                Statement stmt = conn.createStatement();
                String sqlString;
                ResultSet rs;

			    // get member id
                sqlString = "SELECT mid FROM member WHERE mname = '"+member+"';";
                rs = stmt.executeQuery(sqlString);
                int mID = -1;
                if (selected.equals("-1")) ;// do nothing
                else if (rs.next())
                    mID = rs.getInt(1);
                else {
                    // return with pop-up if member does not exist
                    JOptionPane.showMessageDialog(purchaseFrame.getComponent(0), member + " does not exist.");
                    memberText.setText("");
                    //stmt.close();
                    //conn.close();
                }

                JLabel amountLabel;
                JTextField amountText;
                JButton payButton;

                switch (selected) {
                    case "0": //Pay for class
                        // get list of class attributes from classes that 'member' has signed up for and not payed for
                        sqlString =
                                "SELECT cname, ctime, cid FROM class\n" +
                                "WHERE cid IN (\n" +
                                    "SELECT cid FROM signs_up_for\n" +
                                    "WHERE mid = '"+mID+"'\n" +
                                    "EXCEPT\n" +
                                    "SELECT cid FROM payment\n" +
                                    "WHERE mid = '"+mID+"' AND ptype = 'Sign Up'\n" +
                                ");";
                        rs = stmt.executeQuery(sqlString);

                        // parse attributes into array lists
                        ArrayList<Integer> classes = new ArrayList<Integer>();
                        ArrayList<String> times = new ArrayList<String>();
                        ArrayList<String> names = new ArrayList<String>();
                        while (rs.next()) {
                            classes.add(rs.getInt("cid"));
                            times.add(rs.getString("ctime"));
                            names.add(rs.getString("cname"));
                        }

                        // break with pop-up if member has no unpaid classes
                        if (classes.size() == 0) {
                            JOptionPane.showMessageDialog(purchaseFrame.getComponent(0), member + " has no unpaid classes.");
                            memberText.setText("");
                            break;
                        }

                        // build pay for class window
                        JFrame payClassFrame = new JFrame();
                        JPanel payClassPanel = new JPanel();
                        ButtonGroup classGroup = new ButtonGroup();
                        payClassPanel.setLayout(new BoxLayout(payClassPanel, BoxLayout.Y_AXIS));

                        // list classes to pay for as radio buttons
                        for (int i = 0; i < classes.size(); i++) {
                            JRadioButton thisChoice = new JRadioButton(names.get(i) + " at " + times.get(i));
                            thisChoice.setActionCommand("" + classes.get(i) + "");
                            classGroup.add(thisChoice);
                            payClassPanel.add(thisChoice);
                        }

                        // amount prompt
                        amountLabel = new JLabel("Transaction Amount:");
                        amountText = new JTextField();

                        // pay button
                        payButton = new JButton("Pay");

                        payButton.addActionListener((ActionEvent e1) -> {
                            double amount = 0.0;
                            int cID = 0;
                            boolean errorFlag = false;

                            // parse cid
                            String selectedClass = classGroup.getSelection().getActionCommand();
                            try {
                                cID = Integer.parseInt(selectedClass);
                            }
                            catch (NumberFormatException nfe) {
                                JOptionPane.showMessageDialog(payClassFrame.getComponent(0), "Please select a class.");
                                amountText.setText("");
                                errorFlag = true;
                            }

                            // parse amount
                            try {
                                amount = Double.parseDouble(amountText.getText());
                                if (amount < 0) throw new NumberFormatException();
                            }
                            catch (NullPointerException npe) {
                                JOptionPane.showMessageDialog(payClassFrame.getComponent(0), "Please enter an amount.");
                                amountText.setText("");
                                errorFlag = true;
                            }
                            catch (NumberFormatException nfe) {
                                JOptionPane.showMessageDialog(payClassFrame.getComponent(0), "Please enter a valid amount.");
                                amountText.setText("");
                                errorFlag = true;
                            }

                            // make purchase if values parsed without error
                            if (!errorFlag) {
                                try {
                                    Class.forName("org.postgresql.Driver");
                                    Connection innerConn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
                                    Statement stmt2 = innerConn.createStatement();
                                    ResultSet rs2;
                                    int transNum;

                                    rs2 = stmt2.executeQuery("SELECT mid FROM member WHERE mname = '"+member+"';");
                                    int mID2 = (rs2.next()) ? rs2.getInt(1) : -1;

                                    // get transaction number as max number + 1
                                    rs2 = stmt2.executeQuery("SELECT MAX(pTransNum) + 1 FROM payment;");
                                    transNum = (rs2.next()) ? rs2.getInt(1) : 0;

                                    // make the purchase
                                    stmt2.executeUpdate("INSERT INTO Payment VALUES ("+transNum+", 'Sign Up', "+amount+", "+mID2+", "+cID+", NULL, NULL, NULL);");

                                    stmt2.close();
                                    innerConn.close();
                                    JOptionPane.showMessageDialog(payClassFrame.getComponent(0), "Payment was processed!");
                                    payClassFrame.dispatchEvent(new WindowEvent(payClassFrame, WindowEvent.WINDOW_CLOSING));
                                }
                                catch (Exception ee) { //caught exception
                                    JOptionPane.showMessageDialog(payClassFrame.getComponent(0), "Error encountered. Aborting...");
                                    ee.printStackTrace();
                                    payClassFrame.dispatchEvent(new WindowEvent(payClassFrame, WindowEvent.WINDOW_CLOSING));
                                }
                            }
                        });
                        payClassFrame.getRootPane().setDefaultButton(payButton);
                        payClassPanel.add(amountLabel);
                        payClassPanel.add(amountText);
                        payClassPanel.add(payButton);
                        payClassFrame.add(payClassPanel);
                        payClassFrame.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosing(WindowEvent evt) {
                                payClassFrame.dispose();
                            }
                        });
                        payClassFrame.pack();
                        payClassFrame.setResizable(false);
                        payClassFrame.setLocationRelativeTo(null);
                        payClassFrame.setVisible(true);
                        break;

                    case "1": //Buy
                        // get for sale equipment data (i.e. equipment that is not in the buys table)
                        sqlString =
                            "SELECT eqType, COUNT(*) AS stock\n"+
                            "FROM (SELECT * FROM equipment WHERE (eqNum,eqType) NOT IN (SELECT eqNum,eqType FROM buys)) E\n"+
                            "GROUP BY eqType;";
                        rs = stmt.executeQuery(sqlString);

                        // parse attributes into array lists
                        ArrayList<String> eqName = new ArrayList<>();
                        ArrayList<Integer> eqStock = new ArrayList<>();
                        while (rs.next()) {
                            eqName.add(rs.getString("eqtype"));
                            eqStock.add(rs.getInt("stock"));
                        }

                        // break with pop-up if equipment out of stock
                        if (eqName.size() == 0) {
                            JOptionPane.showMessageDialog(purchaseFrame.getComponent(0), "Equipment out of stock.");
                            memberText.setText("");
                            break;
                        }

                        // build buy equipment window
                        JFrame buyEquipmentFrame = new JFrame();
                        JPanel buyEquipmentPanel = new JPanel();
                        ButtonGroup equipmentGroup = new ButtonGroup();
                        buyEquipmentPanel.setLayout(new BoxLayout(buyEquipmentPanel, BoxLayout.Y_AXIS));

                        // list classes to pay for as radio buttons
                        for (int i = 0; i < eqName.size(); i++) {
                            JRadioButton thisEquipment = new JRadioButton(eqStock.get(i) + " x " + eqName.get(i));
                            thisEquipment.setActionCommand(eqName.get(i));
                            equipmentGroup.add(thisEquipment);
                            buyEquipmentPanel.add(thisEquipment);
                        }

                        // amount prompt
                        amountLabel = new JLabel("Transaction Amount:");
                        amountText = new JTextField();

                        // pay button
                        payButton = new JButton("Pay");

                        payButton.addActionListener((ActionEvent e1) -> {
                            double amount = 0.0;
                            boolean errorFlag = false;
                            String eqType = equipmentGroup.getSelection().getActionCommand();

                            // parse amount
                            try {
                                amount = Double.parseDouble(amountText.getText());
                                if (amount < 0) throw new NumberFormatException();
                            }
                            catch (NullPointerException npe) {
                                JOptionPane.showMessageDialog(buyEquipmentFrame.getComponent(0), "Please enter an amount.");
                                amountText.setText("");
                                errorFlag = true;
                            }
                            catch (NumberFormatException nfe) {
                                JOptionPane.showMessageDialog(buyEquipmentFrame.getComponent(0), "Please enter a valid amount.");
                                amountText.setText("");
                                errorFlag = true;
                            }

                            // make purchase if values parsed without error
                            if (!errorFlag) {
                                try {
                                    Class.forName("org.postgresql.Driver");
                                    Connection innerConn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
                                    Statement stmt2 = innerConn.createStatement();
                                    ResultSet rs2;
                                    String sqlString2;
                                    int eqNum;
                                    int transNum;

                                    rs2 = stmt2.executeQuery("SELECT mid FROM member WHERE mname = '"+member+"';");
                                    int mID2 = (rs2.next()) ? rs2.getInt(1) : -1;

                                    // get equipment number to buy
                                    sqlString2 = "SELECT MIN(eqnum)\n" +
                                            "FROM (SELECT * FROM equipment WHERE (eqNum,eqType) NOT IN (SELECT eqNum,eqType FROM buys)) E\n" +
                                            "WHERE eqtype = '"+eqType+"'\n" +
                                            "GROUP BY eqtype;";
                                    rs2 = stmt2.executeQuery(sqlString2);
                                    eqNum = (rs2.next()) ? rs2.getInt(1) : -1;
                                    if (eqNum < 0) throw new SQLException("Negative eqNum");

                                    // get transaction number as max number + 1
                                    rs2 = stmt2.executeQuery("SELECT MAX(pTransNum) + 1 FROM payment;");
                                    transNum = (rs2.next()) ? rs2.getInt(1) : 0;

                                    // make the purchase
                                    stmt2.executeUpdate("INSERT INTO Buys VALUES ("+mID2+", "+eqNum+", '"+eqType+"');");
                                    stmt2.executeUpdate("INSERT INTO Payment VALUES ("+transNum+", 'Buy', "+amount+", "+mID2+", NULL, NULL, "+eqNum+", '"+eqType+"');");

                                    stmt2.close();
                                    innerConn.close();
                                    JOptionPane.showMessageDialog(buyEquipmentFrame.getComponent(0), "Payment was processed!");
                                    buyEquipmentFrame.dispatchEvent(new WindowEvent(buyEquipmentFrame, WindowEvent.WINDOW_CLOSING));
                                }
                                catch (Exception ee) { //caught exception
                                    JOptionPane.showMessageDialog(buyEquipmentFrame.getComponent(0), "Error encountered. Aborting...");
                                    ee.printStackTrace();
                                    buyEquipmentFrame.dispatchEvent(new WindowEvent(buyEquipmentFrame, WindowEvent.WINDOW_CLOSING));
                                }
                            }
                        });
                        buyEquipmentFrame.getRootPane().setDefaultButton(payButton);
                        buyEquipmentPanel.add(amountLabel);
                        buyEquipmentPanel.add(amountText);
                        buyEquipmentPanel.add(payButton);
                        buyEquipmentFrame.add(buyEquipmentPanel);
                        buyEquipmentFrame.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosing(WindowEvent evt) {
                                buyEquipmentFrame.dispose();
                            }
                        });
                        buyEquipmentFrame.pack();
                        buyEquipmentFrame.setResizable(false);
                        buyEquipmentFrame.setLocationRelativeTo(null);
                        buyEquipmentFrame.setVisible(true);
                        break;

                    default:
                        JOptionPane.showMessageDialog(purchaseFrame.getComponent(0), "Please complete all fields.");
                        memberText.setText("");
                        break;
                }
                rs.close();
                stmt.close();
                conn.close();
			}
			catch(Exception e1){
				JOptionPane.showMessageDialog(purchaseFrame.getComponent(0), "Error encountered. Aborting...");
				e1.printStackTrace();
				purchaseFrame.dispatchEvent(new WindowEvent(purchaseFrame, WindowEvent.WINDOW_CLOSING));
			}
			memberText.setText("");
		});
		purchasePanel.add(classChoice);
		purchasePanel.add(buyChoice);
		//purchasePanel.add(rentChoice);
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
	
	/**
	 * ALTERNATIVE 2
	 * View when a facility cannot be accessed, due to a class being given during certain times.
	 */
    private void viewBookings(){
        // build room list window
        JFrame roomsFrame = new JFrame();
        JPanel roomsPanel = new JPanel();
        roomsFrame.setTitle("View Room Bookings");
        roomsPanel.setLayout(new BoxLayout(roomsPanel, BoxLayout.Y_AXIS));
        ButtonGroup roomGroup = new ButtonGroup();
        JButton viewButton = new JButton("View Bookings");

        try {
            // connect to database
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
            Statement stmt = conn.createStatement();
            String sqlString;
            ResultSet rs;

            // get list of rooms (type and num)
            sqlString = "SELECT fRoomType, fRoomNum FROM Facility;";
            rs = stmt.executeQuery(sqlString);

            ArrayList<String> roomTypes = new ArrayList<>();
            ArrayList<Integer> roomNums = new ArrayList<>();

            // make room radio buttons
            for (int i = 0;rs.next(); i++) {
                roomTypes.add(rs.getString("fRoomType"));
                roomNums.add(rs.getInt("fRoomNum"));
                JRadioButton thisRoom = new JRadioButton(roomNums.get(i) + ": " + roomTypes.get(i));
                thisRoom.setActionCommand(Integer.toString(i));
                roomGroup.add(thisRoom);
                roomsPanel.add(thisRoom);
            }

            // make view button
            viewButton.addActionListener((ActionEvent e) -> {
                // build display window
                JFrame dispFrame = new JFrame();
                JPanel dispPanel = new JPanel();
                dispFrame.setTitle("Their classes");
                dispPanel.setLayout(new BoxLayout(dispPanel, BoxLayout.Y_AXIS));

                try {
                    // connect to database
                    Class.forName("org.postgresql.Driver");
                    Connection innerConn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
                    Statement innerStmt = innerConn.createStatement();
                    String sqlString2;
                    ResultSet rs2;

                    // get room selection
                    int selectedRoomIndex = -1;
                    try {
                        String temp = roomGroup.getSelection().getActionCommand();
                        selectedRoomIndex = Integer.parseInt(temp);
                    }
                    catch (Exception eee) {
                        JOptionPane.showMessageDialog(dispFrame.getComponent(0), "Please select a room.");
                        dispFrame.dispatchEvent(new WindowEvent(dispFrame, WindowEvent.WINDOW_CLOSING));
                        innerStmt.close();
                        innerConn.close();
                        return;
                    }

                    String roomType = roomTypes.get(selectedRoomIndex);
                    int roomNum = roomNums.get(selectedRoomIndex);

                    sqlString2 =
                            "SELECT TimeSlot, 'Rented' as Activity FROM rents WHERE fRoomNum = "+roomNum+"\n"+
                            "UNION\n"+
                            "SELECT cTime as TimeSlot, cName as Activity FROM class WHERE fRoomNum = "+roomNum+"\n"+
                            "ORDER BY TimeSlot;";

                    rs2 = innerStmt.executeQuery(sqlString2);

                    // print out room bookings
                    int j;
                    for(j = 0; rs2.next(); j++){
                        JLabel thisLabel = new JLabel(rs2.getString("Activity") + " at " + rs2.getString("TimeSlot"));
                        dispPanel.add(thisLabel);
                    }
                    if (j == 0) {
                        JLabel thisLabel = new JLabel("No bookings.");
                        dispPanel.add(thisLabel);
                    }

                    innerStmt.close();
                    innerConn.close();
                }
                catch (Exception ee) {
                    JOptionPane.showMessageDialog(dispFrame.getComponent(0), "Error encountered. Aborting...");
                    ee.printStackTrace();
                    dispFrame.dispatchEvent(new WindowEvent(dispFrame, WindowEvent.WINDOW_CLOSING));
                }

                JButton dispButton = new JButton("Close");
                dispButton.addActionListener((ActionEvent ee) -> dispFrame.dispose());
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
            });
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(roomsFrame.getComponent(0), "Error encountered. Aborting...");
            e.printStackTrace();
            roomsFrame.dispatchEvent(new WindowEvent(roomsFrame, WindowEvent.WINDOW_CLOSING));
        }

        roomsFrame.getRootPane().setDefaultButton(viewButton);
        roomsPanel.add(viewButton);
        roomsFrame.add(roomsPanel);
        roomsFrame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent evt){
                roomsFrame.dispose();
            }
        });
        roomsFrame.pack();
        roomsFrame.setResizable(false);
        roomsFrame.setLocationRelativeTo(null);
        roomsFrame.setVisible(true);
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
	 * Sign up a new member to the complex. Assign him the next highest available mid
	 */
	private void signUpMember(){
		JFrame signupFrame = new JFrame();
		JPanel signupPanel = new JPanel();
		signupFrame.setTitle("Sign up");
		signupPanel.setLayout(new BoxLayout(signupPanel, BoxLayout.Y_AXIS));
		JLabel memberName = new JLabel("Name:");
		JTextField memberTextField = new JTextField();
		JLabel address = new JLabel("Address:");
		JTextField addressTextField = new JTextField();
		JLabel phone = new JLabel("Phone Number:");
		JTextField phoneTextField = new JTextField();
		JButton signupButton = new JButton("Allow Sign-up");
		signupButton.addActionListener((ActionEvent e) -> {
			String member = memberTextField.getText();
			String addresss = addressTextField.getText();
			int phoneNum = -1;
			try{
				phoneNum = Integer.parseInt(phoneTextField.getText());
			}
			catch(Exception e1){
				JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Please enter a number in the phone number field.");
				signupFrame.dispose();
				return;
			}
			if(member.equals("") || addresss.equals("")){
				JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Please enter a name and address.");
			}
			else if(phoneNum < 1000000 || phoneNum > 10000000){
				JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Please enter a 7-digit phone number.");
			}
			else{
				try{
					Class.forName("org.postgresql.Driver");
					Connection conn = DriverManager.getConnection("jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421", "cs421g03", "2<group<4");
					Statement stmt = conn.createStatement();
					String sqlString = "SELECT MAX(mid) AS max "
							+ "FROM member;";
					ResultSet rs = stmt.executeQuery(sqlString);
					int cid = -2;
					while(rs.next()){
						cid = rs.getInt("max");
					}
					cid = cid + 1;
					sqlString = "INSERT INTO member VALUES ("
							+ cid + ", '" + member + "', " + phoneNum + ", '" + addresss + "');";
					
					stmt.executeUpdate(sqlString);
	        		stmt.close();
	        		conn.close();
	        		JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Member Registered Successfully");
	        		signupFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(signupFrame.getComponent(0), "Error encountered. Aborting...");
					e1.printStackTrace();
	        		signupFrame.dispatchEvent(new WindowEvent(signupFrame, WindowEvent.WINDOW_CLOSING));
				}
			}
			memberTextField.setText("");
			addressTextField.setText("");
			phoneTextField.setText("");
		});
		signupFrame.getRootPane().setDefaultButton(signupButton);
		signupPanel.add(memberName);
		signupPanel.add(memberTextField);
		signupPanel.add(address);
		signupPanel.add(addressTextField);
		signupPanel.add(phone);
		signupPanel.add(phoneTextField);
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
	
	/**
	 * ALTERNATIVE 5
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
	 * ALTERNATIVE 6
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