package com.transactions.input.protocol.response;

import com.transactions.input.protocol.Errors;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 25.04.13
 * Time: 23:14
 */
@XmlRootElement(name = "response")
public class ErrorResponse extends Response{

    public ErrorResponse() {
    }

    public ErrorResponse(int error, String message) {
        super(error,message);
    }

    public ErrorResponse(Errors error) {
        super(error);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error=" + error +
                "message=" + message +
                '}';
    }
}
