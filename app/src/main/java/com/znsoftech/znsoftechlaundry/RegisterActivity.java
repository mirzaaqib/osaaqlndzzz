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
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.znsoftech.znsoftechlaundry.json.DownloadJsonContent;
import com.znsoftech.znsoftechlaundry.network.Network;
import com.znsoftech.znsoftechlaundry.util.AlertUtil;
import com.znsoftech.znsoftechlaundry.util.ProgressDialogClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;

public class RegisterActivity extends ActionBarActivity {

   //   JSONArray searchResult = new JSONArray();
   Handler mHandler = new Handler();
   SharedPreferences prefs;
   //   GPSTracker gps;
   boolean isLoad;

   Spinner spinner_city;
   private JSONObject jsonObject;
   private Spinner spinner_country;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_register);


	  if (!Network.HaveNetworkConnection(this)) {
		 DialogUniversalCustom dialog = new DialogUniversalCustom();
		 dialog.init("No internet Connection!", "Error! No internet Connection", "OK", null, new
				 DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					   finish();
					}
				 }, null);
		 dialog.show(getSupportFragmentManager(), "dialog");
	  }
	  //#00516C
	  ActionBar bar = getSupportActionBar();
	  bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196f3")));
	  bar.setDisplayShowHomeEnabled(true);
	  bar.setDisplayHomeAsUpEnabled(true);
	  bar.setHomeButtonEnabled(true);
	  bar.setTitle("Sign Up");


	  prefs = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);

	  isLoad = false;

	  spinner_city = (Spinner) findViewById(R.id.spinner_city);
	  spinner_country = (Spinner) findViewById(R.id.spinner_country);
	  final EditText editText_name = (EditText) findViewById(R.id.editText_username);
	  final EditText editText_email = (EditText) findViewById(R.id.EditText_email);
	  final EditText editText_email2 = (EditText) findViewById(R.id.EditText_email2);
	  final EditText editText_mobile = (EditText) findViewById(R.id.EditText_mobile);
	  final EditText editText_password = (EditText) findViewById(R.id.EditText_password);
	  final EditText editText_address = (EditText) findViewById(R.id.EditText_address);
//	  final EditText editText_country = (EditText) findViewById(R.id.EditText_country);
	  final EditText editText_phone = (EditText) findViewById(R.id.EditText_phone);

	  final Spinner spinner_operator = (Spinner) findViewById(R.id.spinner_operator);


	  final String[] operator_items = new String[]{"050", "052", "055", "056"};


	  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RegisterActivity.this

			  , android.R.layout.simple_spinner_dropdown_item, operator_items);
	  spinner_operator.setAdapter(arrayAdapter);

	  Button but_register = (Button) findViewById(R.id.but_register);
	  but_register.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
//			https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=AIzaSyDECrKiJEN4JlT3Gk5N4TPz8dsM-wCstaU
			String address = editText_address.getText().toString();
			String country = spinner_country.getSelectedItem().toString();
			String state = spinner_city.getSelectedItem().toString();
			String geoAddress = address + "+," + state + "+," + country;
			geoAddress = geoAddress.replace(" ", "+");
			String geoUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + geoAddress + "&key" +
					"=" + Config.apiKey;
//			String geoUrl="https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=AIzaSyCwvpDui9IeSEj7GdgjzLO7Gg32_S-nqQY";


			String mobileNumberWithCode = "";
			boolean errorFlag = false;
			String errorString = "";

			final AlertUtil alert = new AlertUtil();

			if (Network.HaveNetworkConnection(RegisterActivity.this)) {
			   if (editText_name.getText().toString().equals("")) {
				  errorFlag = true;
				  errorString = "Please enter your name\n";
			   }

			   if (editText_email.getText().toString().equals(editText_email2.getText()
					   .toString())) {
				  if (!editText_email2.getText().toString().contains("@") || !editText_email
						  .getText().toString().contains(".") ||
						  (editText_email
								  .getText().toString().length() < 4)) {
					 errorFlag = true;
					 errorString = errorString + "Please enter valid email address\n";
				  }
			   } else {
				  errorFlag = true;
				  errorString = errorString + "Email addresses don't match.\n";
			   }

			   if (!(editText_password.getText().toString().length() >= 6)) {
				  errorFlag = true;
				  errorString = errorString + "Password must be atleast 6 chars long\n";

			   }
			   if (address.length() == 0) {
				  errorFlag = true;
				  errorString = errorString + "Please fill an address\n";
			   }

			   if (!((editText_mobile.getText().toString().length() == 7) || (editText_mobile
					   .getText().toString().length() == 8))) {
				  errorFlag = true;
				  errorString = errorString + "Mobile must be either 7 or 8 digits long\n";
			   } else {

				  mobileNumberWithCode = String.valueOf(spinner_operator.getSelectedItem()) + editText_mobile.getText().toString();
			   }


			   if (!errorFlag) {


				  ProgressDialogClass.showProgressDialog(RegisterActivity.this,
						  "Loading...");

				  AsyncGeoData asyncGeoData = new AsyncGeoData();
				  asyncGeoData.execute(
						  geoUrl,
						  editText_name.getText().toString(),
						  editText_email.getText().toString(),
						  editText_password.getText().toString(),
						  editText_phone.getText().toString(),
						  mobileNumberWithCode,
						  editText_address.getText().toString(),
						  spinner_city.getSelectedItem().toString() + "",
						  spinner_country.getSelectedItem().toString(),
						  ""
				  );
			   } else {
				  DialogUniversalCustom dialog = new DialogUniversalCustom();
				  dialog.initExtraWithMessage(errorString, "Error!", "OK", null, new DialogInterface.OnClickListener() {
					 @Override
					 public void onClick(DialogInterface dialog, int which) {

					 }
				  }, null);
				  dialog.show(getSupportFragmentManager(), "dialog");
				  return;

			   }

			} else {
			   alert.messageAlert(RegisterActivity.this, getResources()
					   .getString(R.string.network_title), getResources()
					   .getString(R.string.network_message));
			}
		 }
	  });


