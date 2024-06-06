<h1>Coding Test</h1>

This is a coding test to:

1. Get the positions from a mock CSV position file (consisting of tickers and number of shares/contracts of tickers
in the portfolio)
2. Get the security definitions from an embedded database.
3. Implement a mock market data provider that publishes stock prices.
4. Calculate the real time option price with the underlying price
5. Publishes following details in real-time:
6. Implement a portfolio result subscriber.

This application was developed in linux.
<h2>Installation</h2>

In the root directory:
<br>
mvn install
(Open JDK 17 required)

<h2>Usage</h2>
This project uses ActiveMQ JMS for communication.
Usage:
<br>
<br>
Run MarketData publisher:
<br>
in the root directory
<br>
startMarketDataPublisher.sh
<br>
<br>
Run Security Server:
<br>
in the root directory
startSecurityServer.sh
<br>
<br>
Run main program
<br>
startMain.sh
<br>
1. This will load the csv file [portfolio.csv]
2. Lookup security information
3. subscribe to market date
4. perform calculastions
5. print portfolio values whenever maket price changes

Database:
<br> in root directory : security.db
<br> Database schema /db/createTable.sql

Portfolio data:
<br> in root directory : portfolio.csv

<h2>Note:</h2>
There is an issue with running 2 or more Active MQ services locally simultaneously .
<br>
So to make the system work.
<br>
1. run startMarketDataPublisher.sh to publish some price updates
<br>
2. shut it down.
<br>
3. run startMain.sh to load csv and calculate portfolio,


