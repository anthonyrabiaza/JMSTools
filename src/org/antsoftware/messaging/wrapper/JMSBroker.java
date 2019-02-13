package org.antsoftware.messaging.wrapper;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Session;

public interface JMSBroker {
	public void init(Properties properties) throws Exception;
	public Properties getProperties();
	public Connection getConnection();
	public Session 	  getSession();
}
