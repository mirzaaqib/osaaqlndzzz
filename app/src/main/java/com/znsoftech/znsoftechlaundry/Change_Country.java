package com.znsoftech.znsoftechlaundry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.znsoftech.znsoftechlaundry.json.DownloadJsonContent;
import com.znsoftech.znsoftechlaundry.util.ProgressDialogClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Change_Country extends Fragment {

   SharedPreferences preferences;
   private Spinner spinner_city, spinner_country;

   @Override
   public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  setHasOptionsMenu(true);
	  preferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	  View view = inflater.inflate(R.layout.change_country, container, false);
	  return view;
   }

   @Override
   public void onViewCreated(View view, Bundle savedInstanceState) {
	  super.onViewCreated(view, savedInstanceState);
	  spinner_country = (Spinner) view.findViewById(R.id.spinner_country_change_country);
	  spinner_city = (Spinner) view.findViewById(R.id.spinner_country_change_city);

	  AsyncCountryDown asyncCountryDown = new AsyncCountryDown();
	  asyncCountryDown.execute();

	  Button button_submit = (Button) view.findViewById(R.id.submit_changecountry);
	  button_submit.setOnClickListener(new View.OnClickListener() {
		 @Override
		 public void onClick(View v) {
			new AsyncSubmitCountry().execute();
		 }
	  });
   }

   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	  super.onCreateOptionsMenu(menu, inflater);

   }

   void getCityName(final String s) {

	  new AsyncTask<Void, Void, ArrayList<String>>() {


		 public ArrayList<String> arrayList;

		 @Override
		 protected void onPreExecute() {
			super.onPreExecute();

			ProgressDialogClass.showProgressDialog(getActivity(),
					"Loading...");

		 }

		 @Override
		 protected ArrayList<String> doInBackground(Void... params) {
			try {
			   String data = DownloadJsonContent.downloadContentUsingPostMethod(Config.City_Url,
					   "country_name=" + s);
			   JSONObject jsonObject = new JSONObject(data);
			   JSONArray js = jsonObject.getJSONArray("data");
			   arrayList = new ArrayList<String>();

			   Config.cityArray.clear();
			   for (int i = 0; i < js.length(); i++) {
				  JSONObject object = js.getJSONObject(i);
				  String s = object.getString("CityName");
				  arrayList.add(s);
				  Config.cityArray.add(s);
			   }
			   Log.e("arrallist", arrayList.toString());


			} catch (IOException e) {
			   e.printStackTrace();
			} catch (JSONException e) {
			   e.printStackTrace();
			}
			return arrayList;
		 }

		 @Override
		 protected void onPostExecute(ArrayList<String> strings) {
			super.onPostExecute(strings);
			if (spinner_city != null) {
			   spinner_city.setAdapter(new ArrayAdapter<String>(getActivity(),
					   android.R.layout.simple_spinner_dropdown_item,
					   strings));
			}
			Log.e("error city", strings.toString());
//			if (arrayList != null) {
//			   TinyDB tinyDB = new TinyDB(getActivity());
//			   Collections.sort(arrayList);
//			   tinyDB.putListString("CityArray", arrayList);
//			}
			spinner_city.setSelection(arrayList.indexOf(Config.city));
			ProgressDialogClass.dismissProgressDialog();
		 }

	  }.execute();
   }

   @Override
   public void onStop() {
	  super.onStop();

   }

   class AsyncCountryDown extends AsyncTask<String, String, String> {

	  private ArrayList<String> arrCountryList, arrCountryListId;

	  @Override
	  protected void onPreExecute() {
		 super.onPreExecute();
		 ProgressDialogClass.showProgressDialog(getActivity(),
				 "Loading...");
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
//			Config.cityArray=arrCountryList;

		 } catch (JSONException e) {
			e.printStackTrace();
		 } catch (IOException e) {
			e.printStackTrace();
		 }
		 return null;
	  }

	  @Override
	  protected void onPostExecute(String s) {

		 if (!isCancelled()) {
			super.onPostExecute(s);
			if (spinner_country != null && arrCountryList != null) {
			   spinner_country.setAdapter(new ArrayAdapter<String>(getActivity(),
					   android.R.layout.simple_spinner_dropdown_item,
					   arrCountryList
			   ));
			}
			spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			   @Override
			   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				  getCityName(arrCountryList.get(position));
			   }

			   @Override
			   public void onNothingSelected(AdapterView<?> parent) {

			   }
			});
			spinner_country.setSelection(arrCountryList.indexOf(Config.country));
		 }
		 ProgressDialogClass.dismissProgressDialog();
	  }
   }

   class AsyncSubmitCountry extends AsyncTask<String, String, String> {
	  private String country, city;

	  @Override
	  protected void onPreExecute() {
		 super.onPreExecute();

		 if (spinner_country != null) {
			country = spinner_country.getSelectedItem().toString();
			if (Config.cityArray.size() > 0) {
			   city = spinner_city.getSelectedItem().toString();
			   ProgressDialogClass.showProgressDialog(getActivity(), "Submitting request....");
			} else {
			   Toast.makeText(getActivity(), "Service is not available in " + country, Toast
					   .LENGTH_LONG).show();
			   this.cancel(true);
			   return;

			}

			Config.city = city;
			Config.country = country;
			preferences.edit().putString(FlagLaun.cCITY, city).commit();
			preferences.edit().putString(FlagLaun.cCOUNTRY, country).commit();
		 }
	  }

	  @Override
	  protected String doInBackground(String... params) {
		 String json = null;
		 try {
			json = DownloadJsonContent.downloadContentUsingPostMethod(Config.SET_COUNTRY,
					"country_name=" + country + "&user_id=" + Config.userid + "&city_name=" + city);
//			Config.country = country;
//			Config.city = city;
			Log.e("inside chan back", Config.country + Config.city);
		 } catch (IOException e) {
			e.printStackTrace();
		 }
		 return json;
	  }

	  @Override
	  protected void onPostExecute(String s) {
		 if (!isCancelled()) {
			super.onPostExecute(s);
			if (s != null) {
			   try {
				  JSONObject object = new JSONObject(s);
				  if (object.has("status") && !object.isNull("status")) {
					 if (object.getInt("status") == 200) {
						//everything is fine
						// TODO: 15/12/15
						TinyDB tinyDB = new TinyDB(getActivity());
						Collections.sort(Config.cityArray);
						tinyDB.putListString("CityArray", Config.cityArray);
						Log.e("tinydb lin271", Config.cityArray.toString());


						Intent intent = new Intent(getActivity(), LoginActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
								.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);


					 } else {
						String message = object.getString("stauts_message");
						DialogUniversalCustom dialog = new DialogUniversalCustom();
						dialog.init("Error!", message, "OK", null, new DialogInterface.OnClickListener() {
						   @Override
						   public void onClick(DialogInterface dialog, int which) {

						   }
						}, null).setCancelable(false);
						dialog.show(getActivity().getSupportFragmentManager(), "dialog");
					 }
				  } else {
					 Toast.makeText(getActivity(), "Error! Server", Toast.LENGTH_LONG).show();
				  }
			   } catch (JSONException e) {
				  e.printStackTrace();
				  Log.e("json", e.getMessage());
			   }
			}
			ProgressDialogClass.dismissProgressDialog();
		 }
	  }
   }
}
