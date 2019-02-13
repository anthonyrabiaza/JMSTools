package org.antsoftware.messaging.wrapper;

import javax.jms.Message;

public interface JMSGetter {
	public Message getMessage(String queue, String selector) throws Exception;
}
