package com.transactions.input;

import com.sigma.utils.jpa.DataManager;
import com.transactions.db.DataBase;
import com.transactions.model.CardType;
import com.transactions.model.Point;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 25.04.13
 * Time: 22:05
 */
public class SystemData {

    public static void main(String[] args) throws Exception{


        DataBase db = null;
        if (args.length>0)
            db=new DataBase(args[0], args[1], args[2], "UTF-8");
        else
            db=new DataBase("jdbc:mysql://localhost/test2", "root", "root", "UTF-8");
        db.init();
        DataManager em = db.getDb();
        em.begin();
        try{

//            List<CardType> list = em.createQuery("select c from CardType c").getResult(CardType.class);
//            System.out.println(list);

            CardType card = new CardType();
            card.setValue(3);
            card.setName("АмериканЭкспресс");
            em.persist(card);
    
            card = new CardType();
            card.setValue(4);
            card.setName("ВИЗА");
            em.persist(card);
    
            card = new CardType();
            card.setValue(5);
            card.setName("МАСТЕР КАРТ");
            em.persist(card);
    
            card = new CardType();
            card.setValue(6);
            card.setName("карты МАЭСТРО МастерКарт");
            em.persist(card);

            em.flush();
        }catch (PersistenceException e)
        {
            e.printStackTrace();
            System.out.println("warning "+e.getMessage());
            //значит уже есть
        }




        em.commit();
    }
}
