package com.znsoftech.znsoftechlaundry.tabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.znsoftech.znsoftechlaundry.Config;
import com.znsoftech.znsoftechlaundry.DealsDataStructure;
import com.znsoftech.znsoftechlaundry.DealsIntermediateActivity;
import com.znsoftech.znsoftechlaundry.FlagLaun;
import com.znsoftech.znsoftechlaundry.R;
import com.znsoftech.znsoftechlaundry.json.DownloadJsonContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class HomeTabNewGrid extends Fragment {

   public JSONObject json_banner = null;
   ImageView imageView_banner, imageView_small_banner;
   Context mContext = null;
   private ArrayList<DealsDataStructure> arrayListDeals;
   private AsyncOffers offers;
   private ProgressBar progressBar;
	public String TopBannerUrl;
	public String BottomBannerUrl;


   public static HomeTabNewGrid newInstance() {
	  HomeTabNewGrid fragment = new HomeTabNewGrid();
	  return fragment;
   }

   @Override
   public void onStop() {
	  super.onStop();
//	  offers.cancel(true);
	  Log.e("onstop", "hometab");
   }

   @Override
   public void onDestroy() {
	  super.onDestroy();
	  if (offers != null) {
		 offers.cancel(true);
	  }
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  if (mContext == null)
		 mContext = getActivity();

	  arrayListDeals = new ArrayList<>();

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


	  View view = inflater.inflate(R.layout.home_fragment, container, false);

	  imageView_banner = (ImageView) view.findViewById(R.id.imageView_banner);
	  imageView_small_banner = (ImageView) view.findViewById(R.id.imageView_small_banner);
	  progressBar = (ProgressBar) view.findViewById(R.id.gridhomeprogress);
	  progressBar.setVisibility(View.VISIBLE);

	  imageView_banner.setVisibility(View.GONE);
	  imageView_small_banner.setVisibility(View.GONE);

	  return view;
   }

   @Override
   public void onStart() {
	  super.onStart();
	  offers = new AsyncOffers();
	  offers.execute();

	  Log.e("onstart", "hometab");

   }

   @Override
   public void onViewCreated(View view, Bundle savedInstanceState) {
	  super.onViewCreated(view, savedInstanceState);

   }

   class AsyncOffers extends AsyncTask<String, Void, String> {
	  String jsonString;
	  private AsyncBanner asyncBanner;

	  @Override
	  protected void onPreExecute() {
		 super.onPreExecute();
		 asyncBanner = new AsyncBanner();
		 asyncBanner.execute();
		 Log.e("no problem", "loading pre execute");
	  }

	  @Override
	  protected String doInBackground(String... params) {
		 try {

			Log.e("problem", "Loading started Deals url");
			jsonString = DownloadJsonContent.downloadContentUsingPostMethod(Config.Deals_Url,
					"LaundryCity=" + Config.city);
			Log.e("problem", jsonString);
		 } catch (IOException e) {
			e.printStackTrace();
//			Log.e("problem json downloading", e.getMessage().toString());
		 }
		 return jsonString;
	  }

	  @Override
	  protected void onPostExecute(String s) {
		 super.onPostExecute(s);
		 if (!isCancelled() && s != null) {
			try {
			   JSONObject jsonObject = new JSONObject(s);
			   if (isOk(jsonObject, FlagLaun.cSTATUS) && jsonObject.getInt(FlagLaun.cSTATUS) == 200) {
				  if (isOk(jsonObject, FlagLaun.cDATA)) {
					 final JSONArray array = jsonObject.getJSONArray(FlagLaun.cDATA);
					 arrayListDeals.clear();

					 for (int i = 0; i < array.length(); i++) {
						JSONObject vo = array.getJSONObject(i);
						String id, name, did, dtitle, dtext, durl, daddress, lat, longi, rank, serviceArea;
						if (isOk(vo, FlagLaun.cLAUNDRY_ID)) {
						   id = vo.getString(FlagLaun.cLAUNDRY_ID);
						} else id = "no id set";
						if (isOk(vo, FlagLaun.cLAUNDRY_NAME)) {
						   name = vo.getString(FlagLaun.cLAUNDRY_NAME);
						} else name = "";
						if (isOk(vo, FlagLaun.cDEALID)) {
						   did = vo.getString(FlagLaun.cDEALID);
						} else did = "";
						if (isOk(vo, FlagLaun.cDEALTITLE)) {
						   dtitle = vo.getString(FlagLaun.cDEALTITLE);
						} else dtitle = "";
						if (isOk(vo, FlagLaun.cDEALTEXT)) {
						   dtext = vo.getString(FlagLaun.cDEALTEXT);
						} else dtext = "";
						if (isOk(vo, FlagLaun.cDEALIMAGEURL)) {
						   durl = vo.getString(FlagLaun.cDEALIMAGEURL);
						} else durl = "";
						if (isOk(vo, FlagLaun.cLAUNDRYADDRESS)) {
						   daddress = vo.getString(FlagLaun.cLAUNDRYADDRESS);
						} else daddress = "";
						if (isOk(vo, FlagLaun.cLAUNDRYLATITUDE)) {
						   lat = vo.getString(FlagLaun.cLAUNDRYLATITUDE);
						} else lat = "23.332";
						if (isOk(vo, FlagLaun.cLAUNDRYLONGITUDE)) {
						   longi = vo.getString(FlagLaun.cLAUNDRYLONGITUDE);
						} else longi = "72.233";
						if (isOk(vo, FlagLaun.cRANK)) {
						   rank = vo.getString(FlagLaun.cRANK);
						} else rank = "20";
						if (isOk(vo, FlagLaun.cLAUNDRYSERVICEAREA)) {
						   serviceArea = vo.getString(FlagLaun.cLAUNDRYSERVICEAREA);
						} else serviceArea = "";

						DealsDataStructure dealsDataStructure = new DealsDataStructure(id, name, did
								, dtitle, dtext, durl, daddress, lat, longi, rank, serviceArea);

						arrayListDeals.add(dealsDataStructure);
					 }

					 Log.e("done", "Offers on post");

					 GridView gridView = (GridView) getActivity().findViewById(R.id.gridview_home);
					 progressBar.setVisibility(View.GONE);

					 if (gridView != null && arrayListDeals != null) {


						gridView.setAdapter(new CustomGridAdapter(mContext, arrayListDeals));
						Log.e("gridview", "adapter set");
						gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						   @Override
						   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							  Intent intent = new Intent(getActivity(), DealsIntermediateActivity.class);

							  DealsDataStructure dealsArr = arrayListDeals.get(position);
//						   intent.putExtra("LaundryID", array_list.get(position).get("LaundryID"));
							  intent.putExtra("LaundryID", dealsArr.getLaundryId());
							  intent.putExtra("LaundryName", dealsArr.getLaundryName());
							  intent.putExtra("deal_id", dealsArr.getDealid());
							  intent.putExtra("deal_title", dealsArr.getDealtitle());
							  intent.putExtra("deal_text", dealsArr.getDealtext());
							  intent.putExtra("deal_image_url", dealsArr.getDealimageurl());
							  intent.putExtra("deal_address", dealsArr.getDealaddress());
							  intent.putExtra("lat", dealsArr.getLatitude());
							  intent.putExtra("log", dealsArr.getLongitude());
							  intent.putExtra(FlagLaun.cLAUNDRYSERVICEAREA, dealsArr.getLaundryServiceArea
									  ());


							  startActivity(intent);
							  getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
						   }
						});
					 } else {
						Toast.makeText(mContext, "Error! Null Grid view", Toast.LENGTH_LONG).show();
					 }
				  }
			   } else {
				  if (progressBar != null) {
					 progressBar.setVisibility(View.GONE);

				  }
				  Log.e("problem", "Problem with data object");
				  Toast.makeText(getActivity(), "No Offer available in this city", Toast.LENGTH_LONG)
						  .show();
			   }

			} catch (JSONException e) {
			   e.printStackTrace();
			   Log.e("problem", "Problem while data json parsing\n\n" + e.getMessage());

			}

		 } else {
			if (asyncBanner != null) {
			   asyncBanner.cancel(true);
			}
		 }
	  }

	  boolean isOk(JSONObject js, String objectName) {
		 return js != null && js.has(objectName) && !js.isNull(objectName);
	  }
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
			 TopBannerUrl=jsonArraybanner.getJSONObject(0).getString("BannerRedURL");

			if (newurlOpenStream != null) {
			   BitmapFactory.Options options = new BitmapFactory.Options();
			   options.inPurgeable = true;
			   mIcon_val = BitmapFactory.decodeStream(newurlOpenStream);


//			   mIcon_val = Bitmap.createScaledBitmap(mIcon_val, 350, 350, true);
			}

			InputStream newurl_bannersmall_stream = new URL(jsonArraybanner.getJSONObject(1).getString("BannerURL")).openStream();
			// InputStream newurl_bannersmall_stream = new URL(jsonArraybanner.getJSONObject(1).getString("BannerURL")).openStream();

			 BottomBannerUrl=jsonArraybanner.getJSONObject(1).getString("BannerRedURL");


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
				imageView_banner.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view){
						Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(TopBannerUrl));
						startActivity(intent);
						System.out.println("hello");
						}

			});
			}

			if (imageView_small_banner != null && mIcon_val2 != null) {
			   imageView_small_banner.setImageBitmap(mIcon_val2);
			   imageView_small_banner.setVisibility(View.VISIBLE);
				imageView_small_banner.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BottomBannerUrl));
						startActivity(intent);
						System.out.println("hello");
					}

				});
			}
		 }
	  }
   }


   public class CustomGridAdapter extends BaseAdapter {

	  private final ArrayList<DealsDataStructure> items;
	  LayoutInflater inflater;
	  private Context context;

	  public CustomGridAdapter(Context context, ArrayList<DealsDataStructure> arrayList) {
		 this.context = context;
		 this.items = arrayList;
		 inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }

	  public View getView(int position, View convertView, ViewGroup parent) {

		 if (convertView == null) {
			convertView = inflater.inflate(R.layout.griditem, null);
		 }


		 ImageView viewImage = (ImageView) convertView.findViewById(R.id.image_gridviewitem);
		 TextView textDealText = (TextView) convertView.findViewById(R.id.text_grid_dealtext);
		 TextView textLaundryName = (TextView) convertView.findViewById(R.id.text_grid_laundrynaem);

		 DealsDataStructure dea = items.get(position);

		 Glide.with(HomeTabNewGrid.this)
				 .load(dea.getDealimageurl())
				 .into(viewImage);


		 if (dea.getDealtext().length() > 20) {
			textDealText.setText(dea.getDealtext().substring(0, 16) + "...");
		 } else textDealText.setText(dea.getDealtext());

		 if (dea.getLaundryName().length() > 20) {
			textLaundryName.setText(dea.getLaundryName().substring(0, 16) + "...");
		 } else textLaundryName.setText(dea.getLaundryName());

		 return convertView;
	  }

	  @Override
	  public int getCount() {
		 return items.size();
	  }

	  @Override
	  public Object getItem(int position) {
		 return items.get(position);
	  }

	  @Override
	  public long getItemId(int position) {
		 return position;
	  }
   }

}