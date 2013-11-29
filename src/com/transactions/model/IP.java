package com.transactions.model;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 21.04.13
 * Time: 19:36
 */
@Entity
@Table(name = "ip")
public class IP extends AEntity
{
    @Column(name = "host")
    private String host;

    @Column(name = "port")
    private Integer port;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "IP{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
