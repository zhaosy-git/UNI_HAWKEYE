package com.uni.hawkeye.crawler.suning.bean;

import java.util.Date;

public class ReviewBadInfo_SUNING {
	
	private int review_bad_id;
	
	private String review_bad_info;
	
	private Date review_bad_time;
	
	private String product_id_fk;
	
	private String json;
	
	private int execute_id;
	
	private Date ctime;
	
	private Date mtime;

	public ReviewBadInfo_SUNING(){}

	public ReviewBadInfo_SUNING(String review_bad_info, Date review_bad_time, String product_id_fk, String json, int execute_id) {
		super();
		this.review_bad_info = review_bad_info;
		this.review_bad_time = review_bad_time;
		this.product_id_fk = product_id_fk;
		this.json = json;
		this.execute_id = execute_id;
	}

	/**
	 * @return the review_bad_id
	 */
	public int getReview_bad_id() {
		return review_bad_id;
	}

	/**
	 * @param review_bad_id the review_bad_id to set
	 */
	public void setReview_bad_id(int review_bad_id) {
		this.review_bad_id = review_bad_id;
	}

	/**
	 * @return the review_bad_info
	 */
	public String getReview_bad_info() {
		return review_bad_info;
	}

	/**
	 * @param review_bad_info the review_bad_info to set
	 */
	public void setReview_bad_info(String review_bad_info) {
		this.review_bad_info = review_bad_info;
	}

	/**
	 * @return the review_bad_time
	 */
	public Date getReview_bad_time() {
		return review_bad_time;
	}

	/**
	 * @param review_bad_time the review_bad_time to set
	 */
	public void setReview_bad_time(Date review_bad_time) {
		this.review_bad_time = review_bad_time;
	}

	/**
	 * @return the product_id_fk
	 */
	public String getProduct_id_fk() {
		return product_id_fk;
	}

	/**
	 * @param product_id_fk the product_id_fk to set
	 */
	public void setProduct_id_fk(String product_id_fk) {
		this.product_id_fk = product_id_fk;
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
	 * @return the execute_id
	 */
	public int getExecute_id() {
		return execute_id;
	}

	/**
	 * @param execute_id the execute_id to set
	 */
	public void setExecute_id(int execute_id) {
		this.execute_id = execute_id;
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
