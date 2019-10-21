package org.antsoftware.messaging.wrapper;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.Session;

public abstract class AbstractBroker implements JMSBroker {
	
	Connection connection;
	Properties properties;
	Session session;
	
	public final void setConnection(Connection connection) {
		this.connection = connection;
	}

	public final void setProperties(Properties properties) {
		this.properties = properties;
	}

	public final void setSession(Session session) {
		this.session = session;
	}
	
	@Override
	public void init(Properties properties) throws Exception {
		this.properties = properties;
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
