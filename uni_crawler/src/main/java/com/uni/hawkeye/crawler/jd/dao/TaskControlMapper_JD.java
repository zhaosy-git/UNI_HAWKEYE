package com.uni.hawkeye.crawler.jd.dao;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.jd.bean.TaskControl;

public interface TaskControlMapper_JD {

	TaskControl selectMaxTaskControl(@Param(value = "site_code") String siteCode, @Param(value = "task_id") int task_id);

	void insertTaskControl(TaskControl tc);

	void updateByPrimaryKeySelective(TaskControl tc);

}
