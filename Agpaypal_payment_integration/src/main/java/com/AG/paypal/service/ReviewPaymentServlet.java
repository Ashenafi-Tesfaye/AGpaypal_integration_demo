package com.AG.paypal.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

/**
 * Servlet implementation class ReviewPaymentServlet
 */
@WebServlet("/review_payment")
public class ReviewPaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ReviewPaymentServlet() {
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		String paymentId = request.getParameter("paymentId");
		String payerId = request.getParameter("payerId");
		
		try {
				PaymentService paymentService = new PaymentService();
				Payment payment = paymentService.getPaymentDetails(paymentId);
				
				PayerInfo payerInfo = payment.getPayer().getPayerInfo();
				Transaction transaction = payment.getTransactions().get(0);
				ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();
				
				request.setAttribute("payer", payerInfo);
				request.setAttribute("transaction", transaction);
				request.setAttribute("shippingAddress", transaction);
				
				String Url = "review.jsp?paymentId = " + paymentId + "&payerId=" + payerId;
				
				request.getRequestDispatcher(Url).forward(request, response);
				
		} catch (PayPalRESTException ex) {
			
			request.setAttribute("errorMessage", ex.getMessage());
			ex.printStackTrace();
			request.getRequestDispatcher("error.jsp").forward(request, response);
			
		}
	}

}
