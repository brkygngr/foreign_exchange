package com.brkygngr.foreign_exchange.exception;

public class ValidationErrorMessages {

    public static final String CONVERSION_AMOUNT_REQUIRED = "Amount is required!";
    public static final String CONVERSION_AMOUNT_POSITIVE = "Amount must be positive!";
    public static final String SOURCE_CURRENCY_REQUIRED = "Source currency is required!";
    public static final String TARGET_CURRENCY_REQUIRED = "Target currency is required!";
    public static final String SOURCE_CURRENCY_CODE = "Source currency must be a three letter currency code!";
    public static final String TARGET_CURRENCY_CODE = "Target currency must be a three letter currency code!";
    public static final String SOURCE_AND_TARGET_MUST_BE_DIFFERENT = "Source and target currencies must be different!";
    public static final String CONVERSION_HISTORY_REQUIRED = "Either transaction ID or date is required!";
}
