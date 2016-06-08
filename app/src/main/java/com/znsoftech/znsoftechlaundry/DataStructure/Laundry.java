package com.znsoftech.znsoftechlaundry.DataStructure;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by sandeeprana on 06/01/16.
 */
public class Laundry {
   private String name;
   private String id;
   private String distance;
   private String totalreviews;
   private String totalratings;
   private String avgratings;
   private String address;
   private String city;
   private String state;
   private String country;
   private String zipcode;
   private String owner;
   private String servicearea;
   private String phone;
   private String mobile;
   private String callpreference;
   private String email;
   private String website;
   private String imageurl;
   private String latitude;
   private String longitude;
   private String createdby;
   private String createdon;
   private String modifiedon;
   private String isactive;
   private String total_ratiings;
   private String total_reviews;
   private String ratingsum;
   private String laundryworking;
   private String services;

   public static String distance(double lat1, double lat2, double lon1,
								 double lon2) {

	  final int R = 6371; // Radius of the earth

	  Double latDistance = Math.toRadians(lat2 - lat1);
	  Double lonDistance = Math.toRadians(lon2 - lon1);
	  Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
			  + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
			  * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	  Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	  double distance = R * c * 1000; // convert to meters

	  double height = 0;

	  distance = Math.pow(distance, 2) + Math.pow(height, 2);

	  NumberFormat formatter = new DecimalFormat("#0.00");

	  return formatter.format(Math.sqrt(distance) / 1000.0f);

   }

   public String getName() {
	  return name;
   }

   public void setName(String name) {
	  this.name = name;
   }

   public String getId() {
	  return id;
   }

   public void setId(String id) {
	  this.id = id;
   }

   public String getDistance() {
	  return distance;
   }

   /**
	* This is if distance is not available
	*
	* @param message
	*/
   public void setDistance(String message) {

	  this.distance = message;
   }

   public void setDistance(Double lat1, Double lat2, Double lon1, Double lon2) {

	  this.distance = distance(lat1, lat2, lon1, lon2);
   }

   public String getTotalreviews() {
	  return totalreviews;
   }

   public void setTotalreviews(String totalreviews) {
	  this.totalreviews = totalreviews;
   }

   public String getTotalratings() {
	  return totalratings;
   }

   public void setTotalratings(String totalratings) {
	  this.totalratings = totalratings;
   }

   public String getAvgratings() {
	  return avgratings;
   }

   public void setAvgratings(String avgratings) {
	  this.avgratings = avgratings;
   }

   public String getAddress() {
	  return address;
   }

   public void setAddress(String address) {
	  this.address = address;
   }

   public String getCity() {
	  return city;
   }

   public void setCity(String city) {
	  this.city = city;
   }

   public String getState() {
	  return state;
   }

   public void setState(String state) {
	  this.state = state;
   }

   public String getCountry() {
	  return country;
   }

   public void setCountry(String country) {
	  this.country = country;
   }

   public String getZipcode() {
	  return zipcode;
   }

   public void setZipcode(String zipcode) {
	  this.zipcode = zipcode;
   }

   public String getOwner() {
	  return owner;
   }

   public void setOwner(String owner) {
	  this.owner = owner;
   }

   public String getServicearea() {
	  return servicearea;
   }

   public void setServicearea(String servicearea) {
	  this.servicearea = servicearea;
   }

   public String getPhone() {
	  return phone;
   }

   public void setPhone(String phone) {
	  this.phone = phone;
   }

   public String getMobile() {
	  return mobile;
   }

   public void setMobile(String mobile) {
	  this.mobile = mobile;
   }

   public String getCallpreference() {
	  return callpreference;
   }

   public void setCallpreference(String callpreference) {
	  this.callpreference = callpreference;
   }

   public String getEmail() {
	  return email;
   }

   public void setEmail(String email) {
	  this.email = email;
   }

   public String getWebsite() {
	  return website;
   }

   public void setWebsite(String website) {
	  this.website = website;
   }

   public String getImageurl() {
	  return imageurl;
   }

   public void setImageurl(String imageurl) {
	  this.imageurl = imageurl;
   }

   public String getLatitude() {
	  return latitude;
   }

   public void setLatitude(String latitude) {
	  this.latitude = latitude;
   }

   public String getLongitude() {
	  return longitude;
   }

   public void setLongitude(String longitude) {
	  this.longitude = longitude;
   }

   public String getCreatedby() {
	  return createdby;
   }

   public void setCreatedby(String createdby) {
	  this.createdby = createdby;
   }

   public String getCreatedon() {
	  return createdon;
   }

   public void setCreatedon(String createdon) {
	  this.createdon = createdon;
   }

   public String getModifiedon() {
	  return modifiedon;
   }

   public void setModifiedon(String modifiedon) {
	  this.modifiedon = modifiedon;
   }

   public String getIsactive() {
	  return isactive;
   }

   public void setIsactive(String isactive) {
	  this.isactive = isactive;
   }

   public String getTotal_ratiings() {
	  return total_ratiings;
   }

   public void setTotal_ratiings(String total_ratiings) {
	  this.total_ratiings = total_ratiings;
   }

   public String getTotal_reviews() {
	  return total_reviews;
   }

   public void setTotal_reviews(String total_reviews) {
	  this.total_reviews = total_reviews;
   }

   public String getRatingsum() {
	  return ratingsum;
   }

   public void setRatingsum(String ratingsum) {
	  this.ratingsum = ratingsum;
   }

   public String getLaundryworking() {
	  return laundryworking;
   }

   public void setLaundryworking(String laundryworking) {
	  this.laundryworking = laundryworking;
   }

   public String getServices() {
	  return services;
   }

   public void setServices(String services) {
	  this.services = services;
   }
}