//	  if (Config.cityArray.size() == 0) {
//		 getCityName();
//	  } else {
//		 spinner_city
//				 .setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
//						 android.R.layout.simple_spinner_dropdown_item,
//						 Config.cityArray));
//	  }

	  AsyncCountry country = new AsyncCountry();
	  country.execute();

   }

   @Override
   public void onBackPressed() {
	  // TODO Auto-generated method stub
	  super.onBackPressed();
//	  gps.stopUsingGPS();
	  Intent intent = new Intent(
			  RegisterActivity.this,
			  LoginActivity.class);

	  startActivity(intent);

	  overridePendingTransition(R.anim.left_in, R.anim.right_out);


   }

   void register(final String name, final String email, final String password,
				 final String phone, final String mobile, final String address,
				 final String city, final String country, final String usertype, final String lat, final String log) {

//	  ProgressDialogClass.showProgressDialog(RegisterActivity.this,
//			  "Loading...");

	  final Thread splashTread = new Thread() {
		 @Override
		 public void run() {


			String ipAddress = getLocalIpAddress();

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strDate = sdf.format(c.getTime());

			String dataParams = "uname=" + name +
					"&ucreatedon=" + strDate +
					"&uemail=" + email +
					"&upassword=" + password +
					"&uphone=" + phone +
					"&umobile=" + mobile +
					"&uaddress=" + address +
					"&ucountry=" + country +
					"&ucity=" + city +
					"&uusertype=" + usertype +
					"&ulat=" + lat +
					"&ulong=" + log +
					"&ucreatedby=" +
					"&uip=" + ipAddress +
					"&ustate=" + "" +
					"&uzipcode=" + "";

			try {
			   String strData = DownloadJsonContent.downloadContentUsingPostMethod(Config
					   .Register_Url, dataParams);
			   jsonObject = new JSONObject(strData);

			} catch (IOException e) {
			   e.printStackTrace();
			} catch (JSONException e) {
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
//					 if (searchResult.length() > 0) {
					 try {
						if (jsonObject.getString("status").equals("200")) {

						   Config.userid = jsonObject.getJSONObject("data").getString("UserID");
						   Config.name = name;
						   Config.password = password;
						   Config.email = email;
						   Config.phone = phone;
						   Config.mobile = mobile;
						   Config.address = address;
						   Config.city = city;
						   Config.country = country;
						   Config.usertype = usertype;

						   prefs.edit().putString("name", Config.name)
								   .commit();
						   prefs.edit()
								   .putString("userid",
										   Config.userid).commit();
						   prefs.edit()
								   .putString("email",
										   Config.email).commit();
						   prefs.edit()
								   .putString("mobile",
										   Config.mobile).commit();
						   prefs.edit()
								   .putString("password",
										   Config.password)
								   .commit();

						   prefs.edit()
								   .putString("phone",
										   Config.phone).commit();
						   prefs.edit()
								   .putString("address",
										   Config.address)
								   .commit();
						   prefs.edit()
								   .putString("city", Config.city)
								   .commit();


						   prefs.edit()
								   .putString("country",
										   Config.country)
								   .commit();

						   prefs.edit()
								   .putString("usertype",
										   Config.usertype)
								   .commit();

						   Toast.makeText(getApplicationContext(), "User created successfully!",
								   Toast.LENGTH_LONG).show();


						   Intent intent = new Intent(
								   RegisterActivity.this,
								   BaseFragmentActivity.class).addFlags(Intent
								   .FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						   startActivity(intent);
						   overridePendingTransition(R.anim.right_in, R.anim.left_out);
						   finish();
						} else {


						   new AlertDialog.Builder(RegisterActivity.this)
								   .setTitle("Error!")
								   .setMessage(jsonObject.getString("stauts_message"))
								   .setPositiveButton("OK", new DialogInterface.OnClickListener
										   () {
									  public void onClick(DialogInterface dialog, int which) {
										 // continue with delete
									  }
								   })
								   .setIcon(android.R.drawable.ic_dialog_alert)
								   .show();


						}
					 } catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					 }


					 ProgressDialogClass.dismissProgressDialog();

//					 startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
				  }

			   });
			}
		 }
	  });
	  displayThread.start();

   }

   public String getLocalIpAddress() {
	  try {
		 for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
			   InetAddress inetAddress = enumIpAddr.nextElement();
			   if (!inetAddress.isLoopbackAddress()) {
				  String ipaddress = inetAddress.getHostAddress().toString();
				  Log.e("ip address", "" + ipaddress);
				  return ipaddress;
			   }
			}
		 }
	  } catch (SocketException ex) {
		 Log.e("ip address", ex.toString());
	  }
	  return null;
   }

   private void showGPSDisabledAlertToUser(final int code) {
	  // AlertDialog.Builder dialog = new AlertDialog.Builder(this);


//	  final Dialog dialog = new Dialog(RegisterActivity.this);

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
   }

