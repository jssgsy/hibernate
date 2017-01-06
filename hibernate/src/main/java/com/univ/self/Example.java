package com.univ.self;

import java.util.HashSet;
import java.util.Set;

/** 
 * @author	Univ 
 * @date	2016年5月6日 下午9:30:02 
 * @version 1.0 
 * 演示双向一对多自身关联
*/

/**
 * 虽然只有一个类，但仍然需要parent和children两个属性，不能只有children;
 */

public class Example {
	private Long id;
	private String name;
	private Integer age;
	
	private Example parent;
	private Set<Example> children = new HashSet<Example>();
	
	public Example(){
		
	}
	
	public Example(String name, Integer age, Example parent, Set<Example> children){
		this.name = name;
		this.age = age;
		this.parent = parent;
		this.children = children;
	}
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
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Example getParent() {
		return parent;
	}
	public void setParent(Example parent) {
		this.parent = parent;
	}
	public Set<Example> getChildren() {
		return children;
	}
	public void setChildren(Set<Example> children) {
		this.children = children;
	}
	
	
}

