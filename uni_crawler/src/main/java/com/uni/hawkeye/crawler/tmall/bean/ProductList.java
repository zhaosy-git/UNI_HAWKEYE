package com.uni.hawkeye.crawler.tmall.bean;

import java.util.Date;

public class ProductList {
	
	private Integer product_list_id;

	private Integer category_id;

	private Integer execute_id;

	private Integer pageNo;

	private String url;

	private int status;

	private Date ctime;

	private Date mtime;

	public ProductList() {}

	public ProductList(Integer category_id, Integer execute_id, Integer pageNo, String url, int status) {
		super();
		this.category_id = category_id;
		this.execute_id = execute_id;
		this.pageNo = pageNo;
		this.url = url;
		this.status = status;
	}

	/**
	 * @return the product_list_id
	 */
	public Integer getProduct_list_id() {
		return product_list_id;
	}

	/**
	 * @param product_list_id the product_list_id to set
	 */
	public void setProduct_list_id(Integer product_list_id) {
		this.product_list_id = product_list_id;
	}

	/**
	 * @return the category_id
	 */
	public Integer getCategory_id() {
		return category_id;
	}

	/**
	 * @param category_id the category_id to set
	 */
	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	/**
	 * @return the execute_id
	 */
	public Integer getExecute_id() {
		return execute_id;
	}

	/**
	 * @param execute_id the execute_id to set
	 */
	public void setExecute_id(Integer execute_id) {
		this.execute_id = execute_id;
	}

	/**
	 * @return the pageNo
	 */
	public Integer getPageNo() {
		return pageNo;
	}

	/**
	 * @param pageNo the pageNo to set
	 */
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
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
