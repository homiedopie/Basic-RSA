package chat;

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

}
