package com.cryptotest.data;

public class PriceData {
    public enum CURRENCY {USD,EUR,GBP};
    public String ticker;
    public double  price;    
    public CURRENCY currency;
    public PriceData(String ticker,double  price,CURRENCY currency){
        this.ticker = ticker;
        this.price = price;
        this.currency =  currency;

    }
}
