package com.lab.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.lab.model.core.BaseEntity;
import com.lab.util.ModelUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the products database table.
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
public class Product extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String productCode;

	private BigDecimal buyPrice;

	private BigDecimal msrp;

	@Lob
	private String productDescription;

	private String productLine;

	private String productName;

	private String productScale;

	private String productVendor;

	private short quantityInStock;

	// bi-directional many-to-one association to Orderdetail
	@OneToMany(mappedBy = "product")
	@OrderBy("id.orderNumber desc")
	private List<Orderdetail> orderdetails;

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
		orderdetail.setProduct(this);

		return orderdetail;
	}

	public Orderdetail removeOrderdetail(Orderdetail orderdetail) {
		getOrderdetails().remove(orderdetail);
		orderdetail.setProduct(null);

		return orderdetail;
	}

	@Override
	public Object getId() {
		return productCode;
	}

}