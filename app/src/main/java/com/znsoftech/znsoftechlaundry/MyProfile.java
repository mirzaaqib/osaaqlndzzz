package com.znsoftech.znsoftechlaundry;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.znsoftech.znsoftechlaundry.json.DownloadJsonContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MyProfile extends ActionBarActivity {

   SharedPreferences prefs;
   SharedPreferences.Editor editor;
   String name, userid, email, mobile, phone, password, address, city, country, usertype, latitude, longitude;
   EditText tname, temail, tmobile, taddress, tphone, tpassword, tpassword_confirm;
   Context context;
   Spinner cityspinner;
   ScrollView scrollView;
   private boolean isLoad;
   private EditText tpassword_old;
   private String password_old;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.myprofile);

	  context = this;
	  ActionBar bar = getSupportActionBar();
	  bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
	  bar.setDisplayShowHomeEnabled(false);
	  bar.setDisplayHomeAsUpEnabled(false);
	  bar.setHomeButtonEnabled(false);
	  bar.setTitle("Sign In");
	  bar.hide();

	  prefs = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
	  tname = (EditText) findViewById(R.id.pro_name);
	  temail = (EditText) findViewById(R.id.pro_email);
	  taddress = (EditText) findViewById(R.id.pro_address);
	  tphone = (EditText) findViewById(R.id.pro_phone);
	  tmobile = (EditText) findViewById(R.id.pro_mobile);
	  cityspinner = (Spinner) findViewById(R.id.pro_city);
//	  tcountry = (EditText) findViewById(R.id.pro_country);
	  tpassword = (EditText) findViewById(R.id.pro_password);
	  tpassword_old = (EditText) findViewById(R.id.pro_old_password);
	  tpassword_confirm = (EditText) findViewById(R.id.pro_password_confirm);
	  scrollView = (ScrollView) findViewById(R.id.scroll_password);


	  name = prefs.getString("name", Config.name);
	  userid = prefs.getString("userid", Config.userid);
	  email = prefs.getString("email", Config.email);
	  mobile = prefs.getString("mobile", Config.mobile);
	  phone = prefs.getString("phone", Config.phone);
	  password = prefs.getString("password", Config.password);
	  address = prefs.getString("address", Config.address);
	  city = prefs.getString("city", Config.city);
	  country = prefs.getString("country", Config.country);
	  usertype = prefs.getString("usertype", Config.usertype);
	  latitude = prefs.getString("Latitude", Config.latitude);
	  longitude = prefs.getString("Longitude", Config.longitude);

	  tname.setText(name);
	  temail.setText(email);
	  tphone.setText(phone);
	  tmobile.setText(mobile);
	  taddress.setText(address);
//	  tpassword.setText(password);
//	  tpassword_confirm.setText(password);


	  cityspinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Config.cityArray));

	  cityspinner.setSelection(Config.cityArray.indexOf(city), true);


//	  tcountry.setText(country);

	  tname.setEnabled(false);
	  temail.setEnabled(false);
	  tphone.setEnabled(false);
	  tmobile.setEnabled(false);
	  taddress.setEnabled(false);
	  cityspinner.setEnabled(false);
