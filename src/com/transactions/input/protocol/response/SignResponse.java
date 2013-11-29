package com.transactions.input.protocol.response;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 19.04.13
 * Time: 19:47
 */
@XmlRootElement(name = "response")
public class SignResponse extends Response{

    @Override
    public String toString() {
        return "SignResponse{" +
                "message='" + message + '\'' +
                ", error=" + error +
                '}';
    }
}
