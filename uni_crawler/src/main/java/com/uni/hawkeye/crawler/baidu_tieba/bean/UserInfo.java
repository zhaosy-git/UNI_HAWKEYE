package com.uni.hawkeye.crawler.baidu_tieba.bean;

import java.util.Date;

public class UserInfo {
	
	private String user_id;
	private String user_nick;
	private float bar_age;
	private int tiezi_cnt;
	private Date ctime;
	private Date mtime;
	
	public UserInfo(){}
	
	public UserInfo(String user_id, String user_nick, float bar_age, int tiezi_cnt) {
		super();
		this.user_id = user_id;
		this.user_nick = user_nick;
		this.bar_age = bar_age;
		this.tiezi_cnt = tiezi_cnt;
	}

	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}


	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	/**
	 * @return the user_nick
	 */
	public String getUser_nick() {
		return user_nick;
	}

	/**
	 * @param user_nick the user_nick to set
	 */
	public void setUser_nick(String user_nick) {
		this.user_nick = user_nick;
	}

	/**
	 * @return the bar_age
	 */
	public float getBar_age() {
		return bar_age;
	}

	/**
	 * @param bar_age the bar_age to set
	 */
	public void setBar_age(float bar_age) {
		this.bar_age = bar_age;
	}

	/**
	 * @return the tiezi_cnt
	 */
	public int getTiezi_cnt() {
		return tiezi_cnt;
	}

	/**
	 * @param tiezi_cnt the tiezi_cnt to set
	 */
	public void setTiezi_cnt(int tiezi_cnt) {
		this.tiezi_cnt = tiezi_cnt;
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
