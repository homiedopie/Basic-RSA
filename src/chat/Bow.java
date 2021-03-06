package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketException;

import crypto.Cryptography;
import crypto.Key;
import crypto.RSAKeyPair;

public class Bow {

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
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
		if (socket.isConnected()) {
			try {
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				throw new RuntimeException(
						"Couldn't instantiate streams for the socket.", e);
			}
		}
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
	 * 
	 * @param message
	 *            the message to be sent.
	 */
	public void shootArrow(String message) {
		// Encrypt string in format: "Username: fromUser".
		BigInteger biEncrypted = crypto.Cryptography.encrypt(userName + ": "
				+ message, theirPublicKey);

		out.println(biEncrypted.toString());
	}

	/**
	 * Receives and decrypts a message using an existing connection.
	 * 
	 * @return the decrypted message
	 */
	public String receiveArrow() {
		String message = null;
		try {
			String encryptedMessage = in.readLine();
			if (encryptedMessage != null) {
				BigInteger cipherText = new BigInteger(encryptedMessage);
				message = Cryptography.decrypt(cipherText, ourPrivateKey);
			}
		} catch (IOException e) {
			setChatState(ChatState.NOT_CONNECTED);
			System.out.println("Connection lost.");
		}
		return message;
	}
	
	public void generateAndSendKey() throws IOException {
		// new connection, generate RSA keys and send public key
		setChatState(ChatState.EXCHANGING);

		RSAKeyPair rsaKeys = Cryptography.generateRSAKeys();
		setOurPrivateKey(rsaKeys.getPrivateKey());

		ObjectOutputStream outLine = new ObjectOutputStream(
				socket.getOutputStream());

		outLine.writeObject(rsaKeys.getPublicKey());
	}
}
