package chat;

public class Concorde {

	private Bow bow;

	public Concorde(Bow bow) {
		this.bow = bow;
	}

	public void stopAcceptingNewConnections() {
		/*
		 * call the "close" method on my SocketServer and start the message
		 * receiving thread
		 */
	}
	
	public void receiveArrows() {
		// Grab and decrypt/print incoming messages.
	}

	public void listen() {
		// Listen and accept new connection.
	}

	// Needs a stopListening (shutup) method.
}
