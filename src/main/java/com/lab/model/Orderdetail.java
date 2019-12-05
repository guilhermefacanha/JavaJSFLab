package com.lab.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lab.model.core.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the orderdetails database table.
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orderdetails")
@NamedQuery(name = "Orderdetail.findAll", query = "SELECT o FROM Orderdetail o")
public class Orderdetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private OrderdetailPK id;

	private short orderLineNumber;

	private BigDecimal priceEach;

	private int quantityOrdered;

	// bi-directional many-to-one association to Order
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderNumber", insertable = false, updatable = false)
	private Order order;

	// bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name = "productCode", insertable = false, updatable = false)
	private Product product;

}