package com.znsoftech.znsoftechlaundry.tabs;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.znsoftech.znsoftechlaundry.Config;
import com.znsoftech.znsoftechlaundry.R;
import com.znsoftech.znsoftechlaundry.json.JGetParsor;
import com.znsoftech.znsoftechlaundry.laundryDetailActivity;
import com.znsoftech.znsoftechlaundry.lazyloading.ImageLoader;
import com.znsoftech.znsoftechlaundry.network.Network;
import com.znsoftech.znsoftechlaundry.util.AlertUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LaundryFragmentNearbyLeadingFavourite extends Fragment {

   Context mContext = null;
   String tabStr;

   Handler mHandler = new Handler();
   ListView listView_laundry;
   ProgressBar progressBar1;
   ImageView imageView_banner;
   ImageLoader imageLoader;

   String str = "";
   JSONObject json;
   ArrayList<HashMap<String, String>> array_list = new ArrayList<HashMap<String, String>>();
   boolean loading_flag;

   private EfficientAdapter list_ed;

   public static LaundryFragmentNearbyLeadingFavourite newInstance() {
	  LaundryFragmentNearbyLeadingFavourite fragment = new LaundryFragmentNearbyLeadingFavourite();
	  return fragment;
   }

   public static String distance(double lat1, double lat2, double lon1,
								 double lon2) {

	  final int R = 6371; // Radius of the earth

	  Double latDistance = Math.toRadians(lat2 - lat1);
	  Double lonDistance = Math.toRadians(lon2 - lon1);
	  Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
			  + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
			  * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	  Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	  double distance = R * c * 1000; // convert to meters

	  double height = 0;

	  distance = Math.pow(distance, 2) + Math.pow(height, 2);

	  NumberFormat formatter = new DecimalFormat("#0.00");

	  return formatter.format(Math.sqrt(distance) / 1000.0f) + " km";

   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  if (mContext == null)
		 mContext = getActivity();


   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	  if (mContext == null)
		 mContext = getActivity();

   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
							Bundle savedInstanceState) {

	  View view = inflater.inflate(R.layout.fragment_nearby_home, container, false);

	  progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
	  listView_laundry = (ListView) view.findViewById(R.id.listView_laundry);

	  list_ed = new EfficientAdapter(mContext);
	  listView_laundry.setAdapter(list_ed);

	  imageView_banner = (ImageView) view.findViewById(R.id.imageView_banner);
	  imageView_banner.setVisibility(View.GONE);

	  if (mContext == null)
		 mContext = getActivity();

	  imageLoader = new ImageLoader(getActivity().getApplicationContext());

	  listView_laundry.setOnItemClickListener(new OnItemClickListener() {
		 @Override
		 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			Intent intent = new Intent(getActivity(), laundryDetailActivity.class);

			intent.putExtra("LaundryID", array_list.get(position).get("LaundryID"));
			intent.putExtra("LaundryName", array_list.get(position).get("LaundryName"));
//                String la=array_list.get(position).get("LaundryWorking");

			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
		 }
	  });

	  Bundle bundel = getArguments();
	  tabStr = bundel.getString("Tab");
	  Log.e("laundryfragment", tabStr);

//	  if (tabStr.equals("suggest")&&!Config.GPS_ENABLED){
//		 Toast.makeText(getActivity().getApplicationContext(),"Please Enable GPS for accurate " +
//				 "results",Toast.LENGTH_LONG).show();
//	  }

	  if (Network.HaveNetworkConnection(getActivity())) {
		 getLaundryListing(tabStr);
	  } else {
		 AlertUtil alert = new AlertUtil();
		 alert.messageAlert(getActivity(),
				 getResources()
						 .getString(R.string.network_title),
				 getResources().getString(
						 R.string.network_message));
	  }
	  return view;
   }

   private void getLaundryListing(final String tabId) {
	  progressBar1.setVisibility(View.VISIBLE);
	  array_list.clear();
	  final Thread fetch_address = new Thread() {
		 @Override
		 public void run() {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("city", Config.city));

			String url = String.format(Config.Get_All_Laundry, tabId);
			str = tabId + " ";

			if (tabId.equals("nearby")) {

			   params.add(new BasicNameValuePair("lat", Config.latitude));
			   params.add(new BasicNameValuePair("long", Config.longitude));

			} else if (tabId.equals("favorite")) {
			   params.add(new BasicNameValuePair("userid", Config.userid));
			}
			JGetParsor j = new JGetParsor();
			if (tabId.equals("nearby") && Config.nearby_json != null) {
			   json = Config.nearby_json;
			} else if (tabId.equals("suggest") && Config.leading_json != null) {
			   json = Config.leading_json;
			} else {
			   json = j.makeHttpRequest(url, "POST", params);
			}
			if (json != null) {
			   try {
				  if (json.getInt("status") == 200) {
					 JSONArray json_array = json.getJSONArray("data");

					 for (int i = 0; i < json_array.length(); i++) {
						HashMap<String, String> hashmap = new HashMap<String, String>();
						JSONObject j_obj = json_array.getJSONObject(i);

						hashmap.put("LaundryID", j_obj.getString("LaundryID"));
						hashmap.put("LaundryName", j_obj.getString("LaundryName"));
						hashmap.put("avgratings", j_obj.getString("avgratings"));
						hashmap.put("LaundryAddress", j_obj.getString("LaundryAddress"));
						hashmap.put("LaundryLat", j_obj.getString("LaundryLat"));
						hashmap.put("LaundryLong", j_obj.getString("LaundryLong"));


						if (j_obj.getString("LaundryCity").compareToIgnoreCase(Config.city) == 0) {
						   array_list.add(hashmap);
						} else if (tabId.equals("nearby") || tabId.equals("favorite")) {
						   array_list.add(hashmap);
						}


					 }

				  }
			   } catch (JSONException e) {
				  // TODO Auto-generated catch block
				  getLaundryListing(tabStr);
				  loading_flag = true;

				  if (tabId.equals("nearby")) {
					 Config.nearby_json = null;

				  } else if (tabId.equals("favorite")) {
					 Config.fav_json = null;

				  } else if (tabId.equals("suggest")) {
					 Config.leading_json = null;

				  }

				  return;
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
					 list_ed.notifyDataSetChanged();

					 if (loading_flag) {
						progressBar1.setVisibility(View.VISIBLE);
						loading_flag = false;
					 } else {
						progressBar1.setVisibility(View.GONE);

						if (tabId.equals("nearby")) {
						   Config.nearby_json = json;

						} else if (tabId.equals("favorite")) {
						   Config.fav_json = json;

						} else if (tabId.equals("suggest")) {
						   Config.leading_json = json;

						}

						getBanner();
					 }
				  }
			   });
			}

		 }
	  });
	  display.start();
   }

   private void getBanner() {
	  imageLoader = new ImageLoader(mContext);
	  json = null;
	  final Thread image_url = new Thread() {
		 @Override
		 public void run() {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JGetParsor j = new JGetParsor();
			if (Config.banner_json != null) {
			   json = Config.banner_json;
			} else {
			   json = j.makeHttpRequest(Config.banner_url, "POST", params);
			}
		 }
	  };
	  image_url.start();

	  final Thread show = new Thread(new Runnable() {

		 @Override
		 public void run() {
			// TODO Auto-generated method stub
			try {
			   image_url.join();
			} catch (InterruptedException e) {
			   // TODO Auto-generated catch block
			   //e.printStackTrace();
			}
			if (mHandler != null) {
			   mHandler.post(new Runnable() {

				  @Override
				  public void run() {
					 // TODO Auto-generated method stub
					 try {
						if (json.has("status") && !json.isNull("status")) {

						   if (json.getInt("status") == 200) {
							  JSONArray j_arr = json.getJSONArray("data");

							  JSONObject j_obj = j_arr.getJSONObject(0);
							  imageLoader.DisplayImage(j_obj.getString("BannerURL"), imageView_banner, false);
							  Config.banner_json = json;

						   }
						}
					 } catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					 }
				  }
			   });
			}
		 }
	  });
	  show.start();
   }

   public boolean isGPSEnabled(Context mContext) {
	  LocationManager locationManager = (LocationManager)
			  mContext.getSystemService(Context.LOCATION_SERVICE);
	  return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
   }

   static class ViewHolder {
	  public TextView textViewdistance;
	  TextView textView_laundry_name;
	  TextView textView_description;
	  ImageView imageView_star1, imageView_star2, imageView_star3, imageView_star4, imageView_star5;
   }

   private class EfficientAdapter extends BaseAdapter {
	  private LayoutInflater mInflater;

	  public EfficientAdapter(Context context) {
		 mInflater = LayoutInflater.from(context);
	  }

	  public int getCount() {

		 if (array_list.size() > 0)
			return array_list.size();

		 return 0;
	  }

	  public Object getItem(int position) {
		 return position;
	  }

	  public long getItemId(int position) {
		 return position;
	  }

	  public View getView(int position, View convertView, ViewGroup parent) {

		 final ViewHolder holder;

		 if (convertView == null || convertView.getTag() == null) {
			convertView = mInflater.inflate(R.layout.laundary_row, null);
			holder = new ViewHolder();

			holder.textView_laundry_name = (TextView) convertView.findViewById(R.id.textView_laundry_name);
			holder.textView_description = (TextView) convertView.findViewById(R.id.textView_description);
			holder.textViewdistance = (TextView) convertView.findViewById(R.id.distance);

			holder.imageView_star1 = (ImageView) convertView.findViewById(R.id.imageView_star1);
			holder.imageView_star2 = (ImageView) convertView.findViewById(R.id.imageView_star2);
			holder.imageView_star3 = (ImageView) convertView.findViewById(R.id.imageView_star3);
			holder.imageView_star4 = (ImageView) convertView.findViewById(R.id.imageView_star4);
			holder.imageView_star5 = (ImageView) convertView.findViewById(R.id.imageView_star5);

			convertView.setTag(holder);
		 } else {
			holder = (ViewHolder) convertView.getTag();
		 }

		 holder.textView_laundry_name.setText(array_list.get(position).get("LaundryName"));
		 holder.textView_description.setText(array_list.get(position).get("LaundryAddress"));



		 Double Lat1 = Double.valueOf(array_list.get(position).get("LaundryLat"));
		 Double Long1 = Double.valueOf(array_list.get(position).get("LaundryLong"));
		 String distanceString;
		 if (!Config.latitude.equals("") || !Config.longitude.equals("")) {
			Double Lat2 = Double.valueOf(Config.latitude);
			Double Long2 = Double.valueOf(Config.longitude);
			distanceString = distance(Lat1, Lat2, Long1, Long2);

		 } else {
			distanceString = "not available";
		 }

		 if (isGPSEnabled(mContext)) {
			holder.textViewdistance.setText(distanceString);
		 } else {
			holder.textViewdistance.setVisibility(View.GONE);
		 }


		 int first, second;
		 first = second = 0;
		 try {
			String str_rate = array_list.get(position).get("avgratings");
			if (str_rate != null) {
			   int rateInt = 0, rateHalf = 0;
			   double rate = Double.parseDouble(str_rate);
			   rateInt = (int) Math.floor(rate);
			   double half = Math.ceil(rate) - Math.floor(rate);

			   if (half > 0)
				  rateHalf = rateInt + 1;

			   if (rateInt > 0)
				  first = rateInt;

			   if (rateHalf > 0)
				  second = rateHalf;
			}
		 } catch (NumberFormatException e) {
			// TODO: handle exception
		 }

		 holder.imageView_star5.setImageResource(R.drawable.rating_before);
		 holder.imageView_star4.setImageResource(R.drawable.rating_before);
		 holder.imageView_star3.setImageResource(R.drawable.rating_before);
		 holder.imageView_star2.setImageResource(R.drawable.rating_before);
		 holder.imageView_star1.setImageResource(R.drawable.rating_before);

		 if (first == 5) {
			holder.imageView_star5.setImageResource(R.drawable.rating_after);
			holder.imageView_star4.setImageResource(R.drawable.rating_after);
			holder.imageView_star3.setImageResource(R.drawable.rating_after);
			holder.imageView_star2.setImageResource(R.drawable.rating_after);
			holder.imageView_star1.setImageResource(R.drawable.rating_after);

		 } else if (first == 4) {
			holder.imageView_star4.setImageResource(R.drawable.rating_after);
			holder.imageView_star3.setImageResource(R.drawable.rating_after);
			holder.imageView_star2.setImageResource(R.drawable.rating_after);
			holder.imageView_star1.setImageResource(R.drawable.rating_after);
		 } else if (first == 3) {
			holder.imageView_star3.setImageResource(R.drawable.rating_after);
			holder.imageView_star2.setImageResource(R.drawable.rating_after);
			holder.imageView_star1.setImageResource(R.drawable.rating_after);
		 } else if (first == 2) {
			holder.imageView_star2.setImageResource(R.drawable.rating_after);
			holder.imageView_star1.setImageResource(R.drawable.rating_after);
		 } else if (first == 1) {
			holder.imageView_star1.setImageResource(R.drawable.rating_after);
		 }

		 if (second == 5) {
			holder.imageView_star5.setImageResource(R.drawable.star_half);
		 } else if (second == 4) {
			holder.imageView_star4.setImageResource(R.drawable.star_half);
		 } else if (second == 3) {
			holder.imageView_star3.setImageResource(R.drawable.star_half);
		 } else if (second == 2) {
			holder.imageView_star2.setImageResource(R.drawable.star_half);
		 } else if (second == 1) {
			holder.imageView_star1.setImageResource(R.drawable.star_half);
		 }

		 return convertView;
	  }

   }

}