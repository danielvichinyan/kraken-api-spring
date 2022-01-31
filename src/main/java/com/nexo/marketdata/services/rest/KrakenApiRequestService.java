package com.nexo.marketdata.services.rest;

import com.nexo.marketdata.model.enums.KrakenRestApiMethod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Map;

public interface KrakenApiRequestService {

    String execute() throws IOException;

    String setMethod(KrakenRestApiMethod method) throws MalformedURLException;

    String setParameters(Map<String, String> parameters) throws UnsupportedEncodingException;

    void setKey(String key);

    void setSignature(String signature);
}
