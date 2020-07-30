package org.antsoftware.messaging.wrapper.interfaces;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

public interface JMSSender {
	static int DEFAULT_DELIVERY 	= javax.jms.DeliveryMode.NON_PERSISTENT;
	static int DEFAULT_PRIORITY 	= javax.jms.Message.DEFAULT_PRIORITY;
	static long DEFAULT_TIMETOLIVE 	= 0;
	
	public void sendMessage(Queue queue, Message message) throws Exception;
	public void sendMessage(Queue queue, Message message, int deliveryMode, int priority, long timeToLive) throws Exception;
	public Session getSession();
}
