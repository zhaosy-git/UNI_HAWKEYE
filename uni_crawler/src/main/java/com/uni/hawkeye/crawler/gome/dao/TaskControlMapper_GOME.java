package com.uni.hawkeye.crawler.gome.dao;

import org.apache.ibatis.annotations.Param;

import com.uni.hawkeye.crawler.gome.bean.TaskControl;

public interface TaskControlMapper_GOME {

	TaskControl selectMaxTaskControl(@Param(value = "site_code") String site_code, @Param(value = "task_id") int task_id);

	void insertTaskControl(TaskControl tc);

	void updateByPrimaryKeySelective(TaskControl tc);

}
