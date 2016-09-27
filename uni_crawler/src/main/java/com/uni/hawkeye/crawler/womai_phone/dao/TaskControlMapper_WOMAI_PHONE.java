package com.uni.hawkeye.crawler.womai_phone.dao;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.womai_phone.bean.TaskControl;

public interface TaskControlMapper_WOMAI_PHONE {

	TaskControl selectMaxTaskControl(@Param(value = "siteCode") String site_code);

	void insertTaskControl(TaskControl tc);

	void updateByPrimaryKeySelective(TaskControl tc);

}
