package com.znsoftech.znsoftechlaundry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;

public class User_Agreement extends Fragment {

   @Override
   public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  setHasOptionsMenu(true);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	  View view = inflater.inflate(R.layout.user_agreement, container, false);
//        TextView title = (TextView) view.findViewById(R.id.title);
//
//        title.setText(getResources().getString(R.string.user_agreement));

	  DocumentView documentView = (DocumentView) view.findViewById(R.id.text_useragreement_justified);
	  documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);


	  return view;
   }

   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	  super.onCreateOptionsMenu(menu, inflater);

//        int city_array_size = Config.cityArray.size();
//        if (city_array_size > 0)
//            for (int i = 0; i < city_array_size; i++)
//                menu.findItem(i).setVisible(false);
   }
}
