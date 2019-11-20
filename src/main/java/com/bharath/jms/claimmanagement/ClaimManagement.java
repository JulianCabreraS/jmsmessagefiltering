package com.bharath.jms.claimmanagement;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClaimManagement {
    public static void main(String[] args) throws NamingException, JMSException {
        InitialContext context =new InitialContext();
        Queue requestQueue = (Queue) context.lookup("queue/claimQueue");

        try(
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
                JMSContext jmsContext = connectionFactory.createContext()) {
            //Producer
            JMSProducer producer = jmsContext.createProducer();
            //Consumer
            JMSConsumer consumer = jmsContext.createConsumer(requestQueue,"claimAmount BETWEEN 1000 AND 5000");
            //Object message
            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            objectMessage.setIntProperty("hospitalId",1);
            objectMessage.setIntProperty("claimAmount",1000);
            Claim claim = new Claim(1,
                    "John",
                    "gyna",
                    "blue cross",
                    1000
                    );
            objectMessage.setObject(claim);
            producer.send(requestQueue,objectMessage);

            Claim receiveBody = consumer.receiveBody(Claim.class);
            System.out.println(receiveBody.getClaimAmount());

        }

    }
}
