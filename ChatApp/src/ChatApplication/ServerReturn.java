package ChatApplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Aaron Ward - Brian Kelly - Daire Homan
 */
public class ServerReturn implements Runnable {
	
	private Socket connection;
	private PrintWriter printwriter;
	
	
	ServerReturn(Socket connection){
		this.connection = connection;
	}
	
	
	public void run() {
		try {
			@SuppressWarnings("resource")
			Scanner input = new Scanner(connection.getInputStream());
			printwriter = new PrintWriter(connection.getOutputStream());
			
			while(true){
				checkConnectionStatus();
				
				if(!input.hasNext()){
					return;
				}
				
				String message = input.nextLine();
				
				System.out.println("User said Message "+message);
				
				for(int counter  = 1; counter<=AppServer.connections.size(); counter++){
					
					//stores each socket in the connectons list array
					Socket tempSocket  = AppServer.connections.get(counter-1);
					
					//figure out what this does
					printwriter  = new PrintWriter(tempSocket.getOutputStream());
					
					//Sends the users list array  to each connected user ****Learn about this****
					printwriter.println(message);
					
					//figure it out
					printwriter.flush();
					
					System.out.println("This was sent to"+connection.getLocalAddress().getHostName());
					
				}
				
				
					connection.close();
			}
		} 
		
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void checkConnectionStatus() throws IOException{
		
		//checks against a bool
		if(!connection.isConnected()){
			for(int counter = 1; counter<=AppServer.connections.size(); counter++){
				if(AppServer.connections.get(counter)==connection){
					
					AppServer.connections.remove(counter);
				}
			}
			
			for(int counter  = 1; counter<=AppServer.connections.size(); counter++){
				
				//stores each socket in the connectons list array
				Socket tempSocket  = AppServer.connections.get(counter-1);
				
				//figure out what this does
				printwriter  = new PrintWriter(tempSocket.getOutputStream());
				
				//Sends the users that a user has left the chat ****Learn about this****
				printwriter.println(tempSocket.getLocalAddress().getHostName()+" Has Disconnected");
				
				//figure out what this does
				printwriter.flush();
				
				//Server message 
				System.out.println(tempSocket.getLocalAddress().getHostName()+" Has Disconnected");
			}
		}
	}
}
