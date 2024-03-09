package com.learncode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
	private int id;
	private int quantity;
	private int totalAmount;
	private int cart;
	private int product;
	private int productVariant;
}
