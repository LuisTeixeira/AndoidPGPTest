package com.example.androidpgptest.business.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.NoSuchProviderException;

import org.spongycastle.openpgp.PGPException;
import org.spongycastle.openpgp.PGPPrivateKey;
import org.spongycastle.openpgp.PGPPublicKey;
import org.spongycastle.openpgp.PGPSecretKey;

public class EncryptedMessage extends BasicMessage implements Message {
	
	private PGPPublicKey  publicKey;
	private InputStream  privateKey;

	public EncryptedMessage(User sender, PGPPublicKey publicKey, InputStream privateKey) {
		super(sender);
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	@Override
	public void addRecipient(User recipient) {
		super.addRecipient(recipient);
		//TODO: fetch the recipient key?
	}

	public String getEncryptedContent() {
		
		return super.getContent();
	}
	
	@Override
	public String getContent() {
		ByteArrayInputStream encryptedStream = new ByteArrayInputStream(super.getContent().getBytes());
		ByteArrayOutputStream decryptedStream = new ByteArrayOutputStream();
		DecryptionHelper util;
		
		try {
			util = new DecryptionHelper(encryptedStream, decryptedStream, privateKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return decryptedStream.toString(); 
	}
	
	@Override
	public void setContent(String content) {
		ByteArrayOutputStream encryptedStream = new ByteArrayOutputStream();
		EncryptionHelper util;
		try {
			util = new EncryptionHelper(publicKey, "secrets.txt", encryptedStream);
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
