package com.example.androidpgptest.business.model;

import java.util.ArrayList;
import java.util.List;

public class BasicMessage implements Message {
	
	private User sender;
	private List<User> recipients;
	private String content;
	
	public BasicMessage(User sender) {		
		this.setSender(sender);
		recipients = new ArrayList<User>();				
	}
	
	/* (non-Javadoc)
	 * @see com.example.androidpgptest.business.model.Message#addRecipient(com.example.androidpgptest.business.model.User)
	 */
	@Override
	public void addRecipient(User recipient) {
		recipients.add(recipient);
	}
	
	/* (non-Javadoc)
	 * @see com.example.androidpgptest.business.model.Message#removeRecipient(com.example.androidpgptest.business.model.User)
	 */
	@Override
	public void removeRecipient(User recipient) {
		recipients.remove(recipient);
	}

	/* (non-Javadoc)
	 * @see com.example.androidpgptest.business.model.Message#getSender()
	 */
	@Override
	public User getSender() {
		return sender;
	}

	/* (non-Javadoc)
	 * @see com.example.androidpgptest.business.model.Message#setSender(com.example.androidpgptest.business.model.User)
	 */
	@Override
	public void setSender(User sender) {
		this.sender = sender;
	}

	/* (non-Javadoc)
	 * @see com.example.androidpgptest.business.model.Message#getContent()
	 */
	@Override
	public String getContent() {
		return content;
	}

	/* (non-Javadoc)
	 * @see com.example.androidpgptest.business.model.Message#setContent(java.lang.String)
	 */
	@Override
	public void setContent(String content) {
		this.content = content;
	}
}
