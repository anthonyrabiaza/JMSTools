package org.antsoftware.messaging.wrapper.impl;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Session;

import org.antsoftware.messaging.wrapper.JMSBroker;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ActiveMQBroker implements JMSBroker {
	
	Connection connection;
	Properties properties;
	Session session;

	@Override
	public void init(Properties properties) throws Exception {
		this.properties = properties;
		
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(properties.getProperty("brokerUrl"));
		connection = factory.createConnection(properties.getProperty("userName"), properties.getProperty("password"));

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		System.out.println("INFO: ActiveMQ Broker initialized");
	}
	
	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public Connection getConnection() {
		return connection;
	}

	@Override
	public Session getSession() {
		return session;
	}
}
