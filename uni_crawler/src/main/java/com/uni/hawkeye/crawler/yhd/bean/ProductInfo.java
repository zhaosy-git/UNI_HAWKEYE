package com.uni.hawkeye.crawler.yhd.bean;

import java.math.BigDecimal;
import java.util.Date;

public class ProductInfo {

	private String product_id;
	private String product_title;
	private BigDecimal price;
	private int sale_count;
	private String brand_name;
	private int status;
	private int product_list_id;
	private String url;
	private String json;
	private Date ctime;
	private Date mtime;

	public ProductInfo(){}
	
	public ProductInfo(String product_id, String product_title, BigDecimal price, int sale_count, String brand_name, int status, int product_list_id, String url, String json) {
		super();
		this.product_id = product_id;
		this.product_title = product_title;
		this.price = price;
		this.sale_count = sale_count;
		this.brand_name = brand_name;
		this.status = status;
		this.product_list_id = product_list_id;
		this.url = url;
		this.json = json;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_title() {
		return product_title;
	}

	public void setProduct_title(String product_title) {
		this.product_title = product_title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the sale_count
	 */
	public int getSale_count() {
		return sale_count;
	}

	/**
	 * @param sale_count the sale_count to set
	 */
	public void setSale_count(int sale_count) {
		this.sale_count = sale_count;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getProduct_list_id() {
		return product_list_id;
	}

	public void setProduct_list_id(int product_list_id) {
		this.product_list_id = product_list_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
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
