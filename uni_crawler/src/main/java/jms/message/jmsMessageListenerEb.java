package jms.message;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uni.hawkeye.crawler.jd.bean.FromWebParam;
import com.uni.hawkeye.enums.WebSiteEnum;

import uni_hawkeye.core.EBTaskInfo;

@Component
public class jmsMessageListenerEb implements MessageListener {

	@Autowired
	private com.uni.hawkeye.crawler.tmall.logic.impl.ControlLogicImpl tmall_controlLogic;

	@Autowired
	private com.uni.hawkeye.crawler.jd.logic.impl.ControlLogicImpl jd_controlLogic;

	@Autowired
	private com.uni.hawkeye.crawler.suning.logic.impl.ControlLogicImpl suning_controlLogic;

	@Autowired
	private com.uni.hawkeye.crawler.yhd.logic.impl.ControlLogicImpl yhd_controlLogic;

	@Autowired
	private com.uni.hawkeye.crawler.womai.logic.impl.ControlLogicImpl womai_controlLogic;

	@Autowired
	private com.uni.hawkeye.crawler.gome.logic.impl.ControlLogicImpl gome_controlLogic;

	@Autowired
	private com.uni.hawkeye.crawler.amazon.logic.impl.ControlLogicImpl amazon_controlLogic;

	/**
	 * Implementation of <code>MessageListener</code>.
	 */
	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				EBTaskInfo ebTaskInfo = (EBTaskInfo) ((ObjectMessage) message).getObject();
				try {
					if (WebSiteEnum.tmall.value().equals(ebTaskInfo.getSite_code())) {
						tmall_controlLogic.setEBTaskInfo(ebTaskInfo);
						tmall_controlLogic.setSite_code(ebTaskInfo.getSite_code());
						new Thread(tmall_controlLogic).start();
					} 
					else if (WebSiteEnum.jd.value().equals(ebTaskInfo.getSite_code())) {
						jd_controlLogic.setEBTaskInfo(ebTaskInfo);
						jd_controlLogic.setSite_code(ebTaskInfo.getSite_code());
						new Thread(jd_controlLogic).start();
					} else if (WebSiteEnum.suning.value().equals(ebTaskInfo.getSite_code())) {
						suning_controlLogic.setEBTaskInfo(ebTaskInfo);
						suning_controlLogic.setSite_code(ebTaskInfo.getSite_code());
						new Thread(suning_controlLogic).start();
					} else if (WebSiteEnum.yhd.value().equals(ebTaskInfo.getSite_code())) {
						yhd_controlLogic.setEBTaskInfo(ebTaskInfo);
						yhd_controlLogic.setSite_code(ebTaskInfo.getSite_code());
						new Thread(yhd_controlLogic).start();
					} else if (WebSiteEnum.womai.value().equals(ebTaskInfo.getSite_code())) {
						womai_controlLogic.setEBTaskInfo(ebTaskInfo);
						womai_controlLogic.setSite_code(ebTaskInfo.getSite_code());
						new Thread(womai_controlLogic).start();
					} else if (WebSiteEnum.gome.value().equals(ebTaskInfo.getSite_code())) {
						gome_controlLogic.setEBTaskInfo(ebTaskInfo);
						gome_controlLogic.setSite_code(ebTaskInfo.getSite_code());
						new Thread(gome_controlLogic).start();
					} else if (WebSiteEnum.amazon.value().equals(ebTaskInfo.getSite_code())) {
						amazon_controlLogic.setEBTaskInfo(ebTaskInfo);
						amazon_controlLogic.setSite_code(ebTaskInfo.getSite_code());
						new Thread(amazon_controlLogic).start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (JMSException e) {
		}
	}

}