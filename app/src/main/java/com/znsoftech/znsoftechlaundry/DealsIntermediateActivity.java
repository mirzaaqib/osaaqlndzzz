package com.znsoftech.znsoftechlaundry;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.URL;

public class DealsIntermediateActivity extends AppCompatActivity {


   private String lID;
   private String lName;
   private String dId;
   private String dTitle;
   private String dText;
   private String dImageUrl;
   private String dAddress;

   private TextView tlName, tlAddress, tdTitle, tdText;
   private ImageView dImage;
   private String lLat = "28.3";
   private String lLog = "26.5";
   private String lservicearea;
   private TextView tlservicearea;


   @Override
   public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_detailed_deal);
	  getSupportActionBar().setTitle("Claim Deal");
	  Toast.makeText(getApplicationContext(), "Claim Order", Toast.LENGTH_SHORT).show();
	  Intent intent = getIntent();

	  lID = intent.getStringExtra("LaundryID");
	  lName = intent.getStringExtra("LaundryName");
	  dId = intent.getStringExtra("deal_id");
	  dTitle = intent.getStringExtra("deal_title");
	  dText = intent.getStringExtra("deal_text");
	  dImageUrl = intent.getStringExtra("deal_image_url");
	  dAddress = intent.getStringExtra("deal_address");
	  lservicearea = intent.getStringExtra(FlagLaun.cLAUNDRYSERVICEAREA);
	  lLat = intent.getStringExtra("lat");
	  lLog = intent.getStringExtra("log");

	  ActionBar bar = getSupportActionBar();
	  bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#004765")));
	  bar.setDisplayShowHomeEnabled(true);
//	  bar.setDisplayHomeAsUpEnabled(true);
	  bar.setHomeButtonEnabled(true);



	  tdTitle = (TextView) findViewById(R.id.deal_text_heading);
	  tdTitle.setText(dTitle);
	  tdText = (TextView) findViewById(R.id.deal_text_caption);
	  tdText.setText(dText);
	  tlName = (TextView) findViewById(R.id.deal_text_laundry_name);
	  tlName.setText(lName);
	  tlAddress = (TextView) findViewById(R.id.deal_text_laundry_address);
	  tlAddress.setText(Html.fromHtml(dAddress));
	  dImage = (ImageView) findViewById(R.id.deal_image);

	  tlservicearea = (TextView) findViewById(R.id.deal_servicearea);
	  tlservicearea.setText(lservicearea);


	  //Downloading Image
	  new AsyncTask<Void, Void, Void>() {
		 Drawable image = null;
		 boolean cancel = false;

		 @Override
		 protected Void doInBackground(Void... params) {
			URL thumb_u = null;
			try {
			   thumb_u = new URL(dImageUrl);
			   image = Drawable.createFromStream(thumb_u.openStream(), "src");

			} catch (IOException e) {
			   e.printStackTrace();
			}

			return null;
		 }

		 @Override
		 protected void onCancelled() {
			super.onCancelled();
			cancel = true;
		 }

		 @Override
		 protected void onPostExecute(Void aVoid) {

			if (dImage != null && !cancel) {
			   dImage.setImageDrawable(image);
			   FragmentManager fragmentManager = getSupportFragmentManager();

			   SupportMapFragment mMapSupport = ((SupportMapFragment) fragmentManager.findFragmentById(R.id.deal_map));

			   GoogleMap mMap = null;
			   if (mMapSupport != null && !lLat.equals("") && !lLog.equals("")) {
				  mMap = mMapSupport.getMap();
//                    final LatLng LAUNDARY = new LatLng(Double.parseDouble(lLat), Double.parseDouble(lLog));
				  final LatLng LAUNDARY = new LatLng(Double.parseDouble(lLat), Double.parseDouble(lLog));

				  Marker laundary = mMap.addMarker(new MarkerOptions()
						  .position(LAUNDARY)
						  .title(lName));
				  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LAUNDARY, 14));

			   }

			}
		 }
	  }.execute();

   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.menu_deals_intermediate, menu);
	  return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
	  // Handle action bar item clicks here. The action bar will
	  // automatically handle clicks on the Home/Up button, so long
	  // as you specify a parent activity in AndroidManifest.xml.
	  int id = item.getItemId();

//        noinspection SimplifiableIfStatement
	  if (id == R.id.direct_to_home) {
		 Intent intent = new Intent(this, BaseFragmentActivity.class);
		 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(intent);
		 overridePendingTransition(R.anim.right_in, R.anim.left_out);
		 return true;
	  }

	  return super.onOptionsItemSelected(item);
   }

   public void claimOrder(View view) {
	  Intent intent = new Intent(this, ChooseOnlineOfflineActivity.class);
	  intent.putExtra("LaundryId", lID);
	  intent.putExtra("LaundryName", lName);
	  intent.putExtra("deal_text", dText);
	  startActivity(intent);
	  overridePendingTransition(R.anim.right_in, R.anim.left_out);

   }
}
