package com.znsoftech.znsoftechlaundry;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.znsoftech.znsoftechlaundry.adapter.NavDrawerListAdapter;
import com.znsoftech.znsoftechlaundry.json.DownloadJsonContent;
import com.znsoftech.znsoftechlaundry.model.NavDrawerItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//import android.support.v4.widget.SwipeRefreshLayout;

public class BaseFragmentActivity extends ActionBarActivity implements
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		LocationListener {

   int city_array_size;
   JSONObject json;
   ActionBar bar;
   ActionMode actionMode;
   Context context;
   SharedPreferences preferences;
   baseDialogAdvertisement baseDial;
   private DrawerLayout mDrawerLayout;
   private ListView mDrawerList;
   private ActionBarDrawerToggle mDrawerToggle;
   private Fragment fragment = null;
   // used to store app title
   private CharSequence mTitle;
   // slide menu items
   private String[] navMenuTitles;
   private TypedArray navMenuIcons;
   private ArrayList<NavDrawerItem> navDrawerItems;
   private NavDrawerListAdapter adapter;
   private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

	  @Override
	  public boolean onCreateActionMode(final ActionMode mode, Menu menu) {

		 actionMode = mode;
		 MenuInflater inflater = mode.getMenuInflater();
		 inflater.inflate(R.menu.main1, menu);

		 MenuItem searchItem = menu.findItem(R.id.edt_mySearch);
		 RelativeLayout m = (RelativeLayout) MenuItemCompat.getActionView(searchItem);
		 SearchView mSearchView = (SearchView) m.findViewById(R.id.edt_search);

		 mSearchView.setIconifiedByDefault(false);
		 mSearchView.setFocusable(true);
		 mSearchView.requestFocusFromTouch();

		 mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
			   if (!hasFocus) {
				  mode.finish();
			   }

			}
		 });
		 mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {

			   actionMode.finish();

			   Intent intent = new Intent(BaseFragmentActivity.this, SearchActivity.class);
			   intent.putExtra("SearchText", s);
			   startActivity(intent);
			   overridePendingTransition(R.anim.right_in, R.anim.left_out);

			   InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			   if (inputManager != null) {
				  View view = getCurrentFocus();
				  if (view != null) {
					 IBinder token = view.getWindowToken();
					 if (token != null)
						inputManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
				  }
			   }

			   return true;
			}

			@Override
			public boolean onQueryTextChange(String s) {
			   return false;
			}
		 });


		 return true;
	  }

	  @Override
	  public void onDestroyActionMode(ActionMode mode) {

	  }

	  @Override
	  public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		 switch (item.getItemId()) {

			case R.id.action_search:
			   //close the action mode
			   //mode.finish();
			   return true;
			default:
			   actionMode.finish();
			   InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			   if (inputManager != null) {
				  View view = getCurrentFocus();
				  if (view != null) {
					 IBinder token = view.getWindowToken();
					 if (token != null)
						inputManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
				  }
			   }
			   return false;
		 }
	  }


	  @Override
	  public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
		 // TODO Auto-generated method stub
		 return false;
	  }
   };
   private ListView mDrawerList_right;
   private GoogleApiClient mGoogleApiClient;
   private LocationRequest mLocationRequest;

   public static void trimCache(Context context) {
	  try {
		 File dir = context.getCacheDir();
		 if (dir != null && dir.isDirectory()) {
			deleteDir(dir);

		 }
	  } catch (Exception e) {
		 // TODO: handle exception
	  }
   }

   public static boolean deleteDir(File dir) {
	  if (dir != null && dir.isDirectory()) {
		 String[] children = dir.list();
		 for (int i = 0; i < children.length; i++) {
			boolean success = deleteDir(new File(dir, children[i]));
			if (!success) {
			   return false;
			}
		 }
	  }
	  return dir.delete();
   }

