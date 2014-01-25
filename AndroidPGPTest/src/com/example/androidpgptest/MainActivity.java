package com.example.androidpgptest;

import java.io.IOException;
import java.io.InputStreamReader;

import org.spongycastle.openpgp.PGPPublicKey;
import org.spongycastle.openpgp.PGPPublicKeyRing;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		// read a public key from a file
		PGPPublicKeyRing keyRing;
		try {
			keyRing = KeyHelper.getKeyring(getAssets().open("publickey.gpg"));
			// read a public key from that keyring
			publicKey = KeyHelper.getEncryptionKey(keyRing);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		System.out.println("Public Key: " + publicKey);
		System.out.println(" ID: " + publicKey.getKeyID());
		
		
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
				
				EncryptedMessage msg = new EncryptedMessage(new User("test@test.com"), publicKey);
				msg.setContent(decryptedTextET.toString());
				encryptedTextET.setText(msg.getContent());
			}
		});
		
		decryptButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("ButtonListener", "Button " + v.getId() + " clicked");
			}
		});
	}
	
}
