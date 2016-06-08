package com.znsoftech.znsoftechlaundry.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

   public static boolean HaveNetworkConnection(Context context) {
	  boolean isNetworkAvailable = false;

	  ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	  NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	  for (NetworkInfo ni : netInfo) {
		 if (ni.getTypeName().equalsIgnoreCase("WIFI"))
			if (ni.isConnected()) {
			   isNetworkAvailable = true;
			   break;
			}
		 if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
			if (ni.isConnected()) {
			   isNetworkAvailable = true;
			   break;
			}

	  }
	  return isNetworkAvailable;
   }

//    public static String CheckNetworkType(Context context) {
//        final String tag = "CheckNetworkType";
//        String networkType = "wifi";
//        NetworkInfo active_network = ((ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE))
//                .getActiveNetworkInfo();
//        if (active_network != null && active_network.isConnected()) {
//            if (active_network.getType() == ConnectivityManager.TYPE_WIFI) {
//                Log.v(tag, "WIFI");
//                networkType = "wifi";
//
//            } else {
//
//                TelephonyManager tm = (TelephonyManager) context
//                        .getSystemService(Context.TELEPHONY_SERVICE);
//
//                if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE) {
//                    // Network type is 2G
//
//                    Log.v(tag, "2G or GSM");
//                    networkType = "gprs";
//                } else if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_CDMA) {
//                    // Network type is 2G
//                    Log.v(tag, "2G or CDMA");
//                    networkType = "gprs";
//                } else if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
//                    // Network type is 3G
//                    Log.v(tag, "3G Network available.");
//                    networkType = "3G";
//                } else if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS) {
//                    // Network type is 3G
//                    Log.v(tag, "GPRS Network available.");
//                    networkType = "gprs";
//                }
//            }
//        }
//        return networkType;
//    }

}
