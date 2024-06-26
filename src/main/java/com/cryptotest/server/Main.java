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
import java.util.concurrent.ConcurrentHashMap;

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
    private Map<String,PortfolioData> portfolioList = new ConcurrentHashMap<>();
    private List<String[]> csvList = new ArrayList<>();

    private static volatile SecurityClient securityClient;
    private MarketDataClient marketDataClient = new MarketDataClient();
    private PriceCalculator priceCalculator = new PriceCalculator();
    private SecurityServer mockSecurityServer = new SecurityServer();

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

   private Security mockSecurity(String securityId){
    logger.info("mockSecurity() : securityId="+securityId);
    return mockSecurityServer.getSecurity(securityId);
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
                sec = mockSecurity(securityId);
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

   public void printPortfolio() {

        System.out.println("Market Data Update");
        System.out.println("symbol\t\tprice\t\tqty\t\tvalue");

        for (Map.Entry<String,PortfolioData> ent : portfolioList.entrySet()){
            PortfolioData pData = ent.getValue();
            System.out.printf("%s\t\t%f\t%f\t%f\n",
                pData.getSecurityId(),
                pData.getPrice(),
                pData.getQuantity(),
                pData.getValue());
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
                this.printPortfolio();            }     
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
