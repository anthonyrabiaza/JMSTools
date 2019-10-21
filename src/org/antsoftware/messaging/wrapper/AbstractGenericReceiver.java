package org.antsoftware.messaging.wrapper;

import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

public abstract class AbstractGenericReceiver implements JMSReceiver, MessageListener {

	public void init(JMSBroker broker) throws Exception {
		String destinationNames = broker.getProperties().getProperty("receiverDestination");
		String[] destinations 	= destinationNames.split(",");
		
		for(int i=0;i<destinations.length;i++) {
			String destinationName = destinations[i];
			Destination destination 	= broker.getSession().createQueue(destinationName);
			MessageConsumer consumer 	= broker.getSession().createConsumer(destination);
			consumer.setMessageListener(this);
		}
		broker.getConnection().start();
	}

}
