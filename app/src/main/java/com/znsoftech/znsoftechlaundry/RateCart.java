package com.znsoftech.znsoftechlaundry;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.znsoftech.znsoftechlaundry.json.JGetParsor;
import com.znsoftech.znsoftechlaundry.util.ProgressDialogClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RateCart extends ActionBarActivity {
   String laundryId = "";
   AutoCompleteTextView autoCompleteTextView;
   ListView listview_men;
   ArrayList<HashMap<String, String>> arrayList_men, arrayList_women, arrayList_general;
   Button button_men, button_women, button_general;
   String dataMen = "DISHDASHA WOOL\n" +
		   "DISHDASHA COTTON\n" +
		   "GHATRA\n" +
		   "JACKET\n" +
		   "JOGGING SUIT-2 PCS\n" +
		   "KURTA  PJAMA\n" +
		   "LUNGI\n" +
		   "OVERCOAT\n" +
		   "NIGHT SUIT 2PC\n" +
		   "SAFARI SUIT\n" +
		   "PATHAN SUIT\n" +
		   "SHIRT\n" +
		   "SHORTS\n" +
		   "SOCKS\n" +
		   "SUIT 2 PIECE\n" +
		   "SWEATER/ PULLOVER\n" +
		   "T-SHIRT\n" +
		   "TROUSER\n" +
		   "UNDER SHIRT\n" +
		   "UNDER WEAR \n" +
		   "VEST COAT";
   String dataWomen = "ABAYA \n" +
		   "BLOUSE \n" +
		   "BRA\n" +
		   "DESIGNER DRESS                            (DEPEND ON MATERIAL)/                       Call us for Details\n" +
		   "DRESS-NORMAL\n" +
		   "JACKET. W\n" +
		   "NIGHT GOWN\n" +
		   "LEGGINGS\n" +
		   "PETTY COAT\n" +
		   "SALWAR KAMEEJ 2PIECE\n" +
		   "SALWAR KAMEEJ 3PIECE\n" +
		   "SAREE\n" +
		   "SCARF\n" +
		   "SHAWL\n" +
		   "SKIRT\n" +
		   "SKIRT-LONG\n" +
		   "SKIRT SUIT 2 PIECE\n" +
		   "W-SUIT 2 PIECE\n" +
		   "W-TROUSER";
   String dataGeneral = "BATH ROBE\n" +
		   "BATH TOWEL\n" +
		   "BED SHEET- SINGLE/DOUBLE\n" +
		   "BED SPREAD- DOUBBLE\n" +
		   "BED SPREAD- SINGLE\n" +
		   "BLANKET\n" +
		   "CURTAIN- SQ. MTR.          /Call us for Details\n" +
		   "CARPET - SQ. MTR.           /Call us for Details\n" +
		   "FACE TOWEL\n" +
		   "GLOVES\n" +
		   "HAND TOWEL\n" +
		   "NAPKIN\n" +
		   "PILLOW\n" +
		   "PILLOW CASE\n" +
		   "QUILT /DUVET";
   JSONObject json;
   Handler mHandler = new Handler();
   String search_string_auto = "";
   private ArrayList<HashMap<String, String>> arrayList_autotext;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  overridePendingTransition(R.anim.right_in, R.anim.left_out);

	  dataGeneral = dataGeneral.toUpperCase();
	  dataMen = dataMen.toUpperCase();
	  dataWomen = dataWomen.toUpperCase();


	  setContentView(R.layout.ratecard);


//        autoCompleteTextView.setAdapter();

	  button_general = (Button) findViewById(R.id.button_general);
	  button_men = (Button) findViewById(R.id.button_men);
	  button_women = (Button) findViewById(R.id.button_women);

	  ActionBar bar = getSupportActionBar();
	  bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.action_bar_color))));
	  bar.setDisplayShowHomeEnabled(true);
	  bar.setDisplayHomeAsUpEnabled(true);
	  bar.setHomeButtonEnabled(true);
	  bar.setTitle(getResources().getString(R.string.rate_card));

	  listview_men = (ListView) findViewById(R.id.listView1_men);
