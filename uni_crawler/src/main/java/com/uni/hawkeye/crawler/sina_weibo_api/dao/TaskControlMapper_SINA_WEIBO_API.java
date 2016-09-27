package com.uni.hawkeye.crawler.sina_weibo_api.dao;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.sina_weibo_api.bean.TaskControl;

public interface TaskControlMapper_SINA_WEIBO_API {

	TaskControl selectMaxTaskControl(@Param(value = "siteCode") String site_code);

	void insertTaskControl(TaskControl tc);

	void updateByPrimaryKeySelective(TaskControl tc);

}
