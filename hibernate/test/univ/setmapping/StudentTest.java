package univ.setmapping;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import univ.util.HibernateUtil;

/** 
 * @author: liuml
 * @date: 2015年7月22日 上午10:39:58 
 * @version: 1.0 
 * @description: 
 */

public class StudentTest {

	private Session session = HibernateUtil.getSession();
	private Transaction transaction = null;
	
	/**
	 * 保存student时，会自动保存其集合类型值到T_StudentAddress中
	 */
	@Test
	public void save(){
		transaction = session.beginTransaction();
		Student student = new Student();		
		student.setName("stu01");
		Set<String> addressSet = new HashSet<String>();
		addressSet.add("address1");
		addressSet.add("address2");
		addressSet.add("address3");
		addressSet.add("address4");
		student.setAddressSet(addressSet);
		session.save(student);
		transaction.commit();
		session.close();
	}
	
	/**
	 * 会自动删除与其关联的集合属性表
	 */
	@Test
	public void delete(){
		transaction = session.beginTransaction();
		Student student = (Student) session.load(Student.class, (long)1);
		session.delete(student);
		transaction.commit();
		session.close();
	}	
	
	/**
	 * hibernate对集合映射默认采取的是延迟检索策略,即只有在需要用到集合中的值时才去执行对T_StudentAddress的select语句;
	 * 可以通过Hibernate.initialize()方法显示初始化集合,这样才能保证当student成为游离对象后，可以访问到其集合属性中的值。
	 */
	@Test
	public void load(){
		transaction = session.beginTransaction();
		Student student = (Student) session.load(Student.class, (long)2);
		System.out.println(student.getName());//此时不会查找T_StudentAddress
		student.getAddressSet();//此时不会查找T_StudentAddress
		//student.getAddressSet().iterator();//此时才会查找T_StudentAddress
		Hibernate.initialize(student.getAddressSet());//通过Hibernate.initialize()方法显示初始化集合,立即执行select语句
		transaction.commit();
		session.close();
	}
	
	@Test
	public void get(){
		transaction = session.beginTransaction();
		Student student = (Student) session.load(Student.class, (long)2);
		 Set<String> addressSet = student.getAddressSet();
		 Iterator<String> it = addressSet.iterator();//此时才会查找T_StudentAddress
		 while(it.hasNext()){
			 System.out.println(it.next());
		 }
		transaction.commit();
		session.close();
	}
	
	
	
	@Before
	public void setUp() throws Exception {
		System.out.println("before testing============================");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("after testing============================");
	}

}


