package com.transactions.db;

import com.sigma.utils.jpa.DataManager;
import com.transactions.model.Bank;
import com.transactions.model.Company;
import com.transactions.model.Script;
import com.transactions.model.ScriptBank;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 28.04.13
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class DBTest 
{
    public DataBase db;

    @Before
    public void before() {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        System.out.println(context);
        db = (DataBase) context.getBean("dataBase");
        try{

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncoding()
    {
        List<Company> list=db.getDb().createQuery("select cmp from Company cmp where cmp.code='ФЫВА'").getResult();
        System.out.println(list);
    }

    @Test
    public void testScript()
    {

        try{
            Script scr=db.getScript(db.getCompany("CMP1"),4l);
            System.out.println(scr);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Test
    public void testBank()
    {

        try{
            Script scr=db.getScript(db.getCompany("CMP1"),4l);
            System.out.println(scr);
            ScriptBank bank =db.getScriptBank(scr, "323456");
            System.out.println(bank);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    
    @Test
    public void myTestUniqueConstraint()
    {
        try{
            DataManager dm=db.getDb();
            dm.begin();
            Script s=dm.getReference(Script.class,1l);
            Bank b=dm.getReference(Bank.class,1l);
            ScriptBank sb = new ScriptBank();
            sb.setBank(b);
            sb.setScript(s);
            sb.setEmplId("test");
            sb.setTerminalId("test");
            dm.persist(sb);
            dm.flush();
            dm.commit();
            dm.close(true);
        }catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
    
    


}
