package com.uni.hawkeye.crawler.tmall.bean;

import java.util.Date;

public class ReviewInfo_TMALL {
	
	private int review_id;
	
	private String product_id_fk;
	
	private int review_cnt;
	
	private float review_grade_avg;
	
	private String review_json;
	
	private String review_grade_avg_json;
	
	private int execute_id;
	
	private Date ctime;
	
	private Date mtime;

	/**
	 * @return the review_id
	 */
	public int getReview_id() {
		return review_id;
	}

	/**
	 * @param review_id the review_id to set
	 */
	public void setReview_id(int review_id) {
		this.review_id = review_id;
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
	 * @return the review_cnt
	 */
	public int getReview_cnt() {
		return review_cnt;
	}

	/**
	 * @param review_cnt the review_cnt to set
	 */
	public void setReview_cnt(int review_cnt) {
		this.review_cnt = review_cnt;
	}

	/**
	 * @return the review_grade_avg
	 */
	public float getReview_grade_avg() {
		return review_grade_avg;
	}

	/**
	 * @param review_grade_avg the review_grade_avg to set
	 */
	public void setReview_grade_avg(float review_grade_avg) {
		this.review_grade_avg = review_grade_avg;
	}

	/**
	 * @return the review_json
	 */
	public String getReview_json() {
		return review_json;
	}

	/**
	 * @param review_json the review_json to set
	 */
	public void setReview_json(String review_json) {
		this.review_json = review_json;
	}

	/**
	 * @return the review_grade_avg_json
	 */
	public String getReview_grade_avg_json() {
		return review_grade_avg_json;
	}

	/**
	 * @param review_grade_avg_json the review_grade_avg_json to set
	 */
	public void setReview_grade_avg_json(String review_grade_avg_json) {
		this.review_grade_avg_json = review_grade_avg_json;
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
