package ChatApplication;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.TitledBorder;


/**
 * @author Aaron Ward - Brian Kelly - Daire Homan
 * The ClientGUI class is the GUI for the users
 */
@SuppressWarnings("serial")
public class ClientGUI extends JFrame implements ActionListener{
	
	//Global variables needed for updating the GUI on Events
	public static Client client;
	public static String userName = "User Name";
	private JLabel userNameLabel, disconnectLabel;
	public static JTextPane chatBoard;
	public static JList <String> usersList;
	private JTextField userLoginInput, userMessageInput;
	private JButton sendButton,connectButton,disconnectButton;
	private ImageIcon buttonLogo;
	private JPanel middleBottomCenterPanel,middleBottomRightPanel;
	private StringBuilder sb = new StringBuilder();
	private Font fontMain = new Font("Aharoni", Font.BOLD, 13);
	

	public static void main (String [] arg ){
		new ClientGUI();
	}
	
	
	public static void Connect(){
		
		try {
			
			int port = 1200;
			String host = "ChatGroup";
			Socket socket = new Socket(host,port);
			System.out.println("You have connected to : "+host);
			
			Client client = new Client(socket);
			
			PrintWriter printwriter  = new PrintWriter(socket.getOutputStream());
			
			printwriter.print(userName);
			
			printwriter.flush();
			
			Thread session  = new Thread (client);
			
			session.start();
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 *ClientGUI constructor called when an instance of ClientGUI is created
	 */
	public ClientGUI(){
		super.setTitle(userName);
		add(getMainPanel());
		setGUIConfiguration();
	}
	
	/**
	 * The getMainPanel method creates a JPanel with the border layout design.
	 * From this other panels are added to create the overall client GUI.
	 * @return JPanel mainPanel
	 */
	private JPanel getMainPanel() {
		
		//Create a JPanel called mainPanel with the border layout design for the entire GUI layout
		JPanel mainPanel = new JPanel (new BorderLayout());
		
		//Setting the background color of the mainPanel to white
		mainPanel.setBackground(Color.WHITE);
		
		//Creating a border for the mainPanel and setting the color to white
		mainPanel.setBorder(BorderFactory.createMatteBorder(2,2,2,2,(Color.WHITE)));
		
		/**
		 * Adding the other panels to the mainPanel to create the look 
		 * and feel of the entire GUI
		 */
		mainPanel.add(getTopPanel(),BorderLayout.NORTH);
		mainPanel.add(getMiddlePanel(),BorderLayout.CENTER);
		mainPanel.add(getBottomPanel(),BorderLayout.SOUTH);
		
		//return the fully built mainPanel
		return  mainPanel;
	}
	
	
	/**
	 * The getTopPanel method creates a simple JPanel with a logo.
	 * This JPanel will be added to the top of the client GUI.
	 * @return JPanel topPanel
	 */
	private JPanel getTopPanel(){
		
		//Create a JPanel called topPanel
		JPanel topPanel = new JPanel ();
		
		//Setting the size, color and look of the topPanel 
		topPanel.setPreferredSize(new Dimension(0,109));
		topPanel.setBackground(new Color(13, 101,253));
		topPanel.setBorder(BorderFactory.createMatteBorder(2,2,2,2,(new Color(224,3,69))));
		
		//create and ImageIcon name iconlogo and a JLabel called logo to create and set the logo of the GUI 
		ImageIcon iconLogo = new ImageIcon("images/logo.jpg");
		JLabel logo  = new JLabel(iconLogo);
		topPanel.add(logo);
		
		//return the fully built topPanel
		return topPanel;
	}
	
	
	/**
	 * The getMiddlePanel method creates a number of JPanels and components.
	 * This method create most of the client GUI and updates the client with messages
	 * from other users and shows who is currently connected in the  chat application.
	 * This will be added to the middle of the GUI
	 * @return JPanel middlePanel
	 */
	private JPanel getMiddlePanel(){
		
		//creating JPanels needed for the middle section of the GUI
		JPanel middlePanel = new JPanel (new BorderLayout()),middleBottomPanel = new JPanel(new BorderLayout()),
		middleLeftPanel = new JPanel (),middleRightPanel = new JPanel ();
		
		middleBottomCenterPanel= new JPanel();
		middleBottomRightPanel= new JPanel();
		
		//setting all of the background colors of the JPanels to white 
		middlePanel.setBackground(Color.WHITE);
		middleBottomPanel.setBackground(Color.WHITE);
		middleLeftPanel.setBackground(Color.WHITE);
		middleRightPanel.setBackground(Color.WHITE);
		middleBottomCenterPanel.setBackground(Color.WHITE);
		middleBottomRightPanel.setBackground(Color.WHITE);
		
		//setting a border a round the middle panel with a color of white
		middlePanel.setBorder(BorderFactory.createMatteBorder(2,2,2,2,(new Color(255,255,255))));
		middleBottomPanel.setBorder(BorderFactory.createMatteBorder(0,0,-4,0,(Color.WHITE)));
		
		//setting the dimension of the panels that will be added to the middle panel
		middleBottomPanel.setPreferredSize(new Dimension(0,50));
		middleLeftPanel.setPreferredSize(new Dimension(250,0));
		middleRightPanel.setPreferredSize(new Dimension(200,0));

		//adding panels to the middle panel in the bottom, left and right position
		middlePanel.add(middleBottomPanel,BorderLayout.NORTH);
		middlePanel.add(middleLeftPanel,BorderLayout.WEST);
		middlePanel.add(middleRightPanel ,BorderLayout.EAST);
		
		userNameLabel = new JLabel("User Name");
		setLabelLook(userNameLabel);
		
		disconnectLabel = new JLabel("Disconnect");
		setLabelLook(disconnectLabel);
		
		userLoginInput = new JTextField(10);
		setTextFieldLook(userLoginInput);
		
		connectButton = new JButton();
		setButtonLook(connectButton);
		
		disconnectButton = new JButton();
		setButtonLook(disconnectButton);
		

		middleBottomCenterPanel.add(userNameLabel);
		middleBottomCenterPanel.add(userLoginInput);
		middleBottomCenterPanel.add(connectButton);
		
		middleBottomRightPanel.add(disconnectLabel);
		middleBottomRightPanel.add(disconnectButton);
		
		middleBottomRightPanel.setVisible(false);
		
		middleBottomPanel.add(middleBottomCenterPanel,BorderLayout.CENTER);
		middleBottomPanel.add(middleBottomRightPanel,BorderLayout.NORTH);
		
		//creating title borders to add to components later
		TitledBorder messageBorder = new TitledBorder("Messages"), userBorder = new TitledBorder("Users");
		
		//calling method to set up the look of the title borders
		setBorderTitleLook(messageBorder);
		setBorderTitleLook(userBorder);
	
		/**
		 * Create a JTextPane called chatBoard, this is where the chat between clients will be displayed.
		 * The chatBoards content type is text/html to allow editing to the text within it later
		 * It cannot be edited by the users.
		 */
		chatBoard = new JTextPane();
		chatBoard.setContentType("text/html");
		chatBoard.setEditable(false);
		chatBoard.setPreferredSize(new Dimension(230,250));
		
		/**
		 * Create a JScrollPane called chatScrool which chatBoard will be added
		 * This allows the user to scrool up and down to see earlier chats.
		 */
		JScrollPane chatScrool= new JScrollPane (chatBoard);
		chatScrool.setBackground(Color.WHITE);
		chatScrool.setBorder(messageBorder);
		middleLeftPanel.add(chatScrool);
		
		//Cretae a JList for the list of users to be displayed
		usersList = new JList<String>();
		
		/**
		 * create another JScrollPane called userScrool which userList will be added
		 * This allows a user to scrool up and down to see a list of current users in the chat
		 */
		JScrollPane userScrool = new JScrollPane (usersList);
		userScrool.setBackground(Color.WHITE);
		userScrool.setPreferredSize(new Dimension(190,275));
		userScrool.setBorder(userBorder);
		middleRightPanel.add(userScrool);
		
		//return the fully built middlePanel
		return  middlePanel;
	}
	
	
	/**
	 * The getBottomPanel method createss a simple JPanel and components.
	 * This method creates the components that allow the client to chat to other users
	 * This will be added to the bottom of the GUI
	 * @return JPanel pottomPanel
	 */
	private JPanel getBottomPanel(){
		
		//Creating a JPanel bottomPanel and setting its size and look
		JPanel bottomPanel = new JPanel ();
		bottomPanel.setPreferredSize(new Dimension(0,50));
		bottomPanel.setBackground(new Color(13, 101,253));
		bottomPanel.setBorder(BorderFactory.createMatteBorder(2,2,2,2,(new Color(224,3,69))));
		
		/**
		 * Create a JTextField named userInput and pass it to a method called setJTextFieldLook to set its look
		 * This is where the user will enter their message they want to send
		 */
		userMessageInput = new JTextField(20);
		setTextFieldLook(userMessageInput);
		
		/**
		 * Create a JButton called sendButton and passe it to a method called setButtonLook to set its look
		 * This is the button the user will press to send their message
		 */
		sendButton = new JButton();
		setButtonLook(sendButton);
		
		//adding components to the bottom panel
		bottomPanel.add(userMessageInput);
		bottomPanel.add(sendButton);
		
		//return fully built bottomPanel
		return bottomPanel;
	}

	
	/**
	 * The setGUIConfiguration method sets the configuration of the GUI
	 * The size, visible, resizable and location are all set here
	 */
	private void setGUIConfiguration(){
		setSize(500,530);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	/**
	 * The setLabelLook method sets the look of any JLabel passed to it
	 * @param JLabel label
	 */
	private void setLabelLook(JLabel label){
		label.setForeground(new Color(224,3,69));
	}
	
	
	/**
	 * The setButtonLook method sets the look of any JButton passed to this method.
	 * It also adds action listeners.
	 * @param JButton button
	 */
	private void setButtonLook(JButton button){
		if(button==sendButton){
			buttonLogo = new ImageIcon("images/sendButton.png");
		}
		
		if(button==connectButton){
			buttonLogo = new ImageIcon("images/connectButton.png");
		}
		
		if(button==disconnectButton){
			buttonLogo = new ImageIcon("images/disconnectButton.png");
		}
		button.setIcon(buttonLogo);
		button.setBackground(new Color(224,3,69));
		button.setBorderPainted(false);
		button.setFocusable(false);
		button.addActionListener(this);
	}
	
	
	/**
	 * The setJTextFieldLook method sets the look of any JTextField passed to it
	 * @param JTextField Field
	 */
	private void setTextFieldLook(JTextField field){
		field.setBorder(BorderFactory.createMatteBorder(2,2,2,2,(new Color(91,179,49))));
		field.setForeground(new Color(224,3,69));
		field.setFont(fontMain);
	}
	
	
	/**
	 * The setBorderTitleLook method sets the look of any TitledBorder passed to it
	 * @param takes in a TitledBorder title
	 */
	private void setBorderTitleLook(TitledBorder title){
		title.setBorder(BorderFactory.createMatteBorder(2,2,2,2,(new Color(224,3,69))));
		title.setTitleColor(new Color(91,179,49));
		title.setTitleJustification(TitledBorder.CENTER);
		title.setTitlePosition(TitledBorder.TOP);
	}

	
	/**
	 * This actionPerformed method, performs events and updates the GUI 
	 * Once a component is pressed or a action occurs
	 */
	public void actionPerformed(ActionEvent sendEvent) {
		 
		 if(sendEvent.getSource()==connectButton && !userLoginInput.getText().equals("")){
			 userName = userLoginInput.getText();
			 super.setTitle(userName+" Chat Window");
			 userLoginInput.setEditable(false);
			 middleBottomCenterPanel.setVisible(false);
			 middleBottomRightPanel.setVisible(true);
			 Connect();
			 AppServer.users.add(userName);
		 }
		 
		 else if(sendEvent.getSource()==connectButton ||sendEvent.getSource()==sendButton && userLoginInput.getText().equals("")) {
			 JOptionPane.showMessageDialog(null, "Please Enter a user name before you connect");
		 }
		 
		 if(sendEvent.getSource()==sendButton && !userMessageInput.getText().equals("")){
			 sb.append("Me "+ userMessageInput.getText()+"<br>");
			 chatBoard.setText(sb.toString());
			 userMessageInput.setText("");
			 userMessageInput.requestFocus();
			 client.sendMessage(userMessageInput.getText().toString());
		 }
		 
		 else if (sendEvent.getSource()==sendButton && userMessageInput.getText().equals("")){
			 JOptionPane.showMessageDialog(null, "Please Enter a Message");
		 }
		 
		 if(sendEvent.getSource()==disconnectButton){
			 try{
				 client.Disconnect();
			 }
			 
			 catch(Exception error){
				 error.printStackTrace();
			 }
		 }
	}
}