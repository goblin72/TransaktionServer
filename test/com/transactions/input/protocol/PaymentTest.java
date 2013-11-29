package com.transactions.input.protocol;

import com.transactions.input.protocol.response.CheckResponse;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 29.04.13
 * Time: 20:39
 * To change this template use File | Settings | File Templates.
 */
public class PaymentTest
{
    @Test
    public void test()
    {
        CheckResponse re= new CheckResponse();
        List<String> orgs=new ArrayList<String>();
        orgs.add("1");
        orgs.add("2");
        re.setAllowableBank(orgs);

        try {
            JAXBContext context = JAXBContext.newInstance(CheckResponse.class);
            Marshaller um = null;

                um = context.createMarshaller();

            um.marshal(re,System.out);
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

