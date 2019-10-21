package org.antsoftware.messaging.wrapper.scenarios;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;

import org.antsoftware.messaging.wrapper.AbstractGenericReceiver;
import org.antsoftware.messaging.wrapper.AbstractGenericSender;
import org.antsoftware.messaging.wrapper.Helper;
import org.antsoftware.messaging.wrapper.JMSBroker;
import org.antsoftware.messaging.wrapper.JMSSender;
import org.antsoftware.messaging.wrapper.tools.BenchmarkingTools;

public class ReceiveAndReply extends AbstractGenericReceiver {

	JMSBroker broker;
	JMSSender sender;
	BenchmarkingTools benchmarkingTools;
	
	boolean logMessage = false;
	

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			String requestMessage 	= textMessage.getText();

			if(!benchmarkingTools.isEnabled()) {
				System.out.println("INFO: Got a message" + ((logMessage)?("(" + requestMessage + ")"):"") + ", destination: " + textMessage.getJMSDestination() + " at " + new Date());
				System.out.println("INFO: Replying ...");
			} else {
				benchmarkingTools.newTrigger();
			}

			String responseMessage = "<ack name=\"" + this.getClass() + "\">" + requestMessage + "</ack>";

			TextMessage newMessage = sender.getSession().createTextMessage(responseMessage);
			String jmsCorrelationID = message.getJMSCorrelationID();
			if(jmsCorrelationID==null || "".equals(jmsCorrelationID)) {
				jmsCorrelationID = message.getJMSMessageID();
			}
			newMessage.setJMSCorrelationID(jmsCorrelationID);

			sender.sendMessage(sender.getSession().createQueue(getProperties().getProperty("replyDestination")), newMessage);
			message.acknowledge();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(Properties properties) throws Exception {
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		broker = (JMSBroker) Helper.newInstance(properties.getProperty("brokerClassName")); 
		broker.init(properties);
		
		String logMessageProperty = properties.getProperty("logMessage");
		if(logMessageProperty!=null) {
			logMessage = Boolean.valueOf(logMessageProperty);
		}
		
		benchmarkingTools = new BenchmarkingTools();

		super.init(broker);

		sender = new AbstractGenericSender(broker) {
			@Override
			public void sendMessage(Queue queue, Message message, int deliveryMode, int priority, long timeToLive) throws Exception {
				MessageProducer producer = getSession().createProducer(queue);
				producer.send(message);
				if(logMessage) {
					System.out.println("INFO: Message sent to " + queue + " (" + producer.getDestination().toString() + ")");
				}
				producer.close();
			}
		};
		
		System.out.println("INFO: Ready!");
		
		latch.await();
	}

	@Override
	public Properties getProperties() {
		return broker.getProperties();
	}
	
	public static void main(String[] args) {
		System.out.println("INFO: Type Ctrl+C to exit the program");
		try {
			new ReceiveAndReply().init(Helper.getProperties(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
