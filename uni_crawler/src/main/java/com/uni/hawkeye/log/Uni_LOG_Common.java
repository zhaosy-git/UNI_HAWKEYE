package com.uni.hawkeye.log;

public class Uni_LOG_Common{

	public static String Fetch_WhiteList_ERROR(String website, String whiteURL){
		return website + " -- fetch page by whiteList.property error >>> : " + whiteURL;
	}
	
	public static String Parse_Category_ERROR(String website, String whiteURL){
		return website + " -- not parse category error >>> : " + whiteURL;
	}
	
	public static String Fetch_Page_ERROR(String errMessage, String website, String url){
		return website + " -- errMessage >>> : " + errMessage + " -- fetch page by url error >>> : " + url ;
	}
	
	public static String Fetch_JSON_ERROR(String errMessage, String website, String json, String url){
		return website + " -- errMessage >>> : " + errMessage + " -- parse json error, json url >>> : " + url + " -- json >>> : " + json;
	}
	
	public static String Fetch_Product_Begin(String website, String url){
		return website + " --  fetch product begin, list url  ---->> " + url;
	}
	
	public static String Print_Product_ID(String website, String productID){
		return website + " -- product_id ---->> " + productID;
	}
}
