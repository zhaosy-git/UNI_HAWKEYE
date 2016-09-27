package jms.message;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JmsExecutor {

	private static ClassPathXmlApplicationContext applicationContext = null;
	
	public static void main(String[] args) {
		String[] context = new String[] { "classpath:/applicationContext-main.xml", "classpath:/jmsMessageListener-context.xml" };
		applicationContext = new ClassPathXmlApplicationContext(context);
		
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
