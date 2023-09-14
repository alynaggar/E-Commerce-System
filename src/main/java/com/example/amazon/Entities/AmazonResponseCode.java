package com.example.amazon.Entities;

public enum AmazonResponseCode {

    SUCCESS(200, "Success"),
    LOGIN_SUCCESS(201, "Login successfully"),
    NOT_FOUND(404, "Content doesn't exist"),
    USER_FIELD_IS_NULL(455, "A user field is null"),
    PRODUCT_FIELD_IS_NULL(456, "Product name or price is null"),
    PAYMENT_FIELD_IS_NULL(457, "Payment type is null"),
    CART_IS_EMPTY(458, "Cart is empty"),
    QUANTITY_MORE_THAN_AVAILABLE(459, "Quantity is more than available"),
    PRODUCT_NOT_IN_CART(460, "Product is not in cart"),
    USERNAME_EMAIL_NUMBER_SSN_ALREADY_EXIST(461, "Username, Email, Number or ssn already used"),
    CART_NOT_FOUND(470, "Cart doesn't exist"),
    PRODUCT_NOT_FOUND(471, "Product doesn't exist"),
    PAYMENT_NOT_FOUND(472, "Payment doesn't exist"),
    ORDER_NOT_FOUND(473, "Order doesn't exist"),
    USER_NOT_FOUND(474, "User doesn't exist"),
    LOGIN_FAILED(475,"Login Failed"),
    ID_NOT_FOUND(476, "Id doesn't exist in request"),
    WRONG_OTP(477, "Otp is wrong"),
    INTERNAL_SERVER_ERROR(500, "Internal server error");

    private int code;
    private String message;

    AmazonResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
