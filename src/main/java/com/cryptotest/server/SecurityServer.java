package com.cryptotest.server;

import com.cryptotest.service.ResponseService;

public class SecurityServer {

    public static void main(String[] args){

        ResponseService resService = new ResponseService("security.request") ;

        resService.startSerice();    
    }

}
