package com.transactions.input.protocol.request;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 19.04.13
 * Time: 20:03
 * To change this template use File | Settings | File Templates.
 */
public class Request {
    protected String pointId;

    @XmlAttribute
    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

}
