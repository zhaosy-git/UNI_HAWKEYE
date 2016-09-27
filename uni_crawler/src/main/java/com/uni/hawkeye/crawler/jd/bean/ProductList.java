package com.uni.hawkeye.crawler.jd.bean;

import java.util.Date;

public class ProductList {
	
	private Integer product_list_id;

	private String category_id;

	private Integer execute_id;

	private Integer pageNo;

	private String url;

	private String status;

	private Date ctime;

	private Date mtime;

	public ProductList(){}
	
	public ProductList(String category_id, Integer execute_id, Integer pageNo, String url, String status) {
		super();
		this.category_id = category_id;
		this.execute_id = execute_id;
		this.pageNo = pageNo;
		this.url = url;
		this.status = status;
	}

	public Integer getProduct_list_id() {
		return product_list_id;
	}

	public void setProduct_list_id(Integer product_list_id) {
		this.product_list_id = product_list_id;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public Integer getExecute_id() {
		return execute_id;
	}

	public void setExecute_id(Integer execute_id) {
		this.execute_id = execute_id;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
