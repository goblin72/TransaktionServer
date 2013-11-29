package com.transactions.model;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 21.04.13
 * Time: 19:38
 */
@Entity
@Table(name = "bin")
public class BIN extends AEntity
{
    @Column(name = "bin")
    private String bin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "BIN{" +
                "bin='" + bin + '\'' +
                '}';
    }
}
