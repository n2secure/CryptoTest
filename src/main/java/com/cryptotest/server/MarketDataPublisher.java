package com.cryptotest.server;

import com.cryptotest.service.PubService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.cryptotest.data.PriceData;
import java.util.Random;
import org.apache.log4j.Logger;

public class MarketDataPublisher {

    Logger logger = Logger.getLogger(MarketDataPublisher.class) ;

    PubService pubService = new PubService("marketdata.topic") ;

    PriceData[] priceList = {
            new PriceData("AAPL",194.35,PriceData.CURRENCY.USD) ,
            new PriceData("TSLA",174.77,PriceData.CURRENCY.USD), 
            new PriceData("AMZN",179.34,PriceData.CURRENCY.USD),
            new PriceData("GOOG",175.13,PriceData.CURRENCY.USD) ,
            new PriceData("BMW",92.04,PriceData.CURRENCY.EUR)};

    private volatile boolean keepRunning = true;

    Runnable  marketPriceGenerator = () -> {
        while (keepRunning){
            Random rn = new Random();
            int iVal = rn.nextInt(priceList.length);
            PriceData pData = priceList[iVal]; 
            float percentIncrease = rn.nextInt(5);
            int sign = rn.nextInt(2) == 0 ? 1 : -1 ;
            pData.price += pData.price*(percentIncrease/100)*sign;
            Gson gson = new Gson();
            logger.info("pub market Data "+gson.toJson(pData));
            pubService.pubMessage(gson.toJson(pData));
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) { 
              Thread.currentThread().interrupt(); 
           }
       }
    };
    
    public static void main(String[] args){

        MarketDataPublisher pub = new MarketDataPublisher();
        try {
            Thread thr = new Thread(pub.marketPriceGenerator);
            thr.start();
            thr.join();
        }
         catch (InterruptedException e) { 
           Thread.currentThread().interrupt(); 
        }
        // PubService pubService = new PubService("marketdata.topic");
        // pubService.pubMessage("marketdata: data 1");
    }

}
