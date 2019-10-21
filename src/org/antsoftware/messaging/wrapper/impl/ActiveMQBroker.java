package org.antsoftware.messaging.wrapper.impl;

import java.util.Properties;

import javax.jms.Session;

import org.antsoftware.messaging.wrapper.AbstractBroker;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQBroker extends AbstractBroker {
	
	@Override
	public void init(Properties properties) throws Exception {
		super.init(properties);
		
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(properties.getProperty("brokerUrl"));
		this.setConnection(factory.createConnection(properties.getProperty("userName"), properties.getProperty("password")));
		this.setSession(getConnection().createSession(false, Session.CLIENT_ACKNOWLEDGE));
		
		System.out.println("INFO: ActiveMQ Broker initialized");
	}
}
