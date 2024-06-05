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


public class SubService  {

    Logger logger = Logger.getLogger(SubService.class) ;
    static MessageServiceHelper helper = new MessageServiceHelper();

    Connection connection;
    Session session;
    Destination dest;
    String topic;

    public  SubService(String topic){
        this.topic = topic;
    }
    public void startService(MessageListener listener){
        logger.info("SubService: startSerice()");
        Connection connection = null;
        try {
            connection = this.helper.createConnection();
            logger.info("SubService: connection.start()");
            connection.start();
            logger.info("SubService: createSession()");
            session = this.helper.createSession();
            dest = this.helper.getTopic(session, topic);
            logger.info("SubService: tolpic="+dest);

            MessageConsumer consumer = session.createConsumer(dest);
            logger.info("SubService: setMessageListener(this)");
            consumer.setMessageListener(listener);

        } catch (JMSException e) {
            logger.error("exception caught : ",e);
        }
    
    }
//     @Override
//     public void onMessage(Message message) {

//       logger.info("SubService: onMessage() : +message");
//       try {
//             if (message instanceof TextMessage) {
//                 TextMessage textMessage = (TextMessage) message;
//                 logger.info("Received message"
//                         + textMessage.getText() + "'");
//             }     
//         } catch (Exception e) {
//             logger.error("exception caught : ",e);
//         } finally {
//         }

//     } 
// }
}