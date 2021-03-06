package com.univ.hql;

/**
 * Univ
 * 2017/1/6 10:55
 */

/**
 * 测试Hibernate hql的相关知识，这里的查询基于其它类.
 */

import com.sun.org.apache.xpath.internal.operations.Or;
import com.univ.one2many.Order;
import com.univ.self.Example;
import com.univ.single.Single;
import com.univ.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.Iterator;
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
     * list()方法只支持session缓存写入，不支持读取。见test14
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

    /**
     * uniqueResult()：如果确保只能查找到一个结果，可使用此快捷方法(否则得list.get(0))
     *
     * 找到多个结果抛出异常;
     * 一个结果没有找到返回null;
     */
    @Test
    public void test12(){
        Query query = session.createQuery("from com.univ.self.Example where id = 11");
        Example example = (Example) query.uniqueResult();
        System.out.println(example.getName());
    }

    /**
     * Hibernate分页操作
     *  setFirstResult(index)——从id为(index+1)处开始查找,index从0开始
     *  setMaxResults(num)——一次最多查找几条
     *
     *      两个方法的参数分别对应于limit子句的两个参数，不过查询时实际上是从index+1的id处开始查找，
     *      之所以如此，可能是因为面向对象中索引都是从0开始，但数据库中id值最小为1
     *
     */
    @Test
    public void test13(){
        Query query = session.createQuery("from com.univ.self.Example");
        //从id为1的记录开始查找，最多查找3条
        query.setFirstResult(0);
        query.setMaxResults(3);
        List<Example> list = query.list();
        for (Example example:list) {
            System.out.println(example.getName());
        }
    }

    /**
     * list()
     * 1. 采取立即检索策略
     * 2. 只支持往session缓存中写入，
     * 3. 不支持从session缓存中读(想想也是，因为list()是一次查找所有的数据，不应该到session中一个一个去比较)
     */
    @Test public void test14(){
        Query query = session.createQuery("from com.univ.self.Example");
        //1. 会立刻发送sql查询语句
        List list = query.list();
        System.out.println("------------");

        //2. 此时get()方法不会发送sql语句，因为list()方法支持往session缓存中写入
        System.out.println("before get()...");
        session.get(Example.class, (long) 1);
        System.out.println("after get()...");

        //3。 此时会重新发送sql查询语句，即list()不支持session缓存读
        List list1 = query.list();
        System.out.println(".........");
    }

    /** iterate()方法
     * 1. 采取是延迟检索策略；
     * 2. 查询出的结果会放入session缓存中(即支持session缓存的写入)；
     * 3. 查询之前会先到session缓存中查看是否已经存在(即支持session的读取)；
     *
     * 注意和list()方法在session缓存方面的区别
     * */
    @Test public void test15(){
        Query query = session.createQuery("from com.univ.self.Example");
        //iterate()是延迟检索，不过这里会先检索出所有的id
        Iterator iterate1 = query.iterate();
        System.out.println("after iterate()...");

        while (iterate1.hasNext()) {
            Example example = (Example) iterate1.next();
            //1. iterate采用的是延迟检索，直到调用getName()方法时才真正发出sql查询语句
            System.out.println(example.getName());
        }

        //2. 此时get()方法不会发送sql语句，因为iterate()方法支持往session缓存中写入
        System.out.println("before get()...");
        session.get(Example.class, (long) 1);
        System.out.println("after get()...");

        //3. 此时不会再发送sql查询语句，因为iterate方法支持session缓存读
        Iterator iterate2 = query.iterate();
        while (iterate2.hasNext()) {
            Example example = (Example) iterate2.next();
            System.out.println(example.getName());
        }
    }

    /**
     * hql的模糊查询
     *
     * 其实也就是参数绑定的问题.
     * 注意应该将匹配符在绑定参数时设置，而不要放到like子句中。
     */
    @Test
    public void test16(){
        //这里没有“%”
        Query query = session.createQuery("from com.univ.single.Single where name like :name");

        //“%”出现在这里
        query.setString("name", "%b%");
        List<Single> list = query.list();
        for (Single s:list) {
            System.out.println(s.getName());
        }
    }

    /**
     * hql的集合查询(in)  setParameterList(...)
     *
     * 其实也就是参数绑定的问题.不过：
     *  1. 参数(:name)需要用括号括起来；
     *  2. 既然是多个值，所以可以使用集合或者数组。
     */
    @Test
    public void test17(){
        //注意这里需要将“:name”括号起来
        Query query = session.createQuery("from com.univ.single.Single where id in (:name)");

        //这里用的数组存储多个值
        query.setParameterList("name", new Object[]{(long) 1, (long)2, (long)3,(long)4});

        //这里用集合存储多个值
        /*List list1 = new ArrayList();
        list1.add((long)1);
        list1.add((long)2);
        list1.add((long)3);
        list1.add((long)4);
        query.setParameterList("name", list1);*/

        List<Single> list = query.list();
        for (Single s:list) {
            System.out.println(s.getName());
        }
    }

    /**
     * hql 聚合函数的使用
     * 1. 因为只有一个结果，可以使用uniqueResult()方法；
     * 2. 在强制类型转换时，id是什么类型则能转成什么类型,如id是Long类型，则不能转换成Integer类型；
     * 3. 因为上述2的缘故，一般转换成Number而不是具体的类型；
     */
    @Test
    public void test18(){
        Query query = session.createQuery("select count(*) from com.univ.single.Single");

        //id的类型是Long，所以uniqueResult()只能转成Long而不能是Integer
        //long count = ((Long)query.uniqueResult()).intValue();

        //转成Number是最好的，因为不论是Long还是Integer都可以接收
        int count = ((Number)query.uniqueResult()).intValue();
        System.out.println(count + "............");
    }

    /**
     * hql在多对一关联查询采取是迟延检索
     */
    @Test
    public void test19(){

        Query query = session.createQuery("from com.univ.one2many.Order");
        System.out.println("before list()");
        List<Order> list = query.list();
        System.out.println("after list()");

        //此时不会触发对一方Customer表的查询
        System.out.println(list.get(0).getOrderNumber());
        System.out.println(list.get(0).getCustomer().getId());

        //此时将真正触发对一方Customer表的查询
        System.out.println(list.get(0).getCustomer().getName());
    }

}
