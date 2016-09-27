package com.uni.hawkeye.crawler.womai_phone.logic.impl;

import java.net.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.uni.hawkeye.crawler.womai_phone.bean.TaskControl;
import com.uni.hawkeye.crawler.womai_phone.enums.TaskControlStatus;
import com.uni.hawkeye.crawler.womai_phone.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.womai_phone.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;


public class ControlLogicImpl implements Runnable {

	private static Log log = LogFactory.getLog(ControlLogicImpl.class);

	private String site_code;

	private PersistenceLogic persistenceLogic;

	private CrawlerLogic crawlerLogic;

	private boolean proxy_flag;
	
	@Override
	public void run() {
		log.info("womai_phone start task...");

		JsoupCrawler.setProxy_flag(proxy_flag);
		Proxy proxy = JsoupCrawler.getProxy(site_code);
		JsoupCrawler.setProxy(proxy);

		TaskControl tc = persistenceLogic.getLastestTask(site_code);
		if (null == tc || ((tc.getStatus() != null) && (Integer.parseInt(tc.getStatus()) == Integer.parseInt(TaskControlStatus.PRODUCT_CRAWLING_FINISH.value())))) {
			log.info("womai_phone init taskControl ... ");
			tc = new TaskControl();
			tc.setStatus(TaskControlStatus.UNDO.value());
			tc.setSite_code(site_code);
			persistenceLogic.insertTaskControl(tc);
		}

		if ((TaskControlStatus.UNDO.value()).equals(tc.getStatus()) || TaskControlStatus.CATEGORY_CRAWLING.value().equals(tc.getStatus())) {
			log.info("womai_phone category crawling ... ");

			tc.setStatus(TaskControlStatus.CATEGORY_CRAWLING.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);

			crawlerLogic.crawlCategoryList(tc);

			tc.setStatus(TaskControlStatus.CATEGORY_CRAWLING_FINISH.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			log.info("womai_phone category crawling end... ");
		}

		if(TaskControlStatus.CATEGORY_CRAWLING_FINISH.value().equals(tc.getStatus()) || TaskControlStatus.PRODUCT_URL_GENERATOR.value().equals(tc.getStatus())){
			log.info("womai_phone product list url generator ... ");
			tc.setStatus(TaskControlStatus.PRODUCT_URL_GENERATOR.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			
			crawlerLogic.crawlerProductList(tc);
			
			tc.setStatus(TaskControlStatus.PRODUCT_URL_GENERATOR_FINISH.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			log.info("womai_phone product list url generator end... ");
		}
		
		if(TaskControlStatus.PRODUCT_URL_GENERATOR_FINISH.value().equals(tc.getStatus()) || TaskControlStatus.PRODUCT_CRAWLING.value().equals(tc.getStatus())){
			log.info("womai_phone product crawling ... ");
			tc.setStatus(TaskControlStatus.PRODUCT_CRAWLING.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			
			crawlerLogic.crawlerProduct(tc);
			
			tc.setStatus(TaskControlStatus.PRODUCT_CRAWLING_FINISH.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			log.info("womai_phone product crawling end... ");
		}
		log.info("womai_phone end task...");
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
