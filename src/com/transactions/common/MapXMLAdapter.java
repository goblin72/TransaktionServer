package com.transactions.common;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 29.04.13
 * Time: 20:43
 */
public class MapXMLAdapter extends XmlAdapter<String, Map<String, String>> {

    @Override
    public Map<String, String> unmarshal(String text) throws Exception {

        System.out.println(text);
        return new HashMap<String, String>();
    }

    @Override
    public String marshal(Map<String, String> v) throws Exception {
        StringBuilder sb = new StringBuilder();
        ArrayList<KeyValue> results = new ArrayList<KeyValue>(v.size());
        for (Map.Entry<String,String> entry : v.entrySet()) {
            sb.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">");
        }
        return sb.toString();
    }



}
