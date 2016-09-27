package com.uni.hawkeye.crawler.sina_weibo_api.bean;

import java.util.Date;

public class UserInfo {
	private String user_id;
	private String user_nick;
	private String user_area;
	private String user_sex;
	private String user_birthday;
	private String user_blog;
	private String user_blood_type;
	private String user_summery;
	private String user_tags;
	private int user_fans_cnt;
	private int user_weibo_cnt;
	private int user_focus_cnt;
	private Date ctime;
	private Date mtime;
	
	public UserInfo(){}
	
	public UserInfo(String user_id, String user_nick, String user_area, String user_sex, String user_birthday, String user_blog, String user_blood_type, String user_summery,
			String user_tags, int user_fans_cnt, int user_weibo_cnt, int user_focus_cnt) {
		super();
		this.user_id = user_id;
		this.user_nick = user_nick;
		this.user_area = user_area;
		this.user_sex = user_sex;
		this.user_birthday = user_birthday;
		this.user_blog = user_blog;
		this.user_blood_type = user_blood_type;
		this.user_summery = user_summery;
		this.user_tags = user_tags;
		this.user_fans_cnt = user_fans_cnt;
		this.user_weibo_cnt = user_weibo_cnt;
		this.user_focus_cnt = user_focus_cnt;
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
	 * @return the user_area
	 */
	public String getUser_area() {
		return user_area;
	}

	/**
	 * @param user_area the user_area to set
	 */
	public void setUser_area(String user_area) {
		this.user_area = user_area;
	}

	/**
	 * @return the user_sex
	 */
	public String getUser_sex() {
		return user_sex;
	}

	/**
	 * @param user_sex the user_sex to set
	 */
	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}

	/**
	 * @return the user_birthday
	 */
	public String getUser_birthday() {
		return user_birthday;
	}

	/**
	 * @param user_birthday the user_birthday to set
	 */
	public void setUser_birthday(String user_birthday) {
		this.user_birthday = user_birthday;
	}

	/**
	 * @return the user_blog
	 */
	public String getUser_blog() {
		return user_blog;
	}

	/**
	 * @param user_blog the user_blog to set
	 */
	public void setUser_blog(String user_blog) {
		this.user_blog = user_blog;
	}

	/**
	 * @return the user_blood_type
	 */
	public String getUser_blood_type() {
		return user_blood_type;
	}

	/**
	 * @param user_blood_type the user_blood_type to set
	 */
	public void setUser_blood_type(String user_blood_type) {
		this.user_blood_type = user_blood_type;
	}

	/**
	 * @return the user_summery
	 */
	public String getUser_summery() {
		return user_summery;
	}

	/**
	 * @param user_summery the user_summery to set
	 */
	public void setUser_summery(String user_summery) {
		this.user_summery = user_summery;
	}

	/**
	 * @return the user_tags
	 */
	public String getUser_tags() {
		return user_tags;
	}

	/**
	 * @param user_tags the user_tags to set
	 */
	public void setUser_tags(String user_tags) {
		this.user_tags = user_tags;
	}

	/**
	 * @return the user_fans_cnt
	 */
	public int getUser_fans_cnt() {
		return user_fans_cnt;
	}

	/**
	 * @param user_fans_cnt the user_fans_cnt to set
	 */
	public void setUser_fans_cnt(int user_fans_cnt) {
		this.user_fans_cnt = user_fans_cnt;
	}

	/**
	 * @return the user_weibo_cnt
	 */
	public int getUser_weibo_cnt() {
		return user_weibo_cnt;
	}

	/**
	 * @param user_weibo_cnt the user_weibo_cnt to set
	 */
	public void setUser_weibo_cnt(int user_weibo_cnt) {
		this.user_weibo_cnt = user_weibo_cnt;
	}

	/**
	 * @return the user_focus_cnt
	 */
	public int getUser_focus_cnt() {
		return user_focus_cnt;
	}

	/**
	 * @param user_focus_cnt the user_focus_cnt to set
	 */
	public void setUser_focus_cnt(int user_focus_cnt) {
		this.user_focus_cnt = user_focus_cnt;
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
