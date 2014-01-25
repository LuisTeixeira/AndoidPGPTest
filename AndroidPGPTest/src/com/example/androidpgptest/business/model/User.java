package com.example.androidpgptest.business.model;

public class User {
	private String address;
	
	public User(String address) {
		this.setAddress(address);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
