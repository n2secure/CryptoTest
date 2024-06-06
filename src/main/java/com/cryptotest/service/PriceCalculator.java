package com.cryptotest.service;

import java.util.function.Function;

import org.apache.log4j.Logger;

import com.cryptotest.data.MarketData;
import com.cryptotest.data.PortfolioData;
import com.cryptotest.data.PriceData;
import com.cryptotest.data.Security;
import com.cryptotest.service.messaging.ResponseService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PriceCalculator  {
    private static final Logger logger = Logger.getLogger(PriceCalculator.class) ;
    private final double RISKLESS_RATE = .02;
    private final double VOLATILITY = .332  ;

    public double priceCall(double stock,double strike,double d1,double d2,double timeToExp){
        return      (stock * d1)
        -   (strike * Math.exp(-RISKLESS_RATE * timeToExp)
        *   d2);
    }

    public double pricePut(double stock,double strike,double d1,double d2,double timeToExp){
        return  strike * Math.exp(-RISKLESS_RATE * timeToExp)
        * (-d2)
        - stock * (-d1);
    }

    public void recalcPrice(PortfolioData portData,PriceData priceData) {
        Gson gson = new Gson();

        Security sec = portData.getSecurity();
        if (sec.getSecurityType() == Security.SecurityType.OPTION){
            double stock = priceData.getPrice();
            if (sec.getUnderlyingId().equals(priceData.getSecurityId())){
                logger.info("recalc option price : sec="+sec.getSecurityId()+",priceData="+gson.toJson(priceData));

                double strike         = sec.getStrikePrice();
                Date expDate = sec.getExpirationDate();
                Date curDate = new Date();
                long diff = expDate.getTime() - curDate.getTime();
                long daysToExp = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                double timeToExp = daysToExp/365.0;

                double d1 	= (Math.log(stock / strike) 
            	+ (RISKLESS_RATE + (Math.pow(VOLATILITY, 2) / 2)) 
	            * timeToExp)
	            / (VOLATILITY * Math.sqrt(timeToExp));


                double d2 	= d1 - (VOLATILITY * Math.sqrt(timeToExp));    
                SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
                logger.info("recalcPrice() : sec="+sec.getSecurityId()+" : strike="+strike+
                    " ,expDate="+df.format(expDate)+ " , daysToExp="+ daysToExp+" ,d1 ="+d1+" ,d2="+d2);

                if (sec.getOptionType() == Security.OptionType.CALL){
                    double newPrice = priceCall(stock,strike,d1,d2,timeToExp); 
                    logger.info("recalcPrice() : sec="+sec.getSecurityId()+" : update option CALL - new price="+newPrice);
                    portData.setPrice(newPrice);
                    portData.setValue(newPrice*portData.getQuantity());
                } else {
                    double newPrice = pricePut(stock,strike,d1,d2,timeToExp); 
                    logger.info("recalcPrice() : sec="+sec.getSecurityId()+" : update option PUT - new price="+newPrice);
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
    
