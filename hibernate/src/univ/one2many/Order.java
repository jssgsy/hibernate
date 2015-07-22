package univ.one2many;
/** 
 * @author: liuml
 * @date: 2015年7月22日 上午8:58:41 
 * @version: 1.0 
 * @description: 
 */

public class Order {

	private Long id;
	private String orderNumber;
	private Customer customer;//建立多对一的关系
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
}


