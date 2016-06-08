package com.znsoftech.znsoftechlaundry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.znsoftech.znsoftechlaundry.json.DownloadJsonContent;
import com.znsoftech.znsoftechlaundry.json.JsonReturn;
import com.znsoftech.znsoftechlaundry.network.Network;
import com.znsoftech.znsoftechlaundry.util.AlertUtil;
import com.znsoftech.znsoftechlaundry.util.ProgressDialogClass;
import com.znsoftech.znsoftechlaundry.util.ValidationUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LoginActivity extends ActionBarActivity {

   JSONArray searchResult = new JSONArray();
   Handler mHandler = new Handler();
   EditText editText_email;
   EditText editText_password;
   SharedPreferences prefs;
   SharedPreferences.Editor editor;
   Boolean isRemember = false;
   //   private GPSTracker gps;
   private boolean isLoad;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_login);

	  Config.latest_laundries_json = null;
	  Config.latest_offers_json = null;


	  ActionBar bar = getSupportActionBar();
	  bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
	  bar.setDisplayShowHomeEnabled(false);
	  bar.setDisplayHomeAsUpEnabled(false);
	  bar.setHomeButtonEnabled(false);
	  bar.setTitle("Sign In");
	  bar.hide();


//	  gps = new GPSTracker(this);

	  // check if GPS location can get
//	  if (gps.canGetLocation()) {
//		 Log.d("Your Location", "latitude:" + gps.getLatitude()
//				 + ", longitude: " + gps.getLongitude());
//
//		 final Thread splashTread = new Thread() {
//			@Override
//			public void run() {
//
//			   Thread splashTread = new Thread() {
//				  @Override
//				  public void run() {
//					 try {
//						int waited = 0;
//						while (waited < 200) {
//						   sleep(100);
//						   waited += 100;
//						   gps.getLocation();
//						   if (gps.getLatitude() == 0.0) {
//							  waited = 0;
//						   }
//
//						}
//					 } catch (InterruptedException e) {
//						// do nothing
//					 } finally {
//						runOnUiThread(new Thread(new Runnable() {
//						   public void run() {
//							  getLocation();
//						   }
//						}));
//
//					 }
//				  }
//			   };
//			   try {
//				  splashTread.start();
//			   } catch (Exception e) {
//				  // TODO Auto-generated catch block
//				  e.printStackTrace();
//			   }
//
//			}
//		 };
//		 splashTread.start();
//	  } else {
//		 // Can't get user's current location
//		 showGPSDisabledAlertToUser(10);
//		 // stop executing code by return
//		 return;


