package com.transactions.model;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 21.04.13
 * Time: 19:27
 */
@Entity
@Table(name = "card_type")
public class CardType
{
    @Id
    @Column(name = "value")
    private Integer value;

    @Column(name = "name")
    private String name;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CardType{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }
}
