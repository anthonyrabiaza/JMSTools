package org.antsoftware.messaging.wrapper.impl;

import java.util.Hashtable;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.antsoftware.messaging.wrapper.AbstractBroker;

public class GenericJNDI extends AbstractBroker {
	
	@Override
	public void init(Properties properties) throws Exception {
		super.init(properties);
		
		Hashtable<String,Object> environment = new Hashtable<String,Object>();
		environment.put(InitialContext.SECURITY_PRINCIPAL, properties.getProperty("userName"));
		environment.put(InitialContext.SECURITY_CREDENTIALS, properties.getProperty("password"));
		environment.put(InitialContext.INITIAL_CONTEXT_FACTORY, properties.getProperty("initialContextFactory"));
		environment.put(InitialContext.PROVIDER_URL, properties.getProperty("brokerUrl"));
		
		String additionalProperties = properties.getProperty("additionalProperties");
		if (additionalProperties!= null && !"".equals(additionalProperties)) {
			String[] listProperties = additionalProperties.split(",");
			for (int i = 0; i < listProperties.length; i++) {
				String key = listProperties[i];
				environment.put(listProperties[i], properties.getProperty(key));
			}
		}
		
		InitialContext initialContext = new InitialContext(environment);
		ConnectionFactory factory = (ConnectionFactory)initialContext.lookup(properties.getProperty("connectionFactoryName"));
		this.setConnection(factory.createConnection());
		this.setSession(getConnection().createSession(false, Session.CLIENT_ACKNOWLEDGE));
		
		System.out.println("INFO: GenericJNDI Broker initialized with Class (" + properties.getProperty("initialContextFactory") + ")");
	}
}
