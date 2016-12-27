package com.univ.one2many;

import com.univ.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

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
     * 重点：
     *  Hibernate因为延迟检索策略为类生成的代理类已经初始化了OID，因此在调用类的getId()方法时仍不会触发select语句；
     *  Hibernate因为延迟检索策略为集合对象(如Customer类的orders集合属性)生成的集合代理类连OID都没有(也没法有，因为尚不知道)，
     *  因此在访问集合中的对象的任何属性都将触发与此类相关的select语句；
     */
    @Test
    public void getOrderFromCustomer(){
        transaction = session.beginTransaction();
        Customer customer = (Customer) session.load(Customer.class, (long) 1);
        System.out.println("...load(customer)...");

        //此时并不会触发对Order表的select语句，此时的orders代理对象还只是一个空壳，连OID都没有
        //Set<Order> orders = customer.getOrders();

        /*
         * 此时将触发两条select语句:
         *  getOrders()访问了Customer的非主键属性，触发Customer的select;
         *  order1.getId()触发Order的select;
         */
        for (Order order : customer.getOrders()) {
            System.out.println("order.getId(): " + order.getId());
        }
        System.out.println("...after for()...");
        transaction.commit();
    }

    @Test
    public void getCustomerFromOrder(){
        transaction = session.beginTransaction();
        Order order = (Order) session.load(Order.class, (long)1);
        System.out.println("...load(order)...");
        /*
         * 此时将触发对Order表的select,因为getCustomer()访问了Order的非主键属性；
         * 注意，这里的getId()并不会触发对Customer表的查询，但getName()会。
         */
        System.out.println("order.getCustomer().getId(): " + order.getCustomer().getId());
        transaction.commit();
    }



    /**
     * 关联Customer与Order对象
     * 前提：在一的一方设置了inverse=true
     * 重点！
     */
	@Test
    public void connectCustomerAndOrder(){
        transaction = session.beginTransaction();
        Customer customer = (Customer) session.load(Customer.class, (long) 1);
        System.out.println("...load(customer)...");
        Order order = (Order) session.load(Order.class, (long) 4);
        /*
         * 1. 此时将触发两条select语句
         *  getOrders()访问了Customer的非主键属性，触发Customer的select;
         *  add()触发Order的select;
         * 2. 重点：不会触发update语句：因为Customer端set标签设置了inverse=true
         *  最佳实践：因为一般都会在一的一方的set标签设置inverse=true，因此在一对多(不论是否双向)关联关系发生变化时需要同时显示在两端修改关系。
         *  (其实有时可以只在多方更新关系即可，但作为最佳实践不要这样做)
         */
        customer.getOrders().add(order);//关联customer端的order,在这里其实可以少，当然最好不要少，见saveCustomerAndOrder()方法
        //session.update(customer);//也不会触发update语句，因为此时customer对象已经是持久化对象。

        /*
         * 必不可少，是最佳实践的体现。
         * 如此才能在清理缓存时根据order对象的customer属性的变化去更新数据库，即才能真正将customer与order对象关联起来
         */
        order.setCustomer(customer);
        transaction.commit();
        session.close();
    }

    /**
     * 保存Customer的同时级联保存Order对象
     * 级联保存通过设置cascade=save-update.在一方和多方都适用。
     * 重点！
     */
    @Test
    public void saveCustomerAndOrder(){
        transaction = session.beginTransaction();
        Customer customer = new Customer();
        customer.setName(new Date().getTime() + "_customer");
        Order order = new Order();
        order.setOrderNumber(new Date().getTime() + "_orderNumber");
		/*
		 * 注意这句不能少，虽然能级联保存order对象，但没法保存两者之间的关联关系，此时Order表中的外键将为null.
		 */
        order.setCustomer(customer);//关联order和customer之间的关系

        //不可少，如此order才是customer的关联对象，也才能级联保存
        customer.getOrders().add(order);

        //此时会发两条select语句，因为Customer配置文件中配置了customer与order的级联保存
        session.save(customer);
        //session.save(order);//order是customer的关联对象，会级联保存
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

    /**
     * 对inverse的补充：
     * 结合connectCustomerAndOrder()方法理解。
     *
     * 注意，如果没在一的一方设置inverse=true,则下面的代码也能维护customer和order之间的关系，因为此时Customer端会发出update语句。
     * 注意在Customer端发生update的形式，因为这里发出的update语句是为了维护两者之间的关系，而关系在数据库中是通过外键体现的。
     * 因此这里更新的其实是Order表中的外键字段。
     *  update T_Order set CUSTOMER_ID=? where ID=?
     */
	@Test
    public void testInverse(){
        transaction = session.beginTransaction();
        Customer customer = (Customer) session.load(Customer.class, (long) 1);
        Order order = (Order) session.load(Order.class, (long) 6);
        customer.getOrders().add(order);
        transaction.commit();
        session.close();
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


