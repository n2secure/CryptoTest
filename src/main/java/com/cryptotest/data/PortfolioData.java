package com.cryptotest.data;

import java.util.Date;

public class PortfolioData {
    String securityId;
    Security security;
    PriceData priceData;
    double price;
    Date updateTime;
    double value;
    double quantity;

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }
    public void setSecurity(Security security) {
        this.security = security;
    }
    public void setPriceData(PriceData priceData) {
        this.priceData = priceData;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    public String getSecurityId() {
        return securityId;
    }
    public Security getSecurity() {
        return security;
    }
    public PriceData getPriceData() {
        return priceData;
    }
    public double getPrice() {
        return price;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public double getValue() {
        return value;
    }
    public double getQuantity() {
        return quantity;
    }
}
