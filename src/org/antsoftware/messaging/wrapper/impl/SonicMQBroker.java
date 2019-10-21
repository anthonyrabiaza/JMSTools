package org.antsoftware.messaging.wrapper.impl;

import java.util.Properties;

import javax.jms.Session;

import org.antsoftware.messaging.wrapper.AbstractBroker;

import progress.message.jclient.QueueConnection;
import progress.message.jclient.QueueConnectionFactory;

public class SonicMQBroker extends AbstractBroker {
	
	@Override
	public void init(Properties properties) throws Exception {
		super.init(properties);
		
		QueueConnectionFactory factory = new QueueConnectionFactory(properties.getProperty("brokerUrl"), "receiver", properties.getProperty("userName"), properties.getProperty("password"));
		this.setConnection(factory.createQueueConnection());
		this.setSession(((QueueConnection)getConnection()).createQueueSession(false, Session.CLIENT_ACKNOWLEDGE));  
		
		System.out.println("INFO: SonicMQ Broker initialized");
	}
}
