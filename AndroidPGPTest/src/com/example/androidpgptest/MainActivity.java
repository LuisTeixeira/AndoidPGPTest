package com.example.androidpgptest;

import android.os.Bundle;
import android.app.Activity;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
