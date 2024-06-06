package com.cryptotest.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.cryptotest.data.PortfolioData;
import com.cryptotest.data.PriceData;
import com.cryptotest.data.SecurityRequest;
import com.cryptotest.service.PriceCalculator;
import com.google.gson.Gson;
import com.cryptotest.data.Security;

public class Main implements MessageListener{

    private final static Logger logger = Logger.getLogger(Main.class) ;
    public final static String COMMA_DELIMITER = ",";
    private Map<String,PortfolioData> portfolioList = new HashMap<>();
    private List<String[]> csvList = new ArrayList<>();

    private static volatile SecurityClient securityClient;
    MarketDataClient marketDataClient = new MarketDataClient();
    PriceCalculator priceCalculator = new PriceCalculator();

    public static SecurityClient getSecurityClient(){
        if (securityClient == null){
            securityClient = new SecurityClient();
        }
        return securityClient;
    } 


    public void loadCSVfile(String strPath) {
        logger.info("loadCSVfile() : path="+strPath);
        try (BufferedReader br = new BufferedReader(new FileReader(strPath))) {
            String line;
             while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                logger.info("read record : "+Arrays.toString(values));
                csvList.add(values);
            }
        } catch (FileNotFoundException e) {
            logger.error("exception caught : ",e);
        } catch (IOException e) {
            logger.error("exception caught : ",e);
        }
   }
   public void enrichSecurityData() {
    logger.info("enrichSecurityData()");
    String securityId = null;
        double qty = 0.0;
        for (String[] sVar : csvList){
            securityId = sVar[0];
            try {
                qty = Double.valueOf(sVar[1]);
            } catch (Exception ex){
                logger.error("exception caught : ",ex);
                qty = 0.0;
            }
            PortfolioData pData = new PortfolioData();
            pData.setQuantity(qty);
            pData.setSecurityId(securityId);
            SecurityRequest request = new SecurityRequest(securityId);

            Security sec = getSecurityClient().getSecurity(request);
            if (sec == null){
                sec = new Security();
                sec.setSecurityId(securityId);
            }
            pData.setSecurity(sec);
            portfolioList.put(sec.getSecurityId(),pData);
        }

    }

    public void recalcPrice(PriceData priceData) {
        Gson gson = new Gson();
        logger.info("recalcPrice() : pData="+gson.toJson(priceData));

        for (Map.Entry<String,PortfolioData> ent : portfolioList.entrySet()){
            priceCalculator.recalcPrice(ent.getValue(), priceData);
        }
   }

    @Override
    public void onMessage(Message message) {

      logger.info("MarketDataClient: onMessage() : +message");
      try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                logger.info("MarketData Received message"+ textMessage.getText() + "'");
                Gson gson = new Gson();
                PriceData priceData = gson.fromJson(textMessage.getText(), PriceData.class);
                logger.info("got market Data : usin gson "+gson.toJson(priceData));
                this.recalcPrice(priceData);
            }     
        } catch (Exception e) {
            logger.error("exception caught : ",e);
        } finally {
        }
    } 

    public void startMarketDataSubscriber() {
        logger.info("startMarketDataSubscriber()");
        this.marketDataClient.startService(this);
    }

    public static void main(String[] args) {
        Main mainClass = new Main();
        mainClass.loadCSVfile("portfolio.csv");
        mainClass.enrichSecurityData();
        mainClass.startMarketDataSubscriber();
   }
}
