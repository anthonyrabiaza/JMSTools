package org.antsoftware.messaging.wrapper.scenarios;

import java.util.Properties;

import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;

import org.antsoftware.messaging.wrapper.AbstractGenericSender;
import org.antsoftware.messaging.wrapper.Helper;
import org.antsoftware.messaging.wrapper.JMSBroker;
import org.antsoftware.messaging.wrapper.JMSSender;

public class Send {

	JMSBroker broker;
	JMSSender sender;

	public void init(Properties properties) throws Exception {
		broker = (JMSBroker) Helper.newInstance(properties.getProperty("brokerClassName")); 
		broker.init(properties);

		sender = new AbstractGenericSender(broker) {
			@Override
			public void sendMessage(Queue queue, Message message, int deliveryMode, int priority, long timeToLive) throws Exception {
				MessageProducer producer = getSession().createProducer(queue);
				System.out.println("INFO: Sending message ("+ ((TextMessage)message).getText() +") to " + queue + " (" + producer.getDestination().toString() + ")");
				producer.send(message);
				System.out.println("INFO: Message sent");
				producer.close();
			}
		};
		
		System.out.println("INFO: Done");
	}


	public static void main(String[] args) {
		System.out.println("INFO: Type Ctrl+C to exit the program");
		try {
			new Send().init(Helper.getProperties(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
