package com.znsoftech.znsoftechlaundry;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.znsoftech.znsoftechlaundry.json.JGetParsor;
import com.znsoftech.znsoftechlaundry.json.JsonReturn;
import com.znsoftech.znsoftechlaundry.network.Network;
import com.znsoftech.znsoftechlaundry.util.AlertUtil;
import com.znsoftech.znsoftechlaundry.util.ProgressDialogClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class laundryDetailActivity extends ActionBarActivity {

   static String laundaryName, phoneNumber = "";
   static double lat, longt;
   public boolean FAVOURITE;
   JSONArray searchResult = new JSONArray();
   JSONObject jsonObj = new JSONObject();
   Handler mHandler = new Handler();
   TextView textView_laundry_name;
   TextView textView_laundry_review;
   //String for_check="Hello";
   JSONObject json;
   String laundryId, laundryName;
   TextView textView_detail,
		   textView_service_timings; //for expandable timing and services
   TextView textView_laundry_vote;
   Button button_rate, bt1;
   ImageButton button_bookmark;
   boolean visib = false;
   private GoogleMap mMap;
   private String webaddress;
   private TextView textView_services_offered;
   private boolean hasEmail;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_laundry_detail);

	  ActionBar bar = getSupportActionBar();
	  bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#004765")));
	  bar.setDisplayShowHomeEnabled(true);
	  bar.setDisplayHomeAsUpEnabled(true);
	  bar.setHomeButtonEnabled(true);


	  Intent intent = getIntent();
	  laundryId = intent.getExtras().getString("LaundryID");
	  //new loginAccess().execute();
	  Config.Item_Array.clear();
	  Config.Service_Array.clear();
	  Config.Amount_Array.clear();
	  laundryName = intent.getExtras().getString("LaundryName");

	  bar.setTitle(laundryName);

	  bt1 = (Button) findViewById(R.id.button1);
	  bt1.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(laundryDetailActivity.this, RateCart.class);

			intent.putExtra("LaundryID", laundryId);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);

		 }
	  });

	  textView_laundry_name = (TextView) findViewById(R.id.textView_laundry_name);
	  textView_service_timings = (TextView) findViewById(R.id.text_timing_ldetail_showtext);
	  textView_services_offered = (TextView) findViewById(R.id.textView_service_ldetail);
	  textView_laundry_review = (TextView) findViewById(R.id.textView_laundry_review);

	  textView_laundry_review.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
			// TODO Auto-generated method stub
			String reviews = textView_laundry_review.getText().toString();
			String[] reviews_array = reviews.split("\\s");

			int review = 0;
			try {
			   review = Integer.parseInt(reviews_array[0]);
			} catch (NumberFormatException e) {
			   review = 0;
			}

			if (review == 0) {
			   Toast.makeText(getApplicationContext(), "0 reviews.", Toast.LENGTH_SHORT).show();
			} else {
			   Intent intent = new Intent(laundryDetailActivity.this, LaundryReview.class);
			   intent.putExtra("LaundryId", laundryId);
			   intent.putExtra("LaundryName", laundryName);
			   startActivity(intent);
			   overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		 }
	  });

	  textView_detail = (TextView) findViewById(R.id.text_address_laundry_detail);
	  textView_laundry_vote = (TextView) findViewById(R.id.textView_laundry_vote);
	  button_rate = (Button) findViewById(R.id.button_rate);


	  Button button_pick_up_request = (Button) findViewById(R.id.button_pick_up_request);
	  button_pick_up_request.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
			// TODO Auto-generated method stub

			if (hasEmail) {
			   Intent intent = new Intent(laundryDetailActivity.this, ChooseOnlineOfflineActivity.class);
			   intent.putExtra("LaundryId", laundryId);
			   intent.putExtra("LaundryName", laundryName);
			   startActivity(intent);
			} else {
//			   Toast.makeText(getApplicationContext(),"Call th",Toast.LENGTH_LONG).show();
			   DialogUniversalCustom universalCustom = new DialogUniversalCustom();
			   universalCustom.init("Call", "Laundry doesn't provide online order facility", "Call " +
							   "to Order",
					   "Cancel", new DialogInterface.OnClickListener() {
						  private boolean isTelephonyEnabled() {
							 TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
							 if (tm != null) {


								return tm.getPhoneType() != TelephonyManager
										.PHONE_TYPE_NONE;
							 } else {
								return false;
							 }
						  }
						  @Override
						  public void onClick(DialogInterface dialog, int which) {
							 if (isTelephonyEnabled()) {
								Intent intent = new Intent(Intent.ACTION_DIAL);
								intent.setData(Uri.parse("tel:" + phoneNumber));
								startActivity(intent);
							 } else {
								new DialogUniversalCustom().init("Issue", "Your device doesn't " +
												"support telephony. Call " + phoneNumber + " to place order.",
										"OK", null, new DialogInterface.OnClickListener() {
										   @Override
										   public void onClick(DialogInterface dialog, int which) {

										   }
										}, null).show(getSupportFragmentManager(), "dialog");
//							   Toast.makeText(getApplicationContext(),"Telephony is not available" +
//									   " on your Phone",Toast.LENGTH_LONG).show();
							 }
						  }
					   }, new DialogInterface.OnClickListener() {
						  @Override
						  public void onClick(DialogInterface dialog, int which) {
						  }
					   });
			   universalCustom.show(getSupportFragmentManager(), "Dialog");

			}


		 }
	  });

	  ImageButton button_call = (ImageButton) findViewById(R.id.imageButton_call);
	  button_call.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
			// TODO Auto-generated method stub

			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + phoneNumber));
			startActivity(intent);


		 }
	  });

	  button_bookmark = (ImageButton) findViewById(R.id.imageButton_bookmark);
	  button_bookmark.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
			// TODO Auto-generated method stub
			if (Network.HaveNetworkConnection(laundryDetailActivity.this)) {
			   if (FAVOURITE) {
				  DialogUniversalCustom dialogUniversalCustom = new DialogUniversalCustom();
				  dialogUniversalCustom.init("Warning!", "Remove laundry from favourites", "Yes",
						  "Cancel",
						  new DialogInterface.OnClickListener() {
							 @Override
							 public void onClick(DialogInterface dialog, int which) {
								setBookmark(laundryId);
							 }
						  }, null);
				  dialogUniversalCustom.show(getSupportFragmentManager(), "dialog");
			   } else
			   setBookmark(laundryId);

			} else {
			   AlertUtil alert = new AlertUtil();
			   alert.messageAlert(
					   laundryDetailActivity.this,
					   getResources()
							   .getString(R.string.network_title),
					   getResources().getString(
							   R.string.network_message));

			}
		 }
	  });
