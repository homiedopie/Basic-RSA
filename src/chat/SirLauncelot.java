package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class SirLauncelot {

	private Concorde concorde;
	private Bow bow;

	public SirLauncelot(Concorde concorde, Bow bow) {
		this.concorde = concorde;
		this.bow = bow;
	}

	/**
	 * Main chat loop for user input.
	 */
	public void chat() {
		// Get username
		System.out.println("Enter a username:");
		System.out.print(">");
		// Get chat message
		try(BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))){
			String fromUser = stdIn.readLine();
			
			bow.setUserName(fromUser);
			
			System.out.println("You may now enter messages to send.");
			System.out.print(">");
			
			//Loops continuously for proper chat interaction.
			while((fromUser = stdIn.readLine()) != null){
				
				if(fromUser.equals("elderberries"))
				{
					if(bow.getSocket() != null || !bow.getSocket().isClosed())
						bow.getSocket().close();
					break;
				}
				
				//In case a bad hostname is entered, we can just try to get another hostname.
				//This gives the user a new chance as opposed to just exiting.
				while(bow.getChatState() == ChatState.NOT_CONNECTED){
					System.out.println("Please enter a host name to connect to.");
					System.out.print(">");
					
					//If we connect properly, this should no longer loop.
					connect(stdIn.readLine());
				}
				
				//We are going to block until the exchange is done.
				//We'll let Concorde handle exchanging keys for consistency.
				while(bow.getChatState() == ChatState.EXCHANGING);
				
				//Finally, we send out message and loop back around.
				bow.shootArrow(fromUser);
				System.out.print(">");
			}
		}catch(IOException ex){
			System.out.println("Error reading input.");
			concorde.stopReceivingArrows();
			System.exit(1);
		}
	}

	/**
	 * Connects to a specified host on a predetermined port(6969);
	 * @param hostName IP of the other person.
	 */
	private void connect(String hostName) {
		int portNumber = bow.getPort();
		
		//Create a socket and connect.
		try{
			Socket conSocket = new Socket(hostName, portNumber);
			bow.setSocket(conSocket);
			bow.generateAndSendKey();
			concorde.stopAcceptingNewConnections();
			concorde.receiveArrows();
		}catch(UnknownHostException ex){
			System.out.println("Could not find host.");
		}catch(IOException ex){
			System.out.println("Could not connect to host.");
		}
	}
}
