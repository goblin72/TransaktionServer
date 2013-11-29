package com.transactions.input.protocol.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 19.04.13
 * Time: 20:03
 */
@XmlRootElement(name = "request")
public class CheckRequest extends Request{
    
    private String bank;

    @XmlElement
    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "CheckRequest{" +
                "pointId='" + pointId + '\'' +
                ", bank='" + bank + '\'' +
                '}';
    }
}
