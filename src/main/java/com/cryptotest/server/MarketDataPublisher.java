package com.cryptotest.server;

import com.cryptotest.service.PubService;

public class MarketDataPublisher {

    public static void main(String[] args){

        PubService pubService = new PubService("marketdata.topic") ;
        pubService.pubMessage("marketdata: data 1");
    }

}
