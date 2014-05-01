package core;

import chat.Bow;
import chat.BraveSirRobin;
import chat.Patsy;

public class CryptoChat {

	public static void main(String[] args) {
		Bow bow = new Bow();
		Patsy patsy = new Patsy(bow);
		BraveSirRobin braveSirRobin = new BraveSirRobin(patsy, bow);
		// braveSirRobin.chat() // starts the main chat loop
	}

}
