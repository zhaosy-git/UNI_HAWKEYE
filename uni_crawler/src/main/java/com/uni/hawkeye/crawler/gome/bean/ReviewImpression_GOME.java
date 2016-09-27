package com.uni.hawkeye.crawler.gome.bean;

import java.util.Date;

public class ReviewImpression_GOME {

	private String impression_id;
	
	private String product_id_fk;
	
	private String impression_content;
	
	private String json;
	
	private int execute_id;
	
	private Date ctime;
	
	private Date mtime;

	public ReviewImpression_GOME(){}

	public ReviewImpression_GOME(String impression_id, String product_id_fk, String impression_content, String json, int execute_id) {
		super();
		this.impression_id = impression_id;
		this.product_id_fk = product_id_fk;
		this.impression_content = impression_content;
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
