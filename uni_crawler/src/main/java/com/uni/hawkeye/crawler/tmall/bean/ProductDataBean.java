package com.uni.hawkeye.crawler.tmall.bean;

import java.util.List;

public class ProductDataBean<T> {

	private List<ProductInfo> productInfoList;
	
	private List<T> reviewInfoList;
	
	private List<T> reviewImpressionList;
	
	private String productCountText;

	private boolean normalFlg;

	/**
	 * @return the productInfoList
	 */
	public List<ProductInfo> getProductInfoList() {
		return productInfoList;
	}

	/**
	 * @param productInfoList the productInfoList to set
	 */
	public void setProductInfoList(List<ProductInfo> productInfoList) {
		this.productInfoList = productInfoList;
	}

	/**
	 * @return the reviewInfoList
	 */
	public List<T> getReviewInfoList() {
		return reviewInfoList;
	}

	/**
	 * @param reviewInfoList the reviewInfoList to set
	 */
	public void setReviewInfoList(List<T> reviewInfoList) {
		this.reviewInfoList = reviewInfoList;
	}

	/**
	 * @return the reviewImpressionList
	 */
	public List<T> getReviewImpressionList() {
		return reviewImpressionList;
	}

	/**
	 * @param reviewImpressionList the reviewImpressionList to set
	 */
	public void setReviewImpressionList(List<T> reviewImpressionList) {
		this.reviewImpressionList = reviewImpressionList;
	}

	/**
	 * @return the productCountText
	 */
	public String getProductCountText() {
		return productCountText;
	}

	/**
	 * @param productCountText the productCountText to set
	 */
	public void setProductCountText(String productCountText) {
		this.productCountText = productCountText;
	}

	/**
	 * @return the normalFlg
	 */
	public boolean isNormalFlg() {
		return normalFlg;
	}

	/**
	 * @param normalFlg the normalFlg to set
	 */
	public void setNormalFlg(boolean normalFlg) {
		this.normalFlg = normalFlg;
	}

}
