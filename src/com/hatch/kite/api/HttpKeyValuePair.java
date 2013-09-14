package com.hatch.kite.api;

/**
 * Created by vince on 9/14/13.
 */
public class HttpKeyValuePair {
    private String key;
    private String value;

    public HttpKeyValuePair(String key, String val) {
        this.key = key;
        this.value = val;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}