//   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//	  if (gps.canGetLocation()) {
//		 gps.getLocation();
//		 Log.d("Your Location", "latitude:" + gps.getLatitude()
//				 + ", longitude: " + gps.getLongitude());
//			/*
//			 * while (gps.getLatitude()!=0.0) {
//			 *
//			 * gps.getLocation(); }
//			 */
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
//
//	  } else {
//		 // Can't get user's current location
//
//		 showGPSDisabledAlertToUser(10);
//		 // stop executing code by return
//		 return;
//	  }
//   }// on

   void getCityName(final String s) {

	  ProgressDialogClass.showProgressDialog(RegisterActivity.this,
			  "Loading...");

	  new AsyncTask<Void, Void, ArrayList<String>>() {


		 @Override
		 protected ArrayList<String> doInBackground(Void... params) {
			try {
			   String data = DownloadJsonContent.downloadContentUsingPostMethod(Config.City_Url,
					   "country_id=" + s);
			   JSONObject jsonObject = new JSONObject(data);
			   JSONArray js = jsonObject.getJSONArray("data");
//			   Set<String> set = new HashSet<String>();
			   ArrayList<String> arrayList = new ArrayList<String>();

			   Config.cityArray.clear();
			   for (int i = 0; i < js.length(); i++) {
				  JSONObject object = js.getJSONObject(i);
				  String s = object.getString("CityName");
//				  set.add(s);
				  arrayList.add(s);
				  Config.cityArray.add(s);
			   }

			   TinyDB tinyDB = new TinyDB(RegisterActivity.this);
			   Collections.sort(arrayList);
			   tinyDB.putListString("CityArray", arrayList);
//			   Set<String> set = new HashSet<String>(arrayList);


//			   prefs.edit().putStringSet("CityArray", set).commit();


			} catch (IOException e) {
			   e.printStackTrace();
			} catch (JSONException e) {
			   e.printStackTrace();
			}
			return Config.cityArray;
		 }

		 @Override
		 protected void onPostExecute(ArrayList<String> strings) {
			super.onPostExecute(strings);
			if (spinner_city != null) {
//			   prefs.edit().putStringSet("CityArray", new HashSet<String>(strings)).commit();
			   spinner_city.setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
					   android.R.layout.simple_spinner_dropdown_item,
					   strings));
			}
			ProgressDialogClass.dismissProgressDialog();
		 }

	  }.execute();

//	  final Thread splashTread = new Thread() {
//		 @Override
//		 public void run() {
//
//			ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
//
//
//			JSONArray jArray = new JSONArray();
//			JsonReturn jsonReturn = new JsonReturn();
//			jArray = jsonReturn.postLaundryData(
//					Config.City_Url, nameValuePairs);
//			Config.cityArray.clear();
//			Set<String> set=new HashSet<>();
//
//			for (int i = 0; i < jArray.length(); i++) {
//			   try {
//				  String cityThis=jArray.getJSONObject(i).getString("CityName");
//				  set.add(cityThis);
//				  Config.cityArray.add(cityThis);
//			   } catch (JSONException e) {
//				  // TODO Auto-generated catch block
//				  e.printStackTrace();
//			   }
//			}
//			prefs.edit().putStringSet("CityArray",set).apply();

//		 }
//	  };
//	  splashTread.start();

