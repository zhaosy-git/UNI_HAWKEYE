package com.uni.hawkeye.crawler.sina_weibo_api.bean;

import java.util.Date;

public class CategoryInfo {

	private int category_id;
	private String category_code;
	private String category_name;
	private String category_url;
	private String status;
	private int execute_id_fk;
	private Date ctime;
	private Date mtime;

	public CategoryInfo(){}

	public CategoryInfo(String category_code, String category_name, String category_url, String status, int execute_id_fk) {
		super();
		this.category_code = category_code;
		this.category_name = category_name;
		this.category_url = category_url;
		this.status = status;
		this.execute_id_fk = execute_id_fk;
	}

	/**
	 * @return the category_id
	 */
	public int getCategory_id() {
		return category_id;
	}


	/**
	 * @param category_id the category_id to set
	 */
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}


	/**
	 * @return the category_code
	 */
	public String getCategory_code() {
		return category_code;
	}


	/**
	 * @param category_code the category_code to set
	 */
	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}


	/**
	 * @return the category_name
	 */
	public String getCategory_name() {
		return category_name;
	}


	/**
	 * @param category_name the category_name to set
	 */
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}


	/**
	 * @return the category_url
	 */
	public String getCategory_url() {
		return category_url;
	}


	/**
	 * @param category_url the category_url to set
	 */
	public void setCategory_url(String category_url) {
		this.category_url = category_url;
	}


	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * @return the execute_id_fk
	 */
	public int getExecute_id_fk() {
		return execute_id_fk;
	}


	/**
	 * @param execute_id_fk the execute_id_fk to set
	 */
	public void setExecute_id_fk(int execute_id_fk) {
		this.execute_id_fk = execute_id_fk;
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
