package jms.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uni_hawkeye.core.CustomTaskInfo;

@Component
public class jmsMessageListenerCustom implements MessageListener { 

	@Autowired
	private com.uni.hawkeye.crawler.custom.logic.impl.ControlLogicImpl custom_controlLogic;
	
    /**
     * Implementation of <code>MessageListener</code>.
     */
    public void onMessage(Message message) {
        try {   
            
        	if (message instanceof ObjectMessage) {
        		CustomTaskInfo customInfo = (CustomTaskInfo) ((ObjectMessage) message).getObject();
        		custom_controlLogic.setCustomInfo(customInfo);
        		custom_controlLogic.setSite_code("12");
				new Thread(custom_controlLogic).start();
			}
        } catch (JMSException e) {
        	e.printStackTrace();
        }
    }
    
}