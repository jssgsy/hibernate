package com.univ.one2many;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.univ.util.HibernateUtil;

/** 
 * @author: liuml
 * @date: 2015年7月22日 上午9:14:31 
 * @version: 1.0 
 * @description: 
 */

public class One2ManyTest {
	
	private Session session = HibernateUtil.getSession();
	private Transaction transaction = null;
	
	/**
	 * 如果设置了关联关系，则在保存关联方之前，需要先将被关联对象保存到数据库中，（否则此时被关联对象还是临时对象），
	 * 不能数据库中的记录引用一个临时对象。如果想在持久化对象时也自动持久化其所关联的对象，可以通过设置cascade=save-update.在一对多多对一都适用。
	 * 下面演示正确的做法：同时关联关系，同时保存，并将one方的set的inverse属性设为true
	 */
	@Test
	public void saveCustomerAndOrder(){
		transaction = session.beginTransaction();
		Customer customer = new Customer();
		customer.setName("name01");
		
		Order order = new Order();
		order.setOrderNumber("num01");
		
		customer.getOrders().add(order);
		order.setCustomer(customer);
		session.save(customer);
		session.save(order);
		transaction.commit();
		session.close();
	}
	
	@Test
	public void deleteCustomer(){
		transaction = session.beginTransaction();
		Customer customer = (Customer) session.load(Customer.class, (long)1);
		session.delete(customer);
		transaction.commit();
		session.close();
	}
	
	@Test
	public void deleteOrder(){
		transaction = session.beginTransaction();
		Order order = (Order) session.load(Order.class, (long)1);
		session.delete(order);
		transaction.commit();
		session.close();
	}
	
	@Test
	public void update(){
		
	}
	
	@Test
	public void load(){
		
	}
	
	@Test
	public void getFromCustomerToOrder(){
		
	}
	
	@Test
	public void getOrderToCustomer(){
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Before
	public void setUp() throws Exception {
		System.out.println("before testing=========================");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("after testing=========================");
	}

	
}


