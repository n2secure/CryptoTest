<h1>Crypto.com Coding Test</h1>

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

<u>Run MarketData publisher:</u>
<br>
in the root directory
startMarketDataPublisher.sh

<u>Run Security Server:</u>
<br>
in the root directory
startSecurityServer.sh

<u>Run main program</u>
<br>
startMain.sh
<br>
1. This will load the csv file [portfolio.csv]
2. Lookup security information
3. subscribe to market date
4. perform calculastions
5. print portfolio values whenever maket price changes


<h2>Note:</h2>
There is an issue with running 2 or more Active MQ service locally .
So to make the system sork.
1. run startMarketDataPublisher.sh to publish some price updates
2. shut it down.
3. run startMain.sh to load csv and calculate portfolio,


