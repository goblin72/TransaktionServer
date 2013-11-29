package com.transactions.input;

import com.sigma.utils.jpa.DataManager;
import com.transactions.common.TSException;
import com.transactions.common.ThreadContext;
import com.transactions.db.DataBase;
import com.transactions.input.protocol.*;
import com.transactions.input.protocol.request.CheckRequest;
import com.transactions.input.protocol.request.PaymentRequest;
import com.transactions.input.protocol.request.SignRequest;
import com.transactions.input.protocol.response.ErrorResponse;
import com.transactions.input.protocol.response.Response;
import com.transactions.reg.Registrator;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 19.04.13
 * Time: 18:33
 */
@Controller
@RequestMapping()
public class InputHTTPAdapter {
    private static final Logger logger = Logger.getLogger(InputHTTPAdapter.class);

    Date startdate = new Date();

    @Resource
    private DataBase dataBase;

    @Resource
    private Registrator reg;


    @RequestMapping(value = "", method = RequestMethod.GET)
    public 
    @ResponseBody
    String statistic() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transactional server v 1.9 started at ").append(startdate);
        return sb.toString();
    }
    
    @RequestMapping(value = "/do",method = RequestMethod.POST)
    public
    @ResponseBody
    Response getRequest(@RequestBody String body) {
        try {
            long start = System.currentTimeMillis();
            DataManager dm=dataBase.getDb();
            try {

                dm.begin();
                ThreadContext.getThreadContext().setManager(dm);
                String request = URLDecoder.decode(body, "UTF-8");
                logger.debug("Request: " + request);

                JAXBContext context = JAXBContext.newInstance(CheckRequest.class, PaymentRequest.class, SignRequest.class);
                Unmarshaller um = context.createUnmarshaller();
                Object xmlRoot = um.unmarshal(new ByteArrayInputStream(request.getBytes("utf-8")));

                if (xmlRoot instanceof CheckRequest) {
                    logger.debug("get check request: " + xmlRoot);
                    return reg.check((CheckRequest) xmlRoot);
                } else if (xmlRoot instanceof PaymentRequest) {
                    logger.debug("get pay request: " + xmlRoot);
                    return reg.payment((PaymentRequest) xmlRoot);
                } else if (xmlRoot instanceof SignRequest) {
                    logger.debug("get sign request: " + xmlRoot);
                    return reg.sign((SignRequest) xmlRoot);
                } else {
                    logger.debug("get unknown request: " + xmlRoot);
                    return new Response(Errors.UNKNOWN.getCode(), "unknown request");
                }

            } catch (TSException e) {
                logger.error(e, e);
                return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
            } catch (Throwable e) {
                logger.error(e, e);
                return new ErrorResponse(Errors.UNKNOWN.getCode(), e.getMessage());
            } finally {
                dm.close(true);
                logger.debug("time=" + (System.currentTimeMillis() - start));
            }
        } catch (Throwable e) {
            logger.error(e, e);
            throw new RuntimeException(e);
        }
    }
}
