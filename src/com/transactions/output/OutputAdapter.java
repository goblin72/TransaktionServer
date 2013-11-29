package com.transactions.output;

import com.transactions.common.TSException;
import com.transactions.model.Bank;
import com.transactions.model.Payment;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 28.04.13
 * Time: 15:17
 */
public interface OutputAdapter
{
    public AdapterResponse execute (Payment payment) throws TSException;

    public String getCode();
}
