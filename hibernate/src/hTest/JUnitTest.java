package hTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.hibernate.Criteria;

import univ.entity.Student;
import univ.entity.Teacher;
import univ.util.HibernateUtil;

public class JUnitTest {
	private static Session session;
	static {
		session = HibernateUtil.getSession();
	}
	
	/**
	 * 
	 */
	@Test
	public void save(){//必须要transaction
		Student stu = new Student();
		stu.setName("jjj");
		stu.setAge(26);		
		Set<String> set = new HashSet<String>();
		set.add("湖北");
		set.add("湖南");
		set.add(null);
		stu.setAddressSet(null);
		Teacher teacher = (Teacher) session.get(Teacher.class, new Long(5));
		
		//session.save(teacher);//先保存一的一方，其实就是说因为teacher在Student的一部分，需要先持久化
		stu.setTeacher(teacher);
		
		Transaction transaction = session.beginTransaction();
		session.save(stu);		
		transaction.commit();
	}
	
	@Test
	public void saveTeacher(){
		Transaction transaction = session.beginTransaction(); 
		Teacher teacher = new Teacher();
		teacher.setName("号楼");
		teacher.setDepartment("重庆");
		teacher.setSalary((float) 4000.5);
		session.save(teacher);
		transaction.commit();
	}
	@Test
	public void delete(){//必须要transaction
		Transaction transaction = session.beginTransaction();
		Student stu = (Student) session.get(Student.class, new Long(3));
		System.out.println("stu.name: " + stu.getName());
		session.delete(stu);
		transaction.commit();
	}
	
	@Test
	public void update(){//必须要transaction
		Transaction transaction = session.beginTransaction();
		Student stu =(Student) session.get(Student.class, new Long(4));
		stu.setName("aaaa");
		session.update(stu);
		transaction.commit();
	}
	
	@Test
	public void get(){//按照主键查找，transaction不必要
		//Student stu = (Student) session.get(Student.class, new Long(1));
		Criteria  criteria = session.createCriteria(Student.class);
		HashMap<String , String> map = new HashMap<String, String>();
		map.put("name", "lml");
		List<Student> stus = criteria.add(Restrictions.eq("name","lml")).add(Restrictions.eq("age",24)).list();
		for(Student s : stus){
			System.out.println("s.age: "+ s.getAge());
		}
	}
	
	@Test
	public void hqlTotal(){//查询全部
		String hql = "select s from Student s";
		List<Student> list = session.createQuery(hql).list();
		for(Student stu : list){
			System.out.println("id: "+stu.getId() + "  name: "
								+ stu.getName() + "  age: " + stu.getAge());
		}		
	}
	
	@Test 
	public void hqlSome(){//where
		String hql = "from Student where name = 'lml' and age = 24";
		List<Student> list = session.createQuery(hql).list();
		for(Student stu : list){
			System.out.println("id: "+stu.getId() + "  name: "
								+ stu.getName() + "  age: " + stu.getAge());
		}
	}
	
	@Test
	public void manyToOne(){
		/*String hql = "select student.name from Student student,Teacher teacher where student.teacher.id = teacher.id";
		List<String> list = session.createQuery(hql).list();
		for(String stu : list){
			System.out.println(stu);
		}*/
		
		//由多找一
		String hql = "select student.teacher from Student student where student.id = 2";
		List<Teacher> list = session.createQuery(hql).list();
		for(Teacher stu : list){
			System.out.println(stu.getName());
		}
	}
	
	@Test
	public void oneToMany(){
		/*Teacher teacher = (Teacher) session.get(Teacher.class, new Long(5));
		String hql = "select s from Student s, Teacher t where t.id = ? and s.teacher.id = t.id";
		Query query = session.createQuery(hql);
		query.setLong(0, teacher.getId());
		List<Student> list = query.list();
		for(Student stu : list){
			System.out.println(stu.getName());
		}*/
		
		//由一找多
		Teacher teacher = (Teacher) session.get(Teacher.class, new Long(5));
		String hql = "select t.stus from Teacher t where t.id = 7";
		List<Student > list = session.createQuery(hql).list();			
		for(Student s : list){
			System.out.println(s.getName());
		}
		
	}
	
	@Test
	public void getAddressSet(){//集合查询		
		
		//通过查找某个学生，然后找到其对应的地址
		String hql = "from Student s where s.id = 10";
		List<Student> list = session.createQuery(hql).list();
		for(Student stu : list){
			Set<String> set = stu.getAddressSet();		
			for(String add : set){
				System.out.println(add);
			}
		}
	}
	
	@Test
	public void mulColumn(){
		//通用方法
		String hql = "select s.id,s.name from Student s";
		List<Object[]> list = session.createQuery(hql).list();
		for(Object obj : list){
//			Object[] objs = (Object[])obj;
//			System.out.println(objs[0] + "  " + objs[1]);
			System.out.println(Arrays.toString((Object[])obj) );
			
		}
		
		
		//专属
		/*String hql = "select new Student(s.id,s.name) from Student s ";
		List<Student> list = session.createQuery(hql).list();
		for(Student stu : list){
			System.out.println(stu.getId()+ "   " + stu.getName());
		}*/
	}
}
