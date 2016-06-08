package com.znsoftech.znsoftechlaundry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.znsoftech.znsoftechlaundry.tabs.DealsFragment;
import com.znsoftech.znsoftechlaundry.tabs.HomeTabNewGrid;
import com.znsoftech.znsoftechlaundry.tabs.LaundryFragmentNearbyLeadingFavourite;
import com.znsoftech.znsoftechlaundry.tabs.NearbyFragment;
import com.znsoftech.znsoftechlaundry.tabs.lib.TabPageIndicator2;

public class HomeFragment extends Fragment implements LocationListener {

   private static final String[] CONTENT = new String[]{"Home", "Leading", "Nearby", "Favourite", "Deals"};
   static ViewPager pager;
   View rootView;
   FragmentPagerAdapter adapter;
   TabPageIndicator2 indicator;
//
//    public static HomeFragment newInstance() {
//        HomeFragment fragment = new HomeFragment();
//        return fragment;
//    }

   @Override
   public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  gpsCheckandEnableChangeParameter();
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	  rootView = inflater.inflate(R.layout.home_root_fragment, container, false);

	  adapter = new GoogleMusicAdapter(getChildFragmentManager());

	  pager = (ViewPager) rootView.findViewById(R.id.mymusic_pager);
	  indicator = (TabPageIndicator2) rootView.findViewById(R.id.mymusic_indicator);
	  pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

		 @Override
		 public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//			Log.e("OnTabSelected","TabScroll");
		 }

		 @Override
		 public void onPageSelected(int position) {

//			Log.e("OnTabSelected","TabSelected");
			Log.e("OnTabSelected", CONTENT[position]);

			LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			boolean isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


			if (CONTENT[position].equalsIgnoreCase("Nearby") && !isEnabled) {
			   Log.e("OnTabSelected", "TabSelect" + String.valueOf(position));
			   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			   builder.setTitle("Enable GPS")
					   .setMessage("Nearby requires GPS for fine results.")
					   .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
						  @Override
						  public void onClick(DialogInterface dialog, int which) {
//							 Log.e("OK","OK");
//							 Config.GPS_ENABLED=true;
							 startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						  }
					   })
					   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						  @Override
						  public void onClick(DialogInterface dialog, int which) {
//					 Log.e("Cancel","Cancel");
						  }
					   });
			   builder.show();

			}

		 }

		 @Override
		 public void onPageScrollStateChanged(int state) {
//			Log.e("OnTabSelected","TabState");

		 }
	  });
	  pager.setAdapter(adapter);
	  pager.setOffscreenPageLimit(0);
	  indicator.setViewPager(pager);

	  return rootView;
   }

   @Override
   public void onLocationChanged(Location location) {
	  Config.latitude = String.valueOf(location.getLatitude());
	  Config.latitude = String.valueOf(location.getLongitude());
   }

   @Override
   public void onStatusChanged(String provider, int status, Bundle extras) {

   }

   @Override
   public void onProviderEnabled(String provider) {

   }

   @Override
   public void onProviderDisabled(String provider) {

   }

   public void gpsCheckandEnableChangeParameter() {
	  LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
	  if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
		 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		 Config.GPS_ENABLED = true;

	  } else {

		 Config.GPS_ENABLED = false;

	  }

   }

   @Override
   public void onViewCreated(View view, Bundle savedInstanceState) {
	  super.onViewCreated(view, savedInstanceState);
	  TextView textView_currentcountry = (TextView) view.findViewById(R.id.current_city_universal);
	  String currentcity = getActivity().getSharedPreferences("Settings", Context
			  .MODE_PRIVATE).getString("city", null);

	  if (currentcity != null && textView_currentcountry != null) {
		 textView_currentcountry.setVisibility(View.VISIBLE);
		 textView_currentcountry.setText(currentcity);
	  }
   }

   class GoogleMusicAdapter extends FragmentPagerAdapter {
	  public GoogleMusicAdapter(FragmentManager fm) {
		 super(fm);
	  }

	  @Override
	  public Fragment getItem(int position) {
		 Fragment fragment = null;
		 Bundle data = new Bundle();
		 if (CONTENT[position].trim().equalsIgnoreCase("Home")) {
			fragment = HomeTabNewGrid.newInstance();
			data.putString("Tab", "all");
		 } else if (CONTENT[position].trim().equalsIgnoreCase("Nearby")) {
			data.putString("Tab", "nearby");
//			fragment = LaundryFragmentNearbyLeadingFavourite.newInstance();
			fragment = new NearbyFragment();
		 } else if (CONTENT[position].trim().equalsIgnoreCase("Leading")) {
			data.putString("Tab", "suggest");
			fragment = LaundryFragmentNearbyLeadingFavourite.newInstance();
		 } else if (CONTENT[position].trim().equalsIgnoreCase("Favourite")) {
			data.putString("Tab", "favorite");
			fragment = LaundryFragmentNearbyLeadingFavourite.newInstance();
		 } else if (CONTENT[position].trim().equalsIgnoreCase("Deals")) {
			fragment = DealsFragment.newInstance();
		 } else {
			fragment = LaundryFragmentNearbyLeadingFavourite.newInstance();
		 }

		 fragment.setArguments(data);
		 return fragment;
	  }

	  @Override
	  public CharSequence getPageTitle(int position) {
		 return CONTENT[position % CONTENT.length].toUpperCase();
	  }

	  @Override
	  public int getCount() {
		 return CONTENT.length;
	  }


   }
}