//   @Override
//   protected void onResume() {
//	  Intent intent=new Intent(this,LoginActivity.class);
//	  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	  startActivity(intent);
//   }
//   @Override
//   protected void onRestart() {
//	  Intent intent=new Intent(this,LoginActivity.class);
//	  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	  startActivity(intent);
//   }

   //    private SwipeRefreshLayout swipeRefreshLayout;
   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.fragmrnt_activity_main);

	  mGoogleApiClient = new GoogleApiClient.Builder(this)
			  // The next two lines tell the new client that “this” current class will handle connection stuff
			  .addConnectionCallbacks(this)
			  .addOnConnectionFailedListener(this)
					  //fourth line adds the LocationServices API endpoint from GooglePlayServices
			  .addApi(LocationServices.API)
			  .build();
	  mLocationRequest = LocationRequest.create()
			  .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
			  .setInterval(10 * 1000)        // 10 seconds, in milliseconds
			  .setFastestInterval(1 * 1000); // 1 second, in milliseconds


	  context = this;
	  preferences = getSharedPreferences("Settings", MODE_PRIVATE);
	  bar = getSupportActionBar();
	  bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.action_bar_color))));
	  bar.setDisplayShowHomeEnabled(true);

	  adDialog();


	  int order = getIntent().getIntExtra("rate", 0);
	  String al = getIntent().getStringExtra("a");

	  Config.city = preferences.getString("city", "Dubai");

	  try {
		 ViewConfiguration config = ViewConfiguration.get(this);

		 Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
		 //Why multiple objects are used here
		 if (menuKeyField != null) {
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		 }
	  } catch (Exception ex) {
		 // Ignore
	  }


	  View view = getLayoutInflater().inflate(R.layout.actionbarview, null);
	  bar.setCustomView(view);
	  bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);


	  bar.setDisplayHomeAsUpEnabled(true);
	  bar.setHomeButtonEnabled(true);
	  bar.setTitle(getString(R.string.app_name));


	  mTitle = getTitle();

	  // load slide menu items
	  navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

	  // nav drawer icons from resources
	  navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

	  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	  mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
	  mDrawerList_right = (ListView) findViewById(R.id.list_slidermenu_right);
