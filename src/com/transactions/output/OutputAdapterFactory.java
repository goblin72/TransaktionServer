package com.transactions.output;

import com.transactions.common.TSException;
import com.transactions.input.protocol.Errors;
import com.transactions.output.impl.TestAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 28.04.13
 * Time: 15:17
 */
public class OutputAdapterFactory 
{
    static List<OutputAdapter> adapters = new ArrayList<OutputAdapter>();
    static {
        adapters.add(new TestAdapter());
        //todo  регистрация исхъодящих адаптеров
    }
    
    public static OutputAdapter getOutputAdapter(String code) throws TSException
    {
        for (OutputAdapter adapter:adapters)
        {
            if (adapter.getCode().equals(code))
                return adapter;
        }
        throw new TSException(Errors.UNKNOWN,"Не удалось найти исходящий адаптер с кодом "+code);
    }
}
