package com.univ.single;

import com.univ.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: liuml
 * @date: 2015年7月20日 上午11:07:00
 * @version: 1.0
 * @description: 测试Session五个最基本的方法
 */

/**
 * Session缓存清理的时机
 * 1. 调用session.flush()方法立即清理缓存；
 * 2. 调用transaction.commit()方法：commit()方法先清理缓存，然后再向数据库提交事务；
 */
public class SingleTest {
    private Session session = HibernateUtil.getSession();
    private Transaction transaction = null;


    @Test
    public void save() {
        transaction = session.beginTransaction();
        Single single = new Single();
        //使用代理主键，并且设置主键生成策略为increment时，程序会忽略setId,所以此句不起作用,仍然是新增而不是修改数据库中id为1的记录
        single.setId((long) 1);
        single.setName("aaa");
        single.setAge(20);
        System.out.println("before session.save()---------------------------");
        /*
         * 只是计划执行，真正的sql执行是在事务提交的时候。
         * 其实此时也会执行sql语句，不过是 select max(id) from single,用于计算插入记录的id。
         *
         * 重点：调用save()后single已经是持久化对象，被纳入了session的缓存中
         */
        session.save(single);
        System.out.println("after session.save()---------------------------");
        /*
         * 此时内存中single对象与session缓存中的对象不同，成了脏对象，清理缓存时会额外执行update语句
         */
        System.out.println("before single.setAge(21);---------------------------");
        single.setAge(0);
        System.out.println("after single.setAge(21);---------------------------");

        /*
        * 先清理缓存，然后提交事务，缓存的清理应该也是事务的一部分！因为清理时会涉及到对数据库的修改
        */
        transaction.commit();
        /*
        * session.close();//仅仅只是断开和数据库的连接，不会导致事务的提交
        * 重要参考:http://stackoverflow.com/questions/19396563/does-hibernates-session-close-automatically-rollback-uncommitted-transactions
        */
        session.clear();
    }

    @Test
    public void delete() {
        Single single = new Single();
        /**
         * 既可以删除持久化对象，也可以删除游离对象，如果传入的是游离对象，会先将其和当前session关联，使之成为持久化对象
         */
        single.setId((long) 4);//如果数据库中存在id=4的记录，也将被删除

        transaction = session.beginTransaction();
        /**
         * 也是在事务提交时才真正执行sql语句;
         */
        System.out.println("before session.delete()---------------------------");
        session.delete(single);//计划执行sql语句
        System.out.println("after session.delete()---------------------------");
        transaction.commit();
        session.close();
    }

    /**
     * 注意<class>标签中的select-before-update属性，默认为false。
     * 当然只对更新游离对象时起作用(更新前select一次就是为了将对象加入缓存中，然后比较属性是否发生变化，
     * 现在既然已经都在缓存中了就没必要再select一次)。
     */
    @Test
    public void update() {
        transaction = session.beginTransaction();
        Single single = (Single) session.get(Single.class, (long) 1);
        System.out.println(single.getName());
        single.setName("vvvv");//注意，如果需要看到输出语句的变化，这里需要改一下name的值。
        /*重点：
		 * 调用get（或者load方法）方法先到session缓存中是否有id为1的Single，找到则返回，且返回最新的！
		 */
        System.out.println();
        Single single2 = (Single) session.get(Single.class, (long) 1);
        System.out.println(single2.getName() + "...........");
        session.update(single);//有无此句结果是一样的,因为single就是一个持久化对象
        System.out.println("after session.update(single);");

        //此时将发出update语句
        /*Single s = new Single();
        s.setId((long)1);
        s.setName("yuiop");
        session.update(s);*/

        transaction.commit();
        session.close();
    }

    /**
     * load()采取的是默认的延迟检索策略，但受class级别检索方式的影响(如果设置class级别lazy=false，load()将采用立即检索)
     * 如果加载一个对象是为了删除它或者和别的对象建立关联关系，用load；
     * 只有在访问single的非id属性时，才会执行select语句，否则整个过程（即使事务提交，session清除缓存）都不会有select语句的执行；
     */
    @Test
    public void load() {
        transaction = session.beginTransaction();
        Single single = (Single) session.load(Single.class, (long) 1);
        System.out.println("after session.load()---------------------------");
        System.out.println(single.getId());//此时不会执行select语句
        System.out.println("after single.getId()----------------");
        System.out.println(single.getName());//此时才会执行select语句,否则即使事务提交也不会执行真正的sql语句
        System.out.println("after single.getName()---------------------------");
        transaction.commit();
        session.close();
        System.out.println(single.getName());//此时不会有输出，session已经关闭
    }

    /**
     * get()采取的是立即检索策略，且还不受class级别设置的检索方式的影响，即get()始终采用立即检索
     * 如果加载一个对象是为了访问它的属性，用get
     */
    @Test
    public void get() {
        transaction = session.beginTransaction();
        Single single = (Single) session.get(Single.class, (long) 1);//立即执行select语句
        System.out.println("after session.get()---------------------------");
        transaction.commit();
        session.close();
        System.out.println(single.getName());
    }


    @Before
    public void setUp() throws Exception {
        System.out.println("begin testing =============================");
        System.out.println();
    }

    @After
    public void tearDown() throws Exception {
        System.out.println();
        System.out.println("end testing =============================");
    }


}


