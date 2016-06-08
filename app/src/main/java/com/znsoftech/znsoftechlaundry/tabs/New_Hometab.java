package com.znsoftech.znsoftechlaundry.tabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.znsoftech.znsoftechlaundry.Config;
import com.znsoftech.znsoftechlaundry.DealsIntermediateActivity;
import com.znsoftech.znsoftechlaundry.FlagLaun;
import com.znsoftech.znsoftechlaundry.R;
import com.znsoftech.znsoftechlaundry.json.DownloadJsonContent;
import com.znsoftech.znsoftechlaundry.json.JGetParsor;
import com.znsoftech.znsoftechlaundry.laundryDetailActivity;
import com.znsoftech.znsoftechlaundry.lazyloading.ImageLoader;
import com.znsoftech.znsoftechlaundry.network.Network;
import com.znsoftech.znsoftechlaundry.util.AlertUtil;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class New_Hometab extends Fragment {

   Handler mHandler = new Handler();
   JSONObject json_latest_offer,
   //		   json_new_laundry,
   json_banner;

   ImageView imageView_banner, imageView_small_banner;
   ImageLoader imageLoader;
   ListView listView_address;
   ProgressBar progressBar1;
   LinearLayout header_ll;
	public String TopBannerUrl;

   ArrayList<HashMap<String, String>> array_list = new ArrayList<HashMap<String, String>>();
   ArrayList<HashMap<String, String>> array_list1 = new ArrayList<HashMap<String, String>>();
   Context mContext = null;

   boolean loading_flag;
   SharedPreferences preferences;
   private Addadapter list_adapter;

   public static New_Hometab newInstance() {
	  New_Hometab fragment = new New_Hometab();
	  return fragment;
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  if (mContext == null)
		 mContext = getActivity();
	  preferences = mContext.getSharedPreferences("Settings", Context.MODE_PRIVATE);

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


	  View view = inflater.inflate(R.layout.new_home_fragment, container, false);

	  imageView_banner = (ImageView) view.findViewById(R.id.imageView_banner);
	  imageView_small_banner = (ImageView) view.findViewById(R.id.imageView_small_banner);

	  listView_address = (ListView) view.findViewById(R.id.listView_laundry);
	  progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
	  header_ll = (LinearLayout) view.findViewById(R.id.header_ll);

	  if (mContext == null)
		 mContext = getActivity();

	  list_adapter = new Addadapter(mContext);
	  listView_address.setAdapter(list_adapter);
	  imageView_banner.setVisibility(View.GONE);
	  imageView_small_banner.setVisibility(View.GONE);
	  header_ll.setVisibility(View.INVISIBLE);

	  imageLoader = new ImageLoader(getActivity());
	  array_list.clear();
	  array_list1.clear();


	  if (Network.HaveNetworkConnection(getActivity())) {
		 getAllAddresses();

		 new AsyncTask<Void, Void, Void>() {
			public JSONObject jobj_banner;
			public Bitmap mIcon_val2;
			public Bitmap mIcon_val;
			//                Bitmap bannertop,bannerbottom;
			JSONObject jobj_banner_top, jobj_banner_bottom;
			JSONArray jsonArraybanner;


			@Override
			protected void onPreExecute() {
//                    super.onPreExecute();

			}

			@Override
			protected Void doInBackground(Void... params) {


			   try {

				  jobj_banner = new JSONObject(DownloadJsonContent.downloadContent(Config.banner_url));
				  jsonArraybanner = jobj_banner.getJSONArray("data");


//                        URL newurl = new URL(jsonArraybanner.getJSONObject(0).getString("BannerURL"));
				  InputStream newurlOpenStream = new URL(jsonArraybanner.getJSONObject(0).getString("BannerURL")).openStream();
				   TopBannerUrl=jsonArraybanner.getJSONObject(0).getString("BannerRedURL");


				   if (newurlOpenStream != null) {
					 BitmapFactory.Options options = new BitmapFactory.Options();
					 options.inPurgeable = true;
					 mIcon_val = BitmapFactory.decodeStream(newurlOpenStream);
					 mIcon_val = Bitmap.createScaledBitmap(mIcon_val, 150, 150, true);
				  }

				  InputStream newurl_bannersmall_stream = new URL(jsonArraybanner.getJSONObject(1).getString("BannerURL")).openStream();

				  if (newurl_bannersmall_stream != null) {
					 mIcon_val2 = BitmapFactory.decodeStream(newurl_bannersmall_stream);
				  }
//                        mIcon_val2 = BitmapFactory.decodeStream(newurlOpenStream);


//                        mIcon_val_bannersmall = BitmapFactory.decodeStream(newurl_bannersmall.openConnection().getInputStream());

			   } catch (IOException e) {
				  e.printStackTrace();
			   } catch (JSONException e) {
				  e.printStackTrace();
			   }
			   return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
			   if (imageView_banner != null && mIcon_val != null) {
				  imageView_banner.setImageBitmap(mIcon_val);
				  imageView_banner.setVisibility(View.VISIBLE);
				   imageView_banner.setOnClickListener(new View.OnClickListener() {
					   public void onClick(View view) {
						   Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TopBannerUrl));
						   startActivity(intent);
						   System.out.println("hello");
					   }

				   });
			   } else {
				  imageView_banner.setVisibility(View.GONE);
			   }

			   if (imageView_small_banner != null && mIcon_val2 != null) {
				  imageView_small_banner.setImageBitmap(mIcon_val2);
				  imageView_small_banner.setVisibility(View.VISIBLE);

			   } else {
				  imageView_small_banner.setVisibility(View.GONE);
			   }
//                    super.onPostExecute(aVoid);
			}
		 }.execute();

		 //new loginAccess().execute();
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


   void getAllAddresses() {
	  progressBar1.setVisibility(View.VISIBLE);
	  array_list.clear();
	  array_list1.clear();


	  new AsyncTask<Void, Void, Void>() {
		 List<NameValuePair> params = new ArrayList<NameValuePair>();
//            JGetParsor j = new JGetParsor();

		 @Override
		 protected Void doInBackground(Void... paramsd) {
			json_latest_offer = null;
//			json_new_laundry = null;

			try {

			   json_latest_offer = new JSONObject(DownloadJsonContent
					   .downloadContentUsingPostMethod(Config.Deals_Url, "LaundryCity=" + Config
							   .city));

			} catch (JSONException e) {
			   e.printStackTrace();
			} catch (IOException e) {
			   e.printStackTrace();
			}


//			   try {
//				  String downloadedString = DownloadJsonContent.downloadContentUsingPostMethod
//						  (Config.new_laundries_home, "city=" + Config.city);
//				  json_new_laundry = new JSONObject(downloadedString);
//			   } catch (JSONException e) {
//				  e.printStackTrace();
//			   } catch (IOException e) {
//				  e.printStackTrace();
//			   }


			if (json_latest_offer != null) {
			   try {
				  if (json_latest_offer.getInt("status") == 200) {
					 JSONArray json_array = json_latest_offer.getJSONArray("data");
					 for (int i = 0; i < json_array.length(); i++) {

						HashMap<String, String> hashmap = new HashMap<String, String>();
						JSONObject j_obj = json_array.getJSONObject(i);

						if (j_obj.has(FlagLaun.cLaundryCity) && !j_obj.isNull(FlagLaun.cLaundryCity)) {
						   if (j_obj.getString("LaundryCity").compareToIgnoreCase(Config.city) == 0) {

							  hashmap.put("LaundryID", j_obj.getString("LaundryID"));
							  hashmap.put("DealTitle", j_obj.getString("DealTitle"));
							  hashmap.put("DealText", j_obj.getString("DealText"));
							  hashmap.put("DealImage", j_obj.getString("DealImage"));

							  String address = "<b>Address:</b><br>";
							  if (!j_obj.getString("LaundryAddress").equals(""))
								 address = address + j_obj.getString("LaundryAddress");
							  if (!address.equals("")) {
								 address = address + "<br>";
							  }

							  if (!j_obj.getString("LaundryCity").equals(""))
								 address = address + j_obj.getString("LaundryCity") + ", ";

							  if (!address.equals("") && !j_obj.getString("LaundryZipCode").equals("")) {
								 address = address + "Zip code: ";
							  }
							  if (!j_obj.getString("LaundryZipCode").equals(""))
								 address = address + j_obj.getString("LaundryZipCode") + "<br>";


							  hashmap.put("DealTitle", j_obj.getString("DealTitle"));
							  hashmap.put("DealID", j_obj.getString("DealID"));
							  hashmap.put("DealImage", j_obj.getString("DealImage"));
							  hashmap.put("DealAddress", address);
							  hashmap.put("LaundryLat", j_obj.getString("LaundryLat"));
							  hashmap.put("LaundryLong", j_obj.getString("LaundryLong"));


							  array_list.add(hashmap);
						   }
						}

					 }

				  }
			   } catch (JSONException e) {
				  // TODO Auto-generated catch block
				  getAllAddresses();
				  loading_flag = true;
			   }
			} else {
//                    Toast.makeText(getActivity().getApplicationContext(),"No offer in bucket",Toast.LENGTH_SHORT).show();
			}

//			if (json_new_laundry != null) {
//			   try {
//				  if (json_new_laundry.getInt("status") == 200) {
//					 JSONArray json_array = json_new_laundry.getJSONArray("data");
//					 for (int i = 0; i < json_array.length(); i++) {
//						HashMap<String, String> hashmap = new HashMap<String, String>();
//						JSONObject j_obj = json_array.getJSONObject(i);
//
//						if (j_obj.getString("LaundryCity").toUpperCase().contains(Config.city.toUpperCase())) {
//
//
//						   hashmap.put("LaundryID", j_obj.getString("LaundryID"));
//						   hashmap.put("LaundryName", j_obj.getString("LaundryName"));
//						   hashmap.put("LaundryAddress", j_obj.getString("LaundryAddress"));
//						   hashmap.put("LaundryImage", j_obj.getString("LaundryImage"));
//
//						   Log.e("Error Hometab:", String.valueOf(array_list1.size()));
//						   array_list1.add(hashmap);
//						}
//					 }
//
//				  }
//			   } catch (JSONException e) {
//				  // TODO Auto-generated catch block
//				  getAllAddresses();
//				  loading_flag = true;
//
//			   }
//			}


			return null;
		 }

		 @Override
		 protected void onPostExecute(Void aVoid) {
			if (mHandler != null) {
			   mHandler.post(new Runnable() {

				  @Override
				  public void run() {
					 // TODO Auto-generated method stub

					 list_adapter.notifyDataSetChanged();
					 //writeToFile("error", str);
					 if (loading_flag) {
						progressBar1.setVisibility(View.VISIBLE);
						loading_flag = false;
					 } else {
						header_ll.setVisibility(View.VISIBLE);
						progressBar1.setVisibility(View.GONE);

						Config.latest_offers_json = json_latest_offer;
//						Config.latest_laundries_json = json_new_laundry;

						getBanner();
					 }

				  }
			   });
			}
		 }
	  }.execute();
   }

   private void getBanner() {
	  json_banner = null;
	  final Thread image_url = new Thread() {
		 @Override
		 public void run() {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JGetParsor j = new JGetParsor();
			if (Config.banner_json != null) {
			   json_banner = Config.banner_json;
			} else {
			   json_banner = j.makeHttpRequest(Config.banner_url, "POST", params);
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
						if (json_banner.getInt("status") == 200) {
						   JSONArray j_arr = json_banner.getJSONArray("data");

						   JSONObject j_obj = j_arr.getJSONObject(0);
						   JSONObject j_obj1 = j_arr.getJSONObject(1);

//                                    imageLoader.DisplayImage(j_obj1.getString("BannerURL"), imageView_small_banner, false);
//                                    imageLoader.DisplayImage(j_obj.getString("BannerURL"), imageView_banner, false);

//                                        new AsyncTask<Void,Void,Void>(){
//                                            @Override
//                                            protected Void doInBackground(Void... params) {
//                                                return null;
//                                            }
//                                        }.execute();
//

						   Config.banner_json = json_banner;
						   //

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

   static class ViewHolder {
	  ImageView imageView1, imageView2;
	  TextView textView1, textView2, latest_offer_id, new_laundry_id;

   }

   private class Addadapter extends BaseAdapter {
	  private LayoutInflater mInflater;

	  public Addadapter(Context context) {
		 mInflater = LayoutInflater.from(context);
	  }

	  @Override
	  public int getCount() {
		 // TODO Auto-generated method stub

		 if (array_list.size() >= array_list1.size()) {
			return array_list.size();
		 } else if (array_list.size() < array_list1.size()) {
			return array_list1.size();
		 }

		 return 0;
	  }

	  @Override
	  public Object getItem(int position) {
		 // TODO Auto-generated method stub
		 return position;
	  }

	  @Override
	  public long getItemId(int position) {
		 // TODO Auto-generated method stub
		 return position;
	  }

	  @Override
	  public View getView(final int position, View convertView, ViewGroup parent) {
		 // TODO Auto-generated method stub
		 final ViewHolder holder;
		 if (convertView == null || convertView.getTag() == null) {
			convertView = mInflater.inflate(R.layout.new_home_list_items, null);
			holder = new ViewHolder();

			holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
			holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);

			holder.latest_offer_id = (TextView) convertView.findViewById(R.id.latest_offer_id);
			holder.new_laundry_id = (TextView) convertView.findViewById(R.id.new_laundry_id);

			holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);

			convertView.setTag(holder);
		 } else {
			holder = (ViewHolder) convertView.getTag();
		 }

		 holder.textView1.setVisibility(View.INVISIBLE);
		 holder.textView2.setVisibility(View.INVISIBLE);

		 holder.imageView1.setVisibility(View.INVISIBLE);
		 holder.imageView2.setVisibility(View.INVISIBLE);

		 if (position < array_list.size()
//                    &&
//                    array_list.get(position).get("DealAddress").toUpperCase().compareToIgnoreCase(Config.city)==0
				 ) {
			holder.textView1.setVisibility(View.VISIBLE);
			holder.imageView1.setVisibility(View.VISIBLE);
			String str = array_list.get(position).get("DealTitle");
			if (str.length() > 17)
			   str = str.substring(0, 16) + "..";
			holder.textView1.setText(str);
			holder.latest_offer_id.setText(array_list.get(position).get("LaundryID"));

			if (array_list.get(position).get("DealImage").length() > 5) {
			   imageLoader.DisplayImage(array_list.get(position).get("DealImage"), holder.imageView1, false);
			}
		 }

		 if (position < array_list1.size()
//                    &&
//                    array_list1.get(position).get("LaundryAddress").toUpperCase().compareToIgnoreCase(Config.city)==0
				 ) {
			holder.textView2.setVisibility(View.VISIBLE);
			holder.imageView2.setVisibility(View.VISIBLE);
			String str = array_list1.get(position).get("LaundryName");
			if (str.length() > 17)
			   str = str.substring(0, 16) + "..";
			holder.textView2.setText(str);
			holder.new_laundry_id.setText(array_list1.get(position).get("LaundryID"));

			if (array_list1.get(position).get("LaundryImage").length() > 5) {
			   imageLoader.DisplayImage(array_list1.get(position).get("LaundryImage"), holder.imageView2, false);
			}

		 }

		 OnClickListener dealListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

			   Intent intent = new Intent(getActivity(), DealsIntermediateActivity.class);

			   intent.putExtra("LaundryID", array_list.get(position).get("LaundryID"));
			   intent.putExtra("LaundryName", array_list.get(position).get("DealTitle"));
			   intent.putExtra("deal_id", array_list.get(position).get("DealID"));
			   intent.putExtra("deal_title", array_list.get(position).get("DealTitle"));
			   intent.putExtra("deal_text", array_list.get(position).get("DealText"));
			   intent.putExtra("deal_image_url", array_list.get(position).get("DealImage"));


			   intent.putExtra("deal_address", array_list.get(position).get("DealAddress"));

			   intent.putExtra("lat", array_list.get(position).get("LaundryLat"));
			   intent.putExtra("log", array_list.get(position).get("LaundryLong"));


			   startActivity(intent);
			   getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		 };

		 holder.imageView1.setOnClickListener(dealListener);
		 holder.textView1.setOnClickListener(dealListener);

		 OnClickListener laundryListListeneronclick = new OnClickListener() {

			@Override
			public void onClick(View v) {

			   Intent intent = new Intent(getActivity(), laundryDetailActivity.class);
			   intent.putExtra("LaundryID", array_list1.get(position).get("LaundryID"));
			   intent.putExtra("LaundryName", array_list1.get(position).get("LaundryName"));


			   startActivity(intent);
			   getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
			}
		 };

		 holder.imageView2.setOnClickListener(laundryListListeneronclick);
		 holder.textView2.setOnClickListener(laundryListListeneronclick);

		 return convertView;
	  }

   }

}