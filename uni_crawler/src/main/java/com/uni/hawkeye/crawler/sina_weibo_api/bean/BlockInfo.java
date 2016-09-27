package com.uni.hawkeye.crawler.sina_weibo_api.bean;

import java.util.Date;

public class BlockInfo {
	
	private int block_id;
	private int weibo_category_id_fk;
	private String block_title;
	private String block_text;
	private String block_post_time;
	private String block_weibo_url;
	private String block_root_in;
	private String block_user_id_fk;
	private int block_repost_cnt;
	private int block_comment_cnt;
	private int block_favour_cnt;
	private String parent_block_weibo_url;
	private String block_at_username;
	private String block_topic;
	private String block_is_original;
	private Date ctime;
	private Date mtime;

	public BlockInfo(){}

	public BlockInfo(int weibo_category_id_fk, String block_title, String block_text, String block_post_time, String block_weibo_url, String block_root_in, String block_user_id_fk,
			int block_repost_cnt, int block_comment_cnt, int block_favour_cnt, String parent_block_weibo_url, String block_at_username, String block_topic,
			String block_is_original) {
		super();
		this.weibo_category_id_fk = weibo_category_id_fk;
		this.block_title = block_title;
		this.block_text = block_text;
		this.block_post_time = block_post_time;
		this.block_weibo_url = block_weibo_url;
		this.block_root_in = block_root_in;
		this.block_user_id_fk = block_user_id_fk;
		this.block_repost_cnt = block_repost_cnt;
		this.block_comment_cnt = block_comment_cnt;
		this.block_favour_cnt = block_favour_cnt;
		this.parent_block_weibo_url = parent_block_weibo_url;
		this.block_at_username = block_at_username;
		this.block_topic = block_topic;
		this.block_is_original = block_is_original;
	}

	/**
	 * @return the block_id
	 */
	public int getBlock_id() {
		return block_id;
	}

	/**
	 * @param block_id the block_id to set
	 */
	public void setBlock_id(int block_id) {
		this.block_id = block_id;
	}

	/**
	 * @return the weibo_category_id_fk
	 */
	public int getWeibo_category_id_fk() {
		return weibo_category_id_fk;
	}

	/**
	 * @param weibo_category_id_fk the weibo_category_id_fk to set
	 */
	public void setWeibo_category_id_fk(int weibo_category_id_fk) {
		this.weibo_category_id_fk = weibo_category_id_fk;
	}

	/**
	 * @return the block_title
	 */
	public String getBlock_title() {
		return block_title;
	}

	/**
	 * @param block_title the block_title to set
	 */
	public void setBlock_title(String block_title) {
		this.block_title = block_title;
	}

	/**
	 * @return the block_text
	 */
	public String getBlock_text() {
		return block_text;
	}

	/**
	 * @param block_text the block_text to set
	 */
	public void setBlock_text(String block_text) {
		this.block_text = block_text;
	}

	/**
	 * @return the block_post_time
	 */
	public String getBlock_post_time() {
		return block_post_time;
	}

	/**
	 * @param block_post_time the block_post_time to set
	 */
	public void setBlock_post_time(String block_post_time) {
		this.block_post_time = block_post_time;
	}

	/**
	 * @return the block_weibo_url
	 */
	public String getBlock_weibo_url() {
		return block_weibo_url;
	}

	/**
	 * @param block_weibo_url the block_weibo_url to set
	 */
	public void setBlock_weibo_url(String block_weibo_url) {
		this.block_weibo_url = block_weibo_url;
	}

	/**
	 * @return the block_root_in
	 */
	public String getBlock_root_in() {
		return block_root_in;
	}

	/**
	 * @param block_root_in the block_root_in to set
	 */
	public void setBlock_root_in(String block_root_in) {
		this.block_root_in = block_root_in;
	}

	/**
	 * @return the block_user_id_fk
	 */
	public String getBlock_user_id_fk() {
		return block_user_id_fk;
	}

	/**
	 * @param block_user_id_fk the block_user_id_fk to set
	 */
	public void setBlock_user_id_fk(String block_user_id_fk) {
		this.block_user_id_fk = block_user_id_fk;
	}

	/**
	 * @return the block_repost_cnt
	 */
	public int getBlock_repost_cnt() {
		return block_repost_cnt;
	}

	/**
	 * @param block_repost_cnt the block_repost_cnt to set
	 */
	public void setBlock_repost_cnt(int block_repost_cnt) {
		this.block_repost_cnt = block_repost_cnt;
	}

	/**
	 * @return the block_comment_cnt
	 */
	public int getBlock_comment_cnt() {
		return block_comment_cnt;
	}

	/**
	 * @param block_comment_cnt the block_comment_cnt to set
	 */
	public void setBlock_comment_cnt(int block_comment_cnt) {
		this.block_comment_cnt = block_comment_cnt;
	}

	/**
	 * @return the block_favour_cnt
	 */
	public int getBlock_favour_cnt() {
		return block_favour_cnt;
	}

	/**
	 * @param block_favour_cnt the block_favour_cnt to set
	 */
	public void setBlock_favour_cnt(int block_favour_cnt) {
		this.block_favour_cnt = block_favour_cnt;
	}

	/**
	 * @return the parent_block_weibo_url
	 */
	public String getParent_block_weibo_url() {
		return parent_block_weibo_url;
	}

	/**
	 * @param parent_block_weibo_url the parent_block_weibo_url to set
	 */
	public void setParent_block_weibo_url(String parent_block_weibo_url) {
		this.parent_block_weibo_url = parent_block_weibo_url;
	}

	/**
	 * @return the block_at_username
	 */
	public String getBlock_at_username() {
		return block_at_username;
	}

	/**
	 * @param block_at_username the block_at_username to set
	 */
	public void setBlock_at_username(String block_at_username) {
		this.block_at_username = block_at_username;
	}

	/**
	 * @return the block_topic
	 */
	public String getBlock_topic() {
		return block_topic;
	}

	/**
	 * @param block_topic the block_topic to set
	 */
	public void setBlock_topic(String block_topic) {
		this.block_topic = block_topic;
	}

	/**
	 * @return the block_is_original
	 */
	public String getBlock_is_original() {
		return block_is_original;
	}

	/**
	 * @param block_is_original the block_is_original to set
	 */
	public void setBlock_is_original(String block_is_original) {
		this.block_is_original = block_is_original;
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