//	  }


	  prefs = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
	  editor = prefs.edit();

	  isRemember = prefs.getBoolean("isRemember", false);
	  editText_email = (EditText) findViewById(R.id.editText_username);
	  //editText_email.setTypeface(Config.TF_ApexNew_Medum);
	  editText_password = (EditText) findViewById(R.id.EditText_password);
	  //	editText_password.setTypeface(Config.TF_ApexNew_Medum);


	  final CheckBox checkbox_rememberme = (CheckBox) findViewById(R.id.imageView_remember);

	  checkbox_rememberme.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
			isRemember = !isRemember;

		 }
	  });

	  TextView txt_register = (TextView) findViewById(R.id.textView_new_user);
	  String str = "NEW USER";


	  SpannableString content = new SpannableString(str);
	  content.setSpan(new UnderlineSpan(), 0, str.length(), 0);

	  txt_register.setText(content);
	  txt_register.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(new Intent(LoginActivity.this,
					RegisterActivity.class));
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
			finish();

		 }
	  });


	  Button but_submit = (Button) findViewById(R.id.but_submit);
	  //but_submit.setTypeface(Config.TF_ApexNew_Bold);
	  but_submit.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
			// TODO Auto-generated method stub
			final AlertUtil alert = new AlertUtil();
			if (Network.HaveNetworkConnection(LoginActivity.this)) {

			   if (!ValidationUtil
					   .isEmail(editText_email.getText().toString())) {

				  alert.confirmationAlert(
						  LoginActivity.this,
						  "",
						  "Please enter valid email id",
						  new OnClickListener() {

							 @Override
							 public void onClick(
									 View v) {
								editText_email.setText("");
								alert.release();
							 }
						  });


			   } else if (ValidationUtil.isNull(editText_password.getText()
					   .toString())) {
				  alert.confirmationAlert(
						  LoginActivity.this,
						  "",
						  "Please enter password",
						  new OnClickListener() {

							 @Override
							 public void onClick(
									 View v) {
								alert.release();
							 }
						  });
			   } else
				  login(editText_email.getText().toString(),
						  editText_password.getText().toString());


			} else {


			   alert.messageAlert(
					   LoginActivity.this,
					   getResources()
							   .getString(R.string.network_title),
					   getResources().getString(
							   R.string.network_message));

			}
		 }

	  });

	  if (isRemember) {
		 editText_email.setText(Config.email);
		 editText_password.setText(Config.password);
		 checkbox_rememberme.setChecked(true);

		 login(editText_email.getText().toString(),
				 editText_password.getText().toString());
	  }

   }


   void login(final String emailId, final String password) {

	  ProgressDialogClass.showProgressDialog(LoginActivity.this, "Loading...");

	  final Thread splashTread = new Thread() {
		 @Override
		 public void run() {


			ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("iemail", emailId));
			nameValuePairs.add(new BasicNameValuePair("ipwd", password));


			JsonReturn jsonReturn = new JsonReturn();
			searchResult = jsonReturn.postData(Config.Login_Url, nameValuePairs);

		 }
	  };
	  splashTread.start();

	  final Thread displayThread = new Thread(new Runnable() {
		 public void run() {

			try {
			   splashTread.join();
			} catch (InterruptedException e) {
			   e.printStackTrace();
			}


			if (mHandler != null) {
			   mHandler.post(new Runnable() {
				  public void run() {
					 // ProgressDialogClass.dismissProgressDialog();
					 if (searchResult.length() > 0) {
						try {
						   if (!searchResult.getJSONObject(0).has("status")) {
							  if (searchResult
									  .getJSONObject(0).getString(FlagLaun.DAta_STATUS).equals("1")) {


								 Config.name = searchResult.getJSONObject(0).getString("Name");
								 Config.userid = searchResult.getJSONObject(0).getString("UserID");
								 Config.email = emailId;
								 Config.password = searchResult.getJSONObject(0).getString("Password");
								 Config.phone = searchResult.getJSONObject(0).getString("Phone");
								 Config.mobile = searchResult.getJSONObject(0).getString("Mobile");
								 Config.address = searchResult.getJSONObject(0).getString("Address");
								 Config.city = searchResult.getJSONObject(0).getString("City");
								 Config.country = searchResult.getJSONObject(0).getString("Country");
								 Config.usertype = searchResult.getJSONObject(0).getString("UserType");


//							  Config.latitude = searchResult.getJSONObject(0).getString("Latitude");
//							  Config.longitude = searchResult.getJSONObject(0).getString("Longitude");


								 editor.putString("name", Config.name);
								 editor.putString("userid", Config.userid);
								 editor.putString("email", Config.email);
								 editor.putString("mobile", Config.mobile);
								 editor.putString("phone", Config.phone);
								 editor.putString("password", Config.password);
								 editor.putString("address", Config.address);
								 editor.putString("city", Config.city);
								 editor.putString("country", Config.country);
								 editor.putString("usertype", Config.usertype);


								 editor.putBoolean("ad", true);
								 editor.putBoolean("isRemember", isRemember);
								 editor.apply();
								 getCityName();

							  } else {
								 DialogUniversalCustom dialogUniversalCustom = new
										 DialogUniversalCustom();
								 dialogUniversalCustom.init("Error!", "Account suspect please " +
												 "contact please contact nehal.raza@hotmail.com", "OK",
										 null, new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {

											}
										 }, null);
								 dialogUniversalCustom.show(getSupportFragmentManager(), "dialog");
								 ProgressDialogClass.dismissProgressDialog();
							  }

						   } else {
							  ProgressDialogClass.dismissProgressDialog();
							  AlertUtil alert = new AlertUtil();
							  alert.messageAlert(
									  LoginActivity.this, "", "Either Username or " +
											  "password is wrong\n Or no " +
											  " Internet connection available.");


						   }
						} catch (JSONException e) {
						   // TODO Auto-generated catch block
						   e.printStackTrace();
						}

					 }
					 //ProgressDialogClass.dismissProgressDialog();

				  }

			   });
			}
		 }
	  });
	  displayThread.start();

   }

   void getCityName() {


	  final Thread splashTread = new Thread() {
		 @Override
		 public void run() {

			ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

			try {
//			   JSONObject jsonObjectCity = new JSONObject(DownloadJsonContent.downloadContent(Config.City_Url));
			   JSONObject jsonObjectCity = new JSONObject(DownloadJsonContent
					   .downloadContentUsingPostMethod(Config.City_Url, "country_name=" + Config.country));


			   if (jsonObjectCity.has("data") && !jsonObjectCity.isNull("data")) {

				  JSONArray jArray = jsonObjectCity.getJSONArray("data");
//                        JsonReturn jsonReturn = new JsonReturn();

//                        jArray = jsonReturn.postLaundryData(Config.City_Url, nameValuePairs);
				  Config.cityArray.clear();
//				  Set<String> set = new HashSet<>();
				  ArrayList<String> arrayList = new ArrayList<>();



				  for (int i = 0; i < jArray.length(); i++) {
					 try {
						String cityName = jArray.getJSONObject(i).getString("CityName");
						Config.cityArray.add(cityName);
//						set.add(cityName);
						arrayList.add(cityName);

					 } catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					 }
				  }
				  Collections.sort(arrayList);
//				  Set<String> set = new HashSet<String>(arrayList);
				  TinyDB tinyDB = new TinyDB(getApplicationContext());
				  tinyDB.putListString("CityArray", arrayList);

			   }

			} catch (JSONException e) {
			   e.printStackTrace();
			} catch (IOException e) {
			   e.printStackTrace();
			}


		 }
	  };
	  splashTread.start();

	  final Thread displayThread = new Thread(new Runnable() {
		 public void run() {

			try {
			   splashTread.join();
			} catch (InterruptedException e) {
			   e.printStackTrace();
			}

			if (mHandler != null) {
			   mHandler.post(new Runnable() {
				  public void run() {
					 // ProgressDialogClass.dismissProgressDialog();
//					 if (Config.cityArray != null) {
//						if (Config.cityArray.size() > 0) {

						   startActivity(new Intent(LoginActivity.this, BaseFragmentActivity
								   .class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
						   overridePendingTransition(R.anim.right_in, R.anim.left_out);
						   finish();
						   ProgressDialogClass.dismissProgressDialog();


//						}

//					 }


				  }

			   });
			}
		 }
	  });
	  displayThread.start();

   }


   public void forget_password(View view) {
	  setContentView(R.layout.forget_password);
   }

   public void sendForgetPassword(View view) {
	  // Recipient's email ID needs to be mentioned.


	  new AsyncTask<Void, Void, Void>() {
		 public String password;
		 public JSONObject datObj;
		 public boolean error = false;
		 public String status, statusMessage, dataMessage;
		 EditText text;
		 String to;
		 String from;
		 String host;

		 @Override
		 protected void onPreExecute() {
			super.onPreExecute();
			text = (EditText) findViewById(R.id.edit_email_forget);
			to = text.getText().toString();
			from = "no-reply@znsoftech.com";
			host = "smtp.gmail.com";

			setContentView(R.layout.loading);
		 }

//		 pop://mail6.znsoftech.com:143 zns123

		 @Override
		 protected Void doInBackground(Void... params) {

			try {
			   String data = DownloadJsonContent.downloadContentUsingPostMethod(Config
							   .Forgot_password_url,
					   "user_email=" + to);
			   JSONObject object = new JSONObject(data);
			   if (object.has("status") && !object.isNull("status")) {
				  status = String.valueOf(object.getInt("status"));
			   }
			   if (object.has("stauts_message") && !object.isNull("stauts_message")) {
				  statusMessage = object.getString("stauts_message");
			   }
			   if (object.has("data") && !object.isNull("data")) {
				  datObj = object.getJSONObject("data");

				  dataMessage = datObj.getString("msg");
				  password = datObj.getString("pwd");

			   }


			} catch (IOException e) {
			   e.printStackTrace();
			} catch (JSONException e) {
			   e.printStackTrace();
			}

			if (status.equals("200") && dataMessage.equalsIgnoreCase("Email found")) {
			   try {
				  GMailSender sender = new GMailSender("noreplyznsoftech@gmail.com",
						  "zns12345");
				  sender.sendMail("no-reply@znsoftech.com",
						  "Your Spot my Laundry password is \n" + password,
						  from,
						  to);

			   } catch (Exception e) {
				  Log.e("SendMail", e.getMessage(), e);
				  error = true;
			   }
			}

			return null;
		 }

		 @Override
		 protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			if (!error) {
			   if (dataMessage.equalsIgnoreCase("Email Found")) {
				  Toast.makeText(getApplicationContext(), "Email containing your password " +
						  "successfully sent to " + to, Toast
						  .LENGTH_LONG).show();
				  recreate();
			   } else {

				  setContentView(R.layout.forget_password);
				  Toast.makeText(getApplicationContext(), "No email account associated with " + to, Toast
						  .LENGTH_LONG).show();
			   }
			} else Toast.makeText(getApplicationContext(), "Error while sending email to you. " +
					"Please contact us @ info@znsoftech.com for technical support", Toast
					.LENGTH_LONG).show();
		 }
	  }.execute();
	  // Get system properties
   }

