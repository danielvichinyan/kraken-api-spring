package com.nexo.marketdata.services.rest;

import com.nexo.marketdata.model.enums.KrakenRestApiMethod;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface KrakenApiService {

    String queryPublic(KrakenRestApiMethod method) throws IOException;

    String queryPublic(KrakenRestApiMethod method, Map<String, String> parameters) throws IOException;

    String queryPrivate(KrakenRestApiMethod method) throws IOException, NoSuchAlgorithmException, InvalidKeyException;

    String queryPrivate(KrakenRestApiMethod method, String otp) throws IOException, NoSuchAlgorithmException, InvalidKeyException;

    String queryPrivate(
            KrakenRestApiMethod method,
            Map<String, String> parameters
    ) throws IOException, NoSuchAlgorithmException, InvalidKeyException;

    String queryPrivate(
            KrakenRestApiMethod method,
            String otp,
            Map<String, String> parameters
    ) throws IOException, NoSuchAlgorithmException, InvalidKeyException;
}
