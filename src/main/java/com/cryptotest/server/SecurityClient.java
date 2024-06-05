package com.cryptotest.server;

import com.cryptotest.service.messaging.RequestService;

public class SecurityClient {

    public static void main(String[] args){

        RequestService reqService = new RequestService(SecurityServer.SECURITY_TOPIC) ;

        reqService.sendRequestResponse("security1: hello world");    
    }

}
