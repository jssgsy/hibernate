package com.univ.single;

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

    /**
     * 派生属性
     *
     * Hibernate中不是每个实体类的属性都要对应表中的一个字段，不对应表中字段的属性称之为派生属性。
     * 派生属性应该定义成包装类型，因为当Hibernate计算的结果为空时，会赋予此属性null值，如果是原始类型，会抛异常。
     */
	private Long count;

    public Single(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Single() {
    }

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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}


