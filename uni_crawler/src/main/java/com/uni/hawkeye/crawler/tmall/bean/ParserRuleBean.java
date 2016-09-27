package com.uni.hawkeye.crawler.tmall.bean;

public class ParserRuleBean {

	private String protoc;

	private String pageflgSelector;
	private String productCountSelector;
	private String productSelectKey;
	private String idSelectorKey;
	private String titleSelectorKey;
	private String wayTypeSelectorKey;
	private String saleBeginTime;
	private String selfSale;
	private String brandNameDatail;
	private String brandNamePattern;
	private String detailReqUrl;
	private String reviewReqUrl;
	private String reviewGradeAVGUrl;
	private String productImpressionUrl;
	private String changeDetailURL;
	private String changeDetailURLResult;

	public ParserRuleBean() {
	}

	public ParserRuleBean(String protoc, String pageflgSelector, String productCountSelector, String productSelectKey,
			String idSelectorKey, String titleSelectorKey, String wayTypeSelectorKey, String saleBeginTime,
			String selfSale, String brandNameDatail, String brandNamePattern, String detailReqUrl, String reviewReqUrl,
			String reviewGradeAVGUrl, String productImpressionUrl, String changeDetailURL, String changeDetailURLResult) {
		super();
		this.protoc = protoc;
		this.pageflgSelector = pageflgSelector;
		this.productCountSelector = productCountSelector;
		this.productSelectKey = productSelectKey;
		this.idSelectorKey = idSelectorKey;
		this.titleSelectorKey = titleSelectorKey;
		this.wayTypeSelectorKey = wayTypeSelectorKey;
		this.saleBeginTime = saleBeginTime;
		this.selfSale = selfSale;
		this.brandNameDatail = brandNameDatail;
		this.brandNamePattern = brandNamePattern;
		this.detailReqUrl = detailReqUrl;
		this.reviewReqUrl = reviewReqUrl;
		this.reviewGradeAVGUrl = reviewGradeAVGUrl;
		this.productImpressionUrl = productImpressionUrl;
		this.changeDetailURL = changeDetailURL;
		this.changeDetailURLResult = changeDetailURLResult;
	}

	/**
	 * @return the protoc
	 */
	public String getProtoc() {
		return protoc;
	}

	/**
	 * @param protoc the protoc to set
	 */
	public void setProtoc(String protoc) {
		this.protoc = protoc;
	}

	/**
	 * @return the pageflgSelector
	 */
	public String getPageflgSelector() {
		return pageflgSelector;
	}

	/**
	 * @param pageflgSelector the pageflgSelector to set
	 */
	public void setPageflgSelector(String pageflgSelector) {
		this.pageflgSelector = pageflgSelector;
	}

	/**
	 * @return the productCountSelector
	 */
	public String getProductCountSelector() {
		return productCountSelector;
	}

	/**
	 * @param productCountSelector the productCountSelector to set
	 */
	public void setProductCountSelector(String productCountSelector) {
		this.productCountSelector = productCountSelector;
	}

	/**
	 * @return the productSelectKey
	 */
	public String getProductSelectKey() {
		return productSelectKey;
	}

	/**
	 * @param productSelectKey the productSelectKey to set
	 */
	public void setProductSelectKey(String productSelectKey) {
		this.productSelectKey = productSelectKey;
	}

	/**
	 * @return the idSelectorKey
	 */
	public String getIdSelectorKey() {
		return idSelectorKey;
	}

	/**
	 * @param idSelectorKey the idSelectorKey to set
	 */
	public void setIdSelectorKey(String idSelectorKey) {
		this.idSelectorKey = idSelectorKey;
	}

	/**
	 * @return the titleSelectorKey
	 */
	public String getTitleSelectorKey() {
		return titleSelectorKey;
	}

	/**
	 * @param titleSelectorKey the titleSelectorKey to set
	 */
	public void setTitleSelectorKey(String titleSelectorKey) {
		this.titleSelectorKey = titleSelectorKey;
	}

