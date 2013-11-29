package com.transactions.input.protocol.response;

import com.transactions.input.protocol.Errors;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 19.04.13
 * Time: 18:40
 */


public class Response {
    
    protected String message;

    protected  int error;

    public Response() {
    }

    public Response(int error, String message) {
        this.message = message;
        this.error = error;
    }

    public Response(Errors error) {
        this.message = error.getMessage();
        this.error = error.getCode();
    }

    @XmlAttribute(name = "error")
    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
    @XmlAttribute(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
