package org.antsoftware.messaging.wrapper;

import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

public abstract class AbstractGenericReceiver implements JMSReceiver, MessageListener {

	public void init(JMSBroker broker) throws Exception {
		//Receiver
		Destination destination = broker.getSession().createQueue(broker.getProperties().getProperty("receiverDestination"));

		MessageConsumer consumer = broker.getSession().createConsumer(destination);
		consumer.setMessageListener(this);
		broker.getConnection().start();
	}

}
