package jms.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uni_hawkeye.core.WeiboTaskInfo;

@Component
public class jmsMessageListenerWeibo implements MessageListener { 

	@Autowired
	private com.uni.hawkeye.crawler.sina_weibo_html.logic.impl.ControlLogicImpl sina_weibo_html_controlLogic;
	
    /**
     * Implementation of <code>MessageListener</code>.
     */
    public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				WeiboTaskInfo weiboInfo = (WeiboTaskInfo) ((ObjectMessage) message).getObject();
				sina_weibo_html_controlLogic.setWeiboTaskInfo(weiboInfo);
				sina_weibo_html_controlLogic.setSite_code("10");
				new Thread(sina_weibo_html_controlLogic).start();
			}
		} catch (JMSException e) {
		}
	}
    
}