//   private void showGPSDisabledAlertToUser(final int code) {
//	  // AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//
//
//	  final Dialog dialog = new Dialog(this);
//
//	  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//	  dialog.setCanceledOnTouchOutside(false);
//	  dialog.getWindow().setBackgroundDrawable(
//			  new ColorDrawable(Color.TRANSPARENT));
//	  dialog.setContentView(R.layout.custom_alert_popup);
//	  TextView title1 = (TextView) dialog.findViewById(R.id.textView_title);
//	  title1.setText("TURN GPS ON");
//	  TextView msg = (TextView) dialog.findViewById(R.id.textView_message);
//	  msg.setText("To get your nearest metro station distance turn on your GPS.");
//
//	  Button ok = (Button) dialog.findViewById(R.id.button_positive);
//	  ok.setText("ON GPS NOW");
//	  // if button is clicked, close the custom dialog
//	  ok.setOnClickListener(new OnClickListener() {
//		 @Override
//		 public void onClick(View v) {
//			Intent callGPSSettingIntent = new Intent(
//					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//			startActivityForResult(callGPSSettingIntent, code);
//			dialog.dismiss();
//		 }
//	  });
//
//	  Button cencle = (Button) dialog.findViewById(R.id.button_negative);
//	  cencle.setText("Cancel");
//	  // if button is clicked, close the custom dialog
//	  cencle.setOnClickListener(new OnClickListener() {
//		 @Override
//		 public void onClick(View v) {
//			dialog.dismiss();
//			onBackPressed();
//		 }
//	  });
//	  dialog.show();
//
//   }

//   private void getLocation() {
//	  // updateSeekProgress();
//
//	  //progressBar.setVisibility(ProgressBar.VISIBLE);
//
//	  Config.latitude = gps.getLatitude() + "";
//
//	  Config.longitude = gps.getLongitude() + "";
//
//	  if (!isLoad) {
//
//		 //Toast.makeText(RegisterActivity.this, "latitude:" + gps.getLatitude()
//		 //	+ ", longitude: " + gps.getLongitude(),Toast.LENGTH_LONG).show();
//		 isLoad = true;
//		 //progressBar.setVisibility(ProgressBar.GONE);
//		 gps.stopUsingGPS();
//	  }
//
//   }

}
