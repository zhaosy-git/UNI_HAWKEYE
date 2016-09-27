package com.uni.hawkeye.crawler.baidu_tieba.logic.impl;

import java.net.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.uni.hawkeye.crawler.baidu_tieba.bean.TaskControl;
import com.uni.hawkeye.crawler.baidu_tieba.enums.TaskControlStatus;
import com.uni.hawkeye.crawler.baidu_tieba.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.baidu_tieba.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;

import uni_hawkeye.core.TiebaTaskInfo;

public class ControlLogicImpl implements Runnable {

	private static Log log = LogFactory.getLog(ControlLogicImpl.class);

	private String site_code;

	private PersistenceLogic persistenceLogic;

	private CrawlerLogic crawlerLogic;

	private boolean proxy_flag;
	
	private TiebaTaskInfo tiebaInfo;
	
	@Override
	public void run() {
		log.info("baidu_tieba start task...");

		JsoupCrawler.setProxy_flag(proxy_flag);
		Proxy proxy = JsoupCrawler.getProxy(site_code);
		JsoupCrawler.setProxy(proxy);

		TaskControl tc = persistenceLogic.getLastestTask(site_code, tiebaInfo == null ? 0 : tiebaInfo.getId());
		if (null == tc || ((tc.getStatus() != null) && (Integer.parseInt(tc.getStatus()) == Integer.parseInt(TaskControlStatus.TIEZI_CRAWLING_FINISH.value())))) {
			log.info("baidu_tieba init taskControl ... ");
			tc = new TaskControl();
			tc.setStatus(TaskControlStatus.UNDO.value());
			tc.setSite_code(site_code);
			if(tiebaInfo != null){
				tc.setTask_id(tiebaInfo.getId());
			}else{
				tc.setTask_id(0);
			}
			persistenceLogic.insertTaskControl(tc);
		}

		if ((TaskControlStatus.UNDO.value()).equals(tc.getStatus()) || TaskControlStatus.CATEGORY_CRAWLING.value().equals(tc.getStatus())) {
			log.info("baidu_tieba category crawling ... ");

			tc.setStatus(TaskControlStatus.CATEGORY_CRAWLING.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);

			if(tiebaInfo == null){
				crawlerLogic.crawlCategoryList(tc);
			}else{
				crawlerLogic.crawlCategoryList(tc, tiebaInfo);
			}

			tc.setStatus(TaskControlStatus.CATEGORY_CRAWLING_FINISH.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			log.info("baidu_tieba category crawling end... ");
		}

		
		if(TaskControlStatus.CATEGORY_CRAWLING_FINISH.value().equals(tc.getStatus()) || TaskControlStatus.BAR_CRAWLING.value().equals(tc.getStatus())){
			log.info("baidu_tieba bar list crawling ... ");
			tc.setStatus(TaskControlStatus.BAR_CRAWLING.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			
			crawlerLogic.crawlerBarList(tc, tiebaInfo);
			
			tc.setStatus(TaskControlStatus.BAR_CRAWLING_FINISH.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			log.info("baidu_tieba bar list crawling end... ");
		}
		
		if(TaskControlStatus.BAR_CRAWLING_FINISH.value().equals(tc.getStatus()) || TaskControlStatus.TIEZI_CRAWLING.value().equals(tc.getStatus())){
			log.info("baidu_tieba tiezi crawling ... ");
			tc.setStatus(TaskControlStatus.TIEZI_CRAWLING.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			
			crawlerLogic.crawlerTiezi(tc, tiebaInfo);
			
			tc.setStatus(TaskControlStatus.TIEZI_CRAWLING_FINISH.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			log.info("baidu_tieba tiezi crawling end... ");
		}
		log.info("baidu_tieba end task...");
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


	public void setTiebaTaskInfo(TiebaTaskInfo tiebaInfo) {
		this.tiebaInfo = tiebaInfo;
	}
}
