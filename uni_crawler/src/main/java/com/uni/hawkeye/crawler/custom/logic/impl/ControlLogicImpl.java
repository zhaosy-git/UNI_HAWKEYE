package com.uni.hawkeye.crawler.custom.logic.impl;

import java.net.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.uni.hawkeye.crawler.custom.bean.TaskControl;
import com.uni.hawkeye.crawler.custom.enums.TaskControlStatus;
import com.uni.hawkeye.crawler.custom.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.custom.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;

import uni_hawkeye.core.CustomTaskInfo;

public class ControlLogicImpl implements Runnable {

	private static Log log = LogFactory.getLog(ControlLogicImpl.class);

	private String site_code;

	private PersistenceLogic persistenceLogic;

	private CrawlerLogic crawlerLogic;

	private boolean proxy_flag;

	private CustomTaskInfo customInfo;

	@Override
	public void run() {
		log.info("custom crawler start task...");

		JsoupCrawler.setProxy_flag(proxy_flag);
		Proxy proxy = JsoupCrawler.getProxy(site_code);
		JsoupCrawler.setProxy(proxy);

		TaskControl tc = persistenceLogic.getLastestTask(site_code, customInfo == null ? 0 : customInfo.getId());
		if (null == tc || ((tc.getStatus() != null) && (Integer.parseInt(tc.getStatus()) == Integer.parseInt(TaskControlStatus.DONE.value())))) {
			log.info("custom init taskControl ... ");
			tc = new TaskControl();
			tc.setStatus(TaskControlStatus.UNDO.value());
			tc.setSite_code(site_code);
			if (customInfo != null) {
				tc.setTask_id(customInfo.getId());
			} else {
				tc.setTask_id(0);
			}
			persistenceLogic.insertTaskControl(tc);
		}

		if ((TaskControlStatus.UNDO.value()).equals(tc.getStatus()) || TaskControlStatus.DOING.value().equals(tc.getStatus())) {
			log.info("custom crawling ... ");

			tc.setStatus(TaskControlStatus.DOING.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);

			crawlerLogic.crawler(tc, customInfo);

			tc.setStatus(TaskControlStatus.DONE.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			log.info("custom crawling end... ");
		}

		log.info("custom crawler end task...");
	}

	/**
	 * @param site_code
	 *            the site_code to set
	 */
	public void setSite_code(String site_code) {
		this.site_code = site_code;
	}

	/**
	 * @param persistenceLogic
	 *            the persistenceLogic to set
	 */
	public void setPersistenceLogic(PersistenceLogic persistenceLogic) {
		this.persistenceLogic = persistenceLogic;
	}

	/**
	 * @param crawlerLogic
	 *            the crawlerLogic to set
	 */
	public void setCrawlerLogic(CrawlerLogic crawlerLogic) {
		this.crawlerLogic = crawlerLogic;
	}

	/**
	 * @param proxy_flag
	 *            the proxy_flag to set
	 */
	public void setProxy_flag(boolean proxy_flag) {
		this.proxy_flag = proxy_flag;
	}

	/**
	 * @param customInfo
	 *            the customInfo to set
	 */
	public void setCustomInfo(CustomTaskInfo customInfo) {
		this.customInfo = customInfo;
	}

}
