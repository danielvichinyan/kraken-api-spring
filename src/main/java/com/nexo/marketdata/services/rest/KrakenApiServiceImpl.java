package com.nexo.marketdata.services.rest;

import com.nexo.marketdata.model.enums.KrakenRestApiMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class KrakenApiServiceImpl implements KrakenApiService {

    private static final String OTP = "otp";
    private static final String NONCE = "nonce";
    private static final String MICRO_SECONDS = "000";

    /** The API key. */
    private final String key;

    /** The API secret. */
    private final String secret;

    private final KrakenApiRequestService krakenApiRequestService;

    public KrakenApiServiceImpl(
            @Value("${app.nexo.key}") final String key,
            @Value("${app.nexo.secret}") final String secret,
            KrakenApiRequestService krakenApiRequestService
    ) {
        this.key = key;
        this.secret = secret;
        this.krakenApiRequestService = krakenApiRequestService;
    }

    @Override
    public String queryPublic(KrakenRestApiMethod method) throws IOException {
        return this.queryPublic(method, null);
    }

    @Override
    public String queryPublic(KrakenRestApiMethod method, Map<String, String> parameters) throws IOException {
        this.krakenApiRequestService.setMethod(method);

        if (parameters != null) {
            this.krakenApiRequestService.setParameters(parameters);
        }

        return this.krakenApiRequestService.execute();
    }

    @Override
    public String queryPrivate(KrakenRestApiMethod method) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return this.queryPrivate(method, null, null);
    }

    @Override
    public String queryPrivate(
            KrakenRestApiMethod method,
            String otp
    ) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return this.queryPrivate(method, otp, null);
    }

    @Override
    public String queryPrivate(
            KrakenRestApiMethod method,
            Map<String, String> parameters
    ) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return this.queryPrivate(method, null, parameters);
    }

    @Override
    public String queryPrivate(
            KrakenRestApiMethod method,
            String otp,
            Map<String, String> parameters
    ) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        this.krakenApiRequestService.setKey(this.key);

        // clone the parameters map
        parameters = parameters == null ? new HashMap<>() : new HashMap<>(parameters);

        // set otp parameter
        if (otp != null) {
            parameters.put(OTP, otp);
        }

        // generate nonce
        String nonce = String.valueOf(System.currentTimeMillis()) + MICRO_SECONDS;
        parameters.put(NONCE, nonce);

        // set the parameters and retrieve POST data
        String postData = this.krakenApiRequestService.setParameters(parameters);

        // create SHA256 hash of the nonce and the post data
        byte[] sha256 = KrakenUtilsService.sha256(nonce + postData);

        // set the API method and retrieve the path
        byte[] path = KrakenUtilsService.stringToBytes(this.krakenApiRequestService.setMethod(method));

        // decode the API secret, it's the HMAC key
        byte[] hmacKey = KrakenUtilsService.base64Decode(secret);

        // create the HMAC message from the path and the previous hash
        byte[] hmacMessage = KrakenUtilsService.concatArrays(path, sha256);

        // create the HMAC-SHA512 digest, encode it and set it as the request signature
        String hmacDigest = KrakenUtilsService.base64Encode(KrakenUtilsService.hmacSha512(hmacKey, hmacMessage));
        this.krakenApiRequestService.setSignature(hmacDigest);

        return this.krakenApiRequestService.execute();
    }
}
