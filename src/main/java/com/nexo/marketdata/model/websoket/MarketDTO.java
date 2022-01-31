package com.nexo.marketdata.model.websoket;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.Instant;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"price", "volume", "timestamp" , "r"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketDTO {
    public Double price;
    public Double volume;
    public String timestamp;
    public String r;

    private MarketDTO() {}

    public MarketDTO(Double price, Double volume, String timestamp, String r) {
        this.price = price;
        this.volume = volume;
        this.timestamp = timestamp;
        this.r = r;
    }

    public Double getPrice() {
        return price;
    }

    public Double getVolume() {
        return volume;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getR() { return r; }

    public Instant convertTime() {
        String toBeConverted;

        if (this.timestamp.contains(".")) {
            String[] result = this.timestamp.split("\\.");
            toBeConverted = result[0];
        } else {
            toBeConverted = this.timestamp;
        }

        return Instant.ofEpochMilli(Long.parseLong(toBeConverted) * 1000);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(" [ ").append(this.price).append(", ").append(this.volume).append(" ]");

        return stringBuilder.toString();
    }
}
