package com.example.androidpgptest.business.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchProviderException;

import org.spongycastle.openpgp.PGPException;
import org.spongycastle.openpgp.PGPPublicKey;

public class EncryptedMessage extends BasicMessage implements Message {
	
	private PGPPublicKey  publicKey;

	public EncryptedMessage(User sender, PGPPublicKey publicKey) {
		super(sender);
		this.publicKey = publicKey;
	}
	
	@Override
	public void addRecipient(User recipient) {
		super.addRecipient(recipient);
		//TODO: fetch the recipient key?
	}

	@Override
	public String getContent() {
		
		return super.getContent();
	}
	
	@Override
	public void setContent(String content) {
		ByteArrayOutputStream encryptedStream = new ByteArrayOutputStream();
		CryptoHelper util;
		try {
			util = new CryptoHelper(publicKey, "secrets.txt", encryptedStream);
			PrintWriter pw = new PrintWriter(util.getPayloadOutputStream());
			pw.print(content);
			pw.flush();
			
			util.close();
			
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.setContent(encryptedStream.toString());
	}

	public PGPPublicKey getRecipientKey() {
		return publicKey;
	}

	public void setRecipientKey(PGPPublicKey recipientKey) {
		this.publicKey = recipientKey;
	}

	public PGPPublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PGPPublicKey key) {
		this.publicKey = key;
	}
}
