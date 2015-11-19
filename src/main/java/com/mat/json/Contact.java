package com.mat.json;

public class Contact {
    String displayedName;
	String email;
	public Contact(String displayedName, String email) {
		super();
		this.displayedName = displayedName;
		this.email = email;
	}
	public String getDisplayedName() {
		return displayedName;
	}
	public void setDisplayedName(String displayedName) {
		this.displayedName = displayedName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Contact [displayedName=" + displayedName + ", email=" + email + "]";
	}

}
