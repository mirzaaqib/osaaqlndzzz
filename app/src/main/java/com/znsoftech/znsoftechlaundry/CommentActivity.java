package com.znsoftech.znsoftechlaundry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.znsoftech.znsoftechlaundry.json.DownloadJsonContent;
import com.znsoftech.znsoftechlaundry.json.JGetParsor;
import com.znsoftech.znsoftechlaundry.network.Network;
import com.znsoftech.znsoftechlaundry.util.AlertUtil;
import com.znsoftech.znsoftechlaundry.util.ProgressDialogClass;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class CommentActivity extends ActionBarActivity {
   Handler mHandler = new Handler();

   JSONObject json;
   Intent i = null;

   String AddressID, UserID, AddressName, CityID, CountryID, ContactNo, AddressLine1, AddressLine2, AddressLine3, countryName, CityName;
   String address_position1, address_position2;

   String laundryId, laundryArray, laundryName;
   boolean online_offline;

   Button image_edit1;
   Button image_edit2;

   Spinner sp1, sp2;

   EditText et1, et2, et3;

   ArrayList<HashMap<String, String>> array_list = new ArrayList<HashMap<String, String>>();
   ArrayList<String> pick_up = new ArrayList<String>();
   private String deal_text;

   // private GoogleMap mMap;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.new_pick_up_address);

	  ActionBar bar = getSupportActionBar();
	  bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.action_bar_color))));
	  bar.setDisplayShowHomeEnabled(true);
	  bar.setDisplayHomeAsUpEnabled(true);
	  bar.setHomeButtonEnabled(true);

	  this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	  i = getIntent();

	  laundryId = i.getExtras().getString("LaundryId");
	  laundryArray = i.getExtras().getString("LaundryArray");
	  laundryName = i.getExtras().getString("LaundryName");

	  deal_text = i.getExtras().getString("deal_text");
	  if (deal_text == null) {
		 deal_text = "";
	  } else {
		 deal_text = "    Deal Text:" + deal_text;
	  }

	  address_position1 = i.getExtras().getString("address_position1");
	  address_position2 = i.getExtras().getString("address_position2");
	  online_offline = i.getExtras().getBoolean("LaundryOffline");

	  bar.setTitle("Confirm Address");

	  sp1 = (Spinner) findViewById(R.id.spinner1);
	  sp2 = (Spinner) findViewById(R.id.spinner2);
	  //Log.w("On Comment Activity Page 2", "running till here");
	  sp1.setOnItemSelectedListener(new OnItemSelectedListener() {

		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view,
									int position, long id) {
			// TODO Auto-generated method stub

			String sp_name = sp1.getSelectedItem().toString();

			if (sp_name.equals("new")) {


			   i = new Intent(CommentActivity.this, Add_CommentAddress.class);

			   i.putExtra("address_position1", 0 + "");
			   i.putExtra("address_position2", 0 + "");

			   i.putExtra("laundryId", laundryId);
			   i.putExtra("laundryArray", laundryArray);
			   i.putExtra("laundryName", laundryName);
			   i.putExtra("online_offline", online_offline);

			   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			   startActivity(i);
			   finish();
			   return;

			}

			String full_address = "";

			if (array_list.get(position).get("AddressLine1").length() > 2)
			   full_address = array_list.get(position).get("AddressLine1") + ", ";

			if (array_list.get(position).get("AddressLine2").length() > 2)
			   full_address += array_list.get(position).get("AddressLine2") + ", ";

			if (array_list.get(position).get("AddressLine3").length() > 2)
			   full_address += array_list.get(position).get("AddressLine3") + ", ";

			if (array_list.get(position).get("CityName").length() > 2)
			   full_address += array_list.get(position).get("CityName") + ", ";

			if (array_list.get(position).get("countryName").length() > 2)
			   full_address += array_list.get(position).get("countryName");

			et1.setText(full_address);
		 }

		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		 }
	  });

	  sp2.setOnItemSelectedListener(new OnItemSelectedListener() {

		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view,
									int position, long id) {
			// TODO Auto-generated method stub


			String sp_name = sp2.getSelectedItem().toString();

			if (sp_name.equals("new")) {


			   i = new Intent(CommentActivity.this, Add_CommentAddress.class);

			   i.putExtra("address_position1", 0 + "");
			   i.putExtra("address_position2", 0 + "");

			   i.putExtra("laundryId", laundryId);
			   i.putExtra("laundryArray", laundryArray);
			   i.putExtra("laundryName", laundryName);
			   i.putExtra("online_offline", online_offline);

			   startActivity(i);
			   finish();
			   return;

			}

			String full_address2 = "";

			if (array_list.get(position).get("AddressLine1").length() > 2)
			   full_address2 = array_list.get(position).get("AddressLine1") + ", ";

			if (array_list.get(position).get("AddressLine2").length() > 2)
			   full_address2 += array_list.get(position).get("AddressLine2") + ", ";

			if (array_list.get(position).get("AddressLine3").length() > 2)
			   full_address2 += array_list.get(position).get("AddressLine3") + ", ";

			if (array_list.get(position).get("CityName").length() > 2)
			   full_address2 += array_list.get(position).get("CityName") + ", ";

			if (array_list.get(position).get("countryName").length() > 2)
			   full_address2 += array_list.get(position).get("countryName");

			et2.setText(full_address2);
		 }

		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		 }
	  });

	  et1 = (EditText) findViewById(R.id.editText1);
	  et2 = (EditText) findViewById(R.id.editText2);
	  et3 = (EditText) findViewById(R.id.editText3);

	  et1.setEnabled(false);
	  et2.setEnabled(false);
	  et1.setTextColor(Color.parseColor("#A4A1A1"));
	  et2.setTextColor(Color.parseColor("#A4A1A1"));

	  //Log.w("On Comment Activity Page 3", "running till here");

	  SharedPreferences prefs = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
	  Config.name = prefs.getString("name", "");
	  Config.isBookingService = false;

	  Button button_submit = (Button) findViewById(R.id.button1);

	  image_edit1 = (Button) findViewById(R.id.buttonedit_pickup);
	  image_edit1.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
			// TODO Auto-generated method stub
			int address_position1 = sp1.getSelectedItemPosition();
			int address_position2 = sp2.getSelectedItemPosition();

			AddressID = array_list.get(address_position1).get("AddressID");
			UserID = array_list.get(address_position1).get("UserID");
			AddressName = array_list.get(address_position1).get("AddressName");
			CityID = array_list.get(address_position1).get("CityID");
			CountryID = array_list.get(address_position1).get("CountryID");
			ContactNo = array_list.get(address_position1).get("ContactNo");
			AddressLine1 = array_list.get(address_position1).get("AddressLine1");
			AddressLine2 = array_list.get(address_position1).get("AddressLine2");
			AddressLine3 = array_list.get(address_position1).get("AddressLine3");
			countryName = array_list.get(address_position1).get("countryName");
			CityName = array_list.get(address_position1).get("CityName");

			i = new Intent(CommentActivity.this, EditAddressComment.class);

			i.putExtra("address_position1", address_position1 + "");
			i.putExtra("address_position2", address_position2 + "");

			i.putExtra("AddressID", AddressID);
			i.putExtra("UserID", UserID);
			i.putExtra("AddressName", AddressName);
			i.putExtra("CityID", CityID);
			i.putExtra("CountryID", CountryID);
			i.putExtra("ContactNo", ContactNo);
			i.putExtra("AddressLine1", AddressLine1);
			i.putExtra("AddressLine2", AddressLine2);
			i.putExtra("AddressLine3", AddressLine3);
			i.putExtra("countryName", countryName);
			i.putExtra("CityName", CityName);

			i.putExtra("laundryId", laundryId);
			i.putExtra("laundryArray", laundryArray);
			i.putExtra("laundryName", laundryName);
			i.putExtra("online_offline", online_offline);

			startActivity(i);
			finish();

		 }
	  });

	  image_edit2 = (Button) findViewById(R.id.button_edit2);
	  image_edit2.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
			// TODO Auto-generated method stub
			int address_position1 = sp1.getSelectedItemPosition();
			int address_position2 = sp2.getSelectedItemPosition();

			AddressID = array_list.get(address_position2).get("AddressID");
			UserID = array_list.get(address_position2).get("UserID");
			AddressName = array_list.get(address_position2).get("AddressName");
			CityID = array_list.get(address_position2).get("CityID");
			CountryID = array_list.get(address_position2).get("CountryID");
			ContactNo = array_list.get(address_position2).get("ContactNo");
			AddressLine1 = array_list.get(address_position2).get("AddressLine1");
			AddressLine2 = array_list.get(address_position2).get("AddressLine2");
			AddressLine3 = array_list.get(address_position2).get("AddressLine3");
			countryName = array_list.get(address_position2).get("countryName");
			CityName = array_list.get(address_position2).get("CityName");


			i = new Intent(CommentActivity.this, EditAddressComment.class);

			i.putExtra("address_position2", address_position2 + "");
			i.putExtra("address_position1", address_position1 + "");
			i.putExtra("AddressID", AddressID);
			i.putExtra("UserID", UserID);
			i.putExtra("AddressName", AddressName);
			i.putExtra("CityID", CityID);
			i.putExtra("CountryID", CountryID);
			i.putExtra("ContactNo", ContactNo);
			i.putExtra("AddressLine1", AddressLine1);
			i.putExtra("AddressLine2", AddressLine2);
			i.putExtra("AddressLine3", AddressLine3);
			i.putExtra("countryName", countryName);
			i.putExtra("CityName", CityName);
			i.putExtra("laundryId", laundryId);
			i.putExtra("laundryArray", laundryArray);
			i.putExtra("laundryName", laundryName);
			i.putExtra("online_offline", online_offline);

			startActivity(i);
			finish();

		 }
	  });


	  button_submit.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
			// TODO Auto-generated method stub
			Booking();


		 }
	  });

	  if (Network.HaveNetworkConnection(CommentActivity.this)) {
		 getAllAddresses();
	  } else {
		 final AlertUtil alert = new AlertUtil();
		 alert.confirmationAlert(CommentActivity.this,
				 getResources().getString(R.string.network_title),
				 getResources().getString(R.string.network_message),
				 new OnClickListener() {

					@Override
					public void onClick(View v) {
					   alert.release();
					   onBackPressed();
					}
				 });

	  }

   }

   void getAllAddresses() {
	  ProgressDialogClass.showProgressDialog(CommentActivity.this, getResources().getString(R.string.loading));

	  final Thread fetch_address = new Thread() {
		 @Override
		 public void run() {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JGetParsor j = new JGetParsor();
			json = j.makeHttpRequest(Config.manage_addresses + Config.userid, "POST", params);
			if (json != null) {
			   try {
				  Log.e("json", json.toString());
				  if (json.getInt("status") == 200) {
					 JSONArray json_array = json.getJSONArray("data");
					 for (int i = 0; i < json_array.length(); i++) {
						HashMap<String, String> hashmap = new HashMap<String, String>();
						JSONObject j_obj = json_array.getJSONObject(i);

						hashmap.put("AddressID", j_obj.getString("AddressID"));
						hashmap.put("UserID", j_obj.getString("UserID"));
						hashmap.put("AddressName", j_obj.getString("AddressName"));
						hashmap.put("CityID", j_obj.getString("CityID"));
						hashmap.put("CountryID", j_obj.getString("CountryID"));
						hashmap.put("ContactNo", j_obj.getString("ContactNo"));

						hashmap.put("AddressLine1", j_obj.getString("AddressLine1"));
						hashmap.put("AddressLine2", j_obj.getString("AddressLine2"));
						hashmap.put("AddressLine3", j_obj.getString("AddressLine3"));
						hashmap.put("countryName", j_obj.getString("countryName"));
						hashmap.put("CityName", j_obj.getString("CityName"));
						hashmap.put("DefaultAddress", j_obj.getString("DefaultAddress"));

						pick_up.add(j_obj.getString("AddressName"));
						array_list.add(hashmap);

					 }

				  }
				  pick_up.add("new");
			   } catch (JSONException e) {
				  // TODO Auto-generated catch block
			   }
			}
		 }
	  };
	  fetch_address.start();

	  final Thread display = new Thread(new Runnable() {

		 @Override
		 public void run() {
			// TODO Auto-generated method stub
			try {
			   fetch_address.join();
			} catch (InterruptedException e) {
			   // TODO Auto-generated catch block
			}
			if (mHandler != null) {
			   mHandler.post(new Runnable() {

				  @Override
				  public void run() {
					 // TODO Auto-generated method stub

					 sp1.setAdapter(new ArrayAdapter<String>(CommentActivity.this, android.R.layout.simple_spinner_dropdown_item, pick_up));
					 sp2.setAdapter(new ArrayAdapter<String>(CommentActivity.this, android.R.layout.simple_spinner_dropdown_item, pick_up));

					 if (pick_up.size() > 1) {


						String full_address = "";

						if (array_list.get(0).get("AddressLine1").length() > 1)
						   full_address = array_list.get(0).get("AddressLine1") + ", ";

						if (array_list.get(0).get("AddressLine2").length() > 1)
						   full_address += array_list.get(0).get("AddressLine2") + ", ";

						if (array_list.get(0).get("AddressLine3").length() > 1)
						   full_address += array_list.get(0).get("AddressLine3") + ", ";

						if (array_list.get(0).get("CityName").length() > 1)
						   full_address += array_list.get(0).get("CityName") + ", ";

						if (array_list.get(0).get("countryName").length() > 1)
						   full_address += array_list.get(0).get("countryName");

						et1.setText(full_address);
						et2.setText(full_address);

						if (address_position1 != null) {
						   int position = Integer.parseInt(address_position1);
						   sp1.setSelection(position);
						   if (array_list.get(position).get("AddressLine1").length() > 1)
							  full_address = array_list.get(position).get("AddressLine1") + ", ";

						   if (array_list.get(position).get("AddressLine2").length() > 1)
							  full_address += array_list.get(position).get("AddressLine2") + ", ";

						   if (array_list.get(position).get("AddressLine3").length() > 1)
							  full_address += array_list.get(position).get("AddressLine3") + ", ";

						   if (array_list.get(position).get("CityName").length() > 1)
							  full_address += array_list.get(position).get("CityName") + ", ";

						   if (array_list.get(position).get("countryName").length() > 1)
							  full_address += array_list.get(position).get("countryName");

						   et1.setText(full_address);
						}

						if (address_position2 != null) {
						   int position = Integer.parseInt(address_position2);
						   sp2.setSelection(position);
						   if (array_list.get(position).get("AddressLine1").length() > 1)
							  full_address = array_list.get(position).get("AddressLine1") + ", ";

						   if (array_list.get(position).get("AddressLine2").length() > 1)
							  full_address += array_list.get(position).get("AddressLine2") + ", ";

						   if (array_list.get(position).get("AddressLine3").length() > 1)
							  full_address += array_list.get(position).get("AddressLine3") + ", ";

						   if (array_list.get(position).get("CityName").length() > 1)
							  full_address += array_list.get(position).get("CityName") + ", ";

						   if (array_list.get(position).get("countryName").length() > 1)
							  full_address += array_list.get(position).get("countryName");

						   et2.setText(full_address);
						}
					 }

					 //	getBanner();
					 ProgressDialogClass.dismissProgressDialog();
				  }
			   });
			}

		 }
	  });
	  display.start();
   }


   public void Booking() {
	  json = null;
	  ProgressDialogClass.showProgressDialog(CommentActivity.this, getResources().getString(R.string.loading));

	  new AsyncTask<Void, Void, JSONObject>() {

		 public String p_id, d_id;
		 public String url;
		 public String strDate;
		 public String comment;

		 @Override
		 protected void onPreExecute() {
			super.onPreExecute();
			comment = et3.getText().toString();
//			nameValuePair/s.add(new BasicNameValuePair("comment", comment));

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			strDate = sdf.format(c.getTime());

			int p_index = sp1.getSelectedItemPosition();
			p_id = array_list.get(p_index).get("AddressID");


			int d_index = sp2.getSelectedItemPosition();
			d_id = array_list.get(d_index).get("AddressID");


			url = Config.Order_Offline_Url;
			if (!online_offline) {
//			   nameValuePairs.add(new BasicNameValuePair("orders", laundryArray));
			   url = Config.Order_Online_Url;
			}

		 }

		 @Override
		 protected JSONObject doInBackground(Void... params) {


			String data = null;
			String parameters =
					"laundry_id=" + laundryId + "" +
					"&user_id=" + Config.userid +
					"&cur_lat=" + Config.latitude +
					"&cur_long=" + Config.longitude +
					"&comment=" + comment + deal_text +
					"&LaundryOrderDate=" + strDate +
					"&pick_up_id=" + p_id +
					"&drop_off_id=" + d_id +
					"&orders=" + laundryArray;
			try {
			   data = DownloadJsonContent.downloadContentUsingPostMethod(url, parameters);
			   Log.e("dataCome", data);
			} catch (IOException e) {
			   e.printStackTrace();
			}
			try {
			   return new JSONObject(data);
			} catch (JSONException e) {
			   e.printStackTrace();
			}

			return null;
		 }

		 @Override
		 protected void onPostExecute(JSONObject jsonObject) {
			super.onPostExecute(jsonObject);
			try {
			   if (jsonObject != null && jsonObject.has("status") && !jsonObject.isNull("status")
					   && jsonObject.getString
					   ("status")
					   .equals("200")) {

				  finish();

				  Intent intent = new Intent(getApplicationContext(), BaseFragmentActivity.class);
				  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				  startActivity(intent);

				  Log.e("error order", "6");

				  Config.isBookingService = true;
				  if (jsonObject.has("stauts_message") && !jsonObject.isNull("stauts_message")) {
					 Toast.makeText(CommentActivity.this, jsonObject.getString(
							 "stauts_message"), Toast.LENGTH_LONG).show();
				  }
				  Log.e("error order", "1");
			   } else {

				  AlertUtil alert = new AlertUtil();
				  if (jsonObject.has("stauts_message") && !jsonObject.isNull("stauts_message")) {
					 alert.messageAlert(
							 CommentActivity.this, "", json.getString("stauts_message"));
				  }
				  Log.e("error order", "2");
			   }
			} catch (JSONException e) {
			   e.printStackTrace();
			   Log.e("error", "json error");
			   Toast.makeText(getApplicationContext(), "Order failed! Data validation error", Toast
					   .LENGTH_LONG)
					   .show();
			   Log.e("error order", "3");
			}
			Log.e("error order", "4");
			ProgressDialogClass.dismissProgressDialog();
			Log.e("error order", "5");


//			12-03 14:56:42.099 14208-14293/com.app.znsoftechlaundry E/dataCome: {"status":200,"stauts_message":"Order Placed Successfully","data":1}
//			12-03 14:56:42.117 14208-14208/com.app.znsoftechlaundry E/error order: 6
//			12-03 14:56:42.136 14208-14208/com.app.znsoftechlaundry E/error order: 1
//			12-03 14:56:42.136 14208-14208/com.app.znsoftechlaundry E/error order: 4
//			12-03 14:56:42.151 14208-14208/com.app.znsoftechlaundry E/error order: 5
//			12-03 14:56:42.218 14208-14256/com.app.znsoftechlaundry E/rsC++: RS CPP error (masked by previous error): Allocation creation failed
//			12-03 14:56:42.218 14208-14256/com.app.znsoftechlaundry E/rsC++: RS CPP error (masked by previous error): Allocation creation failed
//			12-03 14:56:42.218 14208-14256/com.app.znsoftechlaundry A/libc: Fatal signal 11 (SIGSEGV), code 1, fault addr 0x28 in tid 14256 (RenderThread)

		 }
	  }.execute();



   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.home_menu, menu);
	  return true;
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
	  } else if (id == R.id.direct_to_home) {
		 Intent intent = new Intent(CommentActivity.this, BaseFragmentActivity.class);
		 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		 startActivity(intent);

		 overridePendingTransition(R.anim.right_in, R.anim.left_out);
	  }

	  return super.onOptionsItemSelected(item);
   }

   @Override
   public void onBackPressed() {
	  // TODO Auto-generated method stub

//	  Chunked stream ended unexpectedly
//	  11-25 16:10:10.775 18996-18996/com.app.znsoftechlaundry E/AndroidRuntime: FATAL EXCEPTION: main
//	  java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
//	  at android.support.v4.app.FragmentManagerImpl.checkStateLoss(FragmentManager.java:1377)
//	  at android.support.v4.app.FragmentManagerImpl.popBackStackImmediate(FragmentManager.java:504)
//	  at android.support.v4.app.FragmentActivity.onBackPressed(FragmentActivity.java:178)
//	  at com.znsoftech.znsoftechlaundry.CommentActivity.onBackPressed(CommentActivity.java:638)
//	  at com.znsoftech.znsoftechlaundry.CommentActivity$10$1.run(CommentActivity.java:579)
//	  at android.os.Handler.handleCallback(Handler.java:725)
//	  at android.os.Handler.dispatchMessage(Handler.java:92)
//	  at android.os.Looper.loop(Looper.java:176)
//	  at android.app.ActivityThread.main(ActivityThread.java:5302)
//	  at java.lang.reflect.Method.invokeNative(Native Method)
//	  at java.lang.reflect.Method.invoke(Method.java:511)
//	  at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:1102)
//	  at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:869)
//	  at dalvik.system.NativeStart.main(Native Method)


	  super.onBackPressed();
	  overridePendingTransition(R.anim.left_in, R.anim.right_out);
   }

}
