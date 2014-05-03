package chat;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import crypto.Cryptography;
import crypto.RSAKeyPair;

public class Concorde {

	private Bow bow;
	private ServerSocket serverSocket;
	private ConnectionListener listener;

	public Concorde(Bow bow) {
		this.bow = bow;
	}

	public void listen() throws IOException {
		if (serverSocket != null || listener != null) {
			throw new RuntimeException("Concorde cannot be told to listen when"
					+ " he is already listening for new connections.");
		}
		serverSocket = new ServerSocket(bow.getPort());
		listener = new ConnectionListener(serverSocket, bow);
		listener.start();
	}

	public void stopAcceptingNewConnections() throws IOException {
		if (serverSocket == null || serverSocket.isClosed()) {
			throw new RuntimeException("Concorde cannot be told to stop "
					+ "accepting new connections when he is not listening.");
		}
		listener.askedToStopListening = true;
		serverSocket.close();
		serverSocket = null;
		listener = null;
	}

	public void receiveArrows() {
		// Grab and decrypt/print incoming messages.
	}

	private static class ConnectionListener extends Thread {
		private ServerSocket serverSocket;
		private Bow bow;
		private boolean askedToStopListening;

		public ConnectionListener(ServerSocket serverSocket, Bow bow) {
			this.serverSocket = serverSocket;
			this.bow = bow;
		}

		@Override
		public void run() {
			try {
				Socket clientSocket = serverSocket.accept();

				Socket bowSocket = bow.getSocket();
				if (bow.getChatState() == ChatState.NOT_CONNECTED
						&& (bowSocket == null || bowSocket.isClosed())) {
					// new connection, generate RSA keys and send public key
					bow.setChatState(ChatState.EXCHANGING);
					bow.setSocket(clientSocket);

					RSAKeyPair rsaKeys = Cryptography.generateRSAKeys();
					bow.setOurPrivateKey(rsaKeys.getPrivateKey());

					ObjectOutputStream outLine = new ObjectOutputStream(
							clientSocket.getOutputStream());

					outLine.writeObject(rsaKeys.getPublicKey());

					outLine.close();
				} else {
					// bow may be holding an active connection
					clientSocket.close();
				}
			} catch (IOException e) {
				if (!askedToStopListening) {
					e.printStackTrace();
				}
			}
		}
	}

}
