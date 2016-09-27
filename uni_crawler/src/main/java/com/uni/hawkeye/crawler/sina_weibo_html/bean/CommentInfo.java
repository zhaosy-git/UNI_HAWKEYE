package com.uni.hawkeye.crawler.sina_weibo_html.bean;

import java.util.Date;

public class CommentInfo {
	
	private String comment_id;
	private int weibo_block_id_fk;
	private String comment_text;
	private int comment_favour_cnt;
	private String comment_user_id_fk;
	private String comment_post_time;
	private String comment_at_username;
	private String comment_topic;
	private String comment_is_original;
	private Date ctime;
	private Date mtime;
	
	public CommentInfo(){}
	
	public CommentInfo(String comment_id, int weibo_block_id_fk, String comment_text, int comment_favour_cnt, String comment_user_id_fk, String comment_post_time,
			String comment_at_username, String comment_topic, String comment_is_original) {
		super();
		this.comment_id = comment_id;
		this.weibo_block_id_fk = weibo_block_id_fk;
		this.comment_text = comment_text;
		this.comment_favour_cnt = comment_favour_cnt;
		this.comment_user_id_fk = comment_user_id_fk;
		this.comment_post_time = comment_post_time;
		this.comment_at_username = comment_at_username;
		this.comment_topic = comment_topic;
		this.comment_is_original = comment_is_original;
	}

	/**
	 * @return the comment_id
	 */
	public String getComment_id() {
		return comment_id;
	}

	/**
	 * @param comment_id the comment_id to set
	 */
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	/**
	 * @return the weibo_block_id_fk
	 */
	public int getWeibo_block_id_fk() {
		return weibo_block_id_fk;
	}

	/**
	 * @param weibo_block_id_fk the weibo_block_id_fk to set
	 */
	public void setWeibo_block_id_fk(int weibo_block_id_fk) {
		this.weibo_block_id_fk = weibo_block_id_fk;
	}

	/**
	 * @return the comment_text
	 */
	public String getComment_text() {
		return comment_text;
	}

	/**
	 * @param comment_text the comment_text to set
	 */
	public void setComment_text(String comment_text) {
		this.comment_text = comment_text;
	}

	/**
	 * @return the comment_favour_cnt
	 */
	public int getComment_favour_cnt() {
		return comment_favour_cnt;
	}

	/**
	 * @param comment_favour_cnt the comment_favour_cnt to set
	 */
	public void setComment_favour_cnt(int comment_favour_cnt) {
		this.comment_favour_cnt = comment_favour_cnt;
	}

	/**
	 * @return the comment_user_id_fk
	 */
	public String getComment_user_id_fk() {
		return comment_user_id_fk;
	}

	/**
	 * @param comment_user_id_fk the comment_user_id_fk to set
	 */
	public void setComment_user_id_fk(String comment_user_id_fk) {
		this.comment_user_id_fk = comment_user_id_fk;
	}

	/**
	 * @return the comment_post_time
	 */
	public String getComment_post_time() {
		return comment_post_time;
	}

	/**
	 * @param comment_post_time the comment_post_time to set
	 */
	public void setComment_post_time(String comment_post_time) {
		this.comment_post_time = comment_post_time;
	}

	/**
	 * @return the comment_at_username
	 */
	public String getComment_at_username() {
		return comment_at_username;
	}

	/**
	 * @param comment_at_username the comment_at_username to set
	 */
	public void setComment_at_username(String comment_at_username) {
		this.comment_at_username = comment_at_username;
	}

	/**
	 * @return the comment_topic
	 */
	public String getComment_topic() {
		return comment_topic;
	}

	/**
	 * @param comment_topic the comment_topic to set
	 */
	public void setComment_topic(String comment_topic) {
		this.comment_topic = comment_topic;
	}

	/**
	 * @return the comment_is_original
	 */
	public String getComment_is_original() {
		return comment_is_original;
	}

	/**
	 * @param comment_is_original the comment_is_original to set
	 */
	public void setComment_is_original(String comment_is_original) {
		this.comment_is_original = comment_is_original;
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
