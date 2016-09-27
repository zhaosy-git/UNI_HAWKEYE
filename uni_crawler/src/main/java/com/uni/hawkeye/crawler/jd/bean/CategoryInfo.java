package com.uni.hawkeye.crawler.jd.bean;

import java.util.Date;

public class CategoryInfo {

	private String category_id;

	private String category_name;

	private int execute_id;
	
	private String category_url;
	
	private String status;

	private String category_level;

	private String category_parent_id;

	private Date ctime;

	private Date mtime;
	
	public CategoryInfo(){}
	

	public CategoryInfo(String category_id, String category_name, int execute_id, String category_url, String status, String category_level, String category_parent_id) {
		super();
		this.category_id = category_id;
		this.category_name = category_name;
		this.execute_id = execute_id;
		this.category_url = category_url;
		this.status = status;
		this.category_level = category_level;
		this.category_parent_id = category_parent_id;
	}


	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public int getExecute_id() {
		return execute_id;
	}

	public void setExecute_id(int execute_id) {
		this.execute_id = execute_id;
	}

	public String getCategory_url() {
		return category_url;
	}

	public void setCategory_url(String category_url) {
		this.category_url = category_url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCategory_level() {
		return category_level;
	}

	public void setCategory_level(String category_level) {
		this.category_level = category_level;
	}

	public String getCategory_parent_id() {
		return category_parent_id;
	}

	public void setCategory_parent_id(String category_parent_id) {
		this.category_parent_id = category_parent_id;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getMtime() {
		return mtime;
	}

	public void setMtime(Date mtime) {
		this.mtime = mtime;
	}
	
}
