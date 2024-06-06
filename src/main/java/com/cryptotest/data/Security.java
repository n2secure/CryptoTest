package com.cryptotest.data;

import java.util.Date;

public class Security {
    public enum SecurityType {EQUITY,OPTION};
    public enum OptionType {CALL,PUT};
    String securityId;    
    String underlyingId;    
    SecurityType securityType;
    OptionType optionType;
    String expirationDate; 
    double strikePrice;
    String name;

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }
    public void setUnderlyingId(String underlyingId) {
        this.underlyingId = underlyingId;
    }
    public void setSecurityType(SecurityType securityType) {
        this.securityType = securityType;
    }
    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSecurityId() {
        return securityId;
    }
    public String getUnderlyingId() {
        return underlyingId;
    }
    public SecurityType getSecurityType() {
        return securityType;
    }
    public OptionType getOptionType() {
        return optionType;
    }
    public String getExpirationDate() {
        return expirationDate;
    }
    public double getStrikePrice() {
        return strikePrice;
    }
    public String getName() {
        return name;
    }
}
