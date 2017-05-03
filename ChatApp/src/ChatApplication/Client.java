package ChatApplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * @author Aaron Ward - Brian Kelly - Daire Homan
 */
public class Client implements Runnable {
	
	private Socket connection;
	Scanner inputScanner,sendScanner = new Scanner (System.in);
	PrintWriter printWriter;
	
	
	Client(Socket connection){
		
		this.connection = connection;
	}

	
	public void run() {
		
		try {
			
			try {
				
				inputScanner = new Scanner(connection.getInputStream());
				printWriter = new PrintWriter(connection.getOutputStream());
				printWriter.flush();
				checkConnection();
			} 
			
			finally{
				connection.close();
			}
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void Disconnect()throws IOException{
		printWriter.println(AppServer.users+" Has Left The Chat");
		printWriter.flush();
		connection.close();
		JOptionPane.showMessageDialog(null, "You Have Left The Chat");
		System.exit(0);
	}
	
	public void checkConnection(){
		while(true){
			Receive();
		}
	}
	
	public void Receive(){
		if(inputScanner.hasNext()){
			String message = inputScanner.nextLine();
			
			if(message.contains("#?!")){
				String temMessage  = message.substring(3);
				temMessage = temMessage.replace("[", "");
				temMessage = temMessage.replace("]", "");
				
				String [] currentUsers = temMessage.split(" ,");
				ClientGUI.usersList.setListData(currentUsers);
				
			}
			
			else {
				ClientGUI.chatBoard.setText(message);
			}
		}
	}
	
	public void sendMessage(String sendMessage){
		printWriter.println(AppServer.users+": "+sendMessage);
		printWriter.flush();
		ClientGUI.chatBoard.setText(sendMessage);
	}
}
