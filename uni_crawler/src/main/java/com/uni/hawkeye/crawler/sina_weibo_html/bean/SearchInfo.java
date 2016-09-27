package com.uni.hawkeye.crawler.sina_weibo_html.bean;

import java.util.Date;

public class SearchInfo {

	private int search_id;
	private String content_key_word;
	private String crawler_weibo_begin_time;
	private String crawler_weibo_end_time;
	private String search_url;
	private String status;
	private int execute_id_fk;
	private Date ctime;
	private Date mtime;

	public SearchInfo() {
	}

	public SearchInfo(String content_key_word, String crawler_weibo_begin_time, String crawler_weibo_end_time, String search_url, String status, int execute_id_fk) {
		super();
		this.content_key_word = content_key_word;
		this.crawler_weibo_begin_time = crawler_weibo_begin_time;
		this.crawler_weibo_end_time = crawler_weibo_end_time;
		this.search_url = search_url;
		this.status = status;
		this.execute_id_fk = execute_id_fk;
	}

	/**
	 * @return the search_id
	 */
	public int getSearch_id() {
		return search_id;
	}

	/**
	 * @param search_id
	 *            the search_id to set
	 */
	public void setSearch_id(int search_id) {
		this.search_id = search_id;
	}

	/**
	 * @return the content_key_word
	 */
	public String getContent_key_word() {
		return content_key_word;
	}

	/**
	 * @param content_key_word
	 *            the content_key_word to set
	 */
	public void setContent_key_word(String content_key_word) {
		this.content_key_word = content_key_word;
	}

	/**
	 * @return the crawler_weibo_begin_time
	 */
	public String getCrawler_weibo_begin_time() {
		return crawler_weibo_begin_time;
	}

	/**
	 * @param crawler_weibo_begin_time
	 *            the crawler_weibo_begin_time to set
	 */
	public void setCrawler_weibo_begin_time(String crawler_weibo_begin_time) {
		this.crawler_weibo_begin_time = crawler_weibo_begin_time;
	}

	/**
	 * @return the crawler_weibo_end_time
	 */
	public String getCrawler_weibo_end_time() {
		return crawler_weibo_end_time;
	}

	/**
	 * @param crawler_weibo_end_time
	 *            the crawler_weibo_end_time to set
	 */
	public void setCrawler_weibo_end_time(String crawler_weibo_end_time) {
		this.crawler_weibo_end_time = crawler_weibo_end_time;
	}

	/**
	 * @return the search_url
	 */
	public String getSearch_url() {
		return search_url;
	}

	/**
	 * @param search_url
	 *            the search_url to set
	 */
	public void setSearch_url(String search_url) {
		this.search_url = search_url;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
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
	 * @param execute_id_fk
	 *            the execute_id_fk to set
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
	 * @param ctime
	 *            the ctime to set
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
	 * @param mtime
	 *            the mtime to set
	 */
	public void setMtime(Date mtime) {
		this.mtime = mtime;
	}

}
