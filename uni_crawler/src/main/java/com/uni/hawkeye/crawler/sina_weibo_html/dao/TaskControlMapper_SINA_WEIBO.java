package com.uni.hawkeye.crawler.sina_weibo_html.dao;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.sina_weibo_html.bean.TaskControl;

public interface TaskControlMapper_SINA_WEIBO {

	TaskControl selectMaxTaskControl(@Param(value = "site_code") String site_code, @Param(value = "task_id") int task_id);

	void insertTaskControl(TaskControl tc);

	void updateByPrimaryKeySelective(TaskControl tc);

}
