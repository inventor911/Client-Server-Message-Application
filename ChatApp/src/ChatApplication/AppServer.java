package ChatApplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * @author Aaron Ward - Brian Kelly - Daire Homan
 * The AppServer class is the server for this application
 */
public class AppServer {
	
	/**
	 * Global Array Lists
	 * Lads we should use ArrayList as they have no fixed size and they can grow and shrink unlike arrays
	 * The connections ArrayList of type Socket stores all the sockets connected to the server
	 * The users ArrayList stores all the current users
	 */
	public static ArrayList <Socket> connections = new  ArrayList <Socket>();
	public static ArrayList <String> users = new  ArrayList <String>();
	
	
	public static void main (String [] args) throws IOException {
		
		try {
			
			// this is the port number the Server is listening on
			int portNumber = 1200;
			
			//creating a new ServerSocket called serverSocket and its going to open and listen on 6000
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(portNumber);
			
			serverSocket.setSoTimeout(100000000);
			
			System.out.println("Waiting for clients on "+portNumber);
			
			
			//create infinite loop while inside the try catch block
			while(true){
				
				/**
				 * Ever time the server accepts a conntection  the returned value of this gets assigned to a Socket called connection. 
				 * In turn the  new socket connection is added to the the array list connections.
				 * This is how we send messages or echo out message to all users using the chat application.
				 */
				Socket connection = serverSocket.accept();
				connections.add(connection);
				
				
				//display some feed back and print to the console a simple message
				System.out.println("A Client has connected from"+ connection.getLocalAddress().getHostName());
				
				//passing connection to addUserNameMethod
				addUserName(connection);
				
				/**
				 * create a ServerReturn Object and in its constructor takes it a socket object.
				 * It is going to be passed every single person that connects that the server socket accepts 
				 */
				ServerReturn chat = new ServerReturn (connection);
				
				Thread session = new Thread(chat);
				
				session.start();
			}
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void addUserName(Socket x)throws IOException {
			
			@SuppressWarnings("resource")
			
			
			/**
			 * A Scanner gets create and gets the input stream from the client who connects with ther name
			 * The Scanner next line is called and is assigned to the string userName
			 */
			Scanner scanner  = new Scanner(x.getInputStream());
			String userName  = scanner.nextLine();
			
			//adding username to the users array list
			users.add(userName);
			
			/**
			 * This for loop gets all the connections in the connections array list
			 * and sends out all of the connected users to all of the connections
			 */
			for(int counter  = 1; counter<=AppServer.connections.size(); counter++){
				
				//stores each socket in the connectons list array
				Socket tempSocket  = (Socket) AppServer.connections.get(counter-1);
				
				//figure out what this does
				PrintWriter printwriter  = new PrintWriter(tempSocket.getOutputStream());
				
				//Sends the users list array  to each connected user ****Learn about this****
				printwriter.println("#?!"+users);
				
				//figure it out
				printwriter.flush();
			}
		
	}
}
