package com.univ.single;

import java.util.*;

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

    /**
     * 下面演示集合属性的映射，包括数组,list,set,map等
     * 1. 集合属性的映射，相当于伪一对多的关系，所以需要将集合放入另一张中间表中，并引用自此表；
     */

    //数组，数组有下标，因此下标和值都需要存储到数据库中，如此取出来时才能还原
	private String[] strArr;

    /**
     * list，有序，与数组一样 ，需要将索引也存到数据库中；
     * 允许存放重复元素；
     */
	private List<String> list = new ArrayList<String>();

    /**
     * list，无序，因此只需要将值也存到数据库中；
     * 不允许存放重复元素；
     */
	private Set<String> set = new HashSet<String>();

    /**
     * map
     * key与value都需要存放到数据库中
     */
    private Map<String, String> map = new HashMap<String, String>();


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

    public String[] getStrArr() {
        return strArr;
    }

    public void setStrArr(String[] strArr) {
        this.strArr = strArr;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}


