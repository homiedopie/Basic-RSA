package chat;

import java.net.Socket;
import java.security.KeyPair;

public class Bow {

	private static Socket socket = null;
	private static ChatState chatState = ChatState.NOT_CONNECTED;
	private static String userName;
	private static KeyPair keys;
	
	public static Socket getSocket() {
		return socket;
	}
	public static void setSocket(Socket socket) {
		Bow.socket = socket;
	}
	public static ChatState getChatState() {
		return chatState;
	}
	public static void setChatState(ChatState chatState) {
		Bow.chatState = chatState;
	} 

}
