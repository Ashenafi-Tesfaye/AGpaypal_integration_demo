package com.AG.paypal.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

/**
 * Servlet implementation class ExecutePaymentServlet
 */
@WebServlet("/ExecutePaymentServlet")
public class ExecutePaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public ExecutePaymentServlet() {
      
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws 
	ServletException, IOException {
		
		String paymentId = request.getParameter("paymentId");
		String payerId = request.getParameter("payerId");
		
		try {
			 PaymentService paymentService = new PaymentService();
			 Payment payment = paymentService.executePayment(paymentId, payerId);
			 
			 PayerInfo payerInfo = payment.getPayer().getPayerInfo();
			 Transaction transaction = payment.getTransactions().get(0);
			 
			 request.setAttribute("payer", payerInfo);
			 request.setAttribute("transaction", transaction);
			 
			 request.getRequestDispatcher("receipt.jsp").forward(request, response);
			 
			 
		} catch(PayPalRESTException ex) {
			
			request.setAttribute("errorMEssage", ex.getMessage());
			ex.printStackTrace();
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

}
