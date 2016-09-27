package com.uni.hawkeye;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.uni.hawkeye.enums.WebSiteEnum;

public class Executor {

	private static Log log = LogFactory.getLog(Executor.class);

	private static ClassPathXmlApplicationContext applicationContext = null;

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("config-web", Locale.getDefault());
	
	public static void main(String[] args) {

		try {

			String[] context = new String[] { "classpath:/applicationContext-main.xml" };
			applicationContext = new ClassPathXmlApplicationContext(context);
			String[] web_sites = null;
			if(RESOURCE_BUNDLE.getString("web_site").indexOf(",") != -1){
				web_sites = RESOURCE_BUNDLE.getString("web_site").split(",");
			}else{
				web_sites = new String[]{RESOURCE_BUNDLE.getString("web_site")};
			}
			
			for(String web_site : web_sites){
				if(web_site.equals(WebSiteEnum.tmall.value())){
					com.uni.hawkeye.crawler.tmall.logic.impl.ControlLogicImpl tmall_control = (com.uni.hawkeye.crawler.tmall.logic.impl.ControlLogicImpl) applicationContext.getBean("tmall_controlLogic");
					new Thread(tmall_control).start();
				} else if(web_site.equals(WebSiteEnum.jd.value())){
					com.uni.hawkeye.crawler.jd.logic.impl.ControlLogicImpl jd_control = (com.uni.hawkeye.crawler.jd.logic.impl.ControlLogicImpl) applicationContext.getBean("jd_controlLogic");
					new Thread(jd_control).start();
				} else if(web_site.equals(WebSiteEnum.yhd.value())){
					com.uni.hawkeye.crawler.yhd.logic.impl.ControlLogicImpl yhd_control = (com.uni.hawkeye.crawler.yhd.logic.impl.ControlLogicImpl) applicationContext.getBean("yhd_controlLogic");
					new Thread(yhd_control).start();
				} else if(web_site.equals(WebSiteEnum.womai.value())){
					com.uni.hawkeye.crawler.womai.logic.impl.ControlLogicImpl womai_control = (com.uni.hawkeye.crawler.womai.logic.impl.ControlLogicImpl) applicationContext.getBean("womai_controlLogic");
					new Thread(womai_control).start();
				} else if(web_site.equals(WebSiteEnum.gome.value())){
					com.uni.hawkeye.crawler.gome.logic.impl.ControlLogicImpl gome_control = (com.uni.hawkeye.crawler.gome.logic.impl.ControlLogicImpl) applicationContext.getBean("gome_controlLogic");
					new Thread(gome_control).start();
				} else if(web_site.equals(WebSiteEnum.suning.value())){
					com.uni.hawkeye.crawler.suning.logic.impl.ControlLogicImpl suning_control = (com.uni.hawkeye.crawler.suning.logic.impl.ControlLogicImpl) applicationContext.getBean("suning_controlLogic");
					new Thread(suning_control).start();
				} else if(web_site.equals(WebSiteEnum.amazon.value())){
					com.uni.hawkeye.crawler.amazon.logic.impl.ControlLogicImpl amazon_control = (com.uni.hawkeye.crawler.amazon.logic.impl.ControlLogicImpl) applicationContext.getBean("amazon_controlLogic");
					new Thread(amazon_control).start();
				} else if(web_site.equals(WebSiteEnum.womai_phone.value())){
					com.uni.hawkeye.crawler.womai_phone.logic.impl.ControlLogicImpl womai_phone_control = (com.uni.hawkeye.crawler.womai_phone.logic.impl.ControlLogicImpl) applicationContext.getBean("womai_phone_controlLogic");
					new Thread(womai_phone_control).start();
				} else if(web_site.equals(WebSiteEnum.baidu_tieba.value())){
					com.uni.hawkeye.crawler.baidu_tieba.logic.impl.ControlLogicImpl baidu_tieba_control = (com.uni.hawkeye.crawler.baidu_tieba.logic.impl.ControlLogicImpl) applicationContext.getBean("baidu_tieba_controlLogic");
					new Thread(baidu_tieba_control).start();
				} else if(web_site.equals(WebSiteEnum.sina_weibo_html.value())){
					com.uni.hawkeye.crawler.sina_weibo_html.logic.impl.ControlLogicImpl sina_weibo_control = (com.uni.hawkeye.crawler.sina_weibo_html.logic.impl.ControlLogicImpl) applicationContext.getBean("sina_weibo_html_controlLogic");
					new Thread(sina_weibo_control).start();
				} else if(web_site.equals(WebSiteEnum.sina_weibo_api.value())){
					com.uni.hawkeye.crawler.sina_weibo_api.logic.impl.ControlLogicImpl sina_weibo_control = (com.uni.hawkeye.crawler.sina_weibo_api.logic.impl.ControlLogicImpl) applicationContext.getBean("sina_weibo_api_controlLogic");
					new Thread(sina_weibo_control).start();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
