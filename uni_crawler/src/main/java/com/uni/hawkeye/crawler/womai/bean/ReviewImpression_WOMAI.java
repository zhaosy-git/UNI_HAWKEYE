package com.uni.hawkeye.crawler.womai.bean;

import java.util.Date;

public class ReviewImpression_WOMAI {

	private int impression_id;
	
	private int execute_id;
	
	private String product_id_fk;
	
	private String impression_content;
	
	private float impression_grade;
	
	private String json;
	
	private Date ctime;
	
	private Date mtime;

	public ReviewImpression_WOMAI(){}

	public ReviewImpression_WOMAI(int execute_id, String product_id_fk, String impression_content, float impression_grade, String json) {
		super();
		this.execute_id = execute_id;
		this.product_id_fk = product_id_fk;
		this.impression_content = impression_content;
		this.impression_grade = impression_grade;
		this.json = json;
	}

	/**
	 * @return the impression_id
	 */
	public int getImpression_id() {
		return impression_id;
	}

	/**
	 * @param impression_id the impression_id to set
	 */
	public void setImpression_id(int impression_id) {
		this.impression_id = impression_id;
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
	 * @return the impression_content
	 */
	public String getImpression_content() {
		return impression_content;
	}

	/**
	 * @param impression_content the impression_content to set
	 */
	public void setImpression_content(String impression_content) {
		this.impression_content = impression_content;
	}

	/**
	 * @return the impression_grade
	 */
	public float getImpression_grade() {
		return impression_grade;
	}

	/**
	 * @param impression_grade the impression_grade to set
	 */
	public void setImpression_grade(float impression_grade) {
		this.impression_grade = impression_grade;
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
