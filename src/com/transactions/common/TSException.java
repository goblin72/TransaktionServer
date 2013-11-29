package com.transactions.common;

import com.transactions.input.protocol.Errors;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 25.04.13
 * Time: 22:43
 */
public class TSException extends Exception
{
    Errors error;
    String errorMessage;

    public TSException(Errors error) {
        this.error = error;
    }

    public TSException(Errors error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return error.getCode();
    }

    public String getErrorMessage() {
        return errorMessage==null || errorMessage.isEmpty()?error.getMessage():errorMessage;
    }

    @Override
    public String toString() {
        return "TSException{" +
                "error=" + getErrorCode() +
                ", errorMessage='" + getErrorMessage() + '\'' +
                '}';
    }
}
