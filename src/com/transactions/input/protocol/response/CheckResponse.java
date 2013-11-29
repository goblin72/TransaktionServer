package com.transactions.input.protocol.response;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 19.04.13
 * Time: 18:40
 */
@XmlRootElement(name = "response")
public class CheckResponse extends Response{

    private String inn;
    private String pointStatus;
    private String bankStatus;
    private String contract;
    private String name;
    private String comment;

    Collection<String> allowableBank;
    @XmlElementWrapper(name = "allowableBanks")
    public Collection<String> getAllowableBank() {
        return allowableBank;
    }

    public void setAllowableBank(Collection<String> allowableBanks) {
        this.allowableBank = allowableBanks;
    }

    @XmlAttribute
    public String getPointStatus() {
        return pointStatus;
    }

    public void setPointStatus(String pointStatus) {
        this.pointStatus = pointStatus;
    }

    @XmlAttribute
    public String getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }

    @XmlElement
    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @XmlElement
    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    @Override
    public String toString() {
        return "CheckResponse{" +
                "inn='" + inn + '\'' +
                ", pointStatus='" + pointStatus + '\'' +
                ", bankStatus='" + bankStatus + '\'' +
                ", contract='" + contract + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", allowableBanks='" + allowableBank+ '\'' +
                '}';
    }
}
