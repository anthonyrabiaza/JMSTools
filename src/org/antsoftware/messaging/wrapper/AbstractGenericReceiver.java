package org.antsoftware.messaging.wrapper;

import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;

import org.antsoftware.messaging.wrapper.impl.GenericJNDI;
import org.antsoftware.messaging.wrapper.interfaces.JMSBroker;
import org.antsoftware.messaging.wrapper.interfaces.JMSReceiver;

public abstract class AbstractGenericReceiver implements JMSReceiver, MessageListener {

	public void init(JMSBroker broker) throws Exception {
		String destinationNames = broker.getProperties().getProperty("receiverDestination");
		String[] destinations 	= destinationNames.split(",");
		
		for(int i=0;i<destinations.length;i++) {
			String destinationName = destinations[i];
			Destination destination;
			if(broker instanceof GenericJNDI) {
				destination = (Queue)((GenericJNDI)broker).getInitialContext().lookup(destinationName);
			} else {
				destination = broker.getSession().createQueue(destinationName);
			}
			
			MessageConsumer consumer 	= broker.getSession().createConsumer(destination);
			consumer.setMessageListener(this);
		}
		broker.getConnection().start();
	}

}
