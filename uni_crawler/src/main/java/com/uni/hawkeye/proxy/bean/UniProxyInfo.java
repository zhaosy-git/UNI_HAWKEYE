package com.uni.hawkeye.proxy.bean;

import java.util.Date;

public class UniProxyInfo {

	private String ip;
	private String port;
	private String site_code;
	private String expire;
	private String do_flag;
	private Date ctime;
	private Date mtime;
	
	public UniProxyInfo(){}

	public UniProxyInfo(String ip, String port, String site_code, String expire, String do_flag) {
		super();
		this.ip = ip;
		this.port = port;
		this.site_code = site_code;
		this.expire = expire;
		this.do_flag = do_flag;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the site_code
	 */
	public String getSite_code() {
		return site_code;
	}

	/**
	 * @param site_code the site_code to set
	 */
	public void setSite_code(String site_code) {
		this.site_code = site_code;
	}

	/**
	 * @return the expire
	 */
	public String getExpire() {
		return expire;
	}

	/**
	 * @param expire the expire to set
	 */
	public void setExpire(String expire) {
		this.expire = expire;
	}

	/**
	 * @return the do_flag
	 */
	public String getDo_flag() {
		return do_flag;
	}

	/**
	 * @param do_flag the do_flag to set
	 */
	public void setDo_flag(String do_flag) {
		this.do_flag = do_flag;
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
