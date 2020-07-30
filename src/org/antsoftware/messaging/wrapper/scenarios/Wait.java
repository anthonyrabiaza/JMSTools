package org.antsoftware.messaging.wrapper.scenarios;

import java.util.Properties;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.antsoftware.messaging.wrapper.AbstractGenericGetter;
import org.antsoftware.messaging.wrapper.interfaces.JMSBroker;
import org.antsoftware.messaging.wrapper.interfaces.JMSGetter;
import org.antsoftware.messaging.wrapper.tools.Helper;

public class Wait {

	JMSBroker broker;
	JMSGetter getter;

	public void init(Properties properties) throws Exception {
		broker = (JMSBroker) Helper.newInstance(properties.getProperty("brokerClassName")); 
		broker.init(properties);

		getter = new AbstractGenericGetter(broker) {
			@Override
			public Message getMessage(String queue, String selector) throws Exception {
				System.out.println("INFO: Getting message with selector (" + selector +") ...");
				Message message = getConsumer(queue, selector).receive();
				System.out.println("INFO: Message received (" + ((TextMessage)message).getText() + ")");
				message.acknowledge();
				return message;
			}
		};

		System.out.println("INFO: Ready!");

		System.out.println("INFO: Waiting for message...");

		TextMessage replyMessage = (TextMessage) getter.getMessage(
				broker.getProperties().getProperty("replyDestination"),
				null
		);
		
		System.out.println("INFO: ID of Message received: " + replyMessage.getJMSMessageID());
	}


	public static void main(String[] args) {
		System.out.println("INFO: Type Ctrl+C to exit the program");
		try {
			new Wait().init(Helper.getProperties(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
