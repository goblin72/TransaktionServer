package com.transactions.input;

import com.sigma.utils.net.HTTPConnector;
import com.transactions.input.protocol.response.CheckResponse;
import com.transactions.input.protocol.response.PaymentResponse;
import com.transactions.input.protocol.response.Response;
import com.transactions.input.protocol.response.SignResponse;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.HttpURLConnection;

/**
 * Date: 29.06.2011 17:55:56
 */
public class InputTest {
    public static final String URLPATH = "http://localhost:8080/TS/do";

    public static final String POINT = "point4";

    public static final String BANK = "CMP1";
    public static final String KSN = "KSN"+System.currentTimeMillis();
    public static final String TRACK= ";3236767890123456=12345678901234567?8";
    public static final String DOCUMENT= "Документ 1";
    public static final String SIGN_FILE= "E:\\dev\\workspaces\\TransaktionServer\\sig3.png";
    public static final String SIGN_FILE1= "E:\\dev\\workspaces\\TransaktionServer\\sig1_.png";
    public static long PAYMENT_ID=1;


    public static final String AMOUNT = "9000";

    @Test
    public void test() throws Exception {
        validate();
        payment();
        sign();
//        registry();
    }

    @Test
    public void testNoMail() throws Exception {
        validate();
        payment();
//        signNoMail();
//        registry();
    }


    @Test
    public void validate() throws Exception {
        Response resp = send("<request pointId=\"" + POINT + "\">" +
                "<bank>" + BANK + "</bank>" +
                "</request>",CheckResponse.class);
        System.out.println(resp);
        try {
            JAXBContext context = JAXBContext.newInstance(CheckResponse.class);
            Marshaller um = null;

            um = context.createMarshaller();

            um.marshal(resp,System.out);
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void payment() throws Exception {
        Response resp = send("<payment pointId=\"" + POINT + "\">" +
                "<bank>" + BANK + "</bank>" +
                "<amount>" + AMOUNT + "</amount>" +
                "<ksn>" + KSN + "</ksn>" +
                "<track>" + TRACK + "</track>" +
                "<document>" + DOCUMENT + "</document>" +
                "</payment>",PaymentResponse.class);
        PAYMENT_ID=((PaymentResponse)resp).getId();
        System.out.println(resp);
    }

    @Test
    public void sign() throws Exception {
        FileInputStream fis = new FileInputStream(SIGN_FILE);
        byte[] array = IOUtils.toByteArray(fis);

        Response resp = send("<sign pointId=\"" + POINT + "\"" +
                " mail=\"goblin2372@mail.ru\"" +
                " paymentId=\""+PAYMENT_ID+"\"" +
                ">" +
                "<img>" + new String(Hex.encodeHex(array)) + "</img>" +
                "</sign>",SignResponse.class);
        System.out.println(resp);
    }

    @Test
    public void signNoMail() throws Exception {
        FileInputStream fis = new FileInputStream(SIGN_FILE);
        byte[] array = IOUtils.toByteArray(fis);

        Response resp = send("<sign pointId=\"" + POINT + "\"" +
                " paymentId=\""+PAYMENT_ID+"\"" +
                ">" +
                "<img>" + new String(Hex.encodeHex(array)) + "</img>" +
                "</sign>",SignResponse.class);
        System.out.println(resp);
    }
    
    @Test
    public void coderTest()
    {
        try {
            FileInputStream fis = new FileInputStream(SIGN_FILE);
            byte[] array = IOUtils.toByteArray(fis);


        String hex=new String(Hex.encodeHex(array));
        FileOutputStream fos = new FileOutputStream(SIGN_FILE1);
            fos.write(Hex.decodeHex(hex.toCharArray()));
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response send(byte[] request, Class clazz) throws Exception {
        HttpURLConnection con = HTTPConnector.getConnection(URLPATH);
        con.setRequestProperty("Content-Type","text/xml; charset=UTF-8");

        con.setReadTimeout(60000);
        Response req = null;
        try {
            OutputStream out = con.getOutputStream();
            try {

                out.write(request);
            } finally {
                out.close();
            }
            InputStream in = con.getInputStream();

           /* byte[] b = new byte[1];
            while (-1!=in.read(b))
            {
                System.out.print(new String(b));
            } */
            
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller um = context.createUnmarshaller();
            req= (Response) um.unmarshal(in);
        } finally {
            con.disconnect();
        }
        return req;
    }

    protected Response send(String request, Class clazz) throws Exception {
        System.out.println(request);
        return send(request.getBytes("UTF-8"),clazz);
    }
}
