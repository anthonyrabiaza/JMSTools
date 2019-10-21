package org.antsoftware.messaging.wrapper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Main {
	
	public static void main(String[] args) {
		Map<String, Class<?>> options = new HashMap<String, Class<?>>();
		options.put("receiveandreply", 			org.antsoftware.messaging.wrapper.scenarios.ReceiveAndReply.class);
		options.put("receiveandforwardtohttp", 	org.antsoftware.messaging.wrapper.scenarios.ReceiveAndForwardToHTTP.class);
		options.put("sendandwait", 				org.antsoftware.messaging.wrapper.scenarios.SendAndWait.class);
		options.put("send", 					org.antsoftware.messaging.wrapper.scenarios.Send.class);
		
		if(args.length < 2) {
			System.out.println("Usage: java -jar jmstools<version>.jar <scenario> <propertiesFile>");
			System.out.print("\t where scenario = ");
			boolean first = true;
			for (Iterator<String> iterator = options.keySet().iterator(); iterator.hasNext();) {
				String type = iterator.next();
				if(!first) {
					System.out.print(" or ");
				}
				System.out.print(type);
				first = false;
			}
			System.out.println();
			System.out.print("\t You can add -Dbenchmark=true for receiveandreply to use a progress bar");
		} else {
			try {
				Class<?> scenario = options.get(args[0]);
				if(scenario==null) {
					System.err.println("Scenario not found: " + args[0]);
				} else {
					System.out.println("Use Scenario : " + scenario);
				}
				Method mainMethod = scenario.getMethod("main", String[].class);
				String[] params = new String[] {args[1]};
				mainMethod.invoke(null, (Object) params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
