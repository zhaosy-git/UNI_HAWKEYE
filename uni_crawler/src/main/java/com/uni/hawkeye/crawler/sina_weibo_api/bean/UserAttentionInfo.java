package com.uni.hawkeye.crawler.sina_weibo_api.bean;

import java.util.Date;

public class UserAttentionInfo {
	
	private int attention_id;
	private String user_id;
	private String attention_user_id;
	private Date ctime;
	private Date mtime;

	public UserAttentionInfo() {}

	public UserAttentionInfo(String user_id, String attention_user_id) {
		super();
		this.user_id = user_id;
		this.attention_user_id = attention_user_id;
	}

	/**
	 * @return the attention_id
	 */
	public int getAttention_id() {
		return attention_id;
	}

	/**
	 * @param attention_id the attention_id to set
	 */
	public void setAttention_id(int attention_id) {
		this.attention_id = attention_id;
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
	 * @return the attention_user_id
	 */
	public String getAttention_user_id() {
		return attention_user_id;
	}

	/**
	 * @param attention_user_id the attention_user_id to set
	 */
	public void setAttention_user_id(String attention_user_id) {
		this.attention_user_id = attention_user_id;
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
