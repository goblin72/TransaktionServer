package com.transactions.db;

import com.sigma.utils.jpa.DataManager;
import com.sigma.utils.jpa.DataQuery;
import com.transactions.common.TSException;
import com.transactions.common.ThreadContext;
import com.transactions.input.protocol.Errors;
import com.transactions.model.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 19.04.13
 * Time: 18:09
 */
@Component
public class DataBase
{
    private static final Logger logger = Logger.getLogger(DataBase.class);

    private String databaseURL;
    private String databaseUser;
    private String databasePassword;
    private String databaseEncoding;
    private Properties prop;
    
    public DataBase() {
    }

    public DataBase(String databaseURL, String databaseUser, String databasePassword, String databaseEncoding) {
        this.databaseURL = databaseURL;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
        this.databaseEncoding = databaseEncoding;
    }

    public void init()  {
        try{
            logger.debug("Init method calling");
            prop = new Properties();
            prop.setProperty("database.url",databaseURL);
            prop.setProperty("database.user",databaseUser);
            prop.setProperty("database.password",databasePassword);
            prop.setProperty("database.encoding",databaseEncoding);
            prop.setProperty("database.driver","com.mysql.jdbc.Driver");
            prop.setProperty("database.dialect","org.hibernate.dialect.MySQLDialect");
//            db = DataManagerFactory.getInstance(prop);
        }catch (Exception e)
        {
            logger.error(e,e);
            throw new RuntimeException(e);
        }
    }

    public DataManager getDb() {
        return DataManagerFactory.getInstance(prop);
    }
    
    public Point getPoint(String pointId) throws TSException
    {
        DataQuery  query= ThreadContext.getThreadContext().getManager().createQuery("select point from Point point where point.pointId=:pointId");
        query.setParameter("pointId",pointId);
        List<Point> points=query.getResult(Point.class);
        if (points.size()!=1)
            throw new TSException(Errors.UNKNOWN_POINT);
        else
            return points.get(0);
    }

    public Company getCompany(String code) throws TSException
    {
        DataQuery query=ThreadContext.getThreadContext().getManager().createQuery("select comp from Company comp where comp.code=:code");
        query.setParameter("code",code);
        List<Company> comps=query.getResult();
        if (comps.size()!=1)
            throw new TSException(Errors.UNKNOWN_COMPANY);
        else
            return comps.get(0);
    }

    public Payment getPayment(Long id) throws TSException
    {
        try{
            //Payment pay=db.find(Payment.class,id);
            DataQuery q=ThreadContext.getThreadContext().getManager().createQuery("select p from Payment p where p.id=:id");
            q.setParameter("id",id);
            Payment pay=q.getSingle();
            q=ThreadContext.getThreadContext().getManager().createQuery("select pp from PaymentParam pp where pp.payment=:pay");
            q.setParameter("pay",pay);
            List<PaymentParam> params = q.getResult(PaymentParam.class);
            pay.setParameters(params);
            if (pay==null)
                throw new TSException(Errors.UNKNOWN_PAYMENT_ID);
            else
                return pay;
        }catch (PersistenceException e)
        {
            throw new TSException(Errors.UNKNOWN_PAYMENT_ID);
        }
    }

    public Script getScript(Company bank, Long cardType)throws TSException{
        DataQuery query=ThreadContext.getThreadContext().getManager().createQuery("select scr from Company cmp join cmp.scripts scr join scr.cardTypes ct where cmp=:bank and ct.value=:cardType");
        query.setParameter("bank",bank);
        query.setParameter("cardType",cardType);
        List<Script> comps=query.getResult(Script.class);
        if (comps.size()!=1)
            throw new TSException(Errors.UNKNOWN,"Карта не обслуживается.");
        else
            return comps.get(0);
    }

    public ScriptBank getScriptBank(Script script, String bin)throws TSException{
        logger.debug("call getBank with "+script+" bin="+bin+".");
        DataQuery query=ThreadContext.getThreadContext().getManager().createQuery("select scr from ScriptBank scr join scr.bank bnk join bnk.bins bin where scr.script=:script and bin.bin=:bin");
        query.setParameter("script",script);
        query.setParameter("bin",bin);
        List<ScriptBank> comps=query.getResult();
       if (comps.size()==0)
       {
           query=ThreadContext.getThreadContext().getManager().createQuery("select scr from ScriptBank scr join scr.bank bnk where scr.script=:script");
           query.setParameter("script",script);
           comps=query.getResult();
       }
        if (comps.size()==0)
            throw new TSException(Errors.UNKNOWN,"Невозможно определить банковский номер.");
        else
            return comps.get(0);
    }
}
