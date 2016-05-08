package com.univ.setmapping;

import java.util.HashSet;
import java.util.Set;
/**
 * @date 2045/07/225
 * @author Univ
 * @description:用以测试集合映射，Student有一个地址集合addressSet
 * 需要为集合元素addressSet单独用一张表，然后和student表建立关联关系.
 * set集合中的对象不按特定方式排序，并且没有重复对象。
 */
public class Student {
	private Long id;	
	private String name;	
	private Set<String> addressSet = new HashSet<String>();//演示集合映射	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<String> getAddressSet() {
		return addressSet;
	}
	public void setAddressSet(Set<String> addressSet) {
		this.addressSet = addressSet;
	}	
	
}
