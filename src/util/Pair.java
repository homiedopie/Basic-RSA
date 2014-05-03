package util;

import java.io.Serializable;

public class Pair<A, B> implements Serializable {
	/**
	 * Auto generated serial version uid 
	 */
	private static final long serialVersionUID = 5911758685174453289L;
	
	private A first;
	private B second;
	
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}
	
	public A getFirst() {
		return first;
	}
	
	public B getSecond() {
		return second;
	}
	
	public void setFirst(A first) {
		this.first = first;
	}
	
	public void setSecond(B second) {
		this.second = second;
	}
}
