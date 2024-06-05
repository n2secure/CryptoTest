package com.cryptotest.service;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;


import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageServiceHelper {
    Connection connection;
    public static ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

    public static ActiveMQConnectionFactory getConnectionFactory(){
        return connectionFactory;
    }
    public Connection createConnection() throws JMSException{
        this.connection = connectionFactory.createConnection();
        // this.connection.start();
        return this.connection;

    }
    public Session createSession() throws JMSException {
        // this.connection = connectionFactory.createConnection();
        // this.connection.start();

        // Create a Session
        Session session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        return session;

    } 
    public Destination getQueue(Session session , String queueName) throws JMSException{
        Destination destination = session.createQueue(queueName);
        return destination;
    }
    public Topic getTopic(Session session , String topic) throws JMSException{
        return session.createTopic(topic);
    }
    public Connection getConnection(){
        return this.connection;
    }
}
