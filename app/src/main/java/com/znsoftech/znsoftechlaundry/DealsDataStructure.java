package com.znsoftech.znsoftechlaundry;

/**
 * Created by sandeeprana on 17/12/15.
 */
public class DealsDataStructure {
   private String rank;
   private String LaundryId;
   private String laundryName;
   private String dealid;
   private String dealtitle;
   private String dealtext;
   private String dealimageurl;
   private String dealaddress;
   private String latitude;
   private String longitude;
   private String LaundryServiceArea;

   public DealsDataStructure(String laundryId, String laundryName, String dealid, String dealtitle,
							 String dealtext, String dealimageurl, String dealaddress, String
									 latitude, String longitude, String rank, String laundryServiceArea) {
	  this.LaundryId = laundryId;
	  this.laundryName = laundryName;
	  this.dealid = dealid;
	  this.dealtitle = dealtitle;
	  this.dealtext = dealtext;
	  this.dealimageurl = dealimageurl;
	  this.dealaddress = dealaddress;
	  this.latitude = latitude;
	  this.longitude = longitude;
	  this.rank = rank;
	  this.LaundryServiceArea = laundryServiceArea;
   }

   public String getLaundryId() {
	  return LaundryId;
   }

   public String getLaundryName() {
	  return laundryName;
   }

   public String getDealid() {
	  return dealid;
   }

   public String getDealtitle() {
	  return dealtitle;
   }

   public String getDealtext() {
	  return dealtext;
   }

   public String getDealimageurl() {
	  return dealimageurl;
   }

   public String getDealaddress() {
	  return dealaddress;
   }

   public String getLatitude() {
	  return latitude;
   }

   public String getLongitude() {
	  return longitude;
   }

   public String getRank() {
	  return rank;
   }

   public String getLaundryServiceArea() {
	  return this.LaundryServiceArea;
   }

}
