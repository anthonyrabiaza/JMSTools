package org.antsoftware.messaging.wrapper;

import javax.jms.Destination;
import javax.jms.MessageConsumer;

import org.antsoftware.messaging.wrapper.interfaces.JMSBroker;
import org.antsoftware.messaging.wrapper.interfaces.JMSGetter;

public abstract class AbstractGenericGetter implements JMSGetter {
	
	JMSBroker broker;

	public AbstractGenericGetter(JMSBroker broker) throws Exception {
		this.broker = broker;
	}
	
	public MessageConsumer getConsumer(String queueName, String selector) throws Exception {
		Destination destination = broker.getSession().createQueue(queueName);
		MessageConsumer consumer;
		if(selector == null) {
			consumer = broker.getSession().createConsumer(destination);
		} else {
			consumer = broker.getSession().createConsumer(destination, selector);
		}
		broker.getConnection().start();
		return consumer;
	}

}
