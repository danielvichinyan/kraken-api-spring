package com.nexo.marketdata.model.rest;

import java.util.ArrayList;

public class ResultGenericDTO<T> {

    private T result;
    private ArrayList<String> error = new ArrayList<>();

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public ArrayList<String> getError() {
        return error;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(result + "\n");

        return stringBuilder.toString();
    }
}
