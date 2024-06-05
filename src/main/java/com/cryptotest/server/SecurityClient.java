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

    public static void main(String[] args){

        RequestService reqService = new RequestService(SecurityServer.SECURITY_TOPIC) ;

        SecurityRequest request = new SecurityRequest();
        request.securityId = "AAPL-OCT-2024-110-P";
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
            Security sec = gson.fromJson(strMessage, Security.class);
            logger.info("got security : using gson "+gson.toJson(sec));
        }
   
    }

}
