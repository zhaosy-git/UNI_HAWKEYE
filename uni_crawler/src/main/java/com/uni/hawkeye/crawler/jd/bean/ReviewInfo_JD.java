package com.uni.hawkeye.crawler.jd.bean;

import java.util.Date;

public class ReviewInfo_JD {

	private int review_id;

	private String product_id_fk;

	private int good_cnt;

	private int general_cnt;

	private int poor_cnt;

	private float review_grade_avg;

	private String review_json;
	
	private int execute_id;

	private Date ctime;

	private Date mtime;

	public ReviewInfo_JD() {
	}

	public ReviewInfo_JD(String product_id_fk, int good_cnt, int general_cnt, int poor_cnt, float review_grade_avg, String review_json, int execute_id) {
		super();
		this.product_id_fk = product_id_fk;
		this.good_cnt = good_cnt;
		this.general_cnt = general_cnt;
		this.poor_cnt = poor_cnt;
		this.review_grade_avg = review_grade_avg;
		this.review_json = review_json;
		this.execute_id = execute_id;
	}

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
	 * @return the good_cnt
	 */
	public int getGood_cnt() {
		return good_cnt;
	}

	/**
	 * @param good_cnt the good_cnt to set
	 */
	public void setGood_cnt(int good_cnt) {
		this.good_cnt = good_cnt;
	}

	/**
	 * @return the general_cnt
	 */
	public int getGeneral_cnt() {
		return general_cnt;
	}

	/**
	 * @param general_cnt the general_cnt to set
	 */
	public void setGeneral_cnt(int general_cnt) {
		this.general_cnt = general_cnt;
	}

	/**
	 * @return the poor_cnt
	 */
	public int getPoor_cnt() {
		return poor_cnt;
	}

	/**
	 * @param poor_cnt the poor_cnt to set
	 */
	public void setPoor_cnt(int poor_cnt) {
		this.poor_cnt = poor_cnt;
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
