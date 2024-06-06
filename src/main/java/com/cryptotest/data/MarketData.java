package com.cryptotest.data;

import java.util.Date;

public class MarketData {
    enum MarketDataType {UNDERLYING,EQUITY};
    String ticker;
    String securityId;
    Date    modifiedDate;
    MarketDataType type;
    double  price;    

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    public void setType(MarketDataType type) {
        this.type = type;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getTicker() {
        return ticker;
    }
    public String getSecurityId() {
        return securityId;
    }
    public Date getModifiedDate() {
        return modifiedDate;
    }
    public MarketDataType getType() {
        return type;
    }
    public double getPrice() {
        return price;
    }
}
