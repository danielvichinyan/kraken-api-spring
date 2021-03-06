package com.nexo.marketdata.model.websoket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionDTO {

    private int channelID;
    private String channelName;
    private String event;
    private String pair;
    private String status;
    private String errorMessage;
    private Map<String, Object> subscription;

    public String getSubscriptionName() {
        return (String) this.subscription.get("name");
    }
}