//	  mDrawerList_right.setBackgroundColor(R.color.topbar_back);

	  mDrawerToggle = new ActionBarDrawerToggle(
			  BaseFragmentActivity.this,                             /* host Activity */
			  mDrawerLayout,                    /* DrawerLayout object */
			  R.string.app_name,             /* nav drawer image to replace 'Up' caret */
			  R.string.app_name /* "close drawer" description for accessibility */
	  ) {
		 @Override
		 public void onDrawerClosed(View drawerView) {
			invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
		 }

		 @Override
		 public void onDrawerOpened(View drawerView) {
			invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			   mDrawerLayout.closeDrawer(mDrawerList_right);
			}
		 }
	  };

	  // Defer code dependent on restoration of previous instance state.
	  // NB: required for the drawer indicator to show up!
	  mDrawerLayout.post(new Runnable() {
		 @Override
		 public void run() {
			android.content.res.Resources resources = getResources();
			float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, resources
					.getDisplayMetrics());
			DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
			params.width = (int) (width);
			mDrawerList.setLayoutParams(params);
			mDrawerToggle.syncState();
		 }
	  });


	  mDrawerLayout.setDrawerListener(mDrawerToggle);

	  navDrawerItems = new ArrayList<NavDrawerItem>();

	  // adding nav drawer items to array
	  // Home
	  navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
	  // Find People
	  navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
	  // Photos
	  navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
	  navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
	  navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
	  navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
	  navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
	  navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
	  navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
	  navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));
	  navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));
	  navDrawerItems.add(new NavDrawerItem(navMenuTitles[11], navMenuIcons.getResourceId(11, -1)));


	  // Recycle the typed array
	  navMenuIcons.recycle();
	  mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

	  // setting the nav drawer list adapter
	  adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
	  mDrawerList.setAdapter(adapter);


	  FragmentManager fragmentManager = getSupportFragmentManager();

	  if (al != null && al.equalsIgnoreCase("order")) {
		 order = 2;
		 displayView(order);

//            fragment = new Order_History();
//            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "order_history").commit();

	  } else {
		 displayView(0);

		 fragment = new HomeFragment();
		 fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "home").commit();

	  }

	  TinyDB tinyDB = new TinyDB(this);
	  if (tinyDB.getListString("CityArray") == null) {
//	  if (preferences.getStringSet("CityArray", null)==null){
		 Intent intent = new Intent(this, LoginActivity.class);
		 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 startActivity(intent);
		 return;
	  }

	  mDrawerList_right.setAdapter(new ArrayAdapter<>(
			  BaseFragmentActivity.this,
			  R.layout.simple_list_item_1,
			  android
					  .R.id.text1,
			  tinyDB.getListString("CityArray").toArray()));

	  mDrawerList_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		 @Override
		 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


			ArrayList<String> strings = new TinyDB(context).getListString("CityArray");
			String[] cities = strings.toArray(new String[strings.size()]);

			Config.city = cities[position];
			preferences.edit().putString("city", cities[position]).commit();

			if (HomeFragment.pager != null) {
			   int index = HomeFragment.pager.getCurrentItem();
			   Fragment new_fragment = new HomeFragment();
			   Bundle data = new Bundle();
			   String[] str = new String[]{"all", "nearby", "suggest", "favorite"};

			   if (index < 4) {
				  data.putString("Tab", str[index]);

				  new_fragment.setArguments(data);
				  fragment = new_fragment;

				  FragmentManager fragmentManager = getSupportFragmentManager();
				  fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			   }
			}
			Toast.makeText(getApplicationContext(), Config.city, Toast.LENGTH_LONG).show();
			mDrawerLayout.closeDrawer(mDrawerList_right);
		 }
	  });


   }

   /**
	* Diplaying fragment view for selected nav drawer list item
	*/
   private void displayView(final int position) {
	  // update the main content by replacing fragments
	  FragmentManager fragmentManager = getSupportFragmentManager();
//        ActionBar baraction=getSupportActionBar();
	  TextView titlebar_title = (TextView) findViewById(R.id.action_title_custom);
	  switch (position) {
		 case 0:
			fragment = new HomeFragment();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "home").commit();
			titlebar_title.setText(" ");
			fragmentManager
					.beginTransaction()
					.add(new HomeFragment(), "HOME")
					.commit();

			mDrawerLayout.closeDrawer(mDrawerList);

			break;
		 case 9: {

			titlebar_title.setText("About Us");
//                baraction.setTitle("About Us");
			mDrawerLayout.closeDrawer(mDrawerList);
			fragment = new AboutActivity();
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
					.replace(R.id.frame_container, fragment, "About")
					.addToBackStack("home")
					.commit();

			break;
		 }
		 case 5:


			mDrawerLayout.closeDrawer(mDrawerList);
			try {
			   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName())));
			} catch (ActivityNotFoundException e) {
			   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
			}
			break;
		 case 6: {

			mDrawerLayout.closeDrawer(mDrawerList);
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
			String sAux = getResources().getString(R.string.share_with_friends_text);
			sAux = sAux + "\n\nhttps://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
			i.putExtra(Intent.EXTRA_TEXT, sAux);
			startActivity(Intent.createChooser(i, getResources().getString(R.string.choose_one)));
		 }
		 break;
		 case 7: {

//                baraction.setTitle("HOME");
			mDrawerLayout.closeDrawer(mDrawerList);
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + getResources().getString(R.string.report_issue_mail_id)));
			intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.report_issue_subject));
			startActivity(intent);
		 }
		 break;

		 case 3:
			titlebar_title.setText("My Address");
//                baraction.setTitle("My Address");
			mDrawerLayout.closeDrawer(mDrawerList);
			fragment = new Manage_Address();
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
					.replace(R.id.frame_container, fragment, "manage_address")
					.addToBackStack("home")
					.commit();

			break;
		 case 2:
			mDrawerLayout.closeDrawer(mDrawerList);
			fragment = new Order_History();

			titlebar_title.setText("Order History");
//                baraction.setTitle("Order History");
			fragmentManager.beginTransaction()
					.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
					.replace(R.id.frame_container, fragment, "order_history")
					.addToBackStack("home")
					.commit();
			break;
		 case 8:
			mDrawerLayout.closeDrawer(mDrawerList);
			fragment = new Contact_Us();

			titlebar_title.setText("Contact Us");
//                baraction.setTitle("Contact Us");
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
					.replace(R.id.frame_container, fragment, "contact_us")
					.addToBackStack("home")
					.commit();
			break;

		 case 10:
			mDrawerLayout.closeDrawer(mDrawerList);
			fragment = new User_Agreement();

			titlebar_title.setText("User Agreement");
