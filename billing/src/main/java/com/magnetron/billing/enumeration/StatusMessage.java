package com.magnetron.billing.enumeration;

public enum StatusMessage {
	OK("Success!"),
	KO("Error");
	
	private final String message;
	
	private StatusMessage(String message) {
		this.message = message;
	}	
	
	public String getMessage() {
		return this.message;
	}
}
