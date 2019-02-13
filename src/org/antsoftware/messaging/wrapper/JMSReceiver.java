package org.antsoftware.messaging.wrapper;

import java.util.Properties;

import javax.jms.Message;

public interface JMSReceiver {
	public void init(Properties properties) throws Exception;
	public Properties getProperties();
	public void onMessage(Message message);
}