	/**
	 * @return the wayTypeSelectorKey
	 */
	public String getWayTypeSelectorKey() {
		return wayTypeSelectorKey;
	}

	/**
	 * @param wayTypeSelectorKey the wayTypeSelectorKey to set
	 */
	public void setWayTypeSelectorKey(String wayTypeSelectorKey) {
		this.wayTypeSelectorKey = wayTypeSelectorKey;
	}

	/**
	 * @return the saleBeginTime
	 */
	public String getSaleBeginTime() {
		return saleBeginTime;
	}

	/**
	 * @param saleBeginTime the saleBeginTime to set
	 */
	public void setSaleBeginTime(String saleBeginTime) {
		this.saleBeginTime = saleBeginTime;
	}

	/**
	 * @return the selfSale
	 */
	public String getSelfSale() {
		return selfSale;
	}

	/**
	 * @param selfSale the selfSale to set
	 */
	public void setSelfSale(String selfSale) {
		this.selfSale = selfSale;
	}

	/**
	 * @return the brandNameDatail
	 */
	public String getBrandNameDatail() {
		return brandNameDatail;
	}

	/**
	 * @param brandNameDatail the brandNameDatail to set
	 */
	public void setBrandNameDatail(String brandNameDatail) {
		this.brandNameDatail = brandNameDatail;
	}

	/**
	 * @return the brandNamePattern
	 */
	public String getBrandNamePattern() {
		return brandNamePattern;
	}

	/**
	 * @param brandNamePattern the brandNamePattern to set
	 */
	public void setBrandNamePattern(String brandNamePattern) {
		this.brandNamePattern = brandNamePattern;
	}

	/**
	 * @return the detailReqUrl
	 */
	public String getDetailReqUrl() {
		return detailReqUrl;
	}

	/**
	 * @param detailReqUrl the detailReqUrl to set
	 */
	public void setDetailReqUrl(String detailReqUrl) {
		this.detailReqUrl = detailReqUrl;
	}

	/**
	 * @return the reviewReqUrl
	 */
	public String getReviewReqUrl() {
		return reviewReqUrl;
	}

	/**
	 * @param reviewReqUrl the reviewReqUrl to set
	 */
	public void setReviewReqUrl(String reviewReqUrl) {
		this.reviewReqUrl = reviewReqUrl;
	}

	/**
	 * @return the reviewGradeAVGUrl
	 */
	public String getReviewGradeAVGUrl() {
		return reviewGradeAVGUrl;
	}

	/**
	 * @param reviewGradeAVGUrl the reviewGradeAVGUrl to set
	 */
	public void setReviewGradeAVGUrl(String reviewGradeAVGUrl) {
		this.reviewGradeAVGUrl = reviewGradeAVGUrl;
	}

	/**
	 * @return the productImpressionUrl
	 */
	public String getProductImpressionUrl() {
		return productImpressionUrl;
	}

	/**
	 * @param productImpressionUrl the productImpressionUrl to set
	 */
	public void setProductImpressionUrl(String productImpressionUrl) {
		this.productImpressionUrl = productImpressionUrl;
	}

	/**
	 * @return the changeDetailURL
	 */
	public String getChangeDetailURL() {
		return changeDetailURL;
	}

	/**
	 * @param changeDetailURL the changeDetailURL to set
	 */
	public void setChangeDetailURL(String changeDetailURL) {
		this.changeDetailURL = changeDetailURL;
	}

	/**
	 * @return the changeDetailURLResult
	 */
	public String getChangeDetailURLResult() {
		return changeDetailURLResult;
	}

	/**
	 * @param changeDetailURLResult the changeDetailURLResult to set
	 */
	public void setChangeDetailURLResult(String changeDetailURLResult) {
		this.changeDetailURLResult = changeDetailURLResult;
	}

}
