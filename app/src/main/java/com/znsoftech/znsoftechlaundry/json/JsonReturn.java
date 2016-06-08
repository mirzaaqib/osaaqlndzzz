package com.znsoftech.znsoftechlaundry.json;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JsonReturn {

   JSONObject jObj = null;
   JSONArray jArray = null;
   String json = "";

   public static ArrayList<JSONObject> BannerparseJson1(String url) {
	  JSONObject contacts = null;
	  ArrayList<JSONObject> contactList = new ArrayList<JSONObject>();

	  // Creating JSON Parser instance
	  JSONParser jParser = new JSONParser();

	  // getting JSON string from URL
	  JSONObject json = jParser.getJSONFromUrl(url);
	  if (json != null) {
		 try {
			contacts = json.getJSONArray("data").getJSONObject(0);
			contactList.add(contacts);

		 } catch (JSONException e) {
			e.printStackTrace();
		 }
	  } else {
		 JSONObject map = new JSONObject();
		 try {
			map.put("message",
					"There seems to be some problem while processing your request. Please try after some time.");
			map.put("status", "505");
		 } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }

		 contactList.add(map);
	  }
	  return contactList;
   }

   public JSONArray postData(String url, ArrayList<BasicNameValuePair> valuesHashMap) {
	  // Create a new HttpClient and Post Header
	  json = null;
	  jObj = null;
	  jArray = new JSONArray();
	  HttpClient httpclient = new DefaultHttpClient();
	  HttpPost httppost = new HttpPost(url);

	  try {
		 // Add your data

		 httppost.setEntity(new UrlEncodedFormEntity(valuesHashMap));

		 // Execute HTTP Post Request
		 HttpResponse response = httpclient.execute(httppost);

		 InputStream is = response.getEntity().getContent();
		 BufferedInputStream bis = new BufferedInputStream(is);
		 ByteArrayBuffer baf = new ByteArrayBuffer(20);

		 int current = 0;

		 while ((current = bis.read()) != -1) {
			baf.append((byte) current);
		 }

			/* Convert the Bytes read to a String. */
		 json = new String(baf.toByteArray());
		 Log.v("texttexttext", json);

		 try {
			jObj = new JSONObject(json);

			if (!jObj.getString("status").equals("200")) {
			   JSONObject jObj1 = new JSONObject();
			   jObj1.put("status", jObj.get("status"));
			   jObj1.put("stauts_message", jObj.get("stauts_message"));

			   jArray.put(jObj1);
			} else {
			   jArray = jObj.getJSONArray("data");
			}
			// Log.v("jObj success---------------", "" + jObj);
		 } catch (JSONException e) {
			JSONObject jObj1 = new JSONObject();
			try {
			   jObj1.put("status", "5000");
			   jObj1.put("stauts_message", "Some error occur. Please try again");
			   jArray.put(jObj1);
			} catch (JSONException e1) {
			   // TODO Auto-generated catch block
			   e1.printStackTrace();
			}
		 }
	  } catch (ClientProtocolException e) {
		 JSONObject jObj1 = new JSONObject();
		 try {
			jObj1.put("status", jObj.get("status"));
			jObj1.put("stauts_message", jObj.get("stauts_message"));
			jArray.put(jObj1);
		 } catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		 }

		 // TODO Auto-generated catch block
	  } catch (IOException e) {
		 // TODO Auto-generated catch block

		 JSONObject jObj1 = new JSONObject();
		 try {
			jObj1.put("status", "5000");
			jObj1.put("stauts_message",
					"Some error occur. Please try again");
			jArray.put(jObj1);
		 } catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		 }

	  }
	  return jArray;
   }

   public JSONObject setReview(String url, ArrayList<BasicNameValuePair> valuesHashMap) {
	  // Create a new HttpClient and Post Header
	  json = null;
	  jObj = null;
	  jArray = null;
	  jArray = new JSONArray();
	  JSONObject jObj1 = new JSONObject();
	  HttpClient httpclient = new DefaultHttpClient();
	  HttpPost httppost = new HttpPost(url);

	  try {
		 // Add your data

		 httppost.setEntity(new UrlEncodedFormEntity(valuesHashMap));

		 // Execute HTTP Post Request
		 HttpResponse response = httpclient.execute(httppost);

		 InputStream is = response.getEntity().getContent();
		 BufferedInputStream bis = new BufferedInputStream(is);
		 ByteArrayBuffer baf = new ByteArrayBuffer(20);

		 int current = 0;

		 while ((current = bis.read()) != -1) {
			baf.append((byte) current);
		 }

			/* Convert the Bytes read to a String. */
		 json = new String(baf.toByteArray());
		 Log.v("jObj---------------", "" + json);

		 try {
			jObj = new JSONObject(json);
			if (jObj.has("status")) {

			   jObj1.put("status", jObj.get("status"));
			   jObj1.put("stauts_message", jObj.get("stauts_message"));

			} else {
			   jObj1.put("status", "5000");
			   jObj1.put("stauts_message",
					   "Some error occur. Please try again");
			}
		 } catch (JSONException e) {

			try {
			   jObj1.put("status", "5000");
			   jObj1.put("stauts_message",
					   "Some error occur. Please try again");
			} catch (JSONException e1) {
			   // TODO Auto-generated catch block
			   e1.printStackTrace();
			}
			e.printStackTrace();
		 }

	  } catch (ClientProtocolException e) {
		 // TODO Auto-generated catch block

		 try {
			jObj1.put("status", "5000");
			jObj1.put("stauts_message", "Some error occur. Please try again");
		 } catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		 }


	  } catch (IOException e) {
		 // TODO Auto-generated catch block

		 try {
			jObj1.put("status", "5000");
			jObj1.put("stauts_message", "Some error occur. Please try again");
		 } catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		 }


	  }
	  return jObj1;

   }

   public JSONArray postLaundryData(String url, ArrayList<BasicNameValuePair> valuesHashMap) {
	  // Create a new HttpClient and Post Header
	  json = null;
	  jObj = null;
	  jArray = null;
	  jArray = new JSONArray();
	  HttpClient httpclient = new DefaultHttpClient();
	  HttpPost httppost = new HttpPost(url);

	  try {
		 // Add your data

		 httppost.setEntity(new UrlEncodedFormEntity(valuesHashMap));

		 // Execute HTTP Post Request
		 HttpResponse response = httpclient.execute(httppost);

		 InputStream is = response.getEntity().getContent();
		 BufferedInputStream bis = new BufferedInputStream(is);
		 ByteArrayBuffer baf = new ByteArrayBuffer(20);

		 int current = 0;

		 while ((current = bis.read()) != -1) {
			baf.append((byte) current);
		 }

			/* Convert the Bytes read to a String. */
		 json = new String(baf.toByteArray());
		 Log.v("jObj---------------", "" + json);

		 try {
			jObj = new JSONObject(json);
			if (!jObj.getString("status").equals("200")) {
			   JSONObject jObj1 = new JSONObject();
			   jObj1.put("status", jObj.get("status"));
			   jObj1.put("stauts_message", jObj.get("stauts_message"));

			   jArray.put(jObj1);
			} else {
			   jArray = jObj.getJSONArray("data");
			}
		 } catch (JSONException e) {
			JSONObject jObj1 = new JSONObject();
			try {
			   if (jObj != null) {
				  jObj1.put("status", jObj.get("status"));
				  jObj1.put("stauts_message", jObj.get("stauts_message"));
			   } else {
				  jObj1.put("status", "5000");
				  jObj1.put("stauts_message",
						  "Some error occur. Please try again");
			   }
			   jArray.put(jObj1);
			} catch (JSONException e1) {
			   // TODO Auto-generated catch block
			   try {
				  jObj1.put("status", "5000");
				  jObj1.put("stauts_message",
						  "Some error occur. Please try again");
			   } catch (JSONException e2) {
				  // TODO Auto-generated catch block
				  e2.printStackTrace();
			   }

			   jArray.put(jObj1);
			   e1.printStackTrace();
			}

			Log.v("jObj---------------", "" + jObj);
			Log.v("JSON Parser", "Error parsing data " + e.toString());
		 }
	  } catch (ClientProtocolException e) {
		 // TODO Auto-generated catch block
		 JSONObject jObj1 = new JSONObject();
		 try {
			jObj1.put("status", "5000");
			jObj1.put("stauts_message",
					"Some error occur. Please try again");
			jArray.put(jObj1);
		 } catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		 }
	  } catch (IOException e) {
		 // TODO Auto-generated catch block
		 JSONObject jObj1 = new JSONObject();
		 try {
			jObj1.put("status", "5000");
			jObj1.put("stauts_message",
					"Some error occur. Please try again");
			jArray.put(jObj1);
		 } catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		 }
	  }
	  return jArray;
   }

   public JSONObject setBookmark(String url, ArrayList<BasicNameValuePair> valuesHashMap) {
	  // Create a new HttpClient and Post Header
	  json = null;
	  jObj = null;
	  JSONObject tempJsonObj = new JSONObject();
	  HttpClient httpclient = new DefaultHttpClient();
	  HttpPost httppost = new HttpPost(url);

	  try {
		 // Add your data

		 httppost.setEntity(new UrlEncodedFormEntity(valuesHashMap));

		 // Execute HTTP Post Request
		 HttpResponse response = httpclient.execute(httppost);

		 InputStream is = response.getEntity().getContent();
		 BufferedInputStream bis = new BufferedInputStream(is);
		 ByteArrayBuffer baf = new ByteArrayBuffer(20);

		 int current = 0;

		 while ((current = bis.read()) != -1) {
			baf.append((byte) current);
		 }

			/* Convert the Bytes read to a String. */
		 json = new String(baf.toByteArray());
		 Log.v("jObj---------------", "" + json);

		 try {
			jObj = new JSONObject(json);
			if (!jObj.getString("status").equals("200")) {
			   tempJsonObj.put("status", jObj.get("status"));
			   tempJsonObj.put("stauts_message",
					   jObj.get("stauts_message"));

			} else {
			   tempJsonObj.put("IsBookmark", jObj.getString("data"));
			}
		 } catch (JSONException e) {
			try {
			   if (jObj != null) {
				  tempJsonObj.put("status", jObj.get("status"));
				  tempJsonObj.put("stauts_message",
						  jObj.get("stauts_message"));
			   } else {
				  tempJsonObj.put("status", "5000");
				  tempJsonObj.put("stauts_message",
						  "Some error occur. Please try again");
			   }
			} catch (JSONException e1) {
			   // TODO Auto-generated catch block
			   try {
				  tempJsonObj.put("status", "5000");
				  tempJsonObj.put("stauts_message",
						  "Some error occur. Please try again");
			   } catch (JSONException e2) {
				  // TODO Auto-generated catch block
				  e2.printStackTrace();
			   }

			   e1.printStackTrace();
			}

			Log.v("jObj---------------", "" + jObj);
			Log.v("JSON Parser", "Error parsing data " + e.toString());
		 }
	  } catch (ClientProtocolException e) {
		 // TODO Auto-generated catch block
		 try {
			tempJsonObj.put("status", "5000");
			tempJsonObj.put("stauts_message", "Some error occur. Please try again");
		 } catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		 }
	  } catch (IOException e) {
		 // TODO Auto-generated catch block
		 try {
			tempJsonObj.put("status", "5000");
			tempJsonObj.put("stauts_message", "Some error occur. Please try again");
		 } catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		 }
	  }
	  return tempJsonObj;
   }
}