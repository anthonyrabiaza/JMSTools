package org.antsoftware.messaging.wrapper.scenarios;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.antsoftware.messaging.wrapper.AbstractGenericReceiver;
import org.antsoftware.messaging.wrapper.Helper;
import org.antsoftware.messaging.wrapper.JMSBroker;
import org.antsoftware.messaging.wrapper.tools.BenchmarkingTools;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

public class ReceiveAndForwardToHTTP extends AbstractGenericReceiver {

	private static final String ENCODING = "UTF-8";
	JMSBroker broker;
	Executor httpExecutor;
	BenchmarkingTools benchmarkingTools;

	boolean logMessage = false;

	@Override
	public void onMessage(Message message) {
		String requestMessage = "";
		try {
			TextMessage textMessage = (TextMessage) message;
			requestMessage = textMessage.getText();

			if (!benchmarkingTools.isEnabled()) {
				System.out.println("INFO: Got a message" + ((logMessage) ? ("(" + requestMessage + ")") : "")
						+ ", destination: " + textMessage.getJMSDestination() + " at " + new Date());
				System.out.println("INFO: Forwarding ...");
			} else {
				benchmarkingTools.newTrigger();
			}

			String jmsMessageID = textMessage.getJMSMessageID();
			String jmsCorrelationID = textMessage.getJMSCorrelationID();
			if(jmsCorrelationID == null || "".equals(jmsCorrelationID)) {
				jmsCorrelationID = jmsMessageID;
			}
			
			String appendUri = "?JMSDestination=" + textMessage.getJMSDestination().toString()
					+ "&JMSCorrelationID=" + URLEncoder.encode(jmsCorrelationID, ENCODING)
					+ "&JMSDeliveryMode=" + String.valueOf(textMessage.getJMSDeliveryMode())
					+ "&JMSExpires=" + String.valueOf(textMessage.getJMSExpiration())
					+ "&JMSMessageID=" + URLEncoder.encode(jmsMessageID, ENCODING)
					+ "&JMSPriority=" + String.valueOf(textMessage.getJMSPriority())
					+ "&JMSType=" + textMessage.getJMSType()
					+ "&JMSReplyTo=" + textMessage.getJMSReplyTo() + "";
			
			Response response = httpExecutor.execute(
					Request.Post(getProperties().getProperty("httpURI") + appendUri)
					.addHeader("x-api-key", getProperties().getProperty("httpHeader.x-api-key"))
					.useExpectContinue()
					.bodyString(requestMessage, ContentType.DEFAULT_TEXT)
			);
			
			int statusCode = response.returnResponse().getStatusLine().getStatusCode();
			if(statusCode>=200 && statusCode<=299) {
				if (!benchmarkingTools.isEnabled()) {
					System.out.println("INFO: Message forwarded at " + new Date());
				}
				message.acknowledge();
			} else {
				if (!benchmarkingTools.isEnabled()) {
					System.out.println("ERROR: Message forwarded at " + new Date());
					System.out.println(requestMessage);
					System.out.println("ERROR: End of message:");
				}
			}
			
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
		if (logMessageProperty != null) {
			logMessage = Boolean.valueOf(logMessageProperty);
		}

		benchmarkingTools = new BenchmarkingTools();

		super.init(broker);

		httpExecutor = Executor.newInstance().auth(
				getProperties().getProperty("httpUser"), getProperties().getProperty("httpPassword")
		);

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
			new ReceiveAndForwardToHTTP().init(Helper.getProperties(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
