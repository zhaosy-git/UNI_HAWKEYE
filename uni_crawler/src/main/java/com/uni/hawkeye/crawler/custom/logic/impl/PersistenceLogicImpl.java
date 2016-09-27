package com.uni.hawkeye.crawler.custom.logic.impl;

import javax.annotation.Resource;

import com.uni.hawkeye.crawler.custom.bean.TaskControl;
import com.uni.hawkeye.crawler.custom.dao.TaskControlMapper_Custom;
import com.uni.hawkeye.crawler.custom.logic.PersistenceLogic;

public class PersistenceLogicImpl implements PersistenceLogic {

	private TaskControlMapper_Custom tcMapper;
	
	/**
	 * @param tcMapper the tcMapper to set
	 */
	@Resource
	public void setTcMapper(TaskControlMapper_Custom tcMapper) {
		this.tcMapper = tcMapper;
	}

	@Override
	public TaskControl getLastestTask(String site_code, int task_id) {
		return tcMapper.selectMaxTaskControl(site_code, task_id);
	}

	@Override
	public void insertTaskControl(TaskControl tc) {
		tcMapper.insertTaskControl(tc);
	}

	@Override
	public TaskControl updateTaskAsUrlCrawling(TaskControl tc) {
		tcMapper.updateByPrimaryKeySelective(tc);
		return tc;
	}

}