//	  final Thread displayThread = new Thread(new Runnable() {
//		 public void run() {
//
//			try {
//			   splashTread.join();
//			} catch (InterruptedException e) {
//			   e.printStackTrace();
//			}
//
//			if (mHandler != null) {
//			   mHandler.post(new Runnable() {
//				  public void run() {
//					 // ProgressDialogClass.dismissProgressDialog();
//					 if (Config.cityArray != null) {
//						if (Config.cityArray.size() > 0) {
//
//						}
//					 }
//					 ProgressDialogClass.dismissProgressDialog();
//
//				  }
//
//			   });
//			}
//		 }
//	  });
//	  displayThread.start();

   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
	  // Handle action bar item clicks here. The action bar will
	  // automatically handle clicks on the Home/Up button, so long
	  // as you specify a parent activity in AndroidManifest.xml.
	  int id = item.getItemId();

	  if (id == android.R.id.home) {
		 onBackPressed();
		 return true;

	  }
	  return super.onOptionsItemSelected(item);
   }

   public class AsyncGeoData extends AsyncTask<String, Void, String> {

	  String nameuser, email, password, phone, mobileNumberwithCode, address, city, country, typeuser,
			  latitude = "23.344344", longitude = "78.334484";
	  private String datacame = null;

	  @Override
	  protected String doInBackground(String... params) {
		 nameuser = params[1];
		 email = params[2];
		 password = params[3];
		 phone = params[4];
		 mobileNumberwithCode = params[5];
		 address = params[6];
		 city = params[7];
		 country = params[8];
		 typeuser = params[9];

		 try {
			datacame = DownloadJsonContent.downloadContentUsingPostMethod(params[0], "");
		 } catch (IOException e) {
			e.printStackTrace();
			Log.e("download", e.getMessage());
//			Log.e("download",e.getCause().toString());
			Log.e("download", e.getLocalizedMessage());
//			Log.e("download",e.getSuppressed().toString());
		 }
		 return datacame;
	  }

	  @Override
	  protected void onPostExecute(String s) {
		 if (!isCancelled()) {
			if (s != null) {

			   super.onPostExecute(s);
			   try {
				  JSONObject jsonObject = new JSONObject(s);
				  if (jsonObject.has("results") && !jsonObject.isNull("results")) {
					 JSONArray array = jsonObject.getJSONArray("results");
					 if (array.length() > 0) {
						JSONObject arr0 = array.getJSONObject(0);
						if (arr0.has("geometry") && !arr0.isNull("geometry")) {
						   JSONObject geometry = arr0.getJSONObject("geometry");
						   if (geometry.has("location") && !geometry.isNull("location")) {
							  JSONObject location = geometry.getJSONObject("location");
							  if (location.has("lat") && !location.isNull("lat") && location.has("lng")
									  && !location.isNull("lng")) {
								 latitude = String.valueOf(location.getDouble("lat"));
								 longitude = String.valueOf(location.getDouble("lng"));
							  }
						   }
						}
					 }
				  }
			   } catch (JSONException e) {
				  e.printStackTrace();
			   }
			}
			register(
					nameuser,
					email,
					password,
					phone,
					mobileNumberwithCode,
					address,
					city,
					country,
					"",
					latitude, longitude);

		 }

	  }
   }

   public class AsyncCountry extends AsyncTask<String, Void, String> {
	  public ArrayList<String> arrCountryList, arrCountryListId;

	  @Override
	  protected void onPreExecute() {
		 super.onPreExecute();
//		 ProgressDialogClass.showProgressDialog(getApplicationContext(),"Loading Countries...");
	  }

	  @Override
	  protected String doInBackground(String... params) {
		 JSONObject jsonCountry = null;

		 String dataCountry = null;
		 try {
			dataCountry = DownloadJsonContent.downloadContent(Config.new_country_url);

			jsonCountry = new JSONObject(dataCountry);

			JSONArray jsCountryArray = jsonCountry.getJSONArray("data");
			arrCountryList = new ArrayList<String>();
			arrCountryListId = new ArrayList<String>();
			for (int a = 0; a < jsCountryArray.length(); a++) {
			   JSONObject object = jsCountryArray.getJSONObject(a);
			   String countryName = object.getString("countryName");
			   String countryID = object.getString("CountryID");
			   arrCountryList.add(countryName);
			   arrCountryListId.add(countryID);
			}

		 } catch (JSONException e) {
			e.printStackTrace();
		 } catch (IOException e) {
			e.printStackTrace();
		 }
		 return null;
	  }

	  @Override
	  protected void onPostExecute(String s) {
		 super.onPostExecute(s);
//		 ProgressDialogClass.dismissProgressDialog();

		 if (spinner_country != null && arrCountryList != null) {
			spinner_country.setAdapter(new ArrayAdapter<String>(RegisterActivity.this,
					android.R.layout.simple_spinner_dropdown_item,
					arrCountryList
			));
		 }
		 spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			   getCityName(arrCountryListId.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		 });

	  }
   }

}