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

import org.apache.log4j.Logger;

import com.cryptotest.data.PortfolioData;
import com.cryptotest.data.SecurityRequest;
import com.cryptotest.data.Security;

public class Main {

    private final static Logger logger = Logger.getLogger(Main.class) ;
    public final static String COMMA_DELIMITER = ",";
    private Map<String,PortfolioData> portfolioList = new HashMap<>();
    private List<String[]> csvList = new ArrayList<>();

    private static volatile SecurityClient securityClient;

    public static SecurityClient getSecurityClient(){
        if (securityClient == null){
            securityClient = new SecurityClient();
        }
        return securityClient;
    } 


    public void loadCSVfile(String strPath) {
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
   public void enrichPortfolioData() {
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
            pData.setSecurity(sec);
            portfolioList.put(sec.getSecurityId(),pData);
        }

    }

    public static void main(String[] args) {
        Main mainClass = new Main();
        mainClass.loadCSVfile("portfolio.csv");
        mainClass.enrichPortfolioData();
   }
}
