package com.nexo.marketdata.model.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"price", "volume", "timestamp"})
public class Market {
    public BigDecimal price;
    public BigDecimal volume;
    public Integer timestamp;

    private Market() {}

    public Market(BigDecimal price, BigDecimal volume, Integer timestamp) {
        this.price = price;
        this.volume = volume;
        this.timestamp = timestamp;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public Instant convertTime() {
        Integer timestampCopy = this.timestamp;
        return Instant.ofEpochSecond(timestampCopy);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(" [ " +
                this.price.setScale(1, RoundingMode.HALF_EVEN) + ", " +
                this.volume + " ]");

        return stringBuilder.toString();
    }
}
