package com.cryptotest.server;

import com.cryptotest.service.messaging.ResponseService;
import com.cryptotest.service.messaging.SubService;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.function.Function;
import com.cryptotest.data.MarketData;
import com.cryptotest.data.PriceData;
import com.cryptotest.data.Security;
import org.apache.log4j.Logger;

public class MarketDataClient implements MessageListener {

    Logger logger = Logger.getLogger(MarketDataClient.class) ;

    // Function<MarketData,PriceData> calcPrice = (mData)=>{

    // }

    // Function<MarketData,PriceData> fp;
    // MarketDataClient(Function<MarketData,PriceData> fp){
    //     this.fp = fp;
    // }
    @Override
    public void onMessage(Message message) {

      logger.info("SubService: onMessage() : +message");
      try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                logger.info("Received message"
                        + textMessage.getText() + "'");
            }     
        } catch (Exception e) {
            logger.error("exception caught : ",e);
        } finally {
        }

    } 
    public static void main(String[] args){

        MarketDataClient client = new MarketDataClient();
        ResponseService subService =  new ResponseService(MarketDataPublisher.MARKET_DATA_TOPIC);       
        // SubService subService = new SubService("marketdata.topic") ;

        subService.startService(client);    
    }
}

