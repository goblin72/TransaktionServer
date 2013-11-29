package com.transactions.input.protocol.response;

import com.transactions.common.MapXMLAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 19.04.13
 * Time: 18:40
 */
@XmlRootElement(name = "response")
public class PaymentResponse extends Response{

    private long id;

    private List<Parameter> parameters;
    @XmlElement(name = "parameter")
    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @XmlAttribute(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PayResponse{" +
                " id=" + id +
                " error=" + error +
                " message=" + message +
                " params=" + parameters +
                '}';
    }
}
