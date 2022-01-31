package com.nexo.marketdata.model.rest;

import java.time.Instant;
import java.util.*;

public class OrderBookDTO extends ResultGenericDTO<Map<String, OrderBookDTO.OrderBook>> {

    public static class OrderBook {
        public List<Market> asks;
        public List<Market> bids;

        public Market getBestAsk() {
            return this.asks
                    .stream()
                    .min(Comparator.comparing(Market::getPrice))
                    .orElseThrow(NoSuchElementException::new);
        }

        public Market getBestBid() {
            return this.asks
                    .stream()
                    .max(Comparator.comparing(Market::getPrice))
                    .orElseThrow(NoSuchElementException::new);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            Instant timeStamp = null;

            stringBuilder.append("<------------------------------------->\n");
            stringBuilder.append("asks: \n");

            List<Market> reversedAsks = new ArrayList<>(this.asks);
            Collections.reverse(reversedAsks);

            for (Market market : reversedAsks) {
                stringBuilder.append(market).append(",\n");
            }

            stringBuilder.append("best bid: [ " +
                    this.getBestBid().getPrice() + ", " +
                    this.getBestBid().getVolume() + " ]\n");

            stringBuilder.append("best ask: [ " +
                    this.getBestAsk().getPrice() + ", " +
                    this.getBestAsk().getVolume() + " ]\n");

            stringBuilder.append("bids: \n");

            for (Market market : this.bids) {
                stringBuilder.append(market).append(",\n");
                timeStamp = market.convertTime();
            }

            stringBuilder.append(">-------------------------------------<\n");

            stringBuilder.append(timeStamp + "\n");

            return stringBuilder.toString();
        }
    }
}
