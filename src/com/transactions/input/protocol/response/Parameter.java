package com.transactions.input.protocol.response;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 29.04.13
 * Time: 21:05
 */
public class Parameter 
{
    public Parameter() {
    }

    public Parameter(Map.Entry<String,String> key) {
        this.key = key.getKey();
        this.value = key.getValue();
    }

    public Parameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;


    private  String value;

    @XmlAttribute
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @XmlAttribute
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "value='" + value + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
