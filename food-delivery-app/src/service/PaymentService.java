package service;

import database.AppDOA;
import model.PaymentModel;


/**
 * This is the service class that will implement the customer operation.
 */
public class PaymentService {
	private AppDOA dbHandler = null;
	private static final String SERVICE_LAYER_ERROR_TAG = "[SERVICE LAYER ERROR]";
	private static final String SERVICE_LAYER_ERROR_MESSAGE = "Error from Payment Service Layer";

	public PaymentService(AppDOA dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Creates the query for showing the customer table,
	 * and then implement the connection to the DOA layer.
	 * This is not an operation done determined by the user,
	 * so this is called by a separate function in the main service layer.
	 */
	public PaymentModel[] showTable() {
		String query = "SELECT * " +
				"FROM PaymentBasicInfo PB, PaymentAdvancedInfo PA " +
				"WHERE PB.order_id = PA.order_id " +
				"ORDER BY PB.payment_id";
		return dbHandler.getPaymentTable(query);
	}

}
