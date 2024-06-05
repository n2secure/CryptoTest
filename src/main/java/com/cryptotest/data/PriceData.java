package com.cryptotest.data;

import java.util.Date;

public class PriceData {
    public enum CURRENCY {USD,EUR,GBP};
    public String securityId;
    public double  price;    
    public CURRENCY currency;
    public Date date;
    public PriceData(String securityId,double  price,CURRENCY currency){
        this.securityId = securityId;
        this.price = price;
        this.currency =  currency;

    }
}