//                baraction.setTitle("User Agreement");
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
					.replace(R.id.frame_container, fragment, "user_agreement")
					.addToBackStack("home")
					.commit();

			break;

		 case 4:
			mDrawerLayout.closeDrawer(mDrawerList);
			fragment = new Change_Country();

			titlebar_title.setText("Change Country");
//                baraction.setTitle("User Agreement");
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
					.replace(R.id.frame_container, fragment, "change_country")
					.addToBackStack("home")
					.commit();

			break;

		 case 1:
			mDrawerLayout.closeDrawer(mDrawerList);
//			fragment = new EditProfileFragment();
			Intent intentHT = new Intent(this, MyProfile.class);

			startActivity(intentHT);
//			titlebar_title.setText("User Agreement");
////                baraction.setTitle("User Agreement");
//			fragmentManager
//					.beginTransaction()
//					.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//					.replace(R.id.frame_container, fragment, "user_agreement")
//					.addToBackStack("home")
//					.commit();

			break;

		 case 11: {
			mDrawerLayout.closeDrawer(mDrawerList);
			SharedPreferences prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
			prefs.edit().clear().commit();
			Config.email = "";
			Config.mobile = "";
			Config.password = "";
			Config.name = "";
			Config.userid = "0";
			Config.latitude = "78.0";
			Config.longitude = "28.0";

			titlebar_title.setText("Settings");
//                baraction.setTitle("Settings");
			Intent intent = new Intent(BaseFragmentActivity.this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
			finish();
		 }
		 break;

		 default:
			break;
	  }
   }

   @Override
   public void setTitle(CharSequence title) {
	  mTitle = title;
   }

   //    boolean EXIT=false;
   @Override
   public void onBackPressed() {
	  // TODO Auto-generated method stub
	  if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
		 mDrawerLayout.closeDrawer(mDrawerList);
		 return;
	  }
	  if (actionMode != null)
		 actionMode.finish();


//
	  super.onBackPressed();
   }

   /**
	* When using the ActionBarDrawerToggle, you must call it during
	* onPostCreate() and onConfigurationChanged()...
	*/

   @Override
   protected void onPostCreate(Bundle savedInstanceState) {
	  super.onPostCreate(savedInstanceState);
   }

   @Override
   public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.main, menu);


