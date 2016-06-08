package com.znsoftech.znsoftechlaundry.tabs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.znsoftech.znsoftechlaundry.Config;
import com.znsoftech.znsoftechlaundry.DealsIntermediateActivity;
import com.znsoftech.znsoftechlaundry.FlagLaun;
import com.znsoftech.znsoftechlaundry.R;
import com.znsoftech.znsoftechlaundry.json.DownloadJsonContent;
import com.znsoftech.znsoftechlaundry.json.JGetParsor;
import com.znsoftech.znsoftechlaundry.lazyloading.ImageLoader;
import com.znsoftech.znsoftechlaundry.network.Network;
import com.znsoftech.znsoftechlaundry.util.AlertUtil;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DealsFragment extends Fragment implements OnItemClickListener {

   ProgressBar progressBar1;
   ImageView imageView_banner;
   ImageLoader imageLoader;
   ListView listView1, listView2;
   LinearLayout ll, ll1;
   Handler mHandler = new Handler();
   ArrayList<HashMap<String, String>> all_list1;
   ArrayList<HashMap<String, String>> all_list2;
   JSONObject json;

   Context mContext = null;
   private loginAccess loginA;

   public static DealsFragment newInstance() {
	  DealsFragment fragment = new DealsFragment();
	  return fragment;
   }

   public static void setListViewHeightBasedOnChildren(ListView listView) {
	  ListAdapter listAdapter = listView.getAdapter();
	  if (listAdapter == null) return;
	  if (listAdapter.getCount() <= 1) return;

	  int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
	  int totalHeight = 0;
	  View view = null;
	  for (int i = 0; i < listAdapter.getCount(); i++) {
		 view = listAdapter.getView(i, view, listView);
		 view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
		 totalHeight += view.getMeasuredHeight();
	  }
	  ViewGroup.LayoutParams params = listView.getLayoutParams();
	  params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	  listView.setLayoutParams(params);
	  listView.requestLayout();
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  setHasOptionsMenu(true);
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
	  View view = inflater.inflate(R.layout.deal_listview, container, false);

	  progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);

	  listView1 = (ListView) view.findViewById(R.id.listView1_men);
	  listView2 = (ListView) view.findViewById(R.id.listView2);

	  ll = (LinearLayout) view.findViewById(R.id.ll);
	  ll1 = (LinearLayout) view.findViewById(R.id.ll1);

	  ll.setVisibility(View.GONE);
	  ll1.setVisibility(View.GONE);

	  if (mContext == null)
		 mContext = getActivity();

	  all_list1 = new ArrayList<HashMap<String, String>>();
	  all_list2 = new ArrayList<HashMap<String, String>>();

	  imageView_banner = (ImageView) view.findViewById(R.id.imageView_banner);
	  imageView_banner.setVisibility(View.GONE);

	  imageLoader = new ImageLoader(getActivity().getApplicationContext());
	  if (Network.HaveNetworkConnection(getActivity())) {
		 getBanner();
	  }

	  if (Network.HaveNetworkConnection(getActivity())) {
//		 new loginAccess().execute();
	  } else {
		 AlertUtil alert = new AlertUtil();
		 alert.messageAlert(getActivity(), getResources()
				 .getString(R.string.network_title), getResources().getString(R.string.network_message));
	  }

	  return view;

   }


   @Override
   public void onItemClick(AdapterView<?> parent, View view, int position,
						   long id) {
	  // TODO Auto-generated method stub
	  String laundry_id = ((TextView) view.findViewById(R.id.laundry_id)).getText().toString();
	  String laundry_name = ((TextView) view.findViewById(R.id.laundry_name)).getText().toString();

	  if (laundry_id.contains("http:")) {
		 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(laundry_id));
		 startActivity(intent);
	  } else {
		 HashMap<String, String> hashmap = new HashMap<String, String>();
		 hashmap = all_list1.get(position);

		 Intent intent = new Intent(getActivity(), DealsIntermediateActivity.class);
		 intent.putExtra("LaundryID", laundry_id);
		 intent.putExtra("LaundryName", laundry_name);
		 intent.putExtra("deal_id", hashmap.get("deal_id"));
		 intent.putExtra("deal_title", hashmap.get("deal_title"));
		 intent.putExtra("deal_text", hashmap.get("laundry_offer"));
		 intent.putExtra("deal_image_url", hashmap.get("deal_image_url"));
		 intent.putExtra("deal_address", hashmap.get("deal_address"));
		 intent.putExtra("lat", hashmap.get("lat"));
		 intent.putExtra("log", hashmap.get("log"));
		 intent.putExtra(FlagLaun.cLAUNDRYSERVICEAREA, hashmap.get(FlagLaun.cLAUNDRYSERVICEAREA));


		 startActivity(intent);

		 getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
	  }
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
						if (json.getInt("status") == 200) {
						   JSONArray j_arr = json.getJSONArray("data");
						   JSONObject j_obj = j_arr.getJSONObject(0);
						   imageLoader.DisplayImage(j_obj.getString("BannerURL"), imageView_banner, false);
						   Config.banner_json = json;
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

   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	  super.onCreateOptionsMenu(menu, inflater);

//        int city_array_size = Config.cityArray.size();
//        if (city_array_size > 0)
//            for (int i = 0; i < city_array_size; i++)
//                menu.findItem(i).setVisible(false);

   }

   @Override
   public void onViewCreated(View view, Bundle savedInstanceState) {
	  super.onViewCreated(view, savedInstanceState);
	  loginA = new loginAccess();
	  loginA.execute();
   }

   @Override
   public void onStop() {
	  super.onStop();
	  loginA.cancel(true);
   }

   class loginAccess extends AsyncTask<String, String, String> {

	  protected void onPreExecute() {
		 progressBar1.setVisibility(View.VISIBLE);
	  }

	  @Override
	  protected String doInBackground(String... arg0) {

//		 List<NameValuePair> params = new ArrayList<NameValuePair>();
//		 JGetParsor j = new JGetParsor();
		 JSONObject json = null;
		 if (true)
//                json = j.makeHttpRequest(Config.Deals_Url, "POST", params);
			try {

			   String city = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
					   .getString
							   ("city", Config.city);

			   json = new JSONObject(DownloadJsonContent.downloadContentUsingPostMethod(Config
					   .Deals_Url, "LaundryCity=" + city));
			} catch (JSONException e) {
			   e.printStackTrace();
			} catch (IOException e) {
			   e.printStackTrace();
			}
//		 else
//			json = Config.deals_json;

		 String check = "failed";

		 try {
			int success = json.getInt("status");

			if (success == 200) {
			   check = "Success";
			   JSONArray list_array = json.getJSONArray("data");

			   for (int i = 0; i < list_array.length(); i++) {
				  HashMap<String, String> map1 = new HashMap<String, String>();
				  JSONObject jo = list_array.getJSONObject(i);
//				  if ((jo.getString("LaundryCity").compareToIgnoreCase(Config.city) == 0)) {
				  String laundry_name, laundry_address, laundry_id, dealTitle,
						  laundry_offer_dealText, dealImageURl, lat, log, laundryService;
				  if (jo.has("LaundryName") && !jo.isNull("LaundryName")) {
					 laundry_name = jo.getString("LaundryName");
				  } else laundry_name = "No Name";

				  if (jo.has("LaundryAddress") && !jo.isNull("LaundryAddress")) {
					 laundry_address = jo.getString("LaundryAddress");
				  } else laundry_address = "No Address";

				  if (jo.has("LaundryID") && !jo.isNull("LaundryID")) {
					 laundry_id = jo.getString("LaundryID");
				  } else return null;


				  int dealpercentage;
				  String dealId = jo.getString("DealID");
				  if (jo.has("DealPercentage") && !jo.isNull("DealPercentage")) {
					 dealpercentage = jo.getInt("DealPercentage");
				  } else dealpercentage = 0;

				  if (jo.has("DealTitle") && !jo.isNull("DealTitle")) {
					 dealTitle = jo.getString("DealTitle");
				  } else dealTitle = "No Title";

				  if (jo.has("DealText") && !jo.isNull("DealText")) {
					 laundry_offer_dealText = jo.getString("DealText");
				  } else laundry_offer_dealText = "No Text";

				  if (jo.has("DealImage") && !jo.isNull("DealImage")) {
					 dealImageURl = jo.getString("DealImage");
				  } else dealImageURl = "";

				  if (jo.has("LaundryLat") && !jo.isNull("LaundryLat")) {
					 lat = jo.getString("LaundryLat");
				  } else lat = "23.4848848";

				  if (jo.has(FlagLaun.cLAUNDRYSERVICEAREA) && !jo.isNull(FlagLaun.cLAUNDRYSERVICEAREA)) {
					 laundryService = jo.getString(FlagLaun.cLAUNDRYSERVICEAREA);
				  } else laundryService = "23.4848848";

				  if (jo.has("LaundryLong") && !jo.isNull("LaundryLong")) {
					 log = jo.getString("LaundryLong");
				  } else log = "23.333333";


				  String address = "<b>Address:</b><br>";
				  if (!jo.getString("LaundryAddress").equals(""))
					 address = address + jo.getString("LaundryAddress");
				  if (!address.equals("")) {
					 address = address + "<br>";
				  }

				  if (!jo.getString("LaundryCity").equals(""))
					 address = address + jo.getString("LaundryCity") + ", ";

				  if (!address.equals("") && !jo.getString("LaundryZipCode").equals("")) {
					 address = address + "Zip code: ";
				  }
				  if (!jo.getString("LaundryZipCode").equals(""))
					 address = address + jo.getString("LaundryZipCode") + "<br>";


				  Log.e("data for deal list", laundry_address + laundry_name + laundry_id + laundry_offer_dealText + dealTitle + dealId);

				  map1.put("laundry_name", laundry_name);
				  map1.put("laundry_address", laundry_address);
				  map1.put("laundry_offer", laundry_offer_dealText);
				  map1.put("laundry_id", laundry_id);

				  map1.put(FlagLaun.cLAUNDRYSERVICEAREA, laundryService);

				  map1.put("alpha", String.valueOf(dealpercentage) + "%");

				  map1.put("deal_title", dealTitle);
				  map1.put("deal_id", dealId);
				  map1.put("deal_image_url", dealImageURl);
				  map1.put("deal_address", address);
				  map1.put("lat", lat);
				  map1.put("log", log);


				  all_list1.add(map1);
//				  }

			   }

			   Config.deals_json = json;
			}
		 } catch (JSONException e) {
			//e.printStackTrace();
			Config.deals_json = null;

		 }


		 JSONObject json1 = null;


		 /**
		  * Deprecated url used
		  * so have a look
		  */

		 if (Config.other_deals_json == null)
			try {
			   json1 = new JSONObject(DownloadJsonContent.downloadContent(Config.Deals_Url));
			   Config.other_deals_json = json1;
			} catch (JSONException e) {
			   e.printStackTrace();
			} catch (IOException e) {
			   e.printStackTrace();
			}
//		 json1 = j.makeHttpRequest(Config.Others_Deals_Url, "POST", params);
		 else
			json1 = Config.other_deals_json;

		 try {
			int success = json1.getInt("status");
			if (success == 200) {
			   check = "Success";
			   JSONArray list_array = json1.getJSONArray("data");

			   for (int i = 0; i < list_array.length(); i++) {
				  HashMap<String, String> map2 = new HashMap<String, String>();
				  JSONObject jo = list_array.getJSONObject(i);

				  String laundry_name, laundry_address, laundry_offer, deal_image;
				  if (jo.has("DealTitle") && !jo.isNull("DealTitle")) {
					 laundry_name = jo.getString("DealTitle");
				  } else laundry_name = "No Title";

				  laundry_address = "";

				  if (jo.has("DealText") && !jo.isNull("DealText")) {
					 laundry_offer = jo.getString("DealText");
				  } else laundry_offer = "No Tagline";

				  if (jo.has("RedirectURL") && !jo.isNull("RedirectURL")) {
					 deal_image = jo.getString("RedirectURL");
				  } else deal_image = "2";

				  int dealpercentage;

				  if (jo.has("DealPercentage") && !jo.isNull("DealPercentage")) {
					 dealpercentage = jo.getInt("DealPercentage");
				  } else dealpercentage = 0;


				  map2.put("laundry_name", laundry_name);
				  map2.put("laundry_address", laundry_address);
				  map2.put("laundry_offer", laundry_offer);
				  map2.put("deal_image", deal_image);
				  map2.put("alpha", String.valueOf(dealpercentage) + "%");

				  all_list2.add(map2);
			   }
			   Config.other_deals_json = json1;
			}
		 } catch (JSONException e) {
			//e.printStackTrace();
			Config.other_deals_json = null;
		 }

		 return check;
	  }

	  protected void onPostExecute(String file_url) {

		 if (!isCancelled()) {

			Log.e("dealfragment", "notcancel");

			if (all_list1.size() > 0)
			   ll.setVisibility(View.VISIBLE);

			if (mContext == null)
			   mContext = getActivity();

			SimpleAdapter adapter1 = new SimpleAdapter(mContext, all_list1, R.layout.deal_list_row, new String[]{"laundry_name", "laundry_address", "laundry_offer", "laundry_id", "alpha"}, new int[]{R.id.laundry_name, R.id.laundry_address, R.id.laundry_offer, R.id.laundry_id, R.id.alpha});
			listView1.setAdapter(adapter1);
			setListViewHeightBasedOnChildren(listView1);
			listView1.setOnItemClickListener(DealsFragment.this);

			if (all_list1.size() > 0)
			   ll1.setVisibility(View.VISIBLE);


			ListAdapter adapter2 = new SimpleAdapter(mContext, all_list2, R.layout.deal_list_row, new String[]{"laundry_name", "laundry_address", "laundry_offer", "laundry_id", "alpha"}, new int[]{R.id.laundry_name, R.id.laundry_address, R.id.laundry_offer, R.id.laundry_id, R.id.alpha});
			listView2.setAdapter(adapter2);

			setListViewHeightBasedOnChildren(listView2);
			listView2.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				  HashMap<String, String> deal = all_list2.get(position);
//			   map2.put("laundry_name", laundry_name);
//			   map2.put("laundry_address", laundry_address);
//			   map2.put("laundry_offer", laundry_offer);
//			   map2.put("deal_image", deal_image);
//			   map2.put("alpha", String.valueOf(dealpercentage) + "%");

				  String url = deal.get("deal_image");
				  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				  Intent browserChooserIntent = Intent.createChooser(browserIntent, "Choose browser of your choice");
				  startActivity(browserChooserIntent);
			   }
			});

			progressBar1.setVisibility(View.GONE);
		 } else {
			Log.e("dealfragment", "cancelled");
		 }
	  }

   }
}