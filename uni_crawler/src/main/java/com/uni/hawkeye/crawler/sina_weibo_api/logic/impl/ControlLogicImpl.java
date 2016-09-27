package com.uni.hawkeye.crawler.sina_weibo_api.logic.impl;

import java.net.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.uni.hawkeye.crawler.sina_weibo_api.bean.TaskControl;
import com.uni.hawkeye.crawler.sina_weibo_api.enums.TaskControlStatus;
import com.uni.hawkeye.crawler.sina_weibo_api.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.sina_weibo_api.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;

public class ControlLogicImpl implements Runnable {

	private static Log log = LogFactory.getLog(ControlLogicImpl.class);

	private String site_code;

	private PersistenceLogic persistenceLogic;

	private CrawlerLogic crawlerLogic;

	private boolean proxy_flag;
	
	@Override
	public void run() {
		log.info("sina_weibo start task...");

		JsoupCrawler.setProxy_flag(proxy_flag);
		Proxy proxy = JsoupCrawler.getProxy(site_code);
		JsoupCrawler.setProxy(proxy);

		TaskControl tc = persistenceLogic.getLastestTask(site_code);
		if (null == tc || ((tc.getStatus() != null) && (Integer.parseInt(tc.getStatus()) == Integer.parseInt(TaskControlStatus.USER_ATTENTION_CRAWLING_FINISH.value())))) {
			log.info("sina_weibo init taskControl ... ");
			tc = new TaskControl();
			tc.setStatus(TaskControlStatus.UNDO.value());
			tc.setSite_code(site_code);
			persistenceLogic.insertTaskControl(tc);
		}

		if ((TaskControlStatus.UNDO.value()).equals(tc.getStatus()) || TaskControlStatus.CATEGORY_CRAWLING.value().equals(tc.getStatus())) {
			log.info("sina_weibo category crawling ... ");

			tc.setStatus(TaskControlStatus.CATEGORY_CRAWLING.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);

			crawlerLogic.crawlCategoryList(tc);

			tc.setStatus(TaskControlStatus.CATEGORY_CRAWLING_FINISH.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			log.info("sina_weibo category crawling end... ");
		}

		
		if(TaskControlStatus.CATEGORY_CRAWLING_FINISH.value().equals(tc.getStatus()) || TaskControlStatus.BLOCK_CRAWLING.value().equals(tc.getStatus())){
			log.info("sina_weibo block list crawling ... ");
			tc.setStatus(TaskControlStatus.BLOCK_CRAWLING.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			
			crawlerLogic.crawlerBlockList(tc);
			
			tc.setStatus(TaskControlStatus.BLOCK_CRAWLING_FINISH.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			log.info("sina_weibo block list crawling end... ");
		}
		
		log.info("sina_weibo end task...");
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
	 * @param proxy_flag the proxy_flag to set
	 */
	public void setProxy_flag(boolean proxy_flag) {
		this.proxy_flag = proxy_flag;
	}
}
