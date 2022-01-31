# kraken-api-spring
Implemented Kraken Websockets 1.8 API and Kraken REST API. Created a websocket connection to the exchange and it receives a stream of orderbook updates for the currency the user queries. Then, the orderbook streams are fit into appropriate data structures, which are ordered from highest ask to lowest bid. The OrderBook data is separated by pair and printed on the console. On every update, data is printed to the console along with the best ask and best bid. The application is implemented in Java 11, Spring Framework.

# Instructions

***The project MUST be run with Java 11.***

The Application provides 2 different implementation of APIs:

***REST API:***

For the this endpoint you have to provide query param "pair", for example: ?pair=BTCUSD

1). Get Order Book Data Endpoint (GET) -> http://localhost:8080/data?pair=BTCUSD

***WebSockets API*:**

***WARNING***

***The Output of the Request is displayed in the CONSOLE of the IDE you are running it on.***

***After sending the /subscribe POST request get back to the IDE's console and watch the order data stream.***

1). Send an empty POST request to http://localhost:8080/connect

2). Send a POST request to http://localhost:8080/subscribe?pair=BTC/USD,ETH/USD&interval=5&depth=10&name=book
This /subscribe POST request MUST have 5 query params:

pair=list of strings -> "BTC/USD","ETH/USD"

interval=integer -> 5

depth=integer -> 10

name=string -> "book"

Correctly queried params look like: ?pair=BTC/USD,ETH/USD&interval=5&depth=10&name=book

3). Finally close the websocket, sending a POST request to http://localhost:8080/close
