package com.univ.single;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.univ.util.HibernateUtil;

/** 
 * @author: liuml
 * @date: 2015年7月20日 上午11:07:00 
 * @version: 1.0 
 * @description: 测试五个最基本的方法
 */

public class SingleTest {
	private Session session = HibernateUtil.getSession();
	private Transaction transaction = null;
	

	@Test
	public void save() {
		Single single = new Single();
		single.setName("bbb");
		single.setAge(20);		
		transaction = session.beginTransaction();
		
		System.out.println("before session.save()---------------------------");
		/**
		 * 只是计划执行，真正的sql执行是在事务提交的时候。
		 * 其实此时也会执行sql语句，不过是 select max(id) from T_Single,用于计算插入记录的id
		 */
		session.save(single);
		System.out.println("after session.save()---------------------------");
		
		/**重点：
		 * 如果在save方法后又修改了single的属性，这会使得session在清理缓存时额外执行update语句。update方法不会。
		 * update T_Single set name=?, age=?, date=? where id=?
		 */
		System.out.println("before single.setAge(21);---------------------------");
		single.setAge(21);
		System.out.println("after single.setAge(21);---------------------------");
		
		transaction.commit();
		session.close();
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
		
		Single single = (Single)session.get(Single.class, (long)1);
		System.out.println(single.getName());
		single.setName("vvvv");//注意，如果需要看到输出语句的变化，这里需要改一下name的值。
		
		
		/*重点：
		 * 此时调用get（或者load方法）方法时，因为其支持session级别的读，因此这里或到session缓存中是否有
		 * id为1的Single，显然找到，但调用single2.getName()时不会进行脏检查，但获得的结果是正确的，因为内存中的对象的name值
		 * 已经被改成了fds
		 */
		Single single2 = (Single) session.get(Single.class, (long)1);
		System.out.println(single2.getName()+"...........");
		
		session.update(single);//有无此句结果是一样的
		System.out.println("after session.update(single);");
		transaction.commit();
		session.close();
	}
	
	/**
	 * load()采取的是默认的延迟检索策略，如果加载一个对象是为了删除它或者和别的对象建立关联关系，用load；
	 * 只有在访问single的非id属性时，才会执行select语句，否则整个过程（即使事务提交，session清除缓存）都不会有select语句的执行；
	 */
	@Test
	public void load() {
		transaction = session.beginTransaction();
		
		Single single = (Single)session.load(Single.class, (long)1);
		System.out.println("after session.load()---------------------------");	
		System.out.println(single.getId());//此时不会执行select语句
		System.out.println("after single.getId()----------------");
		System.out.println(single.getName());//此时才会执行select语句,否则即使事务提交也不会执行真正的sql语句
		System.out.println("after single.getName()---------------------------");	
		transaction.commit();
		session.close();
	}
	
	/**
	 * get()采取的是立即检索策略，如果加载一个对象是为了访问它的属性，用get
	 */
	@Test
	public void get() {
		transaction = session.beginTransaction();
		
		session.get(Single.class, (long)1);//立即执行select语句
		System.out.println("after session.get()---------------------------");
		
		transaction.commit();
		
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


