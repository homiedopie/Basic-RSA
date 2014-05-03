package chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketImpl;

import crypto.Key;
import crypto.RSAKeyPair;

public class Bow {

	private Socket socket;
	private int port;
	private ChatState chatState;
	private String userName;
	private Key ourPrivateKey;
	private Key theirPublicKey;

	public Bow(int port) {
		this.port = port;
		chatState = ChatState.NOT_CONNECTED;
	}

	public synchronized Socket getSocket() {
		return socket;
	}

	public synchronized void setSocket(Socket socket) {
		this.socket = socket;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public synchronized ChatState getChatState() {
		return chatState;
	}

	public synchronized void setChatState(ChatState chatState) {
		this.chatState = chatState;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setOurPrivateKey(Key ourPrivateKey) {
		this.ourPrivateKey = ourPrivateKey;
	}

	public void setTheirPublicKey(Key theirPublicKey) {
		this.theirPublicKey = theirPublicKey;
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
