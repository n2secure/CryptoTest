package com.cryptotest.server;

import com.cryptotest.service.messaging.ResponseService;
import com.cryptotest.service.messaging.SubscribeService;
import com.google.gson.Gson;

import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.function.Function;
import com.cryptotest.data.MarketData;
import com.cryptotest.data.PriceData;
import com.cryptotest.data.Security;

import org.apache.camel.component.jms.MessageListenerContainerFactory;
import org.apache.log4j.Logger;

public class MarketDataClient implements MessageListener {

    private final static Logger logger = Logger.getLogger(MarketDataClient.class) ;
    ResponseService subcribeService =  new ResponseService(MarketDataPublisher.MARKET_DATA_TOPIC);       
 
    public void startService(){
        subcribeService.startService(this);    
    }
    public void startService(MessageListener listener){
        subcribeService.startService(listener);    
    }

    @Override
    public void onMessage(Message message) {

      logger.info("subcribeService: onMessage() : +message");
      try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                logger.info("Received message"
                        + textMessage.getText() + "'");
                Gson gson = new Gson();
                PriceData pData = gson.fromJson(textMessage.getText(), PriceData.class);
                logger.info("got market Data : usin gson "+gson.toJson(pData));
            }     
        } catch (Exception e) {
            logger.error("exception caught : ",e);
        } finally {
        }

    } 
    public static void main(String[] args){

        MarketDataClient client = new MarketDataClient();
        client.startService();
        // SubService subService = new SubcribeService("marketdata.topic") ;

    }
}

