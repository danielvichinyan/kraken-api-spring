package com.nexo.marketdata.services.rest;

import com.nexo.marketdata.constants.ExceptionConstants;
import com.nexo.marketdata.constants.KrakenUrlConstants;
import com.nexo.marketdata.constants.RequestConstants;
import com.nexo.marketdata.model.enums.KrakenRestApiMethod;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class KrakenApiRequestServiceImpl implements KrakenApiRequestService {

    private static final String AMPERSAND = "&";
    private static final String EQUAL_SIGN = "=";

    /** The request URL. */
    private URL url;

    /** The request message signature. */
    private String signature;

    /** The API key. */
    private String key;

    /** The request's POST data. */
    private StringBuilder postData;

    /** Tells whether the API method is public or private. */
    private boolean isPublic;

    @Override
    public String execute() throws IOException {
        HttpsURLConnection connection = null;

        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(RequestConstants.REQUEST_POST);
            connection.addRequestProperty(RequestConstants.REQUEST_USER_AGENT, RequestConstants.REQUEST_USER_AGENT_NAME);

            // Set public key & signature if method is private
            if (!isPublic) {
                if (this.key == null || this.signature == null || this.postData == null) {
                    throw new IllegalStateException(ExceptionConstants.ERROR_INCOMPLETE_PRIVATE_METHOD);
                }

                connection.addRequestProperty(RequestConstants.REQUEST_API_KEY, this.key);
                connection.addRequestProperty(RequestConstants.REQUEST_API_SIGN, this.signature);
            }

            // Write Post to request
            if (this.postData != null && !this.postData.toString().isEmpty()) {
                connection.setDoOutput(true);

                try(OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream())) {
                    out.write(postData.toString());
                }
            }

            // Execute Request and Read Response
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;

                while((line = in.readLine()) != null) {
                    response.append(line);
                }

                return response.toString();
            }
        }
        finally {
            connection.disconnect();
        }
    }

    @Override
    public String setMethod(KrakenRestApiMethod method) throws MalformedURLException {
        if (method == null) {
            throw new IllegalArgumentException(ExceptionConstants.ERROR_NULL_METHOD);
        }

        this.isPublic = method.isPublic;
        this.url = new URL((isPublic ?
                KrakenUrlConstants.REST_PUBLIC_URL :
                KrakenUrlConstants.REST_PRIVATE_URL) + method.name);

        return this.url.getPath();
    }

    @Override
    public String setParameters(Map<String, String> parameters) throws UnsupportedEncodingException {
        if (parameters == null || parameters.isEmpty()) {
            throw new IllegalArgumentException(ExceptionConstants.ERROR_NO_PARAMETERS);
        }

        this.postData = new StringBuilder();
        for (Entry<String, String> entry : parameters.entrySet()) {
            this.postData.append(entry.getKey()).append(EQUAL_SIGN).append(KrakenUtilsService.urlEncode(entry.getValue())).append(AMPERSAND);
        }

        return this.postData.toString();
    }

    @Override
    public void setKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException(ExceptionConstants.ERROR_NULL_KEY);
        }

        this.key = key;
    }

    @Override
    public void setSignature(String signature) {
        if (signature == null) {
            throw new IllegalArgumentException(ExceptionConstants.ERROR_NULL_SIGNATURE);
        }

        this.signature = signature;
    }
}
