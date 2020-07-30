package org.antsoftware.messaging.wrapper.scenarios;

import java.util.Date;
import java.util.Properties;

import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;

import org.antsoftware.messaging.wrapper.AbstractGenericGetter;
import org.antsoftware.messaging.wrapper.AbstractGenericSender;
import org.antsoftware.messaging.wrapper.impl.GenericJNDI;
import org.antsoftware.messaging.wrapper.interfaces.JMSBroker;
import org.antsoftware.messaging.wrapper.interfaces.JMSGetter;
import org.antsoftware.messaging.wrapper.interfaces.JMSSender;
import org.antsoftware.messaging.wrapper.tools.Helper;

public class SendAndWait {

	JMSBroker broker;
	JMSSender sender;
	JMSGetter getter;

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
		System.out.println("INFO: Waiting for reply...");

		TextMessage replyMessage = (TextMessage) getter.getMessage(
				broker.getProperties().getProperty("replyDestination"),
				"JMSCorrelationID='" + msgId + "'"
		);
		
		System.out.println("INFO: ID of Message received: " + replyMessage.getJMSMessageID());
	}


	public static void main(String[] args) {
		System.out.println("INFO: Type Ctrl+C to exit the program");
		try {
			new SendAndWait().init(Helper.getProperties(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
