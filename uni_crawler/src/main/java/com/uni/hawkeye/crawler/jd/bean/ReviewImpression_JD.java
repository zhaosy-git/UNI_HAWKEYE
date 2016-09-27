package com.uni.hawkeye.crawler.jd.bean;

import java.util.Date;

public class ReviewImpression_JD {

	private String impression_id;
	
	private String product_id_fk;
	
	private String impression_content;
	
	private int impression_cnt;
	
	private String json;
	
	private int execute_id;
	
	private Date ctime;
	
	private Date mtime;

	public ReviewImpression_JD(){}

	public ReviewImpression_JD(String impression_id, String product_id_fk, String impression_content, int impression_cnt, String json, int execute_id) {
		super();
		this.impression_id = impression_id;
		this.product_id_fk = product_id_fk;
		this.impression_content = impression_content;
		this.impression_cnt = impression_cnt;
		this.json = json;
		this.execute_id = execute_id;
	}

	/**
	 * @return the impression_id
	 */
	public String getImpression_id() {
		return impression_id;
	}

	/**
	 * @param impression_id the impression_id to set
	 */
	public void setImpression_id(String impression_id) {
		this.impression_id = impression_id;
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
	 * @return the impression_cnt
	 */
	public int getImpression_cnt() {
		return impression_cnt;
	}

	/**
	 * @param impression_cnt the impression_cnt to set
	 */
	public void setImpression_cnt(int impression_cnt) {
		this.impression_cnt = impression_cnt;
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
