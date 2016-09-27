package com.uni.hawkeye.crawler.baidu_tieba.bean;

import java.util.Date;

public class BarInfo {
	
	private int tieba_id;
	private String tieba_title;
	private int tieba_user_cnt;
	private int tieba_tiezi_cnt;
	private String tieba_summary;
	private String tieba_url;
	private int category_id_fk;
	private String status;
	private Date ctime;
	private Date mtime;
	
	public BarInfo(){}
	
	public BarInfo(String tieba_title, int tieba_user_cnt, int tieba_tiezi_cnt, String tieba_summary, String tieba_url, int category_id_fk, String status) {
		super();
		this.tieba_title = tieba_title;
		this.tieba_user_cnt = tieba_user_cnt;
		this.tieba_tiezi_cnt = tieba_tiezi_cnt;
		this.tieba_summary = tieba_summary;
		this.tieba_url = tieba_url;
		this.category_id_fk = category_id_fk;
		this.status = status;
	}

	/**
	 * @return the tieba_id
	 */
	public int getTieba_id() {
		return tieba_id;
	}

	/**
	 * @param tieba_id the tieba_id to set
	 */
	public void setTieba_id(int tieba_id) {
		this.tieba_id = tieba_id;
	}

	/**
	 * @return the tieba_title
	 */
	public String getTieba_title() {
		return tieba_title;
	}

	/**
	 * @param tieba_title the tieba_title to set
	 */
	public void setTieba_title(String tieba_title) {
		this.tieba_title = tieba_title;
	}

	/**
	 * @return the tieba_user_cnt
	 */
	public int getTieba_user_cnt() {
		return tieba_user_cnt;
	}

	/**
	 * @param tieba_user_cnt the tieba_user_cnt to set
	 */
	public void setTieba_user_cnt(int tieba_user_cnt) {
		this.tieba_user_cnt = tieba_user_cnt;
	}

	/**
	 * @return the tieba_tiezi_cnt
	 */
	public int getTieba_tiezi_cnt() {
		return tieba_tiezi_cnt;
	}

	/**
	 * @param tieba_tiezi_cnt the tieba_tiezi_cnt to set
	 */
	public void setTieba_tiezi_cnt(int tieba_tiezi_cnt) {
		this.tieba_tiezi_cnt = tieba_tiezi_cnt;
	}

	/**
	 * @return the tieba_summary
	 */
	public String getTieba_summary() {
		return tieba_summary;
	}

	/**
	 * @param tieba_summary the tieba_summary to set
	 */
	public void setTieba_summary(String tieba_summary) {
		this.tieba_summary = tieba_summary;
	}

	/**
	 * @return the tieba_url
	 */
	public String getTieba_url() {
		return tieba_url;
	}

	/**
	 * @param tieba_url the tieba_url to set
	 */
	public void setTieba_url(String tieba_url) {
		this.tieba_url = tieba_url;
	}

	/**
	 * @return the category_id_fk
	 */
	public int getCategory_id_fk() {
		return category_id_fk;
	}

	/**
	 * @param category_id_fk the category_id_fk to set
	 */
	public void setCategory_id_fk(int category_id_fk) {
		this.category_id_fk = category_id_fk;
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
