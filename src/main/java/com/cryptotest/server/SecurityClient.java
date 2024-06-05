package com.cryptotest.server;

import com.cryptotest.service.RequestService;

public class SecurityClient {

    public static void main(String[] args){

        RequestService reqService = new RequestService("security.request") ;

        reqService.sendRequestResponse("security1: hello world");    
    }

}
