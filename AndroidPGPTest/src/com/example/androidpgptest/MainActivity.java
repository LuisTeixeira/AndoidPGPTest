package com.example.androidpgptest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PrivateKey;

import org.spongycastle.openpgp.PGPPrivateKey;
import org.spongycastle.openpgp.PGPPublicKey;
import org.spongycastle.openpgp.PGPPublicKeyRing;
import org.spongycastle.openpgp.PGPSecretKey;
import org.spongycastle.openpgp.PGPSecretKeyRing;

import com.example.androidpgptest.business.model.EncryptedMessage;
import com.example.androidpgptest.business.model.User;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private EditText decryptedTextET;
	private EditText encryptedTextET;
	private Button encryptButton;
	private Button decryptButton;
	
	private PGPPublicKey publicKey;
	private PGPPrivateKey privateKey;
	
	private EncryptedMessage message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// read keyrings from file
		try {
			PGPPublicKeyRing publicKeyRing = KeyHelper.getPublicKeyring(getAssets().open("publickeyring.gpg"));
			// read a public key from that keyring
			publicKey = KeyHelper.getEncryptionKey(publicKeyRing);
			
			//PGPSecretKeyRing privateKeyRing = KeyHelper.getSecretKeyring(getAssets().open("secretkeyring.gpg"));
			//privateKey = KeyHelper.getDecryptionKey(privateKeyRing);
			
			message = new EncryptedMessage(new User("test@test.com"), publicKey, getAssets().open("secretkeyring.gpg"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		 
		System.out.println("Public Key: " + publicKey);
		System.out.println(" ID: " + publicKey.getKeyID());
		//System.out.println("Private Key: " + privateKey);
		//System.out.println(" ID: " + privateKey.getKeyID());
		
		instantiateLayoutElements();
		registerButtonListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*private methods*/
	
	/*Get the layout elements*/
	private void instantiateLayoutElements(){
		decryptedTextET = (EditText) findViewById(R.id.decryptedEditText);
		encryptedTextET = (EditText) findViewById(R.id.encryptedEditText);
		encryptButton = (Button) findViewById(R.id.encryptButton);
		decryptButton = (Button) findViewById(R.id.decryptButton);
	}
	
	/*Register Listeners*/
	private void registerButtonListeners(){
		encryptButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("ButtonListener", "Button " + v.getId() + " clicked");
								
				message.setContent(decryptedTextET.getText().toString());
				encryptedTextET.setText(message.getEncryptedContent());
				decryptedTextET.setText("");
			}
		});
		
		decryptButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("ButtonListener", "Button " + v.getId() + " clicked");

				decryptedTextET.setText(message.getContent());
				encryptedTextET.setText("");
			}
		});
	}
	
}
