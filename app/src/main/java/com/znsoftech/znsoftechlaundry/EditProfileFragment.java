package com.znsoftech.znsoftechlaundry;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.znsoftech.znsoftechlaundry.json.DownloadJsonContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EditProfileFragment extends ActionBarActivity {

   Context mContext = null;
   EditText eName, eEmail, ePassword, ePasswordConfirm, eMobile, ePhone, eAddress, eCity, eCountry;
   Spinner sCity;
   private Spinner spinner_operator;


   @Override
   public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);


   }


   public void initialize() {

	  eName = (EditText) findViewById(R.id.e_name);
	  eName = (EditText) findViewById(R.id.e_email);
	  eName = (EditText) findViewById(R.id.e_password);
	  eName = (EditText) findViewById(R.id.e_password_confirm);
	  eName = (EditText) findViewById(R.id.e_mobile);
	  eName = (EditText) findViewById(R.id.e_phone);
	  eName = (EditText) findViewById(R.id.e_address);
	  spinner_operator = (Spinner) findViewById(R.id.spinner_operator);
	  sCity = (Spinner) findViewById(R.id.spinner_city);


	  final String[] operator_items = new String[]{"050", "052", "055", "056"};


	  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext
			  , android.R.layout.simple_spinner_dropdown_item, operator_items);
//	  spinner_operator.setAdapter(arrayAdapter);

   }

   class DownloadProfile extends AsyncTask<String, Integer, Void> {

	  JSONObject rootObject;
	  private String data, userId, name, email, password, phone, mobile, address, city,
			  state, country;
	  private JSONObject dataObject;

	  @Override
	  protected void onPreExecute() {
		 super.onPreExecute();
		 setContentView(R.layout.loading);
	  }

	  @Override
	  protected Void doInBackground(String... params) {
		 try {
			data = DownloadJsonContent.downloadContentUsingPostMethod(Config.Login_Url,
					"iemail=" + params[0] + "&" + "ipwd=" + params[1]);
			rootObject = new JSONObject(data);
			if (rootObject.has("status") && !rootObject.isNull("status") && rootObject.getString
					("status").equalsIgnoreCase("200")) {
			   dataObject = rootObject.getJSONObject("data");

			   if (isRight(dataObject, "UserID")) {
				  userId = dataObject.getString("UserID");
			   }
			   if (isRight(dataObject, "Name")) {
				  name = dataObject.getString("Name");
			   }
			   if (isRight(dataObject, "Email")) {
				  email = dataObject.getString("Email");
			   }
			   if (isRight(dataObject, "Password")) {
				  password = dataObject.getString("Password");
			   }
			   if (isRight(dataObject, "Phone")) {
				  phone = dataObject.getString("Phone");
			   }
			   if (isRight(dataObject, "Mobile")) {
				  mobile = dataObject.getString("Mobile");
			   }
			   if (isRight(dataObject, "Address")) {
				  address = dataObject.getString("Address");
			   }
			   if (isRight(dataObject, "City")) {
				  city = dataObject.getString("City");
			   }
			   if (isRight(dataObject, "State")) {
				  state = dataObject.getString("State");
			   }
			   if (isRight(dataObject, "Country")) {
				  country = dataObject.getString("Country");
			   }

			} else if (!rootObject.getString("status").equals("200")) {
			   // TODO something

			}


		 } catch (IOException e) {
			e.printStackTrace();
		 } catch (JSONException e) {
			e.printStackTrace();
		 }

		 return null;
	  }

	  @Override
	  protected void onPostExecute(Void aVoid) {
		 super.onPostExecute(aVoid);
//		 EditText ename,eemail,epassword,ephone,emobile,eaddress,ecity,state,country;
		 try {
			if (rootObject != null && rootObject.getString("status").equals("200")) {
			   setContentView(R.layout.edit_profile);
//			   View v=getView();
			   eName = (EditText) findViewById(R.id.e_name);
			   eEmail = (EditText) findViewById(R.id.e_email);
			   ePassword = (EditText) findViewById(R.id.e_password);
			   ePasswordConfirm = (EditText) findViewById(R.id.e_password_confirm);
			   ePhone = (EditText) findViewById(R.id.e_phone);
			   eMobile = (EditText) findViewById(R.id.e_mobile);
			   eAddress = (EditText) findViewById(R.id.e_address);

			   eName.setText(name);
			   eEmail.setText(email);
			   ePhone.setText(phone);
			   eMobile.setText(mobile);
			   eAddress.setText(address);
//			   eCity.setText(city);
//			   eCountry.setText(country);
			} else {
			   //TODO something
			}

		 } catch (JSONException e) {
			e.printStackTrace();
		 }
	  }

	  private boolean isRight(JSONObject obj, String jsonName) {
		 if (obj.has(jsonName) && !obj.isNull(jsonName)) {
			return true;
		 } else
			return false;
	  }
   }

}
