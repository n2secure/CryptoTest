package com.cryptotest.data;

import java.util.Date;

public class Security {
    public enum SecurityType {EQUITY,OPTION};
    public enum OptionType {CALL,PUT};
    public String securityId;    
    public String underlyingId;    
    public SecurityType securityType;
    public OptionType optionType;
    public String expirationDate; 
    public double strikePrice;
    public String name;
}
