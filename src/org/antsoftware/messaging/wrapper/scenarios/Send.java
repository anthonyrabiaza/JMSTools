package org.antsoftware.messaging.wrapper.scenarios;

import java.util.Date;
import java.util.Properties;

import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;

import org.antsoftware.messaging.wrapper.AbstractGenericSender;
import org.antsoftware.messaging.wrapper.impl.GenericJNDI;
import org.antsoftware.messaging.wrapper.interfaces.JMSBroker;
import org.antsoftware.messaging.wrapper.interfaces.JMSSender;
import org.antsoftware.messaging.wrapper.tools.Helper;

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
		
		System.out.println("INFO: Ready!");

		TextMessage newMessage = sender.getSession().createTextMessage("<message>Bonjour " + new Date() + "</message>");
		Queue queue;
		if(broker instanceof GenericJNDI) {
			queue = (Queue)((GenericJNDI)broker).getInitialContext().lookup(broker.getProperties().getProperty("receiverDestination"));
		} else {
			queue = sender.getSession().createQueue(broker.getProperties().getProperty("receiverDestination"));
		}
		sender.sendMessage(queue, newMessage);
		String msgId = newMessage.getJMSMessageID();

		System.out.println("INFO: ID of Message sent: " + msgId);
		
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