//	  Config.cityArray.clear();
	  city_array_size = Config.cityArray.size();
	  if (Config.cityArray.size() <= 0) {

		 for (int i = 0; i < city_array_size; i++) {

			String citystr = Config.cityArray.get(i);
//			JSONObject jobj = new JSONObject(citystr);
//                menu.add(0, i, i, jobj.getString("CityName"));
			Config.cityArray.add(citystr);

		 }
	  }

	  return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {

	  int id = item.getItemId();
	  if (id == R.id.action_search) {

		 bar.startActionMode(mActionModeCallback);
		 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		 imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//            DialogCities dialogCities=new DialogCities();
//            dialogCities.show(getSupportFragmentManager(),"Dialog");
		 return true;

	  }
//	  else if (id == R.id.direct_to_home) {
//		 getSupportFragmentManager()
//				 .beginTransaction()
//				 .replace(R.id.frame_container, new HomeFragment(), "home")
//				 .commit();
//		 /**
//		  * Removing back stack
//		  */
//
//		 FragmentManager fm = getSupportFragmentManager();
//
//
//		 for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//			fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//		 }
//
//		 TextView titlebar_title = (TextView) findViewById(R.id.action_title_custom);
//		 titlebar_title.setText(" ");
//
//	  }

	  else if (id == R.id.cities) {

		 if (mDrawerLayout.isDrawerOpen(mDrawerList_right)) {
			mDrawerLayout.closeDrawer(mDrawerList_right);
		 } else {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
			   mDrawerLayout.closeDrawer(mDrawerList);
			}
			mDrawerLayout.openDrawer(mDrawerList_right);

		 }

	  } else {
		 if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			mDrawerLayout.closeDrawer(mDrawerList);
		 } else
			mDrawerLayout.openDrawer(Gravity.LEFT);
	  }
	  return super.onOptionsItemSelected(item);
   }

   void setActionBarTitle(String title) {
	  getSupportActionBar().setTitle(title);
//        getSupportActionBar().ba
   }

   @Override
   protected void onDestroy() {
	  super.onDestroy();
	  trimCache(this);
   }

   private void adDialog() {
	  if (preferences.getBoolean("ad", true)) {
		 baseDial = new baseDialogAdvertisement();
		 baseDial.execute();
	  }
	  preferences.edit().putBoolean("ad", false).apply();
   }

   @Override
   protected void onStop() {
	  super.onStop();
	  if (baseDial != null) {
		 baseDial.cancel(true);
	  }
	  mGoogleApiClient.disconnect();
   }

   @Override
   public void onConnected(Bundle bundle) {
	  Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

	  if (location == null) {
		 LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

	  } else {
		 //If everything went fine lets get latitude and longitude
		 Config.latitude = String.valueOf(location.getLatitude());
		 Config.longitude = String.valueOf(location.getLongitude());
		 Log.e("location", Config.latitude + "        " + Config.longitude);
//		 Toast.makeText(this, c + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
	  }
   }

   @Override
   public void onConnectionSuspended(int i) {

   }

   @Override
   public void onLocationChanged(Location location) {
	  Config.latitude = String.valueOf(location.getLatitude());
	  Config.longitude = String.valueOf(location.getLongitude());
	  Log.e("location", Config.latitude + "        " + Config.longitude);
   }

   @Override
   public void onConnectionFailed(ConnectionResult connectionResult) {

   }

   @Override
   protected void onResume() {
	  super.onResume();
	  mGoogleApiClient.connect();
   }

   ;

   @Override
   protected void onPause() {
	  super.onPause();
	  if (mGoogleApiClient.isConnected()) {
		 LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
		 mGoogleApiClient.disconnect();
	  }
   }

   public void tohome(View view) {
	  Intent intent = new Intent(this, BaseFragmentActivity.class);
	  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	  startActivity(intent);
   }

   /**
	* Slide menu item click listener
	*/
   private class SlideMenuClickListener implements
		   ListView.OnItemClickListener {
	  @Override
	  public void onItemClick(AdapterView<?> parent, View view, int position,
							  long id) {
		 // display view for selected nav drawer item
		 displayView(position);
	  }
   }

   private class baseDialogAdvertisement extends AsyncTask<Void, Void, Void> {


	  Bitmap bitmap = null;
	  String text, image_url = null;

	  @Override
	  protected Void doInBackground(Void... params) {
		 try {


			String data = DownloadJsonContent.downloadContent(Config.urlAdvertisement);

			JSONObject object = new JSONObject(data);
			if (object != null && object.has("data") && !object.isNull("data") && object.getInt("status")
					== 200) {
			   JSONObject dataObj = object.getJSONObject("data");

			   if (dataObj.has("Advertisement_text") && !dataObj.isNull("Advertisement_text")) {
				  text = dataObj.getString("Advertisement_text");
			   }
			   if (dataObj.has("Advertisement_image") && !dataObj.isNull("Advertisement_text")) {
				  image_url = dataObj.getString("Advertisement_image");
			   }
			}

			if (image_url != null) {
			   URL url = new URL(image_url);
			   bitmap = BitmapFactory.decodeStream(url.openStream());
			}

		 } catch (MalformedURLException e) {
			e.printStackTrace();
		 } catch (IOException e) {
			e.printStackTrace();
		 } catch (JSONException e) {
			e.printStackTrace();
		 }

		 return null;
	  }

	  @Override
	  protected void onPostExecute(Void aVoid) {

		 if (!isCancelled()) {

			super.onPostExecute(aVoid);
			if (bitmap != null) {
			   final Dialog dialog = new Dialog(context);
			   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

			   dialog.setContentView(R.layout.adview);
			   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//			   dialog.setTitle("OFFER");
			   dialog.show();
			   ImageView view_image = (ImageView) dialog.findViewById(R.id.image_advertisement);
			   view_image.setImageBitmap(bitmap);
			   view_image.setScaleType(ImageView.ScaleType.FIT_XY);
			   TextView text_text = (TextView) dialog.findViewById(R.id.text_advertisement);
			   text_text.setText(text);
			   Button button = (Button) dialog.findViewById(R.id.button_advertisement);
			   button.setOnClickListener(new View.OnClickListener() {
				  @Override
				  public void onClick(View v) {
					 dialog.dismiss();
				  }
			   });

			}

		 }
	  }
   }
}