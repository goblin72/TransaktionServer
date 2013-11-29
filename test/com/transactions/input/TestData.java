package com.transactions.input;

import com.sigma.utils.jpa.DataManager;
import com.transactions.db.DataBase;
import com.transactions.model.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 20.04.13
 * Time: 20:32
 */
public class TestData
{
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        System.out.println(context);
        DataBase db = (DataBase) context.getBean("dataBase");
        DataManager em = db.getDb();

        em.begin();

        try{
            Company company = new Company();
            company.setCode("CMP1");
            company.setComment("тестовая компания");
            company.setContract("Договор 01");
            company.setInn("012345");
            company.setName("ООО Ромашка");
            company.setStatus(Company.Status.ENABLE);
            company.setEmail("goblin2372@mail.ru");
            em.persist(company);

            Point point = new Point();
            point.setPointId("point1");
            point.setStatus(Point.Status.TEST);
            point.setCompany(company);
            em.persist(point);

            point = new Point();
            point.setPointId("point2");
            point.setStatus(Point.Status.INACTIVE);
            point.setCompany(company);
            em.persist(point);

            Company company2 = new Company();
            company2.setCode("упро");
            company2.setComment("тестовая компания2");
            company2.setContract("Договор 02");
            company2.setInn("012345");
            company2.setName("ООО Ромашка2");
            company2.setStatus(Company.Status.DISABLE);
            company2.setEmail("taranyuk@mail.ru");
            em.persist(company2);

            Company company3 = new Company();
            company3.setCode("ФЫВА");
            company3.setComment("тестовая компания3");
            company3.setContract("Договор 02");
            company3.setInn("012345");
            company3.setName("ООО Ромашка2");
            company3.setStatus(Company.Status.DISABLE);
            company3.setEmail("taranyuk@mail.ru");
            em.persist(company3);

            point = new Point();
            point.setPointId("point3");
            point.setStatus(Point.Status.BLOCKED);
            point.setCompany(company2);
            em.persist(point);

            point = new Point();
            point.setPointId("point4");
            point.setStatus(Point.Status.ACTIVE);
            point.setCompany(company);
            List<Company> partners = new ArrayList<Company>();
            partners.add(company);
            partners.add(company2);

            point.setPartners(partners);
            em.persist(point);

            em.flush();
        }catch (PersistenceException e)
        {
            System.out.println("ERROR "+e.getMessage());
        }

        try{
            Bank bank = new Bank();
            bank.setCheckTemplate("check1.html");
            bank.setOutputCode("TEST");
            em.persist(bank);
            
            IP ip = new IP();
            ip.setHost("localhost");
            ip.setPort(8080);
            ip.setBank(bank);
            em.persist(ip);

            BIN bin = new BIN();
            bin.setBin("323456");
            bin.setBank(bank);
            em.persist(bin);

            bin = new BIN();
            bin.setBin("423456");
            bin.setBank(bank);
            em.persist(bin);

            bank = new Bank();
            bank.setCheckTemplate("check1.html");
            bank.setOutputCode("SOME");
            em.persist(bank);

            ip = new IP();
            ip.setHost("localhost");
            ip.setPort(8080);
            ip.setBank(bank);
            em.persist(ip);

            bin = new BIN();
            bin.setBin("500000");
            bin.setBank(bank);
            em.persist(bin);

            bin = new BIN();
            bin.setBin("423456");
            bin.setBank(bank);
            em.persist(bin);
            
            em.flush();
        }catch (Exception e)
        {
            System.out.println("ERROR "+e.getMessage());
        }

        try{
            Script script = new Script();

            List<CardType> ct= new ArrayList<CardType>();
            ct.add((CardType) em.createQuery("select sc from CardType sc where sc.value=3").getSingle());
            ct.add((CardType) em.createQuery("select sc from CardType sc where sc.value=4").getSingle());
            script.setCardTypes(ct);
            em.persist(script);

            List<Bank> banks = new ArrayList<Bank>();
            banks.addAll(em.createQuery("select sc from Bank sc").getResult(Bank.class));
            em.flush();
            for (Bank b:banks)
            {
                ScriptBank sb = new ScriptBank();
                sb.setBank(b);
                sb.setScript(script);
                sb.setEmplId("empl1");
                sb.setTerminalId("term1");
                em.persist(sb);
            }

            em.flush();
        }catch (Exception e)
        {
            System.out.println("ERROR "+e.getMessage());
        }

        try{
            List<Company> comp = em.createQuery("select sc from Company sc").getResult(Company.class);
            List<Script> ct= new ArrayList<Script>();
            ct.addAll(em.createQuery("select sc from Script sc").getResult(Script.class));

            for (Company com:comp)
            {
                com.setScripts(ct);
                em.merge(com);
            }
            em.flush();
        }catch (PersistenceException e)
        {
            System.out.println("ERROR "+e.getMessage());
        }

        em.commit();
    }
}
