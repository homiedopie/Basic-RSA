package core;

import util.Pair;

public class RSAKeyPair {

	private Pair<Key, Key> keyPair;
	
	public RSAKeyPair(Key pubkey, Key privkey) {
		keyPair = new Pair<Key, Key>(pubkey, privkey);
	}
	
	public Key getPublicKey() {
		return keyPair.getFirst();
	}
	
	public Key getPrivateKey() {
		return keyPair.getSecond();
	}
}
