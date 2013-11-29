package com.transactions.input.protocol.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 20.04.13
 * Time: 11:18
 */
@XmlRootElement(name = "payment")
public class PaymentRequest extends Request
{
    private String bank;

    private String ksn;

    private String track;

    private String document;

    private long amount;

    @XmlElement
    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @XmlElement
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @XmlElement
    public String getKsn() {
        return ksn;
    }

    public void setKsn(String ksn) {
        this.ksn = ksn;
    }

    @XmlElement
    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    @XmlElement
    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
