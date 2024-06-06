package com.cryptotest.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.cryptotest.data.PriceData;
import com.cryptotest.data.Security;
import com.cryptotest.data.SecurityRequest;
import com.cryptotest.service.messaging.ResponseService;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

public class SecurityServer implements MessageListener{

    final public static String SECURITY_TOPIC = "security.request";
    static final Logger logger = Logger.getLogger(SecurityServer.class);
    Connection connection;   

    ResponseService responseService;

    public SecurityServer(){
         init();   
    }

    public ResponseService getResponseService(){
        return responseService;
   }
   public void setResponseService(ResponseService serv){
        this.responseService = serv;
   }

   public void init(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            logger.error("exception caught : ",e);
        }
    }
    public Security getSecurity(String secId){
        ResultSet rs = null;
        Statement statement = null;
        Security sec = new Security();
        logger.info("getSecurity() secId="+secId);
        try {
            logger.info("DriverManager.getConnection() : jdbc:sqlite:security.db");
            connection = DriverManager.getConnection("jdbc:sqlite:security.db");
            logger.info("connection.createStatement()");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
      
            String strSql = "select * from Security where securityId='"+secId+"'";
            logger.info("statement.executeQuery() : "+strSql);

            rs = statement.executeQuery(strSql);
            while(rs.next())
            {
                sec.setSecurityId(rs.getString("securityId"));
                sec.setName(rs.getString("name"));
                sec.setStrikePrice(rs.getDouble("strikePrice"));
                sec.setSecurityType(rs.getString("securityType").equals("Option") ? Security.SecurityType.OPTION :
                            Security.SecurityType.EQUITY) ;
                if (sec.getSecurityType() == Security.SecurityType.OPTION){
                    sec.setOptionType(rs.getString("optionType").equals("Call") ?  Security.OptionType.CALL :
                    Security.OptionType.PUT);
                }            
                sec.setUnderlyingId(rs.getString("underlyngId"));
                String strDate = rs.getString("expirationDate");
                if (strDate != null){
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        sec.setExpirationDate(df.parse(strDate));
                    } catch (ParseException e) {
                        logger.error("exception caught : ",e);
                        sec.setExpirationDate(new Date());
                    }
                }
                Gson gson = new Gson();
                logger.info("security found : "+gson.toJson(sec));
                rs.close();
                statement.close();
         
                return sec;
              // read the result set
            }
        } catch (SQLException e) {
            logger.error("exception caught : ",e);
        } finally{ 
            try {     
                if(rs != null){
                    rs.close();
                 }
                 if(statement != null){
                    statement.close();
                 }
                if(connection != null){
                connection.close();
                }
            } catch(SQLException e)
            {
                logger.error("exception caught : ",e);
            }
        }
        return sec;
    }
    @Override
    public void onMessage(Message message) {

      logger.info("SubService: onMessage() : +message");
      try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;

                logger.info("Received message"
                        + textMessage.getText() + "'");
                Gson gson = new Gson();
                SecurityRequest request = gson.fromJson(textMessage.getText(), SecurityRequest.class);
                logger.info("got market Data : usin gson "+gson.toJson(request));
                Security security = this.getSecurity(request.getSecurityId());

                String correlation = message.getJMSCorrelationID();
                if (correlation == null) {
                  correlation = message.getJMSMessageID();
                }
			    String contents = textMessage.getText();
				Destination replyDestination = message.getJMSReplyTo();
				MessageProducer replyProducer = responseService.getSession().createProducer(replyDestination);
				TextMessage replyMessage = responseService.getSession().createTextMessage();
                String secMessage = gson.toJson(security);
				replyMessage.setText(secMessage);

				replyMessage.setJMSCorrelationID(correlation);
                logger.info("replyDestination="+replyDestination+" ,JMSCorrelationID="+correlation+" ,replyMessage="+replyMessage);
                logger.info("replyProducer.send()");

				replyProducer.send(replyMessage);
            }     
        } catch (Exception e) {
            logger.error("exception caught : ",e);
        } finally {
        }

    } 

    public static void main(String[] args){

        SecurityServer secServer = new SecurityServer();
        ResponseService resService = new ResponseService(SECURITY_TOPIC) ;
        secServer.setResponseService(resService);

        resService.startService(secServer);    
    }

}
