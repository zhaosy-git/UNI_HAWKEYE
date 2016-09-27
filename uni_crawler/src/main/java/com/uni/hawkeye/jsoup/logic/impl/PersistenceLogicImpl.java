package com.uni.hawkeye.jsoup.logic.impl;

import javax.annotation.Resource;

import com.uni.hawkeye.jsoup.bean.CrawlerPropertyInfo;
import com.uni.hawkeye.jsoup.dao.JsoupMapper;
import com.uni.hawkeye.jsoup.logic.PersistenceLogic;

public class PersistenceLogicImpl implements PersistenceLogic {

	private JsoupMapper jsoupMapper;

	/**
	 * @param jsoupMapper
	 *            the jsoupMapper to set
	 */
	@Resource
	public void setJsoupMapper(JsoupMapper jsoupMapper) {
		this.jsoupMapper = jsoupMapper;
	}

	@Override
	public CrawlerPropertyInfo getJsoupProperty(String cookie_from) {
		return jsoupMapper.getJsoupProperty(cookie_from);
	}

}
