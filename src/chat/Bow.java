package chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import crypto.RSAKeyPair;

public class Bow {

	private Socket socket = null;
	private ChatState chatState = ChatState.NOT_CONNECTED;
	private String userName;
	private RSAKeyPair keys;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ChatState getChatState() {
		return chatState;
	}

	public void setChatState(ChatState chatState) {
		this.chatState = chatState;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public RSAKeyPair getKeys() {
		return keys;
	}

	public void setKeys(RSAKeyPair keys) {
		this.keys = keys;
	}
	
	/**
	 * Encrypts and sends a message using an existing connection.
	 * @param fromUser the message to be sent.
	 */
	public void shootArrow(String fromUser) {
		//Encrypt string in format: "Username: fromUser".
		BigInteger biEncrypted = crypto.Cryptography.encrypt(userName + ": " + fromUser, keys.getPublicKey());
		
		try(PrintWriter out = new PrintWriter(getSocket().getOutputStream(), true)){
			out.println(biEncrypted.toString());
		}catch(IOException ex){
			System.out.println("Message could not be sent.");
		}
	}

}
