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

    /**
     * Constructor
     *
     * The websocket current message is mapped to a MarketDTO, and it may vary.
     * Some MarketDTOs have the "r" property and some don't - this means it is optional.
     * Unknown json properties are ignored.
     * However, each MarketDTO has price, volume and a timestamp.
     *
     * @param price
     * @param volume
     * @param timestamp
     * @param r
     */
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

    /**
     * Converts the time from a timestamp (example timestamp: 1643640401.302332)
     * To an Instant (example converted Instant: 2022-01-31T17:51:43Z)
     *
     * @return the converted instant
     */
    public Instant convertTime() {
        String toBeConverted;

        // If the provided timestamp has a dot like in the above example
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

        stringBuilder.append(" [ ")
                .append(this.price)
                .append(", ")
                .append(Math.floor(this.volume * 100) / 100)
                .append(" ]");

        return stringBuilder.toString();
    }
}
