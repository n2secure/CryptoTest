package com.cryptotest.server;

import com.cryptotest.service.messaging.ResponseService;

public class SecurityServer {

    final public static String SECURITY_TOPIC = "security.request";

    public static void main(String[] args){

        ResponseService resService = new ResponseService(SECURITY_TOPIC) ;

        resService.startService();    
    }

}
