package com.znsoftech.znsoftechlaundry;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.znsoftech.znsoftechlaundry.json.JGetParsor;
import com.znsoftech.znsoftechlaundry.network.Network;
import com.znsoftech.znsoftechlaundry.util.AlertUtil;
import com.znsoftech.znsoftechlaundry.util.ProgressDialogClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class New_Rate extends FragmentActivity {

   Handler mHandler = new Handler();
   JSONObject json;
   String url_to_call;


   Button ok;
   EditText editText_msg;
   int tag1, tag2, tag3, tag4;
   String review, laundryId;

   RatingBar ratingBar_price,
		   ratingBar_service,
		   ratingBar_pickup,
		   ratingBar_dropoff;

   // private GoogleMap mMap;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.new_rating);

	  editText_msg = (EditText) findViewById(R.id.editText1);
	  ratingBar_price = (RatingBar) findViewById(R.id.ratingbar_pricequality);
	  ratingBar_service = (RatingBar) findViewById(R.id.ratingbar_servicequality);
	  ratingBar_pickup = (RatingBar) findViewById(R.id.ratingbar_pickupquality);
	  ratingBar_dropoff = (RatingBar) findViewById(R.id.ratingbar_dropoffquality);

	  ok = (Button) findViewById(R.id.button_ok);
	  tag1 = tag2 = tag3 = tag4 = 1;
	  Intent intent = getIntent();
	  laundryId = intent.getExtras().getString("LaundryID");

	  ok.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
			// TODO Auto-generated method stub
			if (Network.HaveNetworkConnection(New_Rate.this)) {

			   //getting properties should be here
			   review = editText_msg.getText().toString();

			   update_rating();
			} else {
			   final AlertUtil alert = new AlertUtil();
			   alert.confirmationAlert(New_Rate.this, getResources()
							   .getString(R.string.network_title), getResources()
							   .getString(R.string.network_message),
					   new OnClickListener() {

						  @Override
						  public void onClick(View v) {

							 alert.release();
						  }
					   });

			}

		 }
	  });


	  update_rating_onStart();

   }


   private void update_rating_onStart() {
	  // TODO Auto-generated method stub

	  json = null;
	  ProgressDialogClass.showProgressDialog(New_Rate.this, getResources().getString(R.string.please_wait));
	  final Thread getdata = new Thread() {
		 @SuppressWarnings("deprecation")
		 public void run() {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JGetParsor j = new JGetParsor();
			url_to_call = Config.set_ratings_onstart + "laundryid/" + laundryId + "/userid/" + Config.userid;
			json = j.makeHttpRequest(url_to_call, "POST", params);
		 }
	  };
	  getdata.start();

	  final Thread show = new Thread(new Runnable() {

		 @Override
		 public void run() {
			// TODO Auto-generated method stub
			try {
			   getdata.join();
			} catch (InterruptedException e) {
			   // TODO Auto-generated catch block
			   //e.printStackTrace();
			}

			if (mHandler != null) {
			   mHandler.post(new Runnable() {

				  @Override
				  public void run() {
					 // TODO Auto-generated method stub
					 ProgressDialogClass.dismissProgressDialog();
					 if (json != null) {
						try {
						   if (json.getInt("status") == 200) {
							  //Toast.makeText(New_Rate.this, json.getString("status_message"),Toast.LENGTH_LONG).show();
							  JSONArray j_arr = json.getJSONArray("data");
							  JSONObject j_obj = j_arr.getJSONObject(0);

							  Float PriceQuality = Float.parseFloat(j_obj.getString("PriceQuality"));
							  Float ServiceQuality = Float.parseFloat(j_obj.getString("ServiceQuality"));
							  Float PickupQuality = Float.parseFloat(j_obj.getString("PickupQuality"));
							  Float DropoffQuality = Float.parseFloat(j_obj.getString("DropoffQuality"));


							  String Comments = j_obj.getString("Comments");


//                                        ratingStyle(ratingBar_dropoff); //This functions allows us to customize rating bar programmatically


							  ratingBar_price.setRating(PriceQuality);
							  ratingBar_service.setRating(ServiceQuality);
							  ratingBar_pickup.setRating(PickupQuality);
							  ratingBar_dropoff.setRating(DropoffQuality);


							  editText_msg.setText(Comments);

							  // Toast.makeText(New_Rate.this, PriceQuality + " " + ServiceQuality + " " + PickupQuality + " " + DropoffQuality, Toast.LENGTH_LONG).show();

						   }
						} catch (JSONException e) {
						   // TODO Auto-generated catch block
						   //e.printStackTrace();
						}
					 }
				  }
			   });
			}
		 }
	  });
	  show.start();
   }


   private void update_rating() {
	  // TODO Auto-generated method stub
	  ProgressDialogClass.showProgressDialog(New_Rate.this, getResources().getString(R.string.updating));
	  final Thread getdata = new Thread() {
		 @SuppressWarnings("deprecation")
		 public void run() {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("LaundryID", laundryId));
			params.add(new BasicNameValuePair("UserID", Config.userid));
			params.add(new BasicNameValuePair("PriceQuality", ratingBar_price.getRating() + ""));
			params.add(new BasicNameValuePair("ServiceQuality", ratingBar_service.getRating() + ""));
			params.add(new BasicNameValuePair("PickupQuality", ratingBar_pickup.getRating() + ""));
			params.add(new BasicNameValuePair("DropoffQuality", ratingBar_dropoff.getRating() + ""));
			params.add(new BasicNameValuePair("Comments", review));
			JGetParsor j = new JGetParsor();
			json = j.makeHttpRequest(Config.set_ratings, "POST", params);
		 }
	  };
	  getdata.start();

	  final Thread show = new Thread(new Runnable() {

		 @Override
		 public void run() {
			// TODO Auto-generated method stub
			try {
			   getdata.join();
			} catch (InterruptedException e) {
			   // TODO Auto-generated catch block
			   //e.printStackTrace();
			}

			if (mHandler != null) {
			   mHandler.post(new Runnable() {

				  @Override
				  public void run() {
					 // TODO Auto-generated method stub

					 if (json != null) {
						try {
						   //Toast.makeText(New_Rate.this, json.toString(),Toast.LENGTH_LONG).show();
						   if (json.getInt("status") == 200) {
							  Toast.makeText(New_Rate.this, getResources().getString(R.string.update_successfully), Toast.LENGTH_LONG).show();
							  //  Toast.makeText(New_Rate.this, tag1 + " " + tag2 + " " + tag3 + " " + tag4, Toast.LENGTH_LONG).show();
							  onBackPressed();
						   }
						   //Toast.makeText(New_Rate.this, json.getInt("status"),Toast.LENGTH_LONG).show();
						} catch (JSONException e) {
						   // TODO Auto-generated catch block
						   //e.printStackTrace();
						}
					 }
					 ProgressDialogClass.dismissProgressDialog();
				  }
			   });
			}
		 }
	  });
	  show.start();

   }
   /**
	* this is for customizing the color of rating bar
	*
	*/
}
