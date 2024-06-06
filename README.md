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
Installation

In the root directory:
mvn install
(Open JDK 17 required)


This project uses ActiveMQ JMS for communication.
Usage:

Run MarketData publisher:
in the root directory
startMarketDataPublisher.sh

Run Security Server:
in the root directory
startSecurityServer.sh

Run main program
startMain.sh
1. This will load the csv file [portfolio.csv]
2. Lookup security information
3. subscribe to market date
4. perform calculastions
5. print portfolio values whenever maket price changes


Note:
There is an issue with running 2 or more Active MQ service locally .
So to make the system sork.
1. run startMarketDataPublisher.sh to publish some price updates
2. shut it down.
3. run startMain.sh to load csv and calculate portfolio,


