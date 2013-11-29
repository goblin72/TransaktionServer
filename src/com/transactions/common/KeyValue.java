package com.transactions.common;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 29.04.13
 * Time: 20:45
 */
public class KeyValue
{
    public KeyValue() {
    }

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //obviously needs setters/getters
    @XmlElement
    public String key;

    @XmlAttribute
    public String value;


}
