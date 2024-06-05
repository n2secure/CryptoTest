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


public class ResponseService implements MessageListener {

    Logger logger = Logger.getLogger(ResponseService.class) ;
    static MessageServiceHelper helper = new MessageServiceHelper();

    Connection connection;
    Session session;
    Destination dest;
    String topic;

    public  ResponseService(String topic){
        this.topic = topic;
    }
    public void startSerice(){
        logger.info("ResponseService: startSerice()");
        Connection connection = null;
        try {
            connection = this.helper.createConnection();
            logger.info("ResponseService: connection.start()");
            connection.start();
            logger.info("ResponseService: createSession()");
            session = this.helper.createSession();
            dest = this.helper.getQueue(session, topic);

            MessageConsumer consumer = session.createConsumer(dest);
            logger.info("ResponseService: setMessageListener(this)");
            consumer.setMessageListener(this);

        } catch (JMSException e) {
            logger.error("exception caught : ",e);
        }
    
    }
    @Override
    public void onMessage(Message message) {

      MessageProducer producer = null;
        try {
            // Destination replyDest = message.getJMSReplyTo();
            logger.info("ResponseService: onMessage() ,"+
                " ,message="+message.toString());
            // if (replyDest != null) {
            //     Message response = this.session.createTextMessage("Response");
            //     response.setStringProperty("ServedBy", "test");
            //     logger.info("ResponseService: onMessage() ,response="+response);
            //     producer = session.createProducer(replyDest);
            //     logger.info("ResponseService: onMessage() producer.send() : response="+response);
            //     producer.send(response);
            // }
        } catch (Exception e) {
            logger.error("exception caught : ",e);
        } finally {
            // if (producer != null) {
            //     try {
            //         producer.close();
            //     } catch (Exception e) {
            //     }
            // }
        }

    } 
}