package com.nexo.marketdata.model.websoket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBookWebsocketDTO {
    public List<MarketDTO> a;
    public List<MarketDTO> b;

    public List<MarketDTO> as;
    public List<MarketDTO> bs;

    public MarketDTO getBestAsk() {
        List<MarketDTO> asksCopy = null;

        if (this.as != null || this.a != null) {
            asksCopy = new ArrayList<>(this.as != null ? this.as : this.a);
        }

        return asksCopy
                .stream()
                .min(Comparator.comparing(MarketDTO::getPrice))
                .orElseThrow(NoSuchElementException::new);
    }

    public MarketDTO getBestBid() {
        List<MarketDTO> bidsCopy = null;

        if (this.bs != null || this.b != null) {
            bidsCopy = new ArrayList<>(this.bs != null ? this.bs : this.b);
        }

        return bidsCopy
                .stream()
                .max(Comparator.comparing(MarketDTO::getPrice))
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Instant timeStamp = null;

        if (this.as != null || this.a != null) {
            stringBuilder.append("<------------------------------------->\n");
            stringBuilder.append("asks: \n");

            List<MarketDTO> reversedAsks = new ArrayList<>(this.as != null ? this.as : this.a);
            Collections.reverse(reversedAsks);

            for (MarketDTO market : reversedAsks) {
                stringBuilder.append(market).append(",\n");
                timeStamp = market.convertTime();
            }

            stringBuilder.append("best ask: [ ")
                    .append(this.getBestAsk().getPrice())
                    .append(", ")
                    .append(this.getBestAsk().getVolume())
                    .append(" ]\n");
        }

        if (this.bs != null || this.b != null) {
            stringBuilder.append("best bid: [ ")
                    .append(this.getBestBid().getPrice())
                    .append(", ")
                    .append(this.getBestBid().getVolume())
                    .append(" ]\n");

            stringBuilder.append("bids: \n");

            List<MarketDTO> bidsCopy = new ArrayList<>(this.bs != null ? this.bs : this.b);
            for (MarketDTO market : bidsCopy) {
                stringBuilder.append(market).append(",\n");
                timeStamp = market.convertTime();
            }
        }

        stringBuilder.append(">-------------------------------------<\n");

        stringBuilder.append(timeStamp);

        return stringBuilder.toString();
    }
}