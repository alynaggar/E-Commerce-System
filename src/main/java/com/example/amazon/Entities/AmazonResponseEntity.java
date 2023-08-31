package com.example.amazon.Entities;

public class AmazonResponseEntity<T> {

    private String message;
    private T data;
    private int code;

    public AmazonResponseEntity(AmazonResponseCode amazonResponseCode, T data) {
        this.message = amazonResponseCode.getMessage();
        this.data = data;
        this.code = amazonResponseCode.getCode();
    }

    public AmazonResponseEntity(String message, T data, int code) {
        this.message = message;
        this.data = data;
        this.code = code;
    }

    public AmazonResponseEntity(AmazonResponseCode amazonResponseCode) {
        this.message = amazonResponseCode.getMessage();
        this.code = amazonResponseCode.getCode();
    }

    public AmazonResponseEntity(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
