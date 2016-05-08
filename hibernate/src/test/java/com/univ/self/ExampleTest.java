package com.univ.self;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import com.univ.util.HibernateUtil;

/** 
 * @author	Univ 
 * @date	2016年5月6日 下午9:42:18 
 * @version 1.0 
*/

public class ExampleTest {

	private Session session = HibernateUtil.getSession();
	private Transaction transaction = null;
	
	/*
	 * 父子关系如下：
	 * e1
	 * 	|
	 * 	|---e3
	 * 	|---e4
	 * 	|----|---e6
	 * 	|----|---e7
	 * e2
	 * 	|---e5
	 */
	
	@Test
	public void add() {
		//定义时便指定parent
		Example e1 = new Example("e1",1,null,null);
		Example e2 = new Example("e2",2,null,null);
		Example e3 = new Example("e3",3,e1,null);
		Example e4 = new Example("e4",4,e1,null);
		Example e5 = new Example("e5",5,e2,null);
		Example e6 = new Example("e6",6,e4,null);
		Example e7 = new Example("e7",7,e4,null);
		
		//为e1添加子类
		Set<Example> c1 = new HashSet<Example>();
		c1.add(e3);
		c1.add(e4);
		e1.setChildren(c1);
		
		//为e2添加子类
		Set<Example> c2 = new HashSet<Example>();
		c2.add(e5);
		e2.setChildren(c2);
		
		//为e4添加子类
		Set<Example> c4 = new HashSet<Example>();
		c4.add(e6);
		c4.add(e7);
		e4.setChildren(c4);
		
		transaction = session.beginTransaction();
		session.save(e1);
//		session.save(e2);
//		session.save(e3);
//		session.save(e4);
//		session.save(e5);
//		session.save(e6);
//		session.save(e7);
		transaction.commit();
		
	}
	
	/*
	 * 通过某个父类获取其子类
	 */
	@Test
	public void get(){
		transaction = session.beginTransaction();
		Example e1 = (Example) session.get(Example.class, (long)1);
		Set<Example> children1 = e1.getChildren();
		System.out.println("e1的children有： ");
		for(Example e : children1){
			System.out.println(e.getId() + " : " + e.getName());
		}
		
		Example e4 = (Example) session.get(Example.class, (long)4);
		Set<Example> children4 = e4.getChildren();
		System.out.println("e4的children有： ");
		for(Example e : children4){
			System.out.println(e.getId() + " : " + e.getName());
		}
		
		Example e2 = (Example) session.get(Example.class, (long)2);
		Set<Example> children2 = e2.getChildren();
		System.out.println("e2的children有： ");
		for(Example e : children2){
			System.out.println(e.getId() + " : " + e.getName());
		}
		
		transaction.commit();
	}
	
	/*
	 * 通过某个child获取其parent
	 */
	@Test
	public void get1(){
		transaction = session.beginTransaction();
		
		Example e3 = (Example) session.get(Example.class, (long)3);
		System.out.println("e3的parent为： " + e3.getParent().getName());
		
		Example e4 = (Example) session.get(Example.class, (long)4);
		System.out.println("e4的parent为： " + e4.getParent().getName());
		
		Example e7 = (Example) session.get(Example.class, (long)7);
		System.out.println("e7的parent为： " + e7.getParent().getName());
		transaction.commit();
	}

}

