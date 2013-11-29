package com.transactions.input;

import com.transactions.input.protocol.response.CheckResponse;
import com.transactions.input.protocol.response.PaymentResponse;
import com.transactions.input.protocol.response.Response;
import com.transactions.input.protocol.response.SignResponse;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 29.04.13
 * Time: 23:08
 * To change this template use File | Settings | File Templates.
 */
public class MultyThreadsTest extends InputTest
{
    @Test
    public void testIt()
    {
        final AtomicInteger in = new AtomicInteger();
        ExecutorService service = Executors.newFixedThreadPool(10);
        for(int i=0;i<20;i++)
        {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        int id= in.getAndIncrement();
                        long start = System.currentTimeMillis();
                        Response resp = send("<request pointId=\"" + POINT + "\">" +
                                "<bank>" + BANK + "</bank>" +
                                "</request>",CheckResponse.class);
                        Assert.assertEquals(0,resp.getError());
                        System.out.println(resp);

                        resp = send("<payment pointId=\"" + POINT + "\">" +
                                "<bank>" + BANK + "</bank>" +
                                "<amount>" + AMOUNT + "</amount>" +
                                "<ksn>" + KSN + "</ksn>" +
                                "<track>" + TRACK + "</track>" +
                                "<document>" + DOCUMENT + "</document>" +
                                "</payment>",PaymentResponse.class);
                        long PAYMENT_ID=((PaymentResponse)resp).getId();
                        Assert.assertEquals(0,resp.getError());
                        System.out.println(resp);

                        FileInputStream fis = new FileInputStream(SIGN_FILE);
                        byte[] array = IOUtils.toByteArray(fis);

                        resp = send("<sign pointId=\"" + POINT + "\"" +
                                " paymentId=\""+PAYMENT_ID+"\"" +
//                                " mail=\"goblin2372@mail.ru\"" +
                                ">" +
                                "<img>" + new String(Hex.encodeHex(array)) + "</img>" +
                                "</sign>",SignResponse.class);
                        Assert.assertEquals(0,resp.getError());
                        System.out.println(resp);

                        System.out.println(id+" in "+(System.currentTimeMillis()-start));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        service.shutdown();
    }
}
