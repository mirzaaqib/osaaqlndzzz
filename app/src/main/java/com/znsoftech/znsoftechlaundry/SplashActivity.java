package com.znsoftech.znsoftechlaundry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class SplashActivity extends Activity {


   protected boolean _active = true;

   SharedPreferences prefs;
   ArrayList<HashMap<String, String>> countResult = new ArrayList<HashMap<String, String>>();

   @Override
   public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_splash);
	  TextView view = (TextView) findViewById(R.id.splash_text);
	  view.setShadowLayer(40, 0, 0, Color.parseColor("#ff208da9"));
	  prefs = this.getSharedPreferences("Settings",
			  Context.MODE_PRIVATE);
	  Config.email = prefs.getString("email", "");
	  Config.mobile = prefs.getString("mobile", "");
	  Config.password = prefs.getString("password", "");
	  Config.name = prefs.getString("name", "");
	  Config.userid = prefs.getString("userid", "0");
	  Config.latitude = prefs.getString("Latitude", "78");
	  Config.longitude = prefs.getString("Longitude", "28");


/**
 * Async task anonymous class invocation for better handelation
 * @author Sandeep Rana
 * @date 1 Aug 2015
 */
	  new AsyncTask<Void, Void, Void>() {

		 @Override
		 protected Void doInBackground(Void... params) {
			Thread.currentThread();  // to use async thread
			try {
			   Thread.sleep(1000);
			} catch (InterruptedException e) {
			   e.printStackTrace();
			}
			return null;
		 }

		 @Override
		 protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
//                Intent intent = new Intent(SplashActivity.this, BaseFragmentActivity.class);
//
//                startActivity(intent);

			startActivity(new Intent(SplashActivity.this,
					LoginActivity.class));
			overridePendingTransition(R.anim.right_in, R.anim.left_out);


		 }
	  }.execute();


   }

}