package org.antsoftware.messaging.wrapper;

import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import org.antsoftware.messaging.wrapper.interfaces.JMSBroker;
import org.antsoftware.messaging.wrapper.interfaces.JMSSender;

public abstract class AbstractGenericSender implements JMSSender {

	JMSBroker broker;
	
	public AbstractGenericSender(JMSBroker broker) {
		this.broker = broker;
	}
	
	@Override
	public void sendMessage(Queue queue, Message message) throws Exception {
		sendMessage(queue, message, JMSSender.DEFAULT_DELIVERY, JMSSender.DEFAULT_PRIORITY,	JMSSender.DEFAULT_TIMETOLIVE);
	}
	
	public Session getSession() {
		return broker.getSession()	;
	}
}
