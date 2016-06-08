package com.znsoftech.znsoftechlaundry.tabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.znsoftech.znsoftechlaundry.Config;
import com.znsoftech.znsoftechlaundry.DataStructure.Laundry;
import com.znsoftech.znsoftechlaundry.FlagLaun;
import com.znsoftech.znsoftechlaundry.R;
import com.znsoftech.znsoftechlaundry.json.DownloadJsonContent;
import com.znsoftech.znsoftechlaundry.laundryDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by sandeeprana on 06/01/16.
 */
public class NearbyFragment extends Fragment {
   ImageView imageView_banner, imageView_small_banner;
   private Context context;
   private ArrayList<Laundry> arrListLaundries;
   private ListView listviewLaundries;

   @Override
   public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  if (context == null) {
		 context = getActivity();
	  }
	  arrListLaundries = new ArrayList<>();
	  arrListLaundries.clear();
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	  if (context == null) {
		 context = getActivity();
	  }
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  View view = inflater.inflate(R.layout.fragment_nearby_home, container, false);
	  imageView_banner = (ImageView) view.findViewById(R.id.imageView_banner);
	  imageView_small_banner = (ImageView) view.findViewById(R.id.imageView_small_banner);
	  listviewLaundries = (ListView) view.findViewById(R.id.listView_laundry);

	  imageView_banner.setVisibility(View.GONE);
	  imageView_small_banner.setVisibility(View.GONE);

	  AsyncDownLaundryListandParse asyncDownLaundryListandParse = new AsyncDownLaundryListandParse();
	  asyncDownLaundryListandParse.execute();

	  AsyncBanner banner = new AsyncBanner();
	  banner.execute();

