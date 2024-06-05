package com.cryptotest.service;


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
import java.util.UUID;

public class RequestService {

    Logger logger = Logger.getLogger(ResponseService.class) ;
    static MessageServiceHelper helper = new MessageServiceHelper();

    Connection connection;
    Session session;
    Destination dest;
    String topic;
    MessageConsumer consumer = null;
    MessageProducer producer = null;
    private static final int TIMEOUT = 5000;

    public RequestService(String topic){
        this.topic = topic;
    }
    public Message sendRequestResponse(String strMessage){
        Connection connection = null;
        try {
            logger.info("RequestService: createConnection()");
            connection = this.helper.createConnection();
            connection.start();
            logger.info("RequestService: createSession()");
            session = this.helper.createSession();

            String correlationId = UUID.randomUUID().toString();
            dest = this.helper.getQueue(session, topic);
            logger.info("RequestService: dest="+dest+" ,correlationId="+correlationId);

            Destination replyQueue = this.helper.getQueue(session,topic+".reply");
    
            consumer = session.createConsumer( replyQueue, "JMSCorrelationID = '" + correlationId + "'" );
            final TextMessage textMessage = session.createTextMessage( strMessage );

            textMessage.setJMSCorrelationID( correlationId );
            textMessage.setJMSReplyTo( replyQueue );

            logger.info("RequestService: createProducer( dest )");
            producer = session.createProducer( dest );
            logger.info("RequestService: send() : textMessage="+textMessage);
            producer.send( dest, textMessage );
            // Block on receiving the response with a timeout
            logger.info("RequestService: consumer.receive()");
            return consumer.receive( TIMEOUT );

        } catch (JMSException e) {
            logger.error("exception caught : ",e);
        }
        return null;
    }
    public void sendRequest(String strMessage){
        Connection connection = null;
        try {
            logger.info("RequestService: createConnection()");
            connection = this.helper.createConnection();
            connection.start();
            logger.info("RequestService: createSession()");
            session = this.helper.createSession();

            dest = this.helper.getQueue(session, topic);
            logger.info("RequestService: dest="+dest);

            final TextMessage textMessage = session.createTextMessage( strMessage );

            logger.info("RequestService: createProducer( dest )");
            producer = session.createProducer( dest );
            logger.info("RequestService: send() : textMessage="+textMessage);
            producer.send( dest, textMessage );
            // Block on receiving the response with a timeout

        } catch (JMSException e) {
            logger.error("exception caught : ",e);
        }
    }
}
