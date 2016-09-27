package com.uni.hawkeye.crawler.sina_weibo_html.logic.impl;

import java.net.Proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.uni.hawkeye.crawler.sina_weibo_html.bean.TaskControl;
import com.uni.hawkeye.crawler.sina_weibo_html.enums.TaskControlStatus;
import com.uni.hawkeye.crawler.sina_weibo_html.logic.CrawlerLogic;
import com.uni.hawkeye.crawler.sina_weibo_html.logic.PersistenceLogic;
import com.uni.hawkeye.jsoup.JsoupCrawler;

import uni_hawkeye.core.WeiboTaskInfo;

public class ControlLogicImpl implements Runnable {

	private static Log log = LogFactory.getLog(ControlLogicImpl.class);

	private String site_code;

	private PersistenceLogic persistenceLogic;

	private CrawlerLogic crawlerLogic;

	private boolean proxy_flag;
	
	private WeiboTaskInfo weiboInfo;
	
	@Override
	public void run() {
		log.info("sina_weibo start task...");

		JsoupCrawler.setProxy_flag(proxy_flag);
		Proxy proxy = JsoupCrawler.getProxy(site_code);
		JsoupCrawler.setProxy(proxy);

		TaskControl tc = persistenceLogic.getLastestTask(site_code, weiboInfo == null ? 0 : weiboInfo.getId());
		if (null == tc || ((tc.getStatus() != null) && (Integer.parseInt(tc.getStatus()) == Integer.parseInt(TaskControlStatus.BLOCK_CRAWLING_FINISH.value())))) {
			log.info("sina_weibo init taskControl ... ");
			tc = new TaskControl();
			tc.setStatus(TaskControlStatus.UNDO.value());
			tc.setSite_code(site_code);
			if(weiboInfo != null){
				tc.setTask_id(weiboInfo.getId());
			}else{
				tc.setTask_id(0);
			}
			persistenceLogic.insertTaskControl(tc);
		}

		if ((TaskControlStatus.UNDO.value()).equals(tc.getStatus()) || TaskControlStatus.CATEGORY_OR_SEARCH_CRAWLING.value().equals(tc.getStatus())) {
			log.info("sina_weibo category crawling ... ");

			tc.setStatus(TaskControlStatus.CATEGORY_OR_SEARCH_CRAWLING.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			
			if(weiboInfo == null){
				crawlerLogic.crawlCategoryList(tc);
			}else if(weiboInfo.getCrawler_type().equals("hot_category")){
				crawlerLogic.crawlCategoryList(tc, weiboInfo);
			}else if(weiboInfo.getCrawler_type().equals("search")){
				crawlerLogic.crawlSearchList(tc, weiboInfo);
			}

			tc.setStatus(TaskControlStatus.CATEGORY_OR_SEARCH_CRAWLING_FINISH.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			log.info("sina_weibo category crawling end... ");
		}

		
		if(TaskControlStatus.CATEGORY_OR_SEARCH_CRAWLING_FINISH.value().equals(tc.getStatus()) || TaskControlStatus.BLOCK_CRAWLING.value().equals(tc.getStatus())){
			log.info("sina_weibo block list crawling ... ");
			tc.setStatus(TaskControlStatus.BLOCK_CRAWLING.value());
			tc = persistenceLogic.updateTaskAsUrlCrawling(tc);
			
			if(weiboInfo == null || weiboInfo.getCrawler_type().equals("hot_category")){
				crawlerLogic.crawlerBlockList(tc, weiboInfo);
			}else{
				crawlerLogic.crawlerSearchBlockList(tc, weiboInfo);
			}
			
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


	public void setWeiboTaskInfo(WeiboTaskInfo weiboInfo) {
		this.weiboInfo = weiboInfo;
	}
}
