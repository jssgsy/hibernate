package univ.one2many;

import java.util.HashSet;
import java.util.Set;

/** 
 * @author: liuml
 * @date: 2015年7月22日 上午8:56:55 
 * @version: 1.0 
 * @description: 用以测试一对多，多对一的各种属性操作，Customer和Order是一对多关系。
 */

public class Customer {
	private Long id;
	private String name;
	private Set<Order> orders = new HashSet<Order>();//声明的时候就初始化，可以提高健壮性，避免应用程序访问值为null的orders而抛异常
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
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
}


