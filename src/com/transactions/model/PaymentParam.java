package com.transactions.model;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 28.04.13
 * Time: 14:07
 */

import javax.persistence.*;

@Entity
@Table(name = "payment_param")
public class PaymentParam extends AEntity
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "param_key")
    private String key;

    @Column(name = "param_value")
    private String value;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PaymentParam{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
