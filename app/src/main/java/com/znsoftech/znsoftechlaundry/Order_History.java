package com.znsoftech.znsoftechlaundry;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.znsoftech.znsoftechlaundry.json.JGetParsor;
import com.znsoftech.znsoftechlaundry.lazyloading.ImageLoader;
import com.znsoftech.znsoftechlaundry.network.Network;
import com.znsoftech.znsoftechlaundry.util.AlertUtil;
import com.znsoftech.znsoftechlaundry.util.ProgressDialogClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order_History extends Fragment {

   ImageView imageView_small_banner;
   ImageLoader imageLoader;
   JSONObject json;
   Handler mHandler = new Handler();
   ListView list;
   ArrayList<HashMap<String, String>> array_list = new ArrayList<HashMap<String, String>>();
   Context mContext = null;
   private Add_Adapter list_adapter;

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
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	  View view = inflater.inflate(R.layout.new_order_history, container, false);

//        imageView_banner = (ImageView) view.findViewById(R.id.imageView_banner);
	  imageView_small_banner = (ImageView) view.findViewById(R.id.imageView_small_banner);

//        imageView_banner.setVisibility(View.GONE);
	  imageView_small_banner.setVisibility(View.GONE);

	  list = (ListView) view.findViewById(R.id.listView1_men);
	  if (mContext == null)
		 mContext = getActivity();

	  list_adapter = new Add_Adapter(mContext);
	  list.setAdapter(list_adapter);

	  if (Network.HaveNetworkConnection(getActivity())) {
		 getAllOrders();
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

   private void getAllOrders() {
	  ProgressDialogClass.showProgressDialog(getActivity(), getResources().getString(R.string.loading));
	  array_list.clear();

	  final Thread fetch_address = new Thread() {
		 @Override
		 public void run() {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_id", Config.userid));
			JGetParsor j = new JGetParsor();
			json = j.makeHttpRequest(Config.get_order, "POST", params);


			if (json != null) {
			   try {
				  if (json.getInt("status") == 200) {
					 JSONArray json_array = json.getJSONArray("data");

					 for (int i = 0; i < json_array.length(); i++) {
						HashMap<String, String> hashmap = new HashMap<String, String>();
						JSONObject j_obj = json_array.getJSONObject(i);

						String orderDate, orderId, total, LaundryName, Note, Time;

						if (j_obj.has("LaundryOrderDate") && !j_obj.isNull("LaundryOrderDate")) {
						   orderDate = j_obj.getString("LaundryOrderDate");
						} else orderDate = "No Date";

						if (j_obj.has("LaundryOrderID") && !j_obj.isNull("LaundryOrderID")) {
						   orderId = j_obj.getString("LaundryOrderID");
						} else orderId = null;
						if (j_obj.has("Total") && !j_obj.isNull("Total")) {
						   total = j_obj.getString("Total");
						} else total = "No Total";

						if (j_obj.has("LaundryName") && !j_obj.isNull("LaundryName")) {
						   LaundryName = j_obj.getString("LaundryName");
						} else LaundryName = "No LaundryName";

						if (j_obj.has("laundry_notes") && !j_obj.isNull("laundry_notes")) {
						   String temp = j_obj.getString("laundry_notes");
						   if (temp.length() == 0) {
							  Note = "No Note Provided";
						   } else Note = temp;

						} else Note = "No Note";

//						if (j_obj.has("Time")&&!j_obj.isNull("Time")){
//						   Time=j_obj.getString("Time");
//						}else Time="No Time";


						hashmap.put("LaundryName", LaundryName);
						hashmap.put("Note", Note);
//						hashmap.put("Time",Time);

						hashmap.put("LaundryOrderDate", orderDate);
						hashmap.put("LaundryOrderID", orderId);
						hashmap.put("Total", total);

						array_list.add(hashmap);
					 }

				  }
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
					 if (mContext == null)
						mContext = getActivity();
					 list_adapter.notifyDataSetChanged();
					 getBanner();
					 ProgressDialogClass.dismissProgressDialog();
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
			json = j.makeHttpRequest(Config.banner_url, "POST", params);
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

//                                    JSONObject j_obj = j_arr.getJSONObject(0);
//                                    imageLoader.DisplayImage(j_obj.getString("BannerURL"), imageView_banner, false);

						   JSONObject j_obj1 = j_arr.getJSONObject(1);
						   imageLoader.DisplayImage(j_obj1.getString("BannerURL"), imageView_small_banner, false);
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

   static class ViewHolder {
	  TextView textView1, textView2, textView3, name, time, note;
	  RelativeLayout main;
   }

   private class Add_Adapter extends BaseAdapter {
	  private LayoutInflater mInflater;

	  public Add_Adapter(Context context) {
		 mInflater = LayoutInflater.from(context);
	  }

	  @Override
	  public int getCount() {
		 // TODO Auto-generated method stub
		 if (array_list.size() > 0) {
			return array_list.size();
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
			convertView = mInflater.inflate(R.layout.order_history, null);

			holder = new ViewHolder();

			holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
			holder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
			holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
			holder.main = (RelativeLayout) convertView.findViewById(R.id.main);

			holder.name = (TextView) convertView.findViewById(R.id.laundry_name);
			holder.time = (TextView) convertView.findViewById(R.id.time_list_order);
			holder.note = (TextView) convertView.findViewById(R.id.note);

			convertView.setTag(holder);
		 } else {
			holder = (ViewHolder) convertView.getTag();
		 }

//		 if (position % 2 == 0) {
//			holder.main.setBackgroundColor(Color.LTGRAY);
//		 } else {
//			holder.main.setBackgroundColor(Color.parseColor(getResources().getString(R.string.light_grey)));
////			holder.main.setBackgroundColor(Color.LTGRAY);
//			//this color can be edited from the string.xml file
//		 }

		 HashMap<String, String> hashMap = array_list.get(position);

		 String date = hashMap.get("LaundryOrderDate");
		 String[] d_date = date.split(" ");
		 date = d_date[0];

		 String timeStamp = d_date[1];

		 holder.textView1.setText(date);
		 holder.textView2.setText(hashMap.get("LaundryOrderID"));

		 holder.name.setText(hashMap.get("LaundryName"));
		 holder.time.setText(timeStamp);
		 holder.note.setText(hashMap.get("Note"));

		 try {
			Float total = Float.parseFloat(array_list.get(position).get("Total"));
			int t = Math.round(total);
			holder.textView3.setText(t + "");
		 } catch (NumberFormatException e) {

		 }

		 holder.main.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			   // TODO Auto-generated method stub
			   Bundle data = new Bundle();
			   Fragment fragment = new Invoice();
			   data.putString("LaundryOrderID", array_list.get(position).get("LaundryOrderID"));
			   fragment.setArguments(data);
			   android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
			   fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "invoice").addToBackStack("order_history").commit();

			}
		 });

		 return convertView;
	  }

   }
}
