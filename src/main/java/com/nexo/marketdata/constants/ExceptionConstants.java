package com.nexo.marketdata.constants;

public class ExceptionConstants {

    // Kraken API Service
    public static final String ERROR_NULL_METHOD = "The API method can't be null.";
    public static final String ERROR_NULL_SIGNATURE = "The signature can't be null.";
    public static final String ERROR_NULL_KEY = "The key can't be null.";
    public static final String ERROR_NO_PARAMETERS = "The parameters can't be null or empty.";
    public static final String ERROR_INCOMPLETE_PRIVATE_METHOD = "A private method request requires the API key, the message signature and the method parameters.";

    // Kraken Utils Service
    public static final String ERROR_NULL_INPUT = "Input can't be null.";
    public static final String ERROR_NULL_ARRAYS = "Given arrays can't be null.";
}