//        listview_women = (ListView) findViewById(R.id.listView1_women);
//        listview_general = (ListView) findViewById(R.id.listView1_general);


	  Intent intent = getIntent();
	  laundryId = intent.getExtras().getString("LaundryID");


	  arrayList_men = new ArrayList<HashMap<String, String>>();
	  arrayList_women = new ArrayList<HashMap<String, String>>();
	  arrayList_general = new ArrayList<HashMap<String, String>>();
	  arrayList_autotext = new ArrayList<HashMap<String, String>>();

	  new loginAccess().execute();


	  autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.search_autocomplete);


   }

   @Override
   public void onBackPressed() {
	  // TODO Auto-generated method stub
	  super.onBackPressed();
	  overridePendingTransition(R.anim.left_in, R.anim.right_out);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.home_menu, menu);
	  return true;
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

		 Intent intent = new Intent(RateCart.this, BaseFragmentActivity.class);
		 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		 startActivity(intent);

		 overridePendingTransition(R.anim.right_in, R.anim.left_out);
	  }
	  return super.onOptionsItemSelected(item);
   }

   public void men(View view) {
	  button_men.setBackgroundColor(Color.parseColor("#208da9"));
	  button_women.setBackgroundColor(Color.parseColor("#076D99"));
	  button_general.setBackgroundColor(Color.parseColor("#076D99"));

	  listview_men.setAdapter(new SimpleAdapter(
			  RateCart.this,
			  arrayList_men,
			  R.layout.ratecard_items,
			  new String[]{"textView_item", "textView_service", "textView_amnt"},
			  new int[]{R.id.textView_item, R.id.textView_service, R.id.textView_amnt}));


   }

   public void women(View view) {
	  button_women.setBackgroundColor(Color.parseColor("#208da9"));
	  button_men.setBackgroundColor(Color.parseColor("#076D99"));
	  button_general.setBackgroundColor(Color.parseColor("#076D99"));

	  listview_men.setAdapter(new SimpleAdapter(
			  RateCart.this,
			  arrayList_women,
			  R.layout.ratecard_items,
			  new String[]{"textView_item", "textView_service", "textView_amnt"},
			  new int[]{R.id.textView_item, R.id.textView_service, R.id.textView_amnt}));

   }

   public void general(View view) {
	  button_general.setBackgroundColor(Color.parseColor("#208da9"));
	  button_women.setBackgroundColor(Color.parseColor("#076D99"));
	  button_men.setBackgroundColor(Color.parseColor("#076D99"));

	  listview_men.setAdapter(new SimpleAdapter(
			  RateCart.this,
			  arrayList_general,
			  R.layout.ratecard_items,
			  new String[]{"textView_item", "textView_service", "textView_amnt"},
			  new int[]{R.id.textView_item, R.id.textView_service, R.id.textView_amnt}));

   }

   public void search_item(View view) {
	  search_string_auto = autoCompleteTextView.getText().toString();
	  ArrayList<HashMap<String, String>> have = new ArrayList<>();

	  String service, item, amount;
	  for (int j = 0; j < Config.Item_Array.size(); j++) {
		 if ((Config.Item_Array.get(j).toUpperCase()).contains(search_string_auto.toUpperCase())) {
//			item = search_string_auto;
			item = Config.Item_Array.get(j);
			service = Config.Service_Array.get(j);
			amount = Config.Amount_Array.get(j);
			HashMap<String, String> map = new HashMap<String, String>();

			map.put("textView_item", item);
			map.put("textView_service", service);
			map.put("textView_amnt", amount);

			have.add(map);

		 }
	  }
	  ListAdapter adapter = new SimpleAdapter(RateCart.this, have, R.layout.ratecard_items, new String[]{"textView_item", "textView_service", "textView_amnt"}, new int[]{R.id.textView_item, R.id.textView_service, R.id.textView_amnt});
	  listview_men.setAdapter(adapter);
	  LinearLayout layout = (LinearLayout) findViewById(R.id.lin_gender_buttons);
	  layout.setVisibility(View.GONE);


   }

   class loginAccess extends AsyncTask<String, String, String> {

	  protected void onPreExecute() {
		 ProgressDialogClass.showProgressDialog(RateCart.this, getResources().getString(R.string.loading));
	  }

	  @Override
	  protected String doInBackground(String... arg0) {
		 List<NameValuePair> params = new ArrayList<NameValuePair>();

		 params.add(new BasicNameValuePair("laundryid", laundryId));
		 JGetParsor j = new JGetParsor();
		 JSONObject json = j.makeHttpRequest(Config.Ratecard_Url, "GET", params);

		 String check = "failed";

		 try {
			int success = json.getInt("status");
			if (success == 200) {
			   check = "Success";
			   JSONArray list_array = json.getJSONArray("data");
			   Config.Item_Array.clear();
			   Config.Service_Array.clear();
			   Config.Amount_Array.clear();

			   for (int i = 0; i < list_array.length(); i++) {
				  JSONObject jo = list_array.getJSONObject(i);
				  String item = jo.getString("LaundryItemName");

				  String service = jo.getString("LaundryServiceName");
				  String amount = jo.getString("Amount");

				  Config.Item_Array.add(i, item);
				  Config.Service_Array.add(i, service);
				  Config.Amount_Array.add(i, amount);

			   }
			}
		 } catch (JSONException e) {
			// e.printStackTrace();
		 }
		 return check;
	  }

	  protected void onPostExecute(String file_url) {
		 String item = "";
		 String service = "";
		 String rate = "";

		 for (int i = 0; i < Config.Item_Array.size(); i++) {
			item = Config.Item_Array.get(i);


			if (i != (Config.Item_Array.size() - 1) && item.equals(Config.Item_Array.get(i + 1))) {
			   service += "\n" + Config.Service_Array.get(i);
			   rate += "\n" + Config.Amount_Array.get(i);

			} else {
			   service += "\n" + Config.Service_Array.get(i) + "\n";
			   rate += "\n" + Config.Amount_Array.get(i) + "\n";

			   HashMap<String, String> map = new HashMap<String, String>();

			   map.put("textView_item", item);
			   map.put("textView_service", service);
			   map.put("textView_amnt", rate);


			   arrayList_autotext.add(map);


			   if (dataMen.contains(item.toUpperCase())) {
				  arrayList_men.add(map);
				  service = "";
				  rate = "";
			   } else if (dataWomen.contains(item.toUpperCase())) {
				  arrayList_women.add(map);
				  service = "";
				  rate = "";
			   } else {
				  arrayList_general.add(map);
				  service = "";
				  rate = "";
			   }

			}

		 }

		 ListAdapter adapter = new SimpleAdapter(RateCart.this, arrayList_men, R.layout.ratecard_items, new String[]{"textView_item", "textView_service", "textView_amnt"}, new int[]{R.id.textView_item, R.id.textView_service, R.id.textView_amnt});
		 listview_men.setAdapter(adapter);

		 autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.search_autocomplete);


		 ArrayAdapter<String> adapter_arra = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, Config.Item_Array);

		 autoCompleteTextView.setAdapter(adapter_arra);


		 ProgressDialogClass.dismissProgressDialog();

	  }

   }


}

