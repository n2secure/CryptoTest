package com.cryptotest.service;

import java.util.function.Function;

import org.apache.log4j.Logger;

import com.cryptotest.data.MarketData;
import com.cryptotest.data.PortfolioData;
import com.cryptotest.data.PriceData;
import com.cryptotest.data.Security;
import com.cryptotest.service.messaging.ResponseService;
import com.google.gson.Gson;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;

public class PriceCalculator  {
    private static final Logger logger = Logger.getLogger(PriceCalculator.class) ;
    private final double RISKLESS_RATE = .02;
    private final double VOLATILITY = .332  ;

    public double priceCall(double stock,double strike,double d1,double d2,long timeToExp){
        return      (stock * d1)
        -   (strike * Math.exp(-RISKLESS_RATE * timeToExp)
        *   d2);
    }

    public double pricePut(double stock,double strike,double d1,double d2,long timeToExp){
        return  strike * Math.exp(-RISKLESS_RATE * timeToExp)
        * (-d2)
        - stock * (-d1);
    }

    public void recalcPrice(PortfolioData portData,PriceData priceData) {
        Gson gson = new Gson();

        Security sec = portData.getSecurity();
        logger.info("recalcPrice() : sec="+sec.getSecurityId()+",priceData="+gson.toJson(priceData));
        if (sec.getSecurityType() == Security.SecurityType.OPTION){
            double stock = priceData.getPrice();
            if (sec.getUnderlyingId().equals(priceData.getSecurityId())){

                double strike         = sec.getStrikePrice();
                LocalDate expDate = LocalDate.parse(sec.getExpirationDate());
                LocalDate curDate = LocalDate.now();
                Duration daysBetween = Duration.between(expDate,curDate);
                long timeToExp = daysBetween.toDays()/365;      

                double d1 	= (Math.log(stock / strike) 
            	+ (RISKLESS_RATE + (Math.pow(VOLATILITY, 2) / 2)) 
	            * timeToExp)
	            / (VOLATILITY * Math.sqrt(timeToExp));

                double d2 	= d1 - (VOLATILITY * Math.sqrt(timeToExp));    

                if (sec.getOptionType() == Security.OptionType.CALL){
                    logger.info("recalcPrice() : sec="+sec.getSecurityId()+" : update option CALL");
                    double newPrice = priceCall(stock,strike,d1,d2,timeToExp); 
                    portData.setPrice(newPrice);
                    portData.setValue(newPrice*portData.getQuantity());
                } else {
                    logger.info("recalcPrice() : sec="+sec.getSecurityId()+" : update option PUT");
                    double newPrice = pricePut(stock,strike,d1,d2,timeToExp); 
                    portData.setPrice(newPrice);
                    portData.setValue(newPrice*portData.getQuantity());
                } 
            }
        } else {
            if (sec.getSecurityId().equals(priceData.getSecurityId())){
                logger.info("recalcPrice() : sec="+sec.getSecurityId()+" : update equity Price");
                double newPrice = priceData.getPrice(); 
                portData.setPrice(newPrice);
                portData.setValue(newPrice*portData.getQuantity());
            }
        }
   }
}
    
