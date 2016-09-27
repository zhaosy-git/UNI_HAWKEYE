package com.uni.hawkeye.crawler.baidu_tieba.bean;

import java.util.Date;

public class TaskControl {

	private Integer execute_id;

	private Integer task_id;

	private String site_code;

	private String status;

	private Date ctime;

	private Date mtime;

	/**
	 * @return the execute_id
	 */
	public Integer getExecute_id() {
		return execute_id;
	}

	/**
	 * @param execute_id
	 *            the execute_id to set
	 */
	public void setExecute_id(Integer execute_id) {
		this.execute_id = execute_id;
	}

	/**
	 * @return the task_id
	 */
	public Integer getTask_id() {
		return task_id;
	}

	/**
	 * @param task_id
	 *            the task_id to set
	 */
	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}

	/**
	 * @return the site_code
	 */
	public String getSite_code() {
		return site_code;
	}

	/**
	 * @param site_code
	 *            the site_code to set
	 */
	public void setSite_code(String site_code) {
		this.site_code = site_code;
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