//        ImageButton button_rate = (ImageButton) findViewById(R.id.imageButton_rate);
//		button_rate.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(laundryDetailActivity.this,
//						New_Rate.class);
//				intent.putExtra("LaundryID", laundryId);
//
//				startActivity(intent);
//
//			}
//		});


	  android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
	  mMap = ((SupportMapFragment) fragmentManager.findFragmentById(R.id.map)).getMap();
	  mMap.getUiSettings().setMapToolbarEnabled(true);
	  final LatLng LAUNDARY = new LatLng(lat, longt);
	  Marker laundary = mMap.addMarker(new MarkerOptions()
			  .position(LAUNDARY)
			  .title(laundaryName));

	  laundary.showInfoWindow();


	  final AlertUtil alert = new AlertUtil();
	  if (Network.HaveNetworkConnection(laundryDetailActivity.this)) {
		 getLaundryDetail(laundryId);

	  } else {
		 alert.confirmationAlert(laundryDetailActivity.this, getResources()
						 .getString(R.string.network_title), getResources()
						 .getString(R.string.network_message),
				 new OnClickListener() {
					@Override
					public void onClick(View v) {
					   alert.release();
					   onBackPressed();
					}
				 });
	  }

   }

   void getLaundryDetail(final String laundryId) {

	  ProgressDialogClass.showProgressDialog(laundryDetailActivity.this,
			  "Loading...");
	  json = null;
	  //new loginAccess().execute();

	  final Thread splashTread = new Thread() {
		 @Override
		 public void run() {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("ilaundryid",
					laundryId));

			nameValuePairs.add(new BasicNameValuePair("iuserid",
					Config.userid));
			JGetParsor j = new JGetParsor();

			json = j.makeHttpRequest(Config.Get_Laundry_Detail_Url, "POST", nameValuePairs);


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


				  public String imageUrl;

				  public void run() {
					 if (json != null) {
						Log.e("json_data", json.toString());
						try {
//                                    Log.e("status", json.getString("status"));

						   JSONArray array = json.getJSONArray("data");
						   JSONObject j_obj = array.getJSONObject(0);


						   if (j_obj.has("LaundryImage") && j_obj.isNull("LaundryImgae")) {
							  imageUrl = j_obj.getString("LaundryImage");
							  ImageView imgv = (ImageView) findViewById(R.id.image_logo_detail);
							  Glide.with(getApplicationContext())
									  .load(imageUrl)
									  .into(imgv);

						   } else {
							  imageUrl = null;
						   }
//

//bookmark
						   if (j_obj.has("bookmark") && !j_obj.isNull("bookmark")) {

							  if (j_obj.getString("bookmark").equalsIgnoreCase("1")) {
								 button_bookmark.setBackgroundResource(R.drawable.bookmark_selected);
								 FAVOURITE = true;
								 //="Bookmark True";
							  } else {
								 button_bookmark.setBackgroundResource(R.drawable.second);
								 FAVOURITE = false;
							  }
						   }

						   if (j_obj.has("LaundryServiceArea") && !j_obj.isNull("LaundryServiceArea")) {
							  TextView textView = (TextView) findViewById(R.id.textView_servicearea_ldetail);
							  textView.setText(j_obj.getString("LaundryServiceArea"));
						   }

//laundryName
						   if (j_obj.has("LaundryName") && !j_obj.isNull("LaundryName")) {

							  textView_laundry_name
									  .setText(j_obj
											  .getString(
													  "LaundryName"));
							  laundaryName = j_obj
									  .getString(
											  "LaundryName");

						   }

//laundrylat laundrylong
						   if (j_obj.has("LaundryLat") && !j_obj.isNull("LaundryLong")) {
							  lat = Double.parseDouble(j_obj.getString("LaundryLat"));
							  longt = Double.parseDouble(j_obj.getString("LaundryLong"));
						   }

						   hasEmail = j_obj.has("LaundryEmail") && !j_obj.isNull("LaundryEmail") && !j_obj
								   .getString("LaundryEmail").equals("");

//map fragment
						   android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
						   mMap = ((SupportMapFragment) fragmentManager.findFragmentById(R.id.map)).getMap();
						   final LatLng LAUNDARY = new LatLng(lat, longt);
						   mMap.getUiSettings().setMapToolbarEnabled(true);
						   Marker laundary = mMap.addMarker(new MarkerOptions()
								   .position(LAUNDARY)
								   .title(laundaryName));
						   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LAUNDARY, 14));

						   String address = "<b>ADDRESS:</b><br>";

						   if (j_obj.has("LaundryAddress") && !j_obj.isNull("LaundryAddress")
//                                            && !j_obj.getString("LaundryAddress").equals("")
								   ) {
							  address = address + j_obj.getString("LaundryAddress");
						   }
						   if (!address.equals("")) {
							  address = address + "<br>";
						   }
						   //laundrycity
						   if (j_obj.has("LaundryCity") && !j_obj.isNull("LaundryCity")

//                                            &&!j_obj.getString("LaundryCity").equals("")
								   ) {
							  address = address + j_obj.getString("LaundryCity") + ", ";
						   }
						   //laundryzipcode
						   if (j_obj.has("LaundryZipCode") && !j_obj.isNull("LaundryZipCode") && !j_obj.getString("LaundryZipCode").equals("")) {
							  address = address + "Zip code: ";
							  address = address + j_obj.getString("LaundryZipCode") + "<br>";
						   }

						   if (j_obj.has("LaundryCallPrefernce") && !j_obj.isNull("LaundryCallPrefernce")
//                                            && j_obj.getString("LaundryCallPrefernce") != null
								   && j_obj.has("LaundryWebsite") && !j_obj.isNull("LaundryWebsite")
//                                            && j_obj.getString("LaundryWebsite") != null
								   ) {

							  if (j_obj.has("LaundryCallPrefernce") && !j_obj.isNull("LaundryCallPrefernce")) {
								 phoneNumber = j_obj.getString("LaundryCallPrefernce");
							  }

							  if (j_obj.has("LaundryCallPrefernce") && !j_obj.isNull("LaundryCallPrefernce") && j_obj.has("LaundryWebsite") && !j_obj.isNull("LaundryWebsite")) {
								 address = address + "\n";
							  }
						   }


//laundryworking timings
						   String timings = "Time";
						   if (j_obj.has("LaundryWorking") && !j_obj.isNull("LaundryWorking")
//                                            && j_obj.getString("LaundryWorking") != null
								   ) {
							  if (!(timings = j_obj.getString("LaundryWorking")).equals("")) {
								 textView_service_timings.setText(timings);
							  }
						   }


//services array
						   if (j_obj.has("Services") && !j_obj.isNull("Services")) {

							  JSONArray services_offered = j_obj.getJSONArray("Services");

//							  if (services_offered.length()>0){
							  String service_text="";
							  if (services_offered != null && services_offered.length() > 0) {
								 for(int j=0;j<services_offered.length();j++){
									JSONObject obj=services_offered.getJSONObject(j);
									service_text=service_text+obj.getString
											("LaundryServiceName")+"\n";
								 }


//								 JSONObject object = services_offered.getJSONObject(0);
//								 String services = object.getString("LaundryServiceName");
////                                    services=services+object.get
//								 String service_text = "";
//								 String ser[] = services.split("\\+");
//								 int leng;
//								 if ((leng = ser.length) > 0) {
//									for (int l = 0; l < leng; l++) {
//									   service_text = service_text + ser[l] + "\n";
//									}
//								 }

								 textView_services_offered.setText(service_text);


							  }
//							  }


						   }


						   if (j_obj.has("LaundryWebsite") && !j_obj.isNull("LaundryWebsite")) {

							  webaddress = j_obj.getString("LaundryWebsite");
							  address = address + "<br>" + webaddress;


						   }
//                                    textView_detail.setText(address);
						   textView_detail.setText(Html.fromHtml(address));


						   if (j_obj.has("total_ratings") && !j_obj.isNull("total_ratings")) {

							  int total_rating = 0;

							  try {
								 total_rating = Integer.parseInt(j_obj.getString("total_ratings"));
							  } catch (NumberFormatException e) {
								 total_rating = 0;
							  }
							  String ratingData = "";
							  if (total_rating < 2) {
								 ratingData = total_rating + " Vote";
							  } else {
								 ratingData = total_rating + " Votes";
							  }

							  textView_laundry_vote.setText(ratingData);

							  if (total_rating == 0)
								 button_rate.setVisibility(View.INVISIBLE);


						   } else {
							  textView_laundry_vote.setText("No Vote");
						   }


						   if (j_obj.has("avgratings") && !j_obj.isNull("avgratings")) {

							  try {
								 Double avgDouble = Double.parseDouble(j_obj
										 .getString("avgratings"));
								 String str = String.format("%.1f", avgDouble);
								 button_rate.setText("Score: " + str);
							  } catch (NumberFormatException e) {
								 // TODO: handle exception
							  }
						   } else {
							  if (button_rate != null)
								 button_rate.setText("No score");
						   }


						   String reviewData = "";

						   if (j_obj.has("total_reviews") && !j_obj.isNull("total_reviews")) {

							  int review = 0;
							  try {
								 review = Integer.parseInt(j_obj.getString("total_reviews"));
							  } catch (NumberFormatException e) {
								 review = 0;
							  }

							  if (review < 2)
								 reviewData = review + " Review";
							  else
								 reviewData = review + " Reviews";

							  SpannableString content = new SpannableString(reviewData);
							  content.setSpan(new UnderlineSpan(), 0, reviewData.length(), 0);
							  textView_laundry_review.setText(content);

						   }

						   //
									/* double lat=Double.parseDouble(searchResult.getJSONObject(0).getString("LaundryLat"));
									 double lon=Double.parseDouble(searchResult.getJSONObject(0).getString("LaundryLong"));
									 LatLng  latlong=new LatLng(lat, lon);
									 mMap.addMarker(new MarkerOptions()
							            .position(latlong));
									4
									 mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 14));
										*/


						} catch (JSONException e) {
						   // TODO Auto-generated catch block
						   e.printStackTrace();
						}

					 }
					 // ProgressDialogClass.dismissProgressDialog();
					 ProgressDialogClass.dismissProgressDialog();
					 //Toast.makeText(getApplicationContext(), for_check, Toast.LENGTH_LONG).show();
				  }

			   });
			}
		 }
	  });
	  displayThread.start();

   }

   void setBookmark(final String laundryId) {

	  ProgressDialogClass.showProgressDialog(laundryDetailActivity.this,
			  "Loading...");

	  final Thread splashTread = new Thread() {
		 @Override
		 public void run() {

			ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("laundry_id",
					laundryId));
			nameValuePairs.add(new BasicNameValuePair("user_id",
					Config.userid));

			JsonReturn jsonReturn = new JsonReturn();
			jsonObj = jsonReturn.setBookmark(Config.Bookmark_Url,
					nameValuePairs);

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
					 if (jsonObj != null) {
						if (jsonObj.length() > 0) {
						   try {
							  if (!jsonObj.has(
									  "status")) {
								 if (jsonObj.getString("IsBookmark").equalsIgnoreCase("1")) {
									FAVOURITE = true;
									button_bookmark.setBackgroundResource(R.drawable.bookmark_selected);
									//for_check="Bookmark True onClick";
								 } else {
									button_bookmark.setBackgroundResource(R.drawable.second);
									FAVOURITE = false;
								 }
							  } else {

								 AlertUtil alert = new AlertUtil();

								 alert.messageAlert(
										 laundryDetailActivity.this,
										 "",
										 jsonObj.getString(
												 "stauts_message"));

							  }
						   } catch (JSONException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						   }

						}
					 }
					 ProgressDialogClass.dismissProgressDialog();
					 //Toast.makeText(getApplicationContext(), for_check, Toast.LENGTH_LONG).show();
				  }

			   });
			}
		 }
	  });
	  displayThread.start();

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
		 startActivity(new Intent(laundryDetailActivity.this,
				 BaseFragmentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
		 overridePendingTransition(R.anim.right_in, R.anim.left_out);

//            FragmentManager fm = getSupportFragmentManager();
//            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//                fm.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            }
//
//            TextView titlebar_title = (TextView) findViewById(R.id.action_title_custom);
//            titlebar_title.setText(" ");
	  }
	  return super.onOptionsItemSelected(item);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.home_menu, menu);
	  return true;
   }

   @Override
   public void onBackPressed() {
	  // TODO Auto-generated method stub
	  super.onBackPressed();
	  overridePendingTransition(R.anim.left_in, R.anim.right_out);
   }

   public void ratePopup(View view) {
//        Toast.makeText(getApplicationContext(),"You can rate only after successfully placing an order with this laundry.",Toast.LENGTH_SHORT).show();

//        Intent intent = new Intent(this,DialogCustom.class);
////        intent.setData(Uri.parse("http://" + webaddress));
//        startActivity(intent);

	  new DialogCustom().init(laundryId).show(getSupportFragmentManager(), laundryId);


   }

   public void expand(View view) {

	  RelativeLayout layout = (RelativeLayout) findViewById(R.id.expandableLayout_rel_laundrydetail);
	  if (visib) {
		 layout.setVisibility(View.GONE);
	  } else {
		 layout.setVisibility(View.VISIBLE);
	  }
	  visib = !visib;
   }

}
