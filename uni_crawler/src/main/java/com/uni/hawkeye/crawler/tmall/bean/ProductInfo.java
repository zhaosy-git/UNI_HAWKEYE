package com.uni.hawkeye.crawler.tmall.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ProductInfo {

	private String product_id;
	private String product_title;
	private int way_type;
	private List<?> priceList;
	private int product_month_sale_cnt;
	private String promotion_info;
	private String brand_name;
	private int product_list_id;
	private String url;
	private int status;
	private String json;
	private Date ctime;
	private Date mtime;

	/**
	 * @return the product_id
	 */
	public String getProduct_id() {
		return product_id;
	}

	/**
	 * @param product_id
	 *            the product_id to set
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
	 * @param product_title
	 *            the product_title to set
	 */
	public void setProduct_title(String product_title) {
		this.product_title = product_title;
	}

	/**
	 * @return the way_type
	 */
	public int getWay_type() {
		return way_type;
	}

	/**
	 * @param way_type
	 *            the way_type to set
	 */
	public void setWay_type(int way_type) {
		this.way_type = way_type;
	}

	/**
	 * @return the priceList
	 */
	public List<?> getPriceList() {
		return priceList;
	}

	/**
	 * @param priceList the priceList to set
	 */
	public void setPriceList(List<?> priceList) {
		this.priceList = priceList;
	}

	/**
	 * @return the product_month_sale_cnt
	 */
	public int getProduct_month_sale_cnt() {
		return product_month_sale_cnt;
	}

	/**
	 * @param product_month_sale_cnt
	 *            the product_month_sale_cnt to set
	 */
	public void setProduct_month_sale_cnt(int product_month_sale_cnt) {
		this.product_month_sale_cnt = product_month_sale_cnt;
	}

	/**
	 * @return the promotion_info
	 */
	public String getPromotion_info() {
		return promotion_info;
	}

	/**
	 * @param promotion_info
	 *            the promotion_info to set
	 */
	public void setPromotion_info(String promotion_info) {
		this.promotion_info = promotion_info;
	}

	/**
	 * @return the brand_name
	 */
	public String getBrand_name() {
		return brand_name;
	}

	/**
	 * @param brand_name
	 *            the brand_name to set
	 */
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	/**
	 * @return the product_list_id
	 */
	public int getProduct_list_id() {
		return product_list_id;
	}

	/**
	 * @param product_list_id
	 *            the product_list_id to set
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
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the json
	 */
	public String getJson() {
		return json;
	}

	/**
	 * @param json
	 *            the json to set
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
	 * @param ctime
	 *            the ctime to set
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
	 * @param mtime
	 *            the mtime to set
	 */
	public void setMtime(Date mtime) {
		this.mtime = mtime;
	}

}