//	  tcountry.setEnabled(false);


   }

   public void editProfile(View view) {
	  tname.setEnabled(true);
	  temail.setEnabled(false);
	  tphone.setEnabled(true);
	  tmobile.setEnabled(false);
	  taddress.setEnabled(true);
	  cityspinner.setEnabled(true);
//	  tcountry.setEnabled(false);
   }

   public void save(View view) {
//	  String param="";

	  final String new_password;
//	  password = tpassword.getText().toString();
	  String password_confirm = tpassword_confirm.getText().toString();
	  password_old = tpassword_old.getText().toString();

//	  if (password_old.equals(Config.password)) {


//		 if (((password.length() > 0) || (password_confirm.length() > 0)) && password
//				 .equals(password_confirm) && password.length() >= 4) {
//			new_password = password;
//
//		 } else if (password.length() == 0 && password_confirm.length() == 0) {
//			new_password = Config.password;
//		 } else {
//			Toast.makeText(context, "Either password don't match or is less than 4 characters", Toast
//					.LENGTH_LONG).show();
//			return;
//		 }

	  if (true) {

		 new AsyncTask<Void, Void, Void>() {
			public ProgressDialog progressDialog;
			public String dataParams;

			@Override
			protected void onPreExecute() {

			   super.onPreExecute();

			   Calendar c = Calendar.getInstance();
			   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   String strDate = sdf.format(c.getTime());


			   dataParams =
					   "modified_on=" + strDate +
					   "&userid=" + userid +
					   "&uname=" + tname.getText().toString() +
					   "&uemail=" + temail.getText().toString() +
					   "&upassword=" + Config.password +
					   "&uphone=" + tphone.getText().toString() +
					   "&umobile=" + tmobile.getText().toString() +
					   "&uaddress=" + taddress.getText().toString() +
					   "&ucity=" + cityspinner.getSelectedItem()
			   ;

			   progressDialog = ProgressDialog.show(context, "Profile", "Saving " +
					   "Profile....");
			}

			@Override
			protected Void doInBackground(Void... params) {
			   String response = null;
			   try {
				  response = DownloadJsonContent.downloadContentUsingPostMethod(Config
						  .urlEditProfile, dataParams);
			   } catch (IOException e) {
				  e.printStackTrace();
			   }
			   try {
				  if (!new JSONObject(response).getString("status").equals("200")) {
					 Log.e("error", "jsonparsing");
				  }

			   } catch (JSONException e) {
				  e.printStackTrace();
			   }
			   return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
			   super.onPostExecute(aVoid);
			   SharedPreferences.Editor editor = prefs.edit();

			   editor.putString("name", tname.getText().toString());
			   editor.putString("email", temail.getText().toString());
			   editor.putString("mobile", tmobile.getText().toString());
			   editor.putString("phone", tphone.getText().toString());
			   editor.putString("password", Config.password);
			   editor.putString("address", taddress.getText().toString());
			   editor.putString("city", cityspinner.getSelectedItem().toString());

			   Config.name = tname.getText().toString();
			   Config.email = temail.getText().toString();
			   Config.mobile = tmobile.getText().toString();
			   Config.phone = tphone.getText().toString();
			   Config.password = Config.password;
			   Config.address = taddress.getText().toString();
			   Config.city = cityspinner.getSelectedItem().toString();

			   editor.apply();

			   progressDialog.dismiss();
			   Intent intent = new Intent(context, BaseFragmentActivity.class);
			   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			   startActivity(intent);
			}
		 }.execute();

	  } else {
		 Toast.makeText(this, "Either password don't match or is less than 4 characters", Toast
				 .LENGTH_LONG).show();
	  }

//	  } else {
//		 Toast.makeText(context, "Wrong old password", Toast.LENGTH_LONG).show();
//	  }
   }


   public void savePassword(View view) {
//	  String param="";

	  final String new_password, passconfirm;
	  password = tpassword.getText().toString();
	  passconfirm = tpassword_confirm.getText().toString();
	  String password_confirm = tpassword_confirm.getText().toString();
	  password_old = tpassword_old.getText().toString();

	  if (password_old.equals(Config.password)) {


		 if (password.length() <= 0 || passconfirm.length() <= 0) {
			Toast.makeText(this, "Fill all required fields to change the password", Toast
					.LENGTH_LONG).show();
			return;
		 }

		 if (((password.length() > 0) || (password_confirm.length() > 0)) && password
				 .equals(password_confirm) && password.length() >= 4) {
			new_password = password;

		 } else if (password.length() == 0 && password_confirm.length() == 0) {
			new_password = Config.password;
		 } else {
			Toast.makeText(context, "Either password don't match or is less than 4 characters", Toast
					.LENGTH_LONG).show();
			return;
		 }

		 if (true) {

			new AsyncTask<Void, Void, Void>() {
			   public ProgressDialog progressDialog;
			   public String dataParams;

			   @Override
			   protected void onPreExecute() {

				  super.onPreExecute();

				  Calendar c = Calendar.getInstance();
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  String strDate = sdf.format(c.getTime());


				  dataParams = "modified_on=" + strDate +
						  "&userid=" + userid +
						  "&uname=" + name +
						  "&uemail=" + email +
						  "&upassword=" + new_password +
						  "&uphone=" + phone +
						  "&umobile=" + mobile +
						  "&uaddress=" + address
				  ;

				  progressDialog = ProgressDialog.show(context, "Profile", "Saving " +
						  "Profile....");
			   }

			   @Override
			   protected Void doInBackground(Void... params) {
				  String response = null;
				  try {
					 response = DownloadJsonContent.downloadContentUsingPostMethod(Config
							 .urlEditProfile, dataParams);
				  } catch (IOException e) {
					 e.printStackTrace();
				  }
				  try {
					 if (!new JSONObject(response).getString("status").equals("200")) {
						Log.e("error", "jsonparsing");
					 }

				  } catch (JSONException e) {
					 e.printStackTrace();
				  }
				  return null;
			   }

			   @Override
			   protected void onPostExecute(Void aVoid) {
				  super.onPostExecute(aVoid);
				  SharedPreferences.Editor editor = prefs.edit();

				  editor.putString("name", name);
				  editor.putString("email", email);
				  editor.putString("mobile", mobile);
				  editor.putString("phone", phone);
				  editor.putString("password", new_password);
				  editor.putString("address", address);
				  editor.putString("city", city);

				  Config.name = name;
				  Config.email = email;
				  Config.mobile = mobile;
				  Config.phone = phone;
				  Config.password = new_password;
				  Config.address = address;
				  Config.city = city;

				  editor.apply();

				  progressDialog.dismiss();
				  Intent intent = new Intent(context, BaseFragmentActivity.class);
				  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				  startActivity(intent);
			   }
			}.execute();

		 } else {
			Toast.makeText(this, "Either password don't match or is less than 4 characters", Toast
					.LENGTH_LONG).show();
		 }

	  } else {
		 Toast.makeText(context, "Wrong old password", Toast.LENGTH_LONG).show();
	  }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
	  return super.onCreateOptionsMenu(menu);
   }

   public void changePasswordVisible(View view) {
	  LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lin_lay_profile);
	  linearLayout.setVisibility(View.VISIBLE);
//	  if (scrollView!=null){
//		 scrollView.fullScroll(View.FOCUS_DOWN);
//				 scrollTo((int) scrollView.getScrollX() + 10, (int)scrollView.getScrollY());
//	  }
   }


   public void tohome(View view) {
	  Intent intent = new Intent(this, BaseFragmentActivity.class);
	  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	  startActivity(intent);
   }
   //
//   void login(final String emailId, final String password) {
//
//
//							  editor.putString("name", Config.name);
//							  editor.putString("userid", Config.userid);
//							  editor.putString("email", Config.email);
//							  editor.putString("mobile", Config.mobile);
//							  editor.putString("password", Config.password);
//							  editor.putString("address", Config.address);
//							  editor.putString("city", Config.city);
//							  editor.putString("country", Config.country);
//							  editor.putString("usertype", Config.usertype);
//							  editor.putString("Latitude", Config.latitude);
//							  editor.putString("Longitude", Config.longitude);
//							  editor.apply();
//
//				  }
//

}
