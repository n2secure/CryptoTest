package com.cryptotest.data;

import java.util.Date;

public class MarketData {
    enum MarketDataType {UNDERLYING,EQUITY};
    String ticker;
    String securityId;
    Date    modifiedDate;
    MarketDataType type;
    double  price;    
}
