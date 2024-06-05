package com.cryptotest.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Main.class) ;
    public final static String COMMA_DELIMITER = ",";

    public void loadCSVfile(String strPath) throws FileNotFoundException, IOException{
        try (BufferedReader br = new BufferedReader(new FileReader(strPath))) {
            String line;
             while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                logger.info("read record : "+Arrays.toString(values));
                // records.add(Arrays.asList(values));
            }
        }
    }

    public static void main(String[] args) {
        Main mainClass = new Main();
        try {
            mainClass.loadCSVfile("portfolio.csv");
        } catch (FileNotFoundException e) {
            logger.error("exception caught : ",e);
        } catch (IOException e) {
            logger.error("exception caught : ",e);
        }
    }
}
