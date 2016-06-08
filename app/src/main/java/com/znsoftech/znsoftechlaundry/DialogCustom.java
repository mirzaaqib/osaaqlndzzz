package com.znsoftech.znsoftechlaundry;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class DialogCustom extends DialogFragment {

   private String laundryId;

   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
	  final AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());
	  mDialog.setTitle("Have you placed order with this laundry")
//                .setView(R.layout.activity_dialog_custom)
			  .setPositiveButton("YES", new DialogInterface.OnClickListener() {

				 @Override
				 public void onClick(DialogInterface dialog, int which) {

					Intent i = new Intent(getActivity(), BaseFragmentActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        i.putExtra("rate", 1);
					i.putExtra("rate", 2);

					i.putExtra("a", "order");
					startActivity(i);

				 }
			  })
			  .setNegativeButton("NO",
					  new DialogInterface.OnClickListener() {
						 @Override
						 public void onClick(DialogInterface dialog,
											 int which) {
							Intent i = new Intent(getActivity(), New_Rate.class);
							i.putExtra("LaundryID", laundryId);
							startActivity(i);

						 }
					  }
			  )
	  ;
	  return mDialog.create();
   }

   DialogCustom init(String tag) {
	  laundryId = tag;
	  return this;
   }
}
