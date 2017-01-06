package com.univ.hql;

/**
 * Univ
 * 2017/1/6 10:55
 */

/**
 * 测试Hibernate hql的相关知识，这里的查询基于其它类.
 */

import com.univ.one2many.Order;
import com.univ.self.Example;
import com.univ.single.Single;
import com.univ.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.List;
import java.util.Map;

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
     * 单表查询，不带参数，查询对象
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
     * 单表查询，只查询一列
     * 重点：返回的List中的对象类型是所查询列的类型
     */
    @Test
    public void test4(){
        String queryString = "select single.name from com.univ.single.Single single " +
                " where single.age = :ageParam";
        Query query = session.createQuery(queryString);
        query.setInteger("ageParam", 21);

        //注意此时List里的对象String类型
        List<String> list = query.list();
        for (String s:list) {
            System.out.println(s);
        }
    }

    /**
     * 单表查询，查询多列
     * 此时List中的对象类型是Object[]
     */
    @Test
    public void test5(){
        String queryString = "select single.name, single.age from " +
                " com.univ.single.Single single where single.age = :ageParam";
        Query query = session.createQuery(queryString);
        query.setInteger("ageParam", 21);

        //注意此时List里的对象是Object[]类型
        List<Object[]> list = query.list();
        for (Object[] o:list) {
            System.out.println(o[0]);
            System.out.println(o[1]);
        }
    }

    /**
     * 单表查询，查询多列，对test5的补充
     *
     * 此时
     * 1. 需要为Single类添加相应的带参构造陈函数，当然无参构造函数也不能少；
     * 2. List中的对象类型是Single
     */
    @Test
    public void test6(){
        String queryString = "select new com.univ.single.Single(single.name, single.age) from " +
                " com.univ.single.Single single where single.age = :ageParam";
        Query query = session.createQuery(queryString);
        query.setInteger("ageParam", 21);

        //注意此时List里的对象是Single类型
        List<Single> list = query.list();
        for (Single s:list) {
            System.out.println(s.getName());
            System.out.println(s.getAge());
        }
    }

    /**
     * 单表查询，查询多列，对test6的补充
     *
     * 1. 注意是new list,小写的list，注意hql没有提供new set；
     * 2. List中list的个数即是查询返回的行数，一行对应一个list
     */
    @Test
    public void test7(){
        String queryString = "select new list (single.name, single.age) from " +
                " com.univ.single.Single single where single.age = :ageParam";
        Query query = session.createQuery(queryString);
        query.setInteger("ageParam", 21);

        //注意此时List里的对象是List类型
        List<List> list = query.list();
        for (List s:list) {
            for (Object o : s) {
                System.out.println(o);
            }
        }
    }

    /**
     * 单表查询，查询多列，对test7的补充
     *
     * 1. 是new map,小写的map;
     * 2. 没有给列名起别名时，map的key为字符串形式的下标，注意是字符串形式的下标，不是数字形式的下标；
     * 3. list中map的个数就是查询返回的行数，即一行数据对应一个map；
     * 4. 此时map的内容：
     *      "0" : single.name;
     *      "1" : single.age
     */
    @Test
    public void test8(){
        //不给列名起别名
        String queryString = "select new map(single.name, single.age) from " +
                " com.univ.single.Single single where single.age = :ageParam";
        Query query = session.createQuery(queryString);
        query.setInteger("ageParam", 21);

        //注意此时List里的对象是Map类型
        List<Map> list = query.list();
        for (Map m:list) {
            /*
            注意这里是get("0),而不是get(0),是字符串形式的下标
             */
            System.out.println(m.get("0") + " : " + m.get("1"));
        }
    }

    /**
     * 单表查询，查询多列，对test8的补充
     *
     * 1. 给列名起别名时，map的key为别名；
     * 2. 此时map的内容：
     *      "name" : single.name;
     *      "age" : single.age
     */
    @Test
    public void test9(){
        //给列名起别名
        String queryString = "select new map(single.name as name, single.age as age) from " +
                " com.univ.single.Single single where single.age = :ageParam";
        Query query = session.createQuery(queryString);
        query.setInteger("ageParam", 21);

        //此时List里的对象是Map类型
        List<Map> list = query.list();
        for (Map m:list) {
            System.out.println(m.get("name") + " : " + m.get("age"));
        }
    }

    /**
     * 关联查询
     *
     * 使用字符串拼接时关键字(如这里的from和where)前后最好空格，否则会导致sql语句的错误;
     * 注意语法：where cus.id = o.customer.id，始终记住：hql是面向对象的查询;
     */
    @Test
    public void test10(){
        String queryString = "select o from " +
                " com.univ.one2many.Customer cus, com.univ.one2many.Order o " +
                "where cus.id = o.customer.id";

        Query query = session.createQuery(queryString);
        List<Order> list = query.list();
        for (Order o:list) {
            System.out.println(o.getOrderNumber());
        }
    }

    /**
     * hql在自身一对多双向关联中的应用，其实和普通的一对多关系一样。
     *
     * 查询所有parent为null的记录，即顶层节点。
     * 下面的查询其实只需要配置多对一单向映射即可。
     */
    @Test
    public void test11(){

        List<Example> list = session.createQuery("from com.univ.self.Example where parent.id is null").list();
        for (Example example:list) {
            System.out.println(example.getName());
        }
    }


}
