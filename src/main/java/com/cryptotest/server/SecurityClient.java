package com.cryptotest.server;

import org.apache.log4j.Logger;

import com.cryptotest.data.Security;
import com.cryptotest.data.SecurityRequest;
import com.cryptotest.service.messaging.RequestService;
import com.google.gson.Gson;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class SecurityClient {

    static final Logger logger = Logger.getLogger(SecurityClient.class);

    RequestService reqService = new RequestService(SecurityServer.SECURITY_TOPIC) ;

    public Security getSecurity(SecurityRequest request){
        Security security = null;
        Gson gson = new Gson();
        logger.info("send security request : "+gson.toJson(request));
        Message message = reqService.sendRequestResponse(gson.toJson(request));    
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String strMessage = null;
            try {
                strMessage = textMessage.getText();
            } catch (JMSException e) {
                logger.error("exception caught : ",e);
            }

            logger.info("Received message"+ strMessage + "'");
            security = gson.fromJson(strMessage, Security.class);
            logger.info("got security : using gson "+gson.toJson(security));
        }
        return security;
    }
    public static void main(String[] args){

        SecurityClient client = new SecurityClient();

        SecurityRequest request = new SecurityRequest("AAPL-OCT-2024-110-P");

        Security sec = client.getSecurity(request);
        Gson gson = new Gson();
        logger.info("got security : using gson "+gson.toJson(sec));
   
    }

}
