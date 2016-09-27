package com.uni.hawkeye.crawler.baidu_tieba.bean;

import java.util.Date;

public class TieziInfo {
	
	private String tiezi_id;
	private String tiezi_user_id_fk;
	private String tiezi_title;
	private String tiezi_text;
	private int tiezi_floor;
	private String tiezi_post_time;
	private int tiezi_reply_cnt;
	private String tiezi_is_master;
	private String tiezi_root_in;
	private String tiezi_parent_id;
	private int tieba_id_fk;
	private Date ctime;
	private Date mtime;
	
	public TieziInfo(){}
	
	public TieziInfo(String tiezi_id, String tiezi_user_id_fk, String tiezi_title, String tiezi_text, int tiezi_floor, String tiezi_post_time, int tiezi_reply_cnt,
			String tiezi_is_master, String tiezi_root_in, String tiezi_parent_id, int tieba_id_fk) {
		super();
		this.tiezi_id = tiezi_id;
		this.tiezi_user_id_fk = tiezi_user_id_fk;
		this.tiezi_title = tiezi_title;
		this.tiezi_text = tiezi_text;
		this.tiezi_floor = tiezi_floor;
		this.tiezi_post_time = tiezi_post_time;
		this.tiezi_reply_cnt = tiezi_reply_cnt;
		this.tiezi_is_master = tiezi_is_master;
		this.tiezi_root_in = tiezi_root_in;
		this.tiezi_parent_id = tiezi_parent_id;
		this.tieba_id_fk = tieba_id_fk;
	}

	/**
	 * @return the tiezi_id
	 */
	public String getTiezi_id() {
		return tiezi_id;
	}

	/**
	 * @param tiezi_id the tiezi_id to set
	 */
	public void setTiezi_id(String tiezi_id) {
		this.tiezi_id = tiezi_id;
	}

	/**
	 * @return the tiezi_user_id_fk
	 */
	public String getTiezi_user_id_fk() {
		return tiezi_user_id_fk;
	}

	/**
	 * @param tiezi_user_id_fk the tiezi_user_id_fk to set
	 */
	public void setTiezi_user_id_fk(String tiezi_user_id_fk) {
		this.tiezi_user_id_fk = tiezi_user_id_fk;
	}

	/**
	 * @return the tiezi_title
	 */
	public String getTiezi_title() {
		return tiezi_title;
	}

	/**
	 * @param tiezi_title the tiezi_title to set
	 */
	public void setTiezi_title(String tiezi_title) {
		this.tiezi_title = tiezi_title;
	}

	/**
	 * @return the tiezi_text
	 */
	public String getTiezi_text() {
		return tiezi_text;
	}

	/**
	 * @param tiezi_text the tiezi_text to set
	 */
	public void setTiezi_text(String tiezi_text) {
		this.tiezi_text = tiezi_text;
	}

	/**
	 * @return the tiezi_floor
	 */
	public int getTiezi_floor() {
		return tiezi_floor;
	}

	/**
	 * @param tiezi_floor the tiezi_floor to set
	 */
	public void setTiezi_floor(int tiezi_floor) {
		this.tiezi_floor = tiezi_floor;
	}

	/**
	 * @return the tiezi_post_time
	 */
	public String getTiezi_post_time() {
		return tiezi_post_time;
	}

	/**
	 * @param tiezi_post_time the tiezi_post_time to set
	 */
	public void setTiezi_post_time(String tiezi_post_time) {
		this.tiezi_post_time = tiezi_post_time;
	}

	/**
	 * @return the tiezi_reply_cnt
	 */
	public int getTiezi_reply_cnt() {
		return tiezi_reply_cnt;
	}

	/**
	 * @param tiezi_reply_cnt the tiezi_reply_cnt to set
	 */
	public void setTiezi_reply_cnt(int tiezi_reply_cnt) {
		this.tiezi_reply_cnt = tiezi_reply_cnt;
	}

	/**
	 * @return the tiezi_is_master
	 */
	public String getTiezi_is_master() {
		return tiezi_is_master;
	}

	/**
	 * @param tiezi_is_master the tiezi_is_master to set
	 */
	public void setTiezi_is_master(String tiezi_is_master) {
		this.tiezi_is_master = tiezi_is_master;
	}

	/**
	 * @return the tiezi_root_in
	 */
	public String getTiezi_root_in() {
		return tiezi_root_in;
	}

	/**
	 * @param tiezi_root_in the tiezi_root_in to set
	 */
	public void setTiezi_root_in(String tiezi_root_in) {
		this.tiezi_root_in = tiezi_root_in;
	}

	/**
	 * @return the tiezi_parent_id
	 */
	public String getTiezi_parent_id() {
		return tiezi_parent_id;
	}

	/**
	 * @param tiezi_parent_id the tiezi_parent_id to set
	 */
	public void setTiezi_parent_id(String tiezi_parent_id) {
		this.tiezi_parent_id = tiezi_parent_id;
	}

	/**
	 * @return the tieba_id_fk
	 */
	public int getTieba_id_fk() {
		return tieba_id_fk;
	}

	/**
	 * @param tieba_id_fk the tieba_id_fk to set
	 */
	public void setTieba_id_fk(int tieba_id_fk) {
		this.tieba_id_fk = tieba_id_fk;
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
