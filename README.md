# JMSTools

I wanted to share a solution I recently developed to connect to JMS Providers. JMSTools will allow you to easily create JMS clients (Mock) to receive messages and reply OR to send and wait for a reply.

You can easily use JMSTools as a backend consumer in Message-oriented middleware scenario:
- JMeter will be used to send message to JMS compliant Broker (Solace PubSub+ for instance)
- JMSTools to receive and reply to the messages


![Alt text](resources/SolaceAndJMSTools.png?raw=true "JMSTools")

!!! All the CSV files need to be comma separated. !!!

## Getting Started

Please download the [latest library](jmstools-0.1.jar?raw=true) and the property files [solace](src/solace.properties?raw=true) and [activemq](src/activemq.properties?raw=true).