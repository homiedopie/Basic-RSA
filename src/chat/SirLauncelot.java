package chat;

public class SirLauncelot {

	private Concorde concorde;
	private Bow bow;

	public SirLauncelot(Concorde concorde, Bow bow) {
		this.concorde = concorde;
		this.bow = bow;
	}

	public void chat() {
		// Get username
		// Get chat message
		// if not connected...
		// 		Get hostname
		// 		Connect()
		// Send message (bow.shoot(msg)) (shoot encrypts before sending)
		// Loop back to: "Get chat message"
	}

	private void connect(String hostName) {
		// Put connection stuff here.
	}
}
