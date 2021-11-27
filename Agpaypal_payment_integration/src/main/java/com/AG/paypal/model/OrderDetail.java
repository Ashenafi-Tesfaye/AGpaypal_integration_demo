package com.AG.paypal.model;

public class OrderDetail {

	private String productName;
	private float subTotal;
	private float shipping;
	private float tax;
	private float total;
	
	public OrderDetail() {
		
	}



	public OrderDetail(String productName,String subTotal, String shipping, String tax, String total) {

		this.productName = productName;
		this.subTotal = Float.parseFloat(subTotal);
		this.shipping = Float.parseFloat(shipping);
		this.tax = Float.parseFloat(tax);
		this.total = Float.parseFloat(total);
	}



	public String getProductName() {
		return productName;
	}



	public void setProductName(String productName) {
		this.productName = productName;
	}



	public String getSubTotal() {
		return String.format("%.2f", subTotal);
	}


	public String getShipping() {
		return String.format("%.2f", shipping);
	}


	public String getTax() {
		return String.format("%.2f", tax);
	}



	public String getTotal() {
		return String.format("%.2f", total);
	}



	@Override
	public String toString() {
		return "OrderDetail [productName=" + productName + ", subTotal=" + subTotal + ", shipping=" + shipping
				+ ", tax=" + tax + ", total=" + total + "]";
	}
	
	
	
}
