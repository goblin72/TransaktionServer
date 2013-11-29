package com.transactions.input.protocol.request;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 20.04.13
 * Time: 11:28
 */
@XmlRootElement(name = "sign")
public class SignRequest extends Request{

    private String mail;

    private String img;

    private long paymentId;

    @XmlElement
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @XmlAttribute
    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    @XmlAttribute
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "SignRequest{" +
                "mail='" + mail + '\'' +
                ", img='" + img + '\'' +
                ", paymentId=" + paymentId +
                '}';
    }
}
