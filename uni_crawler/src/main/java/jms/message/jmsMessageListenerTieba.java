package jms.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uni_hawkeye.core.TiebaTaskInfo;

@Component
public class jmsMessageListenerTieba implements MessageListener { 

	@Autowired
	private com.uni.hawkeye.crawler.baidu_tieba.logic.impl.ControlLogicImpl baidu_tieba_controlLogic;
	
    /**
     * Implementation of <code>MessageListener</code>.
     */
    public void onMessage(Message message) {
        try {   
            
        	if (message instanceof ObjectMessage) {
        		TiebaTaskInfo tiebaInfo = (TiebaTaskInfo) ((ObjectMessage) message).getObject();
        		baidu_tieba_controlLogic.setTiebaTaskInfo(tiebaInfo);
        		baidu_tieba_controlLogic.setSite_code("9");
				new Thread(baidu_tieba_controlLogic).start();
			}
        } catch (JMSException e) {
        	e.printStackTrace();
        }
    }
    
}