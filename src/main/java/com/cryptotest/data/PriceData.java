package com.cryptotest.data;

import java.util.Date;

public class PriceData {
    public enum CURRENCY {USD,EUR,GBP};
    String securityId;
    double  price;    
    CURRENCY currency;
    Date date;

    public String getSecurityId() {
        return securityId;
    }
    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setCurrency(CURRENCY currency) {
        this.currency = currency;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public double getPrice() {
        return price;
    }
    public CURRENCY getCurrency() {
        return currency;
    }
    public Date getDate() {
        return date;
    }
    public PriceData(String securityId,double  price,CURRENCY currency){
        this.securityId = securityId;
        this.price = price;
        this.currency =  currency;

    }
}
