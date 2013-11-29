package com.transactions.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 21.04.13
 * Time: 19:32
 */
@Entity
@Table(name = "bank")
public class Bank extends AEntity
{

    @Column(name = "outputCode", nullable = false)
    private String outputCode;

    @Column(name = "checkTemplate", nullable = false)
    private String checkTemplate;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bank")
    private List<BIN> bins;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bank")
    private List<IP> ips;

    public List<BIN> getBins() {
        return bins;
    }

    public void setBins(List<BIN> bins) {
        this.bins = bins;
    }

    public List<IP> getIps() {
        return ips;
    }

    public void setIps(List<IP> ips) {
        this.ips = ips;
    }

    public String getOutputCode() {
        return outputCode;
    }

    public void setOutputCode(String outputCode) {
        this.outputCode = outputCode;
    }

    public String getCheckTemplate() {
        return checkTemplate;
    }

    public void setCheckTemplate(String checkTemplate) {
        this.checkTemplate = checkTemplate;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "outputCode='" + outputCode + '\'' +
                ", checkTemplate='" + checkTemplate + '\'' +
                '}';
    }
}
