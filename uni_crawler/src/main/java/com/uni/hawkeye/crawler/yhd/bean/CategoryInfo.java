package com.uni.hawkeye.crawler.yhd.bean;

import java.util.Date;

public class CategoryInfo {
	
	private int category_id;

	private String category_code;

	private String category_name;

	private int execute_id;
	
	private String category_url;
	
	private String status;

	private String category_level;

	private String category_parent_id;

	private Date ctime;

	private Date mtime;
	
	public CategoryInfo(){}

	public CategoryInfo(String category_code, String category_name, int execute_id, String category_url, String status, String category_level, String category_parent_id) {
		super();
		this.category_code = category_code;
		this.category_name = category_name;
		this.execute_id = execute_id;
		this.category_url = category_url;
		this.status = status;
		this.category_level = category_level;
		this.category_parent_id = category_parent_id;
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
	 * @return the category_level
	 */
	public String getCategory_level() {
		return category_level;
	}

	/**
	 * @param category_level the category_level to set
	 */
	public void setCategory_level(String category_level) {
		this.category_level = category_level;
	}

	/**
	 * @return the category_parent_id
	 */
	public String getCategory_parent_id() {
		return category_parent_id;
	}

	/**
	 * @param category_parent_id the category_parent_id to set
	 */
	public void setCategory_parent_id(String category_parent_id) {
		this.category_parent_id = category_parent_id;
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
