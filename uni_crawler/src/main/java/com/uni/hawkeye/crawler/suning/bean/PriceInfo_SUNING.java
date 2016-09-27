package com.uni.hawkeye.crawler.suning.bean;

import java.math.BigDecimal;
import java.util.Date;

public class PriceInfo_SUNING {
	
	private String price_product_id_fk;

	private int product_list_id;
	
	private int price_type;
	
	private BigDecimal price;

	private Date ctime;
	
	private Date mtime;

	public PriceInfo_SUNING(){}

	public PriceInfo_SUNING(String price_product_id_fk, int product_list_id, int price_type, BigDecimal price) {
		super();
		this.price_product_id_fk = price_product_id_fk;
		this.product_list_id = product_list_id;
		this.price_type = price_type;
		this.price = price;
	}

	/**
	 * @return the price_product_id_fk
	 */
	public String getPrice_product_id_fk() {
		return price_product_id_fk;
	}

	/**
	 * @param price_product_id_fk the price_product_id_fk to set
	 */
	public void setPrice_product_id_fk(String price_product_id_fk) {
		this.price_product_id_fk = price_product_id_fk;
	}

	/**
	 * @return the product_list_id
	 */
	public int getProduct_list_id() {
		return product_list_id;
	}

	/**
	 * @param product_list_id the product_list_id to set
	 */
	public void setProduct_list_id(int product_list_id) {
		this.product_list_id = product_list_id;
	}

	/**
	 * @return the price_type
	 */
	public int getPrice_type() {
		return price_type;
	}

	/**
	 * @param price_type the price_type to set
	 */
	public void setPrice_type(int price_type) {
		this.price_type = price_type;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
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
