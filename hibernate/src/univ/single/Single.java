package univ.single;

import java.util.Date;

/** 
 * @author: liuml
 * @date: 2015年7月20日 上午11:03:54 
 * @version: 1.0 
 * @description: 用来演示单个类的增删改查，及其hibernate映射到底层的sql执行情况
 */

public class Single {

	private Long id;
	private String name;
	private int age;
	private Date date;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {//设置为private，防止应用程序为主键赋值
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}


