package com.univ.one2one;

import com.univ.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

/**
 * Univ
 * 2017/1/10 19:27
 */

/**
 * 单向的一对一关联是存在的。
 *
 * 单向的一对一关联就是退化的多对一关联，和多对一配置唯一的区别是需要在many-to-one上设置unique=true
 * 注意，双向的一对一关联与双向的多对一关联在一方的配置有较大差别，需要在一方配置one-to-one
 *
 */
public class One2OneTest {
    private Session session = HibernateUtil.getSession();
    private Transaction transaction = null;

    /**
     * 保存不带关联关系的Person与IDCard
     */
    @Test
    public void add(){
        transaction = session.beginTransaction();
        //保存三个身份证
        for (int i = 1; i <= 3; i++) {
            IDCard idCard = new IDCard();
            idCard.setNumber("aaa" + i);
            session.save(idCard);
        }

        for (int i = 1; i <=5; i++) {
            Person p = new Person();
            p.setName("p" + i);
            session.save(p);
        }

        transaction.commit();
        session.close();
    }

    /**
     * 验证同一个idCard只能有一个Person与之关联
     */
    @Test
    public void test2(){
        transaction = session.beginTransaction();
        Person p1 = (Person) session.load(Person.class, 1);
        IDCard i1 = (IDCard) session.load(IDCard.class, 1);

        //关联p1和i1的一对一关系
        System.out.println("before p1.setIdCard(i1)");
        p1.setIdCard(i1);
        System.out.println("after p1.setIdCard(i1)");

        //关联p2与i1，此时将报异常ConstraintViolationException，因为在Person端设置了unique=true
        Person p2 = (Person) session.load(Person.class, 2);
        System.out.println("before p2.setIdCard(i1)");
        p2.setIdCard(i1);
        System.out.println("after p2.setIdCard(i1)");

        transaction.commit();
        session.close();
    }

    /**
     * 测试一对一关联的查找
     * 注意此测试方法需要确保p1与i1已经正确关联。
     */
    @Test
    public void getIDCardFromPerson(){
        Person p1 = (Person) session.load(Person.class, 1);
        System.out.println(p1.getIdCard().getNumber());
    }

    @Test
    public void getPersonFromIDCard(){
        IDCard i1 = (IDCard) session.load(IDCard.class, 1);
        System.out.println(i1.getPerson().getName());
    }

}

