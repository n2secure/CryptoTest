package com.cryptotest.service.messaging;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import java.util.UUID;

public class PublishService {

    Logger logger = Logger.getLogger(PublishService.class) ;
    static MessageServiceHelper helper = new MessageServiceHelper();

    Connection connection;
    Session session;
    Topic dest;
    String topic;
    MessageConsumer consumer = null;
    MessageProducer producer = null;
    private static final int TIMEOUT = 5000;

    public PublishService(String topic){
        this.topic = topic;
    }
    public Message pubMessage(String strMessage){
        Connection connection = null;
        try {
            logger.info("PubService: createConnection()");
            connection = this.helper.createConnection();
            connection.start();
            logger.info("PubService: createSession()");
            session = this.helper.createSession();

            dest = this.helper.getTopic(session, topic);

            logger.info("PubService: dest="+dest);

            final TextMessage textMessage = session.createTextMessage( strMessage );

            producer = session.createProducer( dest );
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            logger.info("PubService: send() : textMessage="+textMessage);
            producer.send(textMessage );

        } catch (JMSException e) {
            logger.error("exception caught : ",e);
        }
        return null;
    }
}
