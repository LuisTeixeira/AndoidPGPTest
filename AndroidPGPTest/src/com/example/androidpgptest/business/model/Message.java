package com.example.androidpgptest.business.model;

public interface Message {

	public abstract void addRecipient(User recipient);

	public abstract void removeRecipient(User recipient);

	public abstract User getSender();

	public abstract void setSender(User sender);

	public abstract String getContent();

	public abstract void setContent(String content);

}