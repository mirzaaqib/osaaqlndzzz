package com.znsoftech.znsoftechlaundry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
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

@SuppressWarnings("deprecation")
public class
		Invoice extends Fragment {

   JSONObject json;
   //    ImageView imageView_banner;
   ImageLoader imageLoader;
   Handler mHandler = new Handler();
   String LaundryOrderID;
   TextView tv1, tv2;
   ListView list;
   Button button_rate;
   String laundryId;
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
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
							Bundle savedInstanceState) {

	  View view = inflater.inflate(R.layout.new_invoice, container, false);
//        imageView_banner = (ImageView) view.findViewById(R.id.imageView_banner);

	  list = (ListView) view.findViewById(R.id.listView1_men);

	  Bundle bundle = getArguments();
	  LaundryOrderID = bundle.getString("LaundryOrderID");

	  tv1 = (TextView) view.findViewById(R.id.textView1);
	  tv2 = (TextView) view.findViewById(R.id.textView2);

	  if (mContext == null)
		 mContext = getActivity();

	  button_rate = (Button) view.findViewById(R.id.button1);
	  button_rate.setOnClickListener(new View.OnClickListener() {

		 @Override
		 public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(getActivity(), New_Rate.class);
			i.putExtra("LaundryID", laundryId);
			startActivity(i);


		 }
	  });

	  if (Network.HaveNetworkConnection(getActivity())) {
		 getDetails();
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

   private void getDetails() {
	  ProgressDialogClass.showProgressDialog(getActivity(), getResources().getString(R.string.loading));
	  array_list.clear();

	  final Thread fetch_address = new Thread() {
		 @Override
		 public void run() {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_id", Config.userid));
			JGetParsor j = new JGetParsor();
			json = j.makeHttpRequest(Config.get_order + "/order_id/" + LaundryOrderID, "POST", params);
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

					 if (json != null) {
						try {
						   if (json.getInt("status") == 200) {
							  JSONObject jo_order_titles = json.getJSONObject("data");
							  tv1.setText(jo_order_titles.getString("LaundryName"));

							  JSONObject pickup_add = jo_order_titles.getJSONObject("pickup_address");

							  String pickup_address = "";
							  if (!pickup_add.getString("AddressLine1").equals(""))
								 pickup_address += pickup_add.getString("AddressLine1") + ", ";
							  if (!pickup_add.getString("AddressLine2").equals(""))
								 pickup_address += pickup_add.getString("AddressLine2") + ", ";
							  if (!pickup_add.getString("AddressLine3").equals(""))
								 pickup_address += pickup_add.getString("AddressLine3") + ", ";
							  if (!pickup_add.getString("CityName").equals(""))
								 pickup_address += pickup_add.getString("CityName") + ", ";
							  if (!pickup_add.getString("countryName").equals(""))
								 pickup_address += pickup_add.getString("countryName");

							  JSONObject dropoff_address = jo_order_titles.getJSONObject
									  ("drop_address");

							  String drop_address = "";
							  if (!dropoff_address.getString("AddressLine1").equals(""))
								 drop_address += dropoff_address.getString("AddressLine1") + ", ";
							  if (!dropoff_address.getString("AddressLine2").equals(""))
								 drop_address += dropoff_address.getString("AddressLine2") + ", ";
							  if (!dropoff_address.getString("AddressLine3").equals(""))
								 drop_address += dropoff_address.getString("AddressLine3") + ", ";
							  if (!dropoff_address.getString("CityName").equals(""))
								 drop_address += dropoff_address.getString("CityName") + ", ";
							  if (!dropoff_address.getString("countryName").equals(""))
								 drop_address += dropoff_address.getString("countryName");

							  String[] date = jo_order_titles.getString("LaundryOrderDate").split("\\s");


							  //Lets format the string for address
							  String whiteSpace = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
							  String textcolor = getResources().getString(R.string.color_invoice_text);
							  String pickUpaddr[] = pickup_address.split(",");
							  if (pickUpaddr.length != 0) {
								 pickup_address = "";

								 if (pickUpaddr[0] != null) {
									pickup_address = pickup_address + "<br>&nbsp;<font color=\"" + textcolor + "\">" + whiteSpace + pickUpaddr[0];
								 }
								 if (pickUpaddr[1] != null) {
									pickup_address = pickup_address + ",<br>" + whiteSpace + pickUpaddr[1];
								 }
								 if (pickUpaddr[2] != null) {
									pickup_address = pickup_address + ",<br>" + whiteSpace + "" + pickUpaddr[2] + ",";
								 }
								 if (!(pickUpaddr.length < 4)) {
									if (pickUpaddr[3] != null) {
									   pickup_address = pickup_address + "</b><br>" + whiteSpace + pickUpaddr[3] + "</font>";
									}
								 }
							  }
							  String dropaddr[] = drop_address.split(",");
							  if (dropaddr.length != 0) {
								 drop_address = "";

								 if (dropaddr[0] != null) {
									drop_address = drop_address + "<br>&nbsp;<font color=\"" + textcolor + "\">" + whiteSpace + dropaddr[0];
								 }
								 if (dropaddr[1] != null) {
									drop_address = drop_address + ",<br>" + whiteSpace + dropaddr[1];
								 }
								 if (dropaddr[2] != null) {
									drop_address = drop_address + ",<br>" + whiteSpace + "" + dropaddr[2] + ",";
								 }
								 if (!(dropaddr.length < 4)) {
									if (dropaddr[3] != null) {
									   drop_address = drop_address + "</b><br>" + whiteSpace + dropaddr[3] + "</font>";
									}
								 }
							  }


							  String titles = "<b>" +
									  "Date:</b> <font color=\"" + textcolor + "\">" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + date[0] + "</font><br><b>" +
									  "Order Id:" + "&nbsp;&nbsp;&nbsp;&nbsp;" + "</b> <font color=\"" + textcolor + "\">" + LaundryOrderID + "</font><br><b>Pick-up " +
									  "Address:</b> " + pickup_address + "<br><b>" +
									  "Drop-Off Address:</b> " + drop_address + "";


							  tv2.setText(Html.fromHtml(titles));
							  laundryId = jo_order_titles.getString("LaundryID");

							  JSONArray j_array = jo_order_titles.getJSONArray("detail");

							  for (int i = 0; i < j_array.length(); i++) {
								 HashMap<String, String> hashmap = new HashMap<String, String>();
								 JSONObject j_obj = j_array.getJSONObject(i);

								 hashmap.put("LaundryItemName", j_obj.getString("LaundryItemName"));
								 hashmap.put("LaundryServiceName", j_obj.getString("LaundryServiceName"));
								 hashmap.put("Price", j_obj.getString("Price"));
								 hashmap.put("Quantity", j_obj.getString("Quantity"));
								 hashmap.put("Amount", j_obj.getString("Amount"));

								 array_list.add(hashmap);
							  }

							  HashMap<String, String> hashmap = new HashMap<String, String>();

							  hashmap.put("LaundryItemName", " ");
							  hashmap.put("LaundryServiceName", " ");
							  hashmap.put("Price", " ");
							  hashmap.put("Quantity", "Total Amount");
							  hashmap.put("Amount", jo_order_titles.getString("Total"));

							  array_list.add(hashmap);

						   }
						} catch (JSONException e) {
						   // TODO Auto-generated catch block
						}
					 }

					 if (mContext == null)
						mContext = getActivity();

					 list_adapter = new Add_Adapter(mContext);
					 list.setAdapter(list_adapter);
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
//                                    JSONArray j_arr = json.getJSONArray("data");
//                                    JSONObject j_obj = j_arr.getJSONObject(0);
//                                    imageLoader.DisplayImage(j_obj.getString("BannerURL"), imageView_banner, false);
//                                    Config.banner_json = json;
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
	  public TextView item_lab, service_lab, rate_lab, quantity_lab, amount_lab;
	  TextView item, service, rate, quantity, amount;
   }

   private class Add_Adapter extends BaseAdapter {
	  private LayoutInflater mInflater;

	  public Add_Adapter(Context context) {
		 mInflater = LayoutInflater.from(context);
	  }

	  @Override
	  public int getCount() {
		 // TODO Auto-generated method stub
		 if (array_list.size() == 0) {
			return 0;
		 }
		 return array_list.size();
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
			convertView = mInflater.inflate(R.layout.new_invoice_items_new, null);

			holder = new ViewHolder();

			holder.item = (TextView) convertView.findViewById(R.id.item);
			holder.service = (TextView) convertView.findViewById(R.id.service);
			holder.rate = (TextView) convertView.findViewById(R.id.rate);
			holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
			holder.amount = (TextView) convertView.findViewById(R.id.amount);

			convertView.setTag(holder);
		 } else {
			holder = (ViewHolder) convertView.getTag();
		 }

		 if (position == array_list.size() - 1 && position != 0) {
			//String item=break_string("-", );
			holder.item_lab = (TextView) convertView.findViewById(R.id.item_label);
			holder.service_lab = (TextView) convertView.findViewById(R.id.service_label);
			holder.rate_lab = (TextView) convertView.findViewById(R.id.rate_label);
			holder.quantity_lab = (TextView) convertView.findViewById(R.id.quantity_label);
			holder.amount_lab = (TextView) convertView.findViewById(R.id.amount_label);

			holder.item_lab.setText("");
			holder.service_lab.setText("");
			holder.rate_lab.setText("");
			holder.quantity_lab.setText("");
			holder.amount_lab.setText("");

			holder.item.setText(array_list.get(position).get("LaundryItemName").replace("\n", ""));
			holder.service.setText(array_list.get(position).get("LaundryServiceName").replace("\n", ""));
			holder.rate.setText(array_list.get(position).get("Price"));

			holder.quantity.setText(array_list.get(position).get("Quantity"));
			try {
			   Float amount = Float.parseFloat(array_list.get(position).get("Amount"));
			   int a = Math.round(amount);
			   holder.amount.setText(a + "");
			} catch (NumberFormatException e) {
			   holder.amount.setText(" ");
			}
		 } else {


			String item = break_string("-", array_list.get(position).get("LaundryItemName"));
			holder.item.setText(item);

			String service = break_string("+", array_list.get(position).get("LaundryServiceName"));
			holder.service.setText(service);
			try {
			   Float rate = Float.parseFloat(array_list.get(position).get("Price"));
			   int r = Math.round(rate);
			   holder.rate.setText(r + "");
			} catch (NumberFormatException e) {
			   holder.rate.setText(" ");
			}

			holder.quantity.setText(array_list.get(position).get("Quantity"));

			try {
			   Float amount = Float.parseFloat(array_list.get(position).get("Amount"));
			   int a = Math.round(amount);
			   holder.amount.setText(a + "");
			} catch (NumberFormatException e) {
			   holder.amount.setText(" ");
			}
		 }
		 return convertView;
	  }

	  private String break_string(String main_str, String str) {
		 // TODO Auto-generated method stub
//		 String[] arr = str.split("\\s");
//		 main_str = "";
//		 for (int i = 0; i < arr.length; i++)
//			main_str += arr[i] + "\n";

		 return str;
	  }

   }
}
