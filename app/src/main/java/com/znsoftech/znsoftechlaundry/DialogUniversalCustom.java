package com.znsoftech.znsoftechlaundry;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class DialogUniversalCustom extends DialogFragment {

   private String laundryId;
   private String title;
   private String positive;
   private String negative;
   private DialogInterface.OnClickListener negListener;
   private DialogInterface.OnClickListener posListener;
   private String message;


   @NonNull
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
	  final AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());
	  mDialog.setTitle(title)
			  .setMessage(message)
//                .setView(R.layout.activity_dialog_custom)
			  .setPositiveButton(positive, posListener)
			  .setNegativeButton(negative, negListener)
	  ;
	  return mDialog.create();
   }

   DialogUniversalCustom init(String tag, String tit, String pos, String neg, DialogInterface
		   .OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
	  laundryId = tag;
	  this.title = tit;
	  this.positive = pos;
	  this.negative = neg;
	  this.posListener = positiveListener;
	  this.negListener = negativeListener;

	  return this;
   }

   DialogUniversalCustom initExtraWithMessage(String message, String tit, String pos, String neg,
											  DialogInterface
													  .OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
//	  laundryId = tag;
	  this.message = message;
	  this.title = tit;
	  this.positive = pos;
	  this.negative = neg;
	  this.posListener = positiveListener;
	  this.negListener = negativeListener;

	  return this;
   }

}
