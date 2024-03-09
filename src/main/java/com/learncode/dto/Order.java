package com.learncode.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	private int id;
	private String tel;
	private String deliveryAddress;
	private int totalAmount;
	private int totalPaymentAmount;
	private boolean paid;
	private String status;
	private Date createdAt;
	private Date updatedAt;
	private int user;
	private String discountCode; //bo sung edit datatype int -> String de su dung discountCode @Sang
}
