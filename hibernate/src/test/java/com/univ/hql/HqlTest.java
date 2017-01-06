package com.univ.hql;

/**
 * Univ
 * 2017/1/6 10:55
 */

/**
 * 测试Hibernate hql的相关知识，这里的查询基于其它类.
 */

import com.univ.one2many.Order;
import com.univ.single.Single;
import com.univ.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.List;

/**
 * 使用hql的大致步骤
 * 1. 创建查询语句queryString;
 * 2. 利用session创建Query对象，session.createQuery(queryString);
 * 2. 为Query对象的参数赋值(如果有参数)，setString,setInteger等
 * 3. 调用Query对象的查询方法，如list等；
 */
public class HqlTest {

    private Session session = HibernateUtil.getSession();
    private Transaction transaction = null;

    /**
     * 单表查询，不带参数
     *
     * 类使用全路径名(在没有二义性的情况下可以只使用类名)；
     */
    @Test
    public void test1(){
        //等价于"select s from com.univ.single.Single s";
        String queryString = "from com.univ.single.Single";
        Query query = session.createQuery(queryString);
        List<Single> list = query.list();
        for (Single s:list) {
            System.out.println(s.getId());
        }
    }

    /**
     * 单表查询，带参数
     * 1. 参数和“:”之间不能有空格，:ageParam必须挨着写
     */
    @Test
    public void test2(){
        /*
        age：Single类的属性；
        ageParam：相当于占位符，名字任取
         */
        String queryString = "from com.univ.single.Single where age = :ageParam";
        Query query = session.createQuery(queryString);
        query.setInteger("ageParam", 21);
        List<Single> list = query.list();
        for (Single s:list) {
            System.out.println(s.getId());
        }
    }

    /**
     * 单表查询，带参数
     * 1. 为类名取别名是一个好的习惯;
     * 2. Query支持链式调用；
     */
    @Test
    public void test3(){
        String queryString = "from com.univ.single.Single where name = :name and age = :ageParam";
        Query query = session.createQuery(queryString)
                .setString("name", "aaa")
                .setInteger("ageParam", 21);
        List<Single> list = query.list();
        for (Single s:list) {
            System.out.println(s.getId());
        }
    }

    /**
     * 关联查询
     *
     * 使用字符串拼接时关键字(如这里的from和where)前后最好空格，否则会导致sql语句的错误;
     * 注意语法：where cus.id = o.customer.id，始终记住：hql是面向对象的查询;
     */
    @Test
    public void test4(){
        String queryString = "select o from " +
                " com.univ.one2many.Customer cus, com.univ.one2many.Order o " +
                "where cus.id = o.customer.id";
        
        Query query = session.createQuery(queryString);
        List<Order> list = query.list();
        for (Order o:list) {
            System.out.println(o.getOrderNumber());
        }
    }


}
