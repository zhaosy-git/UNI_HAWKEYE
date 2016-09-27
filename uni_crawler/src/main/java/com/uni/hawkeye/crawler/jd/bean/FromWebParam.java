package com.uni.hawkeye.crawler.jd.bean;

public class FromWebParam {
	
	private int id;
	private String web_site;
	private String category_1;
	private String category_1_href;
	private String category_2;
	private String category_2_href;
    private String brand;
    private String brand_href;
    private String price_low;
    private String price_high;
    private String key_word;
    private String crawler_point;
    
    public FromWebParam(){}
    
	public FromWebParam(int id, String web_site, String category_1, String category_1_href, String category_2, String category_2_href, String brand, String brand_href,
			String price_low, String price_high, String key_word, String crawler_point) {
		super();
		this.id = id;
		this.web_site = web_site;
		this.category_1 = category_1;
		this.category_1_href = category_1_href;
		this.category_2 = category_2;
		this.category_2_href = category_2_href;
		this.brand = brand;
		this.brand_href = brand_href;
		this.price_low = price_low;
		this.price_high = price_high;
		this.key_word = key_word;
		this.crawler_point = crawler_point;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the web_site
	 */
	public String getWeb_site() {
		return web_site;
	}

	/**
	 * @param web_site the web_site to set
	 */
	public void setWeb_site(String web_site) {
		this.web_site = web_site;
	}

	/**
	 * @return the category_1
	 */
	public String getCategory_1() {
		return category_1;
	}

	/**
	 * @param category_1 the category_1 to set
	 */
	public void setCategory_1(String category_1) {
		this.category_1 = category_1;
	}

	/**
	 * @return the category_1_href
	 */
	public String getCategory_1_href() {
		return category_1_href;
	}

	/**
	 * @param category_1_href the category_1_href to set
	 */
	public void setCategory_1_href(String category_1_href) {
		this.category_1_href = category_1_href;
	}

	/**
	 * @return the category_2
	 */
	public String getCategory_2() {
		return category_2;
	}

	/**
	 * @param category_2 the category_2 to set
	 */
	public void setCategory_2(String category_2) {
		this.category_2 = category_2;
	}

	/**
	 * @return the category_2_href
	 */
	public String getCategory_2_href() {
		return category_2_href;
	}

	/**
	 * @param category_2_href the category_2_href to set
	 */
	public void setCategory_2_href(String category_2_href) {
		this.category_2_href = category_2_href;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @return the brand_href
	 */
	public String getBrand_href() {
		return brand_href;
	}

	/**
	 * @param brand_href the brand_href to set
	 */
	public void setBrand_href(String brand_href) {
		this.brand_href = brand_href;
	}

	/**
	 * @return the price_low
	 */
	public String getPrice_low() {
		return price_low;
	}

	/**
	 * @param price_low the price_low to set
	 */
	public void setPrice_low(String price_low) {
		this.price_low = price_low;
	}

	/**
	 * @return the price_high
	 */
	public String getPrice_high() {
		return price_high;
	}

	/**
	 * @param price_high the price_high to set
	 */
	public void setPrice_high(String price_high) {
		this.price_high = price_high;
	}

	/**
	 * @return the key_word
	 */
	public String getKey_word() {
		return key_word;
	}

	/**
	 * @param key_word the key_word to set
	 */
	public void setKey_word(String key_word) {
		this.key_word = key_word;
	}

	/**
	 * @return the crawler_point
	 */
	public String getCrawler_point() {
		return crawler_point;
	}

	/**
	 * @param crawler_point the crawler_point to set
	 */
	public void setCrawler_point(String crawler_point) {
		this.crawler_point = crawler_point;
	}
    
    
}
