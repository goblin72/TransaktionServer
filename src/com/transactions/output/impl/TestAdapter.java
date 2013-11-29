package com.transactions.output.impl;

import com.transactions.common.TSException;
import com.transactions.input.protocol.Errors;
import com.transactions.model.Payment;
import com.transactions.output.AdapterResponse;
import com.transactions.output.OutputAdapter;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 28.04.13
 * Time: 15:17
 */
public class TestAdapter implements OutputAdapter
{

    private static final Logger logger = Logger.getLogger(TestAdapter.class);

    @Override
    public AdapterResponse execute(Payment payment) throws TSException {
        logger.debug("call TestOutputAdapter with "+payment);
        logger.debug("bank number "+payment.getScriptBank().getBank());
        String cardNumber=getCardNumber(payment.getTrack2());
        if (Long.parseLong(String.valueOf(cardNumber.charAt(cardNumber.length()-1)))%2==1)
        {
            throw new TSException(Errors.BANK_REJECT,"нет денег на карте");
        }
        AdapterResponse response = new AdapterResponse();
        Map<String,String> params = new HashMap<String,String>();
        response.setParams(params);
        params.put("CHECK",""+System.currentTimeMillis());
        params.put("TERMINAL_ID",payment.getScriptBank().getTerminalId());
        params.put("EMPL_ID",payment.getScriptBank().getEmplId());
        logger.debug("result is "+params);
        return response;
    }

    @Override
    public String getCode() {
        return "TEST";
    }

    private  String getCardNumber(String track)
    {
        String[] strs=track.split("\\=");
        return strs[0].substring(1);
    }
}
