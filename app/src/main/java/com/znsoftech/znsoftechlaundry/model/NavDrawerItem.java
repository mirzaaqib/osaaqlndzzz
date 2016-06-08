package com.znsoftech.znsoftechlaundry.model;

public class NavDrawerItem {

   private String title;
   private int icon;
   private String count = "0";

   public NavDrawerItem(String title, int icon) {
	  this.title = title;
	  this.icon = icon;
   }

   public String getTitle() {
	  return this.title;
   }

   public void setTitle(String title) {
	  this.title = title;
   }

   public int getIcon() {
	  return this.icon;
   }

   public void setIcon(int icon) {
	  this.icon = icon;
   }

   public String getCount() {
	  return this.count;
   }
}
