package com.nexo.marketdata.services.rest;

import com.nexo.marketdata.constants.AlgorithmConstants;
import com.nexo.marketdata.constants.ExceptionConstants;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

final class KrakenUtilsService {

    public static byte[] base64Decode(String input) {
        return Base64.getDecoder().decode(input);
    }

    public static String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] concatArrays(byte[] firstArray, byte[] secondArray) {
        if (firstArray == null || secondArray == null) {
            throw new IllegalArgumentException(ExceptionConstants.ERROR_NULL_ARRAYS);
        }

        byte[] concattedArray = new byte[firstArray.length + secondArray.length];
        for (int i = 0; i < concattedArray.length; i++) {
            concattedArray[i] = i < firstArray.length ? firstArray[i] : secondArray[i - firstArray.length];
        }

        return concattedArray;
    }

    public static byte[] hmacSha512(byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(AlgorithmConstants.HMAC_SHA512);
        mac.init(new SecretKeySpec(key, AlgorithmConstants.HMAC_SHA512));

        return mac.doFinal(message);
    }

    public static byte[] sha256(String message) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(AlgorithmConstants.SHA256);

        return messageDigest.digest(stringToBytes(message));
    }

    public static byte[] stringToBytes(String input) {
        if (input == null) {
            throw new IllegalArgumentException(ExceptionConstants.ERROR_NULL_INPUT);
        }

        return input.getBytes(Charset.forName(AlgorithmConstants.UTF8));
    }

    public static String urlEncode(String input) throws UnsupportedEncodingException {
        return URLEncoder.encode(input, AlgorithmConstants.UTF8);
    }

    private KrakenUtilsService() {}
}
