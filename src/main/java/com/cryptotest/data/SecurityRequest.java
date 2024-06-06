package com.cryptotest.data;

public class SecurityRequest {
    private String securityId;    
    public SecurityRequest(String securityId){
        this.securityId = securityId;
    }
    public String getSecurityId() {
        return securityId;
    }
    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }
}