	  return view;
   }

   @Override
   public void onViewCreated(View view, Bundle savedInstanceState) {
	  super.onViewCreated(view, savedInstanceState);
   }

   public boolean isGPSEnabled(Context mContext) {
	  LocationManager locationManager = (LocationManager)
			  mContext.getSystemService(Context.LOCATION_SERVICE);
	  return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
   }

   class AsyncBanner extends AsyncTask<Void, Void, Void> {
	  public JSONObject jobj_banner;
	  public Bitmap mIcon_val2;
	  public Bitmap mIcon_val;

	  JSONArray jsonArraybanner;

	  @Override
	  protected void onPreExecute() {
		 super.onPreExecute();

	  }

	  @Override
	  protected Void doInBackground(Void... params) {


		 try {

			jobj_banner = new JSONObject(DownloadJsonContent.downloadContent(Config.banner_url));
			jsonArraybanner = jobj_banner.getJSONArray(FlagLaun.cDATA);

			InputStream newurlOpenStream = new URL(jsonArraybanner.getJSONObject(0).getString("BannerURL")).openStream();

			if (newurlOpenStream != null) {
			   BitmapFactory.Options options = new BitmapFactory.Options();
			   options.inPurgeable = true;
			   mIcon_val = BitmapFactory.decodeStream(newurlOpenStream);
//			   mIcon_val = Bitmap.createScaledBitmap(mIcon_val, 350, 350, true);
			}

			InputStream newurl_bannersmall_stream = new URL(jsonArraybanner.getJSONObject(1).getString("BannerURL")).openStream();

			if (newurl_bannersmall_stream != null) {
			   mIcon_val2 = BitmapFactory.decodeStream(newurl_bannersmall_stream);
			}

		 } catch (IOException | JSONException e) {
			e.printStackTrace();
		 }
		 return null;
	  }

	  @Override
	  protected void onPostExecute(Void aVoid) {
		 if (!isCancelled()) {
			if (imageView_banner != null && mIcon_val != null) {
			   imageView_banner.setImageBitmap(mIcon_val);
			   imageView_banner.setVisibility(View.VISIBLE);
			}

			if (imageView_small_banner != null && mIcon_val2 != null) {
			   imageView_small_banner.setImageBitmap(mIcon_val2);
			   imageView_small_banner.setVisibility(View.VISIBLE);
			}
		 }
	  }
   }

   class LaundryAdapter extends BaseAdapter {

	  private TextView textName, textAddress, textDistance;
	  private RatingBar ratingBar;

	  @Override
	  public int getCount() {
		 return arrListLaundries.size();
	  }

	  @Override
	  public Object getItem(int position) {
		 return arrListLaundries.get(position);
	  }

	  @Override
	  public long getItemId(int position) {
		 return 0;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		 convertView = View.inflate(context, R.layout.nearbyrow, null);
		 textName = (TextView) convertView.findViewById(R.id.textView_laundry_name);
		 textAddress = (TextView) convertView.findViewById(R.id.textView_description);
		 textDistance = (TextView) convertView.findViewById(R.id.distance);
		 ratingBar = (RatingBar) convertView.findViewById(R.id.ratingbar_row_nearby);

		 Laundry laundry = arrListLaundries.get(position);

		 textName.setText(laundry.getName());
		 textAddress.setText(laundry.getAddress());
		 if (isGPSEnabled(context)) {
			textDistance.setText(laundry.getDistance() + " km");
		 } else {
			textDistance.setVisibility(View.GONE);
		 }

		 if (!laundry.getAvgratings().equals("")) {
			ratingBar.setRating(Float.valueOf(laundry.getAvgratings()));
		 }

		 return convertView;
	  }
   }

   class AsyncDownLaundryListandParse extends AsyncTask<Void, Void, String> {

	  @Override
	  protected void onPreExecute() {
		 super.onPreExecute();

	  }

	  @Override
	  protected String doInBackground(Void... params) {
		 String url = String.format(Config.Get_All_Laundry, "nearby");
		 String data = null;
		 try {
			data = DownloadJsonContent.downloadContentUsingPostMethod(url, "city=" + Config
					.city + "&lat=" + Config.latitude + "&long=" + Config.longitude);

		 } catch (IOException e) {
			e.printStackTrace();
		 }
		 return data;
	  }

	  @Override
	  protected void onPostExecute(String s) {
		 super.onPostExecute(s);
		 Log.e("nearbyfragment", "ONpostexecuted" + s);
		 if (!isCancelled()) {
			if (s != null) {
			   try {
				  JSONObject rootObj = new JSONObject(s);
				  if (isOk(rootObj, FlagLaun.cSTATUS)) {
					 if (rootObj.getInt(FlagLaun.cSTATUS) == 200) {
						if (isOk(rootObj, FlagLaun.cDATA)) {
						   JSONArray array = rootObj.getJSONArray(FlagLaun.cDATA);

						   for (int i = 0; i < array.length(); i++) {


							  Laundry laundry = new Laundry();


							  JSONObject laund = array.getJSONObject(i);
							  String latitude = null, longitude = null;

							  if (isOk(laund, FlagLaun.cLAUNDRY_ID)) {
								 laundry.setId(laund.getString(FlagLaun.cLAUNDRY_ID));
							  }
							  if (isOk(laund, FlagLaun.cLAUNDRY_NAME)) {
								 laundry.setName(laund.getString(FlagLaun.cLAUNDRY_NAME));
							  }
							  if (isOk(laund, FlagLaun.cAVG_RATING)) {
								 laundry.setAvgratings(laund.getString(FlagLaun.cAVG_RATING));
							  }

							  if (isOk(laund, FlagLaun.cLAUNDRYADDRESS)) {
								 laundry.setAddress(laund.getString(FlagLaun.cLAUNDRYADDRESS));
							  }

							  if (isOk(laund, FlagLaun.cLAUNDRYLATITUDE) && isOk(laund, FlagLaun
									  .cLAUNDRYLONGITUDE) && !laund.getString(FlagLaun
									  .cLAUNDRYLATITUDE).equals("") && !laund.getString(FlagLaun
									  .cLAUNDRYLONGITUDE).equals("")) {
								 latitude = laund.getString(FlagLaun.cLAUNDRYLATITUDE);
								 longitude = laund.getString(FlagLaun.cLAUNDRYLONGITUDE);
								 Double Lat1 = Double.valueOf(latitude);
								 Double Long1 = Double.valueOf(longitude);
								 Double Lat2 = Double.valueOf(Config.latitude);
								 Double Long2 = Double.valueOf(Config.longitude);

								 laundry.setDistance(Lat1, Lat2, Long1, Long2);
							  } else {
								 laundry.setDistance("N/A");
							  }

							  arrListLaundries.add(laundry);
							  Collections.sort(arrListLaundries, new Comparator<Laundry>() {
								 @Override
								 public int compare(Laundry lhs, Laundry rhs) {
									if (!lhs.getDistance().equals("") && !rhs.getDistance()
											.equals("") && !lhs.getDistance().equals("N/A") && !rhs
											.getDistance()
											.equals("N/A")) {

									   if (lhs.getDistance() == rhs.getDistance()) {
										  return 0;
									   }
									   return Float.valueOf(lhs.getDistance()) < Float.valueOf(rhs
											   .getDistance()) ? -1 : 1;
									} else return 0;
								 }
							  });
						   }
						   if (listviewLaundries != null) {


							  listviewLaundries.setAdapter(new LaundryAdapter());
							  Log.e("onitem", "clickedbefore");
							  listviewLaundries.setOnItemClickListener(new AdapterView.OnItemClickListener() {

								 @Override
								 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

									Log.e("onitem", "clicked");
									Intent intent = new Intent(getActivity(), laundryDetailActivity.class);

									intent.putExtra(FlagLaun.cLAUNDRY_ID, arrListLaundries.get
											(position).getId
											());
									intent.putExtra(FlagLaun.cLAUNDRY_NAME, arrListLaundries.get(position)
											.getName());

									startActivity(intent);
									getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
								 }
							  });
						   }
						}
					 }
				  }

			   } catch (JSONException e) {
				  e.printStackTrace();
			   }
			} else {
			   // TODO: 06/01/16 error null string came
			}

		 }
	  }

	  private boolean isOk(JSONObject object, String nameObj) {
		 return object.has(nameObj) && !object.isNull(nameObj);
	  }
   }
}
