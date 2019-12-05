package com.lab.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.lab.util.ModelUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the orders database table.
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="orders")
@NamedQuery(name="Order.findAll", query="SELECT o FROM Order o")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int orderNumber;

	@Lob
	private String comments;

	@Temporal(TemporalType.DATE)
	private Date orderDate;

	@Temporal(TemporalType.DATE)
	private Date requiredDate;

	@Temporal(TemporalType.DATE)
	private Date shippedDate;

	private String status;

	//bi-directional many-to-one association to Orderdetail
	@OneToMany(mappedBy="order")
	@OrderBy("orderLineNumber")
	private List<Orderdetail> orderdetails;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="customerNumber")
	private Customer customer;
	
	public String getTotal() {
		double total = 0;
		if (ModelUtils.isValidList(orderdetails)) {
			for (Orderdetail od : orderdetails) {
				total += od.getPriceEach().doubleValue() * od.getQuantityOrdered();
			}
		}
		return ModelUtils.formatValue(total);
	}

	public String getTotalQty() {
		double total = 0;
		if (ModelUtils.isValidList(orderdetails)) {
			for (Orderdetail od : orderdetails) {
				total += od.getQuantityOrdered();
			}
		}
		return ModelUtils.formatInteger(total);
	}


	public Orderdetail addOrderdetail(Orderdetail orderdetail) {
		getOrderdetails().add(orderdetail);
		orderdetail.setOrder(this);

		return orderdetail;
	}

	public Orderdetail removeOrderdetail(Orderdetail orderdetail) {
		getOrderdetails().remove(orderdetail);
		orderdetail.setOrder(null);

		return orderdetail;
	}
	
	public String getFormattedComments() {
		if(ModelUtils.isNullEmpty(comments))
			return "N/A";
		
		return comments;
	}

}