package com.lab.model;

import java.io.Serializable;
import javax.persistence.*;

import com.lab.model.core.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the customers database table.
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="customers")
@NamedQuery(name="Customer.findAll", query="SELECT c FROM Customer c")
public class Customer extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5640896446912638295L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer customerNumber;

	private String addressLine1;

	private String addressLine2;

	private String city;

	private String contactFirstName;

	private String contactLastName;

	private String country;

	private BigDecimal creditLimit;

	private String customerName;

	private String phone;

	private String postalCode;

	private Integer salesRepEmployeeNumber;

	private String state;

	//bi-directional many-to-one association to Order
	@OneToMany(mappedBy="customer")
	@OrderBy("orderDate desc")
	private List<Order> orders;
	
	@Transient
	private List<Product> products;
	
	@Override
	public Object getId() {
		return customerNumber;
	}

	public Order addOrder(Order order) {
		getOrders().add(order);
		order.setCustomer(this);

		return order;
	}

	public Order removeOrder(Order order) {
		getOrders().remove(order);
		order.setCustomer(null);

		return order;
	}

	

}