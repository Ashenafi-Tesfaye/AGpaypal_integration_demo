package com.AG.paypal.service;

import java.util.ArrayList;
import java.util.List;

import com.AG.paypal.model.OrderDetail;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

public class PaymentService {
	
	private static final String CLIENT_ID ="ATurzGgequZFZKLvc3sg5NilLoqhl-kUG5vDkzjf6grsvPW16oTFiQLVmYA-Yffp9qxoEbHXzTvuZX_5";
	private static final String CLIENT_SECRET ="EJ4Sm2Fiswks6ojan9Ad-ENBLFBZxHELwyYsJALlk7qETyUPYSlfCc326phZcGRmzaAiYsrprQtNXCwL";
	private static final String MODE= "sandbox";
	
	public String authorizePayment(OrderDetail orderDetail) throws PayPalRESTException{
		
			Payer payer = getPayerInformation();
			RedirectUrls redirectUrls = getRedirectUrls();
			
			List<Transaction> listTransaction = getTransactionInformation(orderDetail);
			
			Payment requestPayment = new Payment();
			requestPayment.setTransactions(listTransaction);
			requestPayment.setRedirectUrls(redirectUrls);
			requestPayment.setPayer(payer);
			requestPayment.setIntent("authorize");
			
			APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
			
			Payment approvedPayment = requestPayment.create(apiContext);
			
			return getApprovalLink(approvedPayment);
			
	}

	private Payer getPayerInformation() {
		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");
		
		PayerInfo payerInfo = new PayerInfo();
		payerInfo.setFirstName("William")
		.setLastName("Peterson")
		.setEmail("william.peterson@company.com");
		
		payer.setPayerInfo(payerInfo);
		
		return payer;
	}
	
	private RedirectUrls getRedirectUrls() {

			RedirectUrls redirectUrls = new RedirectUrls();
			redirectUrls.setCancelUrl("http://localhost:8080/Agpaypal_payment_integration/cancel.html");
			redirectUrls.setReturnUrl("http://localhost:8080/Agpaypal_payment_integration/review_payment.html");
			
		return redirectUrls;
	}
		
	private List<Transaction> getTransactionInformation(OrderDetail orderDetail) {

		Details details = new Details();
		details.setShipping(orderDetail.getShipping());
		details.setSubtotal(orderDetail.getSubTotal());
		details.setTax(orderDetail.getTax());
		
		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal(orderDetail.getSubTotal());
		amount.setDetails(details);
		
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription(orderDetail.getProductName());
		
		ItemList itemList = new ItemList();
		List<Item> items = new ArrayList<>();
		
		Item item = new Item();
		item.setCurrency("USD");
		item.setName(orderDetail.getProductName());
		item.setPrice(orderDetail.getSubTotal());
		item.setTax(orderDetail.getTax());
		item.setQuantity("1");
		
		items.add(item);
		itemList.setItems(items);
		transaction.setItemList(itemList);
		
		List<Transaction> listTransaction = new ArrayList<>();
		listTransaction.add(transaction);
		
		return listTransaction;
	}
	
	
	private String getApprovalLink(Payment approvedPayment) {
		List<Links> links = approvedPayment.getLinks();
		String approvalLink = null;
		
		for (Links link : links) {
			if(link.getRel().equalsIgnoreCase("approval_url")) {
				break;
			}
			
		}
		
		return approvalLink;
	}

	public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
		
		APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
		
		return Payment.get(apiContext, paymentId);
		
	}

	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
		
		PaymentExecution paymentExcution = new PaymentExecution();
		paymentExcution.setPayerId(payerId);
		
		Payment payment = new Payment().setId(paymentId);
		
		APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
		
		return payment.execute(apiContext, paymentExcution);
		
	}

	
	
}
