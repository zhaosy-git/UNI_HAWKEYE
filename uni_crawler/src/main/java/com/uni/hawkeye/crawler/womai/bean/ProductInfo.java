package com.uni.hawkeye.crawler.womai.bean;

import java.math.BigDecimal;
import java.util.Date;

public class ProductInfo {

	private String product_id;
	private String product_title;
	private String brand_name;
	private int status;
	private int product_list_id;
	private String url;
	private String json;
	private Date ctime;
	private Date mtime;

	public ProductInfo(){}

	public ProductInfo(String product_id, String product_title, String brand_name, int status, int product_list_id, String url, String json) {
		super();
		this.product_id = product_id;
		this.product_title = product_title;
		this.brand_name = brand_name;
		this.status = status;
		this.product_list_id = product_list_id;
		this.url = url;
		this.json = json;
	}

	/**
	 * @return the product_id
	 */
	public String getProduct_id() {
		return product_id;
	}

	/**
	 * @param product_id the product_id to set
	 */
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	/**
	 * @return the product_title
	 */
	public String getProduct_title() {
		return product_title;
	}

	/**
	 * @param product_title the product_title to set
	 */
	public void setProduct_title(String product_title) {
		this.product_title = product_title;
	}

	/**
	 * @return the brand_name
	 */
	public String getBrand_name() {
		return brand_name;
	}

	/**
	 * @param brand_name the brand_name to set
	 */
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the product_list_id
	 */
	public int getProduct_list_id() {
		return product_list_id;
	}

	/**
	 * @param product_list_id the product_list_id to set
	 */
	public void setProduct_list_id(int product_list_id) {
		this.product_list_id = product_list_id;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the json
	 */
	public String getJson() {
		return json;
	}

	/**
	 * @param json the json to set
	 */
	public void setJson(String json) {
		this.json = json;
	}

	/**
	 * @return the ctime
	 */
	public Date getCtime() {
		return ctime;
	}

	/**
	 * @param ctime the ctime to set
	 */
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	/**
	 * @return the mtime
	 */
	public Date getMtime() {
		return mtime;
	}

	/**
	 * @param mtime the mtime to set
	 */
	public void setMtime(Date mtime) {
		this.mtime = mtime;
	}
	
}
