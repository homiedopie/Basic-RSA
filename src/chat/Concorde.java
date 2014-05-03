package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import crypto.Cryptography;
import crypto.Key;
import crypto.RSAKeyPair;

public class Concorde {

	private Bow bow;
	private ServerSocket serverSocket;
	private ConnectionListener connectionListener;
	private ArrowReceiver arrowReceiver;

	public Concorde(Bow bow) {
		this.bow = bow;
	}

	public void listen() {
		if (serverSocket != null || connectionListener != null) {
			throw new RuntimeException("Concorde cannot be told to listen when"
					+ " he is already listening for new connections.");
		}
		try {
			serverSocket = new ServerSocket(bow.getPort());
			connectionListener = new ConnectionListener(serverSocket, bow, this);
			connectionListener.start();
		} catch (IOException e) {
			throw new RuntimeException(
					"Couldn't open a socket to listen for connections.", e);
		}
	}

	public void stopAcceptingNewConnections() throws IOException {
		if (serverSocket == null || serverSocket.isClosed()) {
			throw new RuntimeException("Concorde cannot be told to stop "
					+ "accepting new connections when he is not listening.");
		}
		connectionListener.askedToStopListening = true;
		serverSocket.close();
		serverSocket = null;
		connectionListener = null;
	}

	public void receiveArrows() {
		// Grab and decrypt/print incoming messages.
		if (arrowReceiver != null && arrowReceiver.isAlive()) {
			throw new RuntimeException("Concorde cannot be told to receive "
					+ "arrows while he's already receiving arrows.");
		}
		arrowReceiver = new ArrowReceiver(bow);
		arrowReceiver.start();
	}

	public void stopReceivingArrows() {
		if (arrowReceiver != null) {
			arrowReceiver.stopReceivingArrows();
			arrowReceiver = null;
		}
	}

	private static class ConnectionListener extends Thread {
		private ServerSocket serverSocket;
		private Bow bow;
		private Concorde concorde;
		private boolean askedToStopListening = false;

		public ConnectionListener(ServerSocket serverSocket, Bow bow,
				Concorde concorde) {
			this.serverSocket = serverSocket;
			this.bow = bow;
			this.concorde = concorde;
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

					concorde.receiveArrows();
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

	private static class ArrowReceiver extends Thread {

		private Bow bow;
		private boolean askedToStop = false;

		public ArrowReceiver(Bow bow) {
			this.bow = bow;
		}

		@Override
		public void run() {
			if (bow.getChatState() == ChatState.EXCHANGING) {
				// try to receive a public key from the other party
				boolean keyReceived = false;
				while (!keyReceived) {
					try {
						keyReceived = receiveKey();
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
						return;
					}
				}
			}
			if (bow.getChatState() == ChatState.CONNECTED) {
				String message;
				while ((message = bow.receiveArrow()) != null && !askedToStop) {
					System.out.println(message);
				}
			}
		}

		private boolean receiveKey() throws IOException, ClassNotFoundException {
			boolean keyReceived = false;
			Socket socket = bow.getSocket();
			if (socket == null || socket.isClosed()) {
				throw new RuntimeException("Can't receive arrows "
						+ "because there is no active connection.");
			}
			ObjectInputStream inLine = new ObjectInputStream(
					socket.getInputStream());
			Object object = inLine.readObject();
			if (object instanceof Key) {
				bow.setTheirPublicKey((Key) object);
				keyReceived = true;
				bow.setChatState(ChatState.CONNECTED);
			} else {
				System.out.println("Received something other than "
						+ "a key while exchanging. Still waiting "
						+ "for a key.");
			}
			return keyReceived;
		}

		public void stopReceivingArrows() {
			askedToStop = true;
		}
	}
